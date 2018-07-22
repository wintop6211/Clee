package main.java.services.product.load;

import main.java.configuration.SessionProvider;
import main.java.entities.PendingRequest;
import main.java.entities.Product;
import main.java.entities.User;
import main.java.entities.managements.ImageManagement;
import main.java.entities.managements.UserManagement;
import main.java.json.JSONResponseGenerator;
import main.java.status.manager.ItemOffsetRecorder;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.CookieParam;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.List;

/**
 * The class contains web services for loading item data from the database
 */
@Path("/product")
public class Load {

    @Context
    HttpServletRequest request;

    /**
     * The service for searching items whose name contains the keyword. Only one item will be returned if it is
     * called once. If you need to more than one items information, please call this service several times. Each time
     * you call the service, the different item will be returned.
     *
     * @param loginIdentifier The login identifier which identifies the user
     * @param keyWords        The keyword of the item
     * @return The JSON response which contains all items whose names are able to map with the keyword
     *
     */
    @Path("/search/{keyWords}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response searchItem(@CookieParam("loginIdentifier") String loginIdentifier,
                               @PathParam("keyWords") String keyWords) {
        JSONObject jsonObject = new JSONObject();
        try (final Session session = SessionProvider.getSession()) {
            Transaction transaction = session.beginTransaction();

            UserManagement userManagement = new UserManagement(session);

            User user = new User();
            user.setLoginIdentifier(loginIdentifier);

            if (userManagement.isExist(user)) {
                user = userManagement.get(loginIdentifier);
                int userId = user.getIdUser();
                Query query = session.createQuery("select p.idProduct, p.name, p.price, p.condition " +
                        "from Product p where p.name like '%" + keyWords + "%' and " +
                        "p.schoolIdSchool in (select u.schoolIdSchool " +
                        "from User u where u.idUser=" + userId + ") order by p.idProduct desc");
                List products = getItemQueryResults(query, ItemOffsetRecorder.SEARCH_ITEM_OFFSET_K);
                putItemJSONToResponseJSON(session, products, jsonObject);
            } else {
                jsonObject = JSONResponseGenerator.formSignedOutJSON();
            }

            transaction.commit();
        }
        return Response.ok(jsonObject.toString()).build();
    }

    /**
     * The service for loading items based on the date posted. Only one item will be returned if it is
     * called once. If you need to more than one items information, please call this service several times. Each time
     * you call the service, the different item will be returned.
     *
     * @param loginIdentifier The login identifier which identifies of the user
     * @return The JSON response which contains the item information
     */
    @Path("/load")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response loadItems(@CookieParam("loginIdentifier") String loginIdentifier) {
        JSONObject jsonObject = new JSONObject();
        try (final Session session = SessionProvider.getSession()) {
            Transaction transaction = session.beginTransaction();

            UserManagement userManagement = new UserManagement(session);

            User user = new User();
            user.setLoginIdentifier(loginIdentifier);

            if (userManagement.isExist(user)) {
                user = userManagement.get(loginIdentifier);
                Query query = session.createQuery("from Product where sold=0 and schoolIdSchool=" + user.getSchoolIdSchool() + " order by date desc");
                List items = getItemQueryResults(query, ItemOffsetRecorder.ALL_ITEM_OFFSET_K);
                putItemJSONToResponseJSON(session, items, jsonObject);
            } else {
                jsonObject = JSONResponseGenerator.formSignedOutJSON();
            }

            transaction.commit();
        }
        return Response.ok(jsonObject.toString()).build();
    }

