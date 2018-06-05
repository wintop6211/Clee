package main.java.services.product;

import main.java.configuration.SessionProvider;
import main.java.entities.Product;
import main.java.entities.User;
import main.java.entities.managements.ImageManagement;
import main.java.entities.managements.ProductManagement;
import main.java.entities.managements.UserManagement;
import main.java.json.JSONResponseGenerator;
import main.java.services.helpers.PathManager;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.json.JSONObject;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.CookieParam;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * This class contains all services for posting the item information
 */
@Path("/product")
public class Post {

    @Context
    HttpServletResponse response;

    /**
     * The service is for posting the item to the database
     * @param loginIdentifier The identifier which identifies the user
     * @param name The name of the product
     * @param description The description of the product
     * @param price The price of the product
     * @param condition The condition of the product
     * @param category The category of the product
     * @return The response of the service.
     * "Success", "The information has been uploaded." will be returned if the item info is created in the database
     * "Fail", "The user has not been verified." will be returned if the use is not verified
     * "Fail", "The user has been signed out." will be returned if the user is signed out (cookie is deleted)
     */
    @Path("/post")
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    public Response postItem(@CookieParam("loginIdentifier") String loginIdentifier,
                             @FormParam("name") String name,
                             @FormParam("description") String description,
                             @FormParam("price") double price,
                             @FormParam("condition") int condition,
                             @FormParam("category") String category) {
        JSONObject jsonObject = new JSONObject();
        try (final Session session = SessionProvider.getSession()) {
            Transaction transaction = session.beginTransaction();

            UserManagement userManagement = new UserManagement(session);
            ProductManagement productManagement = new ProductManagement(session);

            User user = new User();
            user.setLoginIdentifier(loginIdentifier);

            if (userManagement.isExist(user)) {
                user = userManagement.get(loginIdentifier);
                if (user.getVerified() == 1) {
                    // set product basic information
                    Product product = new Product();
                    ProductManagement.setProductBasicInfo(product, user, name, description, price, condition, 0);
                    productManagement.add(product); // generate id
                    // set the category for the product
                    productManagement.setCategory(product, category);
                    // set the item image path
                    String path = PathManager.getImagesDirectory("pics/itemImages/" + product.getIdProduct() + "/");
                    product.setPictures(path);
                    productManagement.set(product);
                    // create the directory
                    System.out.println("The product file path has been created: " + new File(path).mkdir());
                    // create the cookie for recording the id of the product
                    Cookie itemIdCookie = new Cookie("postedItemId", Integer.toString(product.getIdProduct()));
                    response.addCookie(itemIdCookie);
                    jsonObject.put("Success", "The information has been uploaded.");
                } else {
                    jsonObject.put("Fail", "The user has not been verified.");
                }
            } else {
                jsonObject = JSONResponseGenerator.formSignedOutJSON();
            }

            transaction.commit();
        }
        return Response.ok(jsonObject.toString()).build();
    }

    /**
     * The service is for posting the item image to the server storage
     * @param loginIdentifier The identifier which identifies the user
     * @param itemId The id of the item
     * @param imageIndex The index of the image
     * @param imageStream The image data
     * @return
     * "Success", "The image has been uploaded." if the item image is stored
     * "Fail", "The product does not exist." if the item does not exist
     * "Fail", "Please upload item basic info first." if the product information is not uploaded yet
     * "Fail", "The user has been signed out." will be returned if the user is signed out (cookie is deleted)
     * @throws IOException If the image is unable to write to the drive, this exception will be thrown
     */
    @Path("/post/image")
    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response postItemImage(@CookieParam("loginIdentifier") String loginIdentifier,
                                  @CookieParam("postedItemId") int itemId,
                                  @FormDataParam("imageIndex") int imageIndex,
                                  @FormDataParam("itemImage") InputStream imageStream) throws IOException {
        JSONObject jsonObject = new JSONObject();
        if (itemId > 0) {
            try (final Session session = SessionProvider.getSession()) {
                Transaction transaction = session.beginTransaction();
                UserManagement userManagement = new UserManagement(session);
                ProductManagement productManagement = new ProductManagement(session);

                Product product = new Product();
                product.setIdProduct(itemId);
                User user = new User();
                user.setLoginIdentifier(loginIdentifier);

                if (userManagement.isExist(user)) {
                    if (productManagement.isExist(product)) {
                        product = productManagement.get(itemId);
                        String path = product.getPictures() + imageIndex + ".jpeg";
                        ImageManagement.writeImage(path, imageStream);
                        jsonObject.put("Success", "The image has been uploaded.");
                    } else {
                        jsonObject.put("Fail", "The product does not exist.");
                    }
                } else {
                    jsonObject = JSONResponseGenerator.formSignedOutJSON();
                }

                transaction.commit();
            }
        } else {
            jsonObject.put("Fail", "Please upload item basic info first.");
        }
        return Response.ok(jsonObject.toString()).build();
    }
}
