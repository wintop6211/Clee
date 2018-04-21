package main.java.services.product;

import main.java.configuration.SessionProvider;
import main.java.entities.Product;
import main.java.entities.User;
import main.java.entities.managements.ImageManagement;
import main.java.entities.managements.ProductManagement;
import main.java.entities.managements.UserManagement;
import main.java.json.JSONResponseGenerator;
import org.glassfish.jersey.media.multipart.MultiPartMediaTypes;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
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

@Path("/ProductServices")
public class Get {
    @Path("/getItem/{itemId}")
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
        } catch (HibernateException e) {
            jsonObject = JSONResponseGenerator.formHibernateExceptionJSON(e);
            e.printStackTrace();
        } catch (Exception e) {
            jsonObject = JSONResponseGenerator.formUnknownExceptionJSON(e);
            e.printStackTrace();
        }
        return Response.ok(jsonObject.toString()).build();
    }

    @Path("/getItemImage/{isHighResolution}/{itemId}/{itemImageIndex}")
    @GET
    @Produces("image/jpeg")
    public Response getItemImage(@PathParam("isHighResolution") boolean isHighResolution,
                                 @PathParam("itemId") int itemId,
                                 @PathParam("itemImageIndex") int itemImageIndex) {
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
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Response.ok(new ByteArrayInputStream(imageData)).build();
    }

    @Path("/updateView")
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
        } catch (HibernateException e) {
            e.printStackTrace();
            jsonObject = JSONResponseGenerator.formHibernateExceptionJSON(e);
        } catch (Exception e) {
            e.printStackTrace();
            jsonObject = JSONResponseGenerator.formUnknownExceptionJSON(e);
        }
        return Response.ok(jsonObject.toString()).build();
    }
}