    /**
     * Loads the item by the category. Only one item will be returned if it is
     * called once. If you need to more than one items information, please call this service several times. Each time
     * you call the service, the different item will be returned.
     *
     * @param loginIdentifier  The login identifier which identifies the user
     * @param categoryInString The category in the string format
     * @return The JSON response which contains the item information
     */
    @Path("/load/{category}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response loadItemsByCategory(@CookieParam("loginIdentifier") String loginIdentifier,
                                        @PathParam("category") String categoryInString) {
        JSONObject jsonObject = new JSONObject();
        try (final Session session = SessionProvider.getSession()) {
            Transaction transaction = session.beginTransaction();

            UserManagement userManagement = new UserManagement(session);

            User user = new User();
            user.setLoginIdentifier(loginIdentifier);

            if (userManagement.isExist(user)) {
                user = userManagement.get(loginIdentifier);
                Query query = session.createQuery("select p from Product p, Category c, ProductHasCategory phc " +
                        "where p.sold=0 and p.idProduct=phc.productIdProduct and phc.categoryIdCategory=c.idCategory " +
                        "and c.name='" + categoryInString + "' and p.schoolIdSchool=" + user.getSchoolIdSchool() +
                        " order by date desc");
                List items = getItemQueryResults(query, categoryInString);
                putItemJSONToResponseJSON(session, items, jsonObject);
            } else {
                jsonObject = JSONResponseGenerator.formSignedOutJSON();
            }

            transaction.commit();
        }
        return Response.ok(jsonObject.toString()).build();
    }

    /**
     * Gets items which have been bought by the user. Only one item will be returned if it is
     * called once. If you need to more than one items information, please call this service several times. Each time
     * you call the service, the different item will be returned.
     *
     * @param loginIdentifier The login identifier which identifies the user
     * @return The JSON response which contains the item information
     */
    @Path("/load/bought")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getBoughtItems(@CookieParam("loginIdentifier") String loginIdentifier) {
        JSONObject jsonObject = new JSONObject();
        try (final Session session = SessionProvider.getSession()) {
            Transaction transaction = session.beginTransaction();

            UserManagement userManagement = new UserManagement(session);

            User user = new User();
            user.setLoginIdentifier(loginIdentifier);

            if (userManagement.isExist(user)) {
                user = userManagement.get(loginIdentifier);
                int idUser = user.getIdUser();
                Query query = session.createQuery("from Product where idBuyer=" + idUser + " order by date desc");
                List items = getItemQueryResults(query, ItemOffsetRecorder.BOUGHT_ITEM_OFFSET_K);
                putItemJSONToResponseJSON(session, items, jsonObject);
            } else {
                jsonObject = JSONResponseGenerator.formSignedOutJSON();
            }

            transaction.commit();
        }
        return Response.ok(jsonObject.toString()).build();
    }

    /**
     * Gets items being sold by the user. Only one item will be returned if it is
     * called once. If you need to more than one items information, please call this service several times. Each time
     * you call the service, the different item will be returned.
     *
     * @param loginIdentifier The login identifier which identifies the user
     * @return The JSON response which contains the item information
     */
    @Path("/load/selling")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSellingItems(@CookieParam("loginIdentifier") String loginIdentifier) {
        JSONObject jsonObject = new JSONObject();
        try (final Session session = SessionProvider.getSession()) {
            Transaction transaction = session.beginTransaction();

            UserManagement userManagement = new UserManagement(session);

            User user = new User();
            user.setLoginIdentifier(loginIdentifier);

            if (userManagement.isExist(user)) {
                user = userManagement.get(loginIdentifier);
                int userId = user.getIdUser();
                Query query = session.createQuery("from Product where idSeller=" + userId + " and sold=0 order by date desc");
                List items = getItemQueryResults(query, ItemOffsetRecorder.SELLING_ITEM_OFFSET_K);
                putItemJSONToResponseJSON(session, items, jsonObject);
            } else {
                jsonObject = JSONResponseGenerator.formSignedOutJSON();
            }

            transaction.commit();
        }
        return Response.ok(jsonObject.toString()).build();
    }

