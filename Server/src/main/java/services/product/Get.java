package main.java.services.product;

import main.java.configuration.SessionProvider;
import main.java.entities.PendingRequest;
import main.java.entities.Product;
import main.java.entities.User;
import main.java.entities.managements.ImageManagement;
import main.java.entities.managements.ProductManagement;
import main.java.entities.managements.UserManagement;
import main.java.json.JSONResponseGenerator;
import org.glassfish.jersey.media.multipart.MultiPartMediaTypes;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.ws.rs.Consumes;
import javax.ws.rs.CookieParam;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Collection;

/**
 * The class contains web services for getting item information from the database
 */
@Path("/product")
public class Get {
    /**
     * Gets the item information
     * @param loginIdentifier The identifier which identifies the user
     * @param itemId The id of the product
     * @return The JSON response object
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
     *             "verified": 1 (0 will be returend if the user is not verified. Otherwise, 1 will be returned)
     *             "school": "Milwaukee School of Engineering"
     *         }
     *     }
     * }
     * {"Fail": "The product does not exist."}
     * {"Fail": "The user has been signed out."}
     */
    @Path("/get/{itemId}")
    @GET
    @Produces(MultiPartMediaTypes.MULTIPART_MIXED)
    public Response getItem(@CookieParam("loginIdentifier") String loginIdentifier,
                            @PathParam("itemId") int itemId) {
        JSONObject jsonObject = new JSONObject();
        try (final Session session = SessionProvider.getSession()) {
            Transaction transaction = session.beginTransaction();

            UserManagement userManagement = new UserManagement(session);
            ProductManagement productManagement = new ProductManagement(session);

            User user = new User();
            user.setLoginIdentifier(loginIdentifier);

            if (userManagement.isExist(user)) {
                Product product = new Product();
                product.setIdProduct(itemId);
                if (productManagement.isExist(product)) {
                    // get basic information
                    product = productManagement.get(itemId);
                    JSONObject productInfo = JSONResponseGenerator.formBasicItemInfoJSON(session, product);
                    jsonObject.put("Success", productInfo);
                } else {
                    jsonObject.put("Fail", "The product does not exist.");
                }
            } else {
                jsonObject = JSONResponseGenerator.formSignedOutJSON();
            }

            transaction.commit();
        }
        return Response.ok(jsonObject.toString()).build();
    }

    /**
     * Gets the item image
     * @param isHighResolution True if the image needs to be high quality. False if the image needs to be low quality
     * @param itemId The id of the product
     * @param itemImageIndex The index of the image
     * @return The image data. No bytes will be returned if the image does not exist.
     * @throws IOException if the image cannot be found
     */
    @Path("/get/image/{isHighResolution}/{itemId}/{itemImageIndex}")
    @GET
    @Produces("image/jpeg")
    public Response getItemImage(@PathParam("isHighResolution") boolean isHighResolution,
                                 @PathParam("itemId") int itemId,
                                 @PathParam("itemImageIndex") int itemImageIndex) throws IOException {
        byte[] imageData = new byte[0];
        try (final Session session = SessionProvider.getSession()) {
            Transaction transaction = session.beginTransaction();

            ProductManagement productManagement = new ProductManagement(session);

            Product product = new Product();
            product.setIdProduct(itemId);

            if (productManagement.isExist(product)) {
                product = productManagement.get(itemId);
                imageData = ImageManagement.loadItemImage(product, itemImageIndex, isHighResolution);
            }

            transaction.commit();
        }
        return Response.ok(new ByteArrayInputStream(imageData)).build();
    }

    /**
     * Gets all purchasing requests for the product
     * @param loginIdentifier The identifier which identifies the user
     * @param itemId The id of the product
     * @return The JSON response object.
     * For example,
     * {
     *     "Success": [
     *         {
     *             "id": 5,
     *             "price": 200,
     *             "note": "Please send me an email if you want to schedule a time",
     *             "buyer": {
     *                 "id": 4,
     *                 "name": "Harry Liang",
     *                 "email": "thisisemail@gmail.com",
     *                 "gender": 0 (this is a number, and how to handle this number is determined on the client side),
     *                 "phone": "4141234567"
     *             }
     *         },
     *         ...
     *     ]
     * }
     */
    @Path("/get/requests/{itemId}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRequests(@CookieParam("loginIdentifier") String loginIdentifier,
                                @PathParam("itemId") int itemId) {
        JSONObject jsonObject = new JSONObject();
        try (final Session session = SessionProvider.getSession()) {
            Transaction transaction = session.beginTransaction();

            UserManagement userManagement = new UserManagement(session);
            ProductManagement productManagement = new ProductManagement(session);

            User user = new User();
            user.setLoginIdentifier(loginIdentifier);
            Product product = new Product();
            product.setIdProduct(itemId);

            if (userManagement.isExist(user)) {
                if (productManagement.isExist(product)) {
                    product = productManagement.get(itemId);
                    // put request information
                    Collection<PendingRequest> requests = product.getPendingRequestsByIdProduct();
                    JSONArray offersJSON = new JSONArray();
                    for (PendingRequest request : requests) {
                        JSONObject requestJSON = JSONResponseGenerator.formRequestJSON(session, request);
                        offersJSON.put(requestJSON);
                    }
                    jsonObject.put("Success", offersJSON);
                } else {
                    jsonObject.put("Fail", "The product does not exist.");
                }
            } else {
                jsonObject = JSONResponseGenerator.formSignedOutJSON();
            }

            transaction.commit();
        }

        return Response.ok(jsonObject).build();
    }

    /**
     * TODO: Finish this method
     * Updates the view for the product.
     * @param itemId The id of the product
     * @return The JSON response object
     */
    @Path("/view/update")
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response updateView(@FormParam("itemId") int itemId) {
        JSONObject jsonObject = new JSONObject();
        try (final Session session = SessionProvider.getSession()) {
            Transaction transaction = session.beginTransaction();

            ProductManagement productManagement = new ProductManagement(session);

            Product product = productManagement.get(itemId);
            product.setView(product.getView() + 1);
            productManagement.set(product);

            transaction.commit();
        }
        return Response.ok(jsonObject.toString()).build();
    }
}
