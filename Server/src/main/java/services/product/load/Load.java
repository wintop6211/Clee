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

@Path("/product")
public class Load {

    @Context
    HttpServletRequest request;

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
                List products = getItemQueryResults(query, ItemOffsetRecorder.SEARCH_ITEM_OFFSET);
                putItemJSONToResponseJSON(session, products, jsonObject);
            } else {
                jsonObject = JSONResponseGenerator.formSignedOutJSON();
            }

            transaction.commit();
        }
        return Response.ok(jsonObject.toString()).build();
    }

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
                List items = getItemQueryResults(query, ItemOffsetRecorder.ALL_ITEM_OFFSET);
                putItemJSONToResponseJSON(session, items, jsonObject);
            } else {
                jsonObject = JSONResponseGenerator.formSignedOutJSON();
            }

            transaction.commit();
        }
        return Response.ok(jsonObject.toString()).build();
    }

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
                List items = getItemQueryResults(query, ItemOffsetRecorder.BOUGHT_ITEM_OFFSET);
                putItemJSONToResponseJSON(session, items, jsonObject);
            } else {
                jsonObject = JSONResponseGenerator.formSignedOutJSON();
            }

            transaction.commit();
        }
        return Response.ok(jsonObject.toString()).build();
    }

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
                List items = getItemQueryResults(query, ItemOffsetRecorder.SELLING_ITEM_OFFSET);
                putItemJSONToResponseJSON(session, items, jsonObject);
            } else {
                jsonObject = JSONResponseGenerator.formSignedOutJSON();
            }

            transaction.commit();
        }
        return Response.ok(jsonObject.toString()).build();
    }

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
                List items = getItemQueryResults(query, ItemOffsetRecorder.REQUESTED_ITEM_OFFSET);
                putItemJSONToResponseJSON(session, items, jsonObject);
            } else {
                jsonObject = JSONResponseGenerator.formSignedOutJSON();
            }

            transaction.commit();
        }
        return Response.ok(jsonObject.toString()).build();
    }

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
                List items = getItemQueryResults(query, ItemOffsetRecorder.SELLING_ITEM_OFFSET);
                if (items.size() > 0) {
                    Product product = (Product) items.get(0);
                    image = ImageManagement.loadItemCoverImage(product);
                }
            }

            transaction.commit();
        }
        return Response.ok(new ByteArrayInputStream(image)).build();
    }

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
                List products = getItemQueryResults(query, ItemOffsetRecorder.BOUGHT_ITEM_OFFSET);
                if (products.size() > 0) {
                    Product product = (Product) products.get(0);
                    image = ImageManagement.loadItemCoverImage(product);
                }
            }

            transaction.commit();
        }
        return Response.ok(new ByteArrayInputStream(image)).build();
    }

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
                List items = getItemQueryResults(query, ItemOffsetRecorder.REQUESTED_ITEM_OFFSET);
                if (items.size() > 0) {
                    Product product = (Product) items.get(0);
                    image = ImageManagement.loadItemCoverImage(product);
                }
            }

            transaction.commit();
        }
        return Response.ok(new ByteArrayInputStream(image)).build();
    }

    private List getItemQueryResults(Query query, String offsetIdentifier) {
        int itemOffset = ItemOffsetRecorder.getItemOffset(request, offsetIdentifier);
        query.setMaxResults(ItemOffsetRecorder.MAX_ITEM_ALLOWED);
        query.setFirstResult(itemOffset);
        ItemOffsetRecorder.updateItemOffset(request, offsetIdentifier);
        return query.list();
    }

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