    /**
     * Gets items which has been requested by the user. Only one item will be returned if it is
     * called once. If you need to more than one items information, please call this service several times. Each time
     * you call the service, the different item will be returned.
     *
     * @param loginIdentifier The login identifier which identifies the user
     * @return The JSON response which contains the item information
     */
    @Path("/load/requested")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRequestedItems(@CookieParam("loginIdentifier") String loginIdentifier) {
        JSONObject jsonObject = new JSONObject();
        try (final Session session = SessionProvider.getSession()) {
            Transaction transaction = session.beginTransaction();

            UserManagement userManagement = new UserManagement(session);

            User user = new User();
            user.setLoginIdentifier(loginIdentifier);

            if (userManagement.isExist(user)) {
                user = userManagement.get(loginIdentifier);
                int userId = user.getIdUser();
                Query query = session.createQuery("select p from Product p, PendingRequest r " +
                        "where p.id=r.idProduct and r.idBuyer=" + userId);
                List items = getItemQueryResults(query, ItemOffsetRecorder.REQUESTED_ITEM_OFFSET_K);
                putItemJSONToResponseJSON(session, items, jsonObject);
            } else {
                jsonObject = JSONResponseGenerator.formSignedOutJSON();
            }

            transaction.commit();
        }
        return Response.ok(jsonObject.toString()).build();
    }

    /**
     * Loads selling items cover photos. One call will only return one photo. Please call several times if you want
     * to get more cover images. Each time you call the service, the different image will be returned.
     *
     * @param loginIdentifier   The identifier which identifies the user
     * @param sellingItemOffset The number which records offset for loading item images
     * @return The image data
     * @throws IOException if the image cannot be found
     */
    @Path("/load/selling/photo")
    @GET
    @Produces("image/jpeg")
    public Response loadSellingItemCoverPhoto(@CookieParam("loginIdentifier") String loginIdentifier,
                                              @CookieParam("sellingItemOffset") int sellingItemOffset) throws IOException {
        byte[] image = new byte[0];
        try (final Session session = SessionProvider.getSession()) {
            Transaction transaction = session.beginTransaction();

            UserManagement userManagement = new UserManagement(session);

            User user = new User();
            user.setLoginIdentifier(loginIdentifier);

            if (userManagement.isExist(user)) {
                user = userManagement.get(loginIdentifier);
                Query query = session.createQuery("from Product where sold=0 and idSeller=" +
                        user.getIdUser() + " order by date desc");
                List items = getItemQueryResults(query, ItemOffsetRecorder.SELLING_ITEM_OFFSET_K);
                if (items.size() > 0) {
                    Product product = (Product) items.get(0);
                    image = ImageManagement.loadItemCoverImage(product);
                }
            }

            transaction.commit();
        }
        return Response.ok(new ByteArrayInputStream(image)).build();
    }

    /**
     * Loads bought item cover images. Please call several times if you want
     * to get more cover images. Each time you call the service, the different image will be returned.
     *
     * @param loginIdentifier  The identifier which identifies the user
     * @param boughtItemOffset The number which records offset for loading item images
     * @return The image data
     * @throws IOException if the image cannot be found
     */
    @Path("/load/bought/photo")
    @GET
    @Produces("image/jpeg")
    public Response loadBoughtItemCoverPhoto(@CookieParam("loginIdentifier") String loginIdentifier,
                                             @CookieParam("boughtItemOffset") int boughtItemOffset) throws IOException {
        byte[] image = new byte[0];
        try (final Session session = SessionProvider.getSession()) {
            Transaction transaction = session.beginTransaction();

            UserManagement userManagement = new UserManagement(session);

            User user = new User();
            user.setLoginIdentifier(loginIdentifier);

            if (userManagement.isExist(user)) {
                user = userManagement.get(loginIdentifier);
                int idUser = user.getIdUser();
                Query query = session.createQuery("from Product where idBuyer=" + idUser + " order by date desc");
                List products = getItemQueryResults(query, ItemOffsetRecorder.BOUGHT_ITEM_OFFSET_K);
                if (products.size() > 0) {
                    Product product = (Product) products.get(0);
                    image = ImageManagement.loadItemCoverImage(product);
                }
            }

            transaction.commit();
        }
        return Response.ok(new ByteArrayInputStream(image)).build();
    }

