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

@Path("/product")
public class Get {
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
                    // get seller information
                    User seller = product.getUserByIdSeller();
                    JSONObject sellerJSON = JSONResponseGenerator.formUserBasicInfoJSON(session, seller);
                    // put JSON
                    productInfo.put("seller", sellerJSON);
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
