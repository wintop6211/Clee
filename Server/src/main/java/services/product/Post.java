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
import org.hibernate.HibernateException;
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

@Path("/ProductServices")
public class Post {

    @Context
    HttpServletResponse response;

    @Path("/postItem")
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
        } catch (HibernateException e) {
            jsonObject = JSONResponseGenerator.formHibernateExceptionJSON(e);
            e.printStackTrace();
        } catch (Exception e) {
            jsonObject = JSONResponseGenerator.formUnknownExceptionJSON(e);
            e.printStackTrace();
        }
        return Response.ok(jsonObject.toString()).build();
    }

    @Path("/postItemImage")
    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response postItemImage(@CookieParam("loginIdentifier") String loginIdentifier,
                                  @CookieParam("postedItemId") int itemId,
                                  @FormDataParam("imageIndex") int imageIndex,
                                  @FormDataParam("itemImage") InputStream imageStream) {
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
            } catch (HibernateException e) {
                jsonObject = JSONResponseGenerator.formHibernateExceptionJSON(e);
                e.printStackTrace();
            } catch (IOException e) {
                jsonObject = JSONResponseGenerator.formImageIOExceptionWhenWriteJSON(e);
                e.printStackTrace();
            } catch (Exception e) {
                jsonObject = JSONResponseGenerator.formUnknownExceptionJSON(e);
                e.printStackTrace();
            }
        } else {
            jsonObject.put("Fail", "Please upload item basic info first.");
        }
        return Response.ok(jsonObject.toString()).build();
    }
}