    /**
     * Loads requested item cover images. Please call several times if you want
     * to get more cover images. Each time you call the service, the different image will be returned.
     *
     * @param loginIdentifier     The identifier which identifies the user
     * @param requestedItemOffset The number which records offset for loading item images
     * @return The image data
     * @throws IOException if the image cannot be found
     */
    @Path("/load/requested/photo")
    @GET
    @Produces("image/jpeg")
    public Response loadRequestedItemCoverPhoto(@CookieParam("loginIdentifier") String loginIdentifier,
                                                @CookieParam("requestedItemOffset") int requestedItemOffset) throws IOException {
        byte[] image = new byte[0];
        try (final Session session = SessionProvider.getSession()) {
            Transaction transaction = session.beginTransaction();

            UserManagement userManagement = new UserManagement(session);

            User user = new User();
            user.setLoginIdentifier(loginIdentifier);

            if (userManagement.isExist(user)) {
                user = userManagement.get(loginIdentifier);
                int idUser = user.getIdUser();
                Query query = session.createQuery("select p from Product p, PendingRequest r where p.id=r.idProduct and r.idBuyer=" + idUser);
                List items = getItemQueryResults(query, ItemOffsetRecorder.REQUESTED_ITEM_OFFSET_K);
                if (items.size() > 0) {
                    Product product = (Product) items.get(0);
                    image = ImageManagement.loadItemCoverImage(product);
                }
            }

            transaction.commit();
        }
        return Response.ok(new ByteArrayInputStream(image)).build();
    }

    /**
     * Returns item objects array. Although only 1 item will be returned, the array is still used here. The reason
     * was that the number of items returned may change in the future, the maintainability can be higher if the array
     * is used here.
     *
     * @param query            The query for getting items which needs to be executed
     * @param offsetIdentifier The identifier which identifies the item offset stored in the session
     * @return The list of items
     */
    private List getItemQueryResults(Query query, String offsetIdentifier) {
        int itemOffset = ItemOffsetRecorder.getItemOffset(request, offsetIdentifier);
        query.setMaxResults(ItemOffsetRecorder.MAX_ITEM_ALLOWED);
        query.setFirstResult(itemOffset);
        ItemOffsetRecorder.updateItemOffset(request, offsetIdentifier);
        return query.list();
    }

    /**
     * Embeds the item JSON into other JSON response.
     *
     * @param session      The session for interacting with the database
     * @param items        The list which contains items. The JSON will be generated based on these items
     * @param responseJSON The JSON response where the item JSON should be embedded into.
     * {
     *     "Success": {
     *         "id": 3,
     *         "name": "MacBook Pro",
     *         "price": 2000,
     *         "condition": 2, (this is a number, how to handle this number is determined on the client side)
     *         "status": 1, (this means the item has been sold. Otherwise, 0 will be returned)
     *         "description": "This is my favorite laptop.",
     *         "seller": {
     *             "id": 1,
     *             "name": "Harry Liang",
     *             "email": "IamAwsome@yahoo.com",
     *             "gender": 1, (this is a number, how to handler this number is determined on the client side)
     *             "phone": "414-123-4567",
     *             "sellerRating": "2.5", (-1 will be returned if not enough reviews)
     *             "buyerRating": "2.5", (-1 will be returned if not enough reviews)
     *             "verified": 1, (0 will be returend if the user is not verified. Otherwise, 1 will be returned)
     *             "school": "Milwaukee School of Engineering"
     *         },
     *         "offers": [offer1, offer2, offer3] (check the doc for JSONResponseGenerator.formRequestJSON to know more
     *                     information about the format of each offer respons format)
     *     }
     * }
     */
    private void putItemJSONToResponseJSON(Session session, List items, JSONObject responseJSON) {
        if (items.size() > 0) {
            Product product = (Product) items.get(0);
            JSONObject itemJSON = JSONResponseGenerator.formBasicItemInfoJSON(session, product);
            // put request information
            Collection<PendingRequest> requests = product.getPendingRequestsByIdProduct();
            JSONArray offersJSON = new JSONArray();
            for (PendingRequest request : requests) {
                JSONObject requestJSON = JSONResponseGenerator.formRequestJSON(session, request);
                offersJSON.put(requestJSON);
            }
            itemJSON.put("offers", offersJSON);
            responseJSON.put("Success", itemJSON);
        } else {
            responseJSON.put("Fail", "No more items.");
        }
    }
}
