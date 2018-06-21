package main.java.services.user;

import main.java.configuration.SessionProvider;
import main.java.entities.User;
import main.java.entities.managements.ImageManagement;
import main.java.entities.managements.UserManagement;
import main.java.json.JSONResponseGenerator;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.json.JSONObject;

import javax.ws.rs.Consumes;
import javax.ws.rs.CookieParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * The class contains web services for managing user images
 */
@Path("/user/image")
public class ProfileImage {
    /**
     * Gets the profile picture for the user
     * @param loginIdentifier The identifier which identifies the user
     * @return The profile image data
     * @throws IOException if the profile image cannot be found
     */
    @Path("/get")
    @GET
    @Produces("image/jpeg")
    public Response getProfilePic(@CookieParam("loginIdentifier") String loginIdentifier) throws IOException {
        byte[] imageData = loadUserProfileImage(loginIdentifier, 0);
        return Response.ok(new ByteArrayInputStream(imageData)).build();
    }

    /**
     * Gets the profile picture by using user id
     * @param loginIdentifier The identifier which identifies the user
     * @param userId The id of the user whom you want to search for
     * @return The profile image data
     * @throws IOException if the profile image cannot be found
     */
    @Path("/get/{userId}")
    @GET
    @Produces("image/png")
    public Response getProfilePic(@CookieParam("loginIdentifier") String loginIdentifier,
                                  @PathParam("userId") int userId) throws IOException {
        byte[] imageData = loadUserProfileImage(loginIdentifier, userId);
        return Response.ok(new ByteArrayInputStream(imageData)).build();
    }

    /**
     * Gets the profile picture by one single URL
     * @param loginIdentifier The identifier which identifies the user
     * @param userId The id of the user whom you want to search for
     * @return The profile image data
     * @throws IOException if the profile image cannot be found
     */
    @Path("/get/{loginIdentifier}/{userId}")
    @GET
    @Produces("image/jpeg")
    public Response getProfilePicByURL(@PathParam("loginIdentifier") String loginIdentifier,
                                         @PathParam("userId") int userId) throws IOException {
        byte[] imageData = loadUserProfileImage(loginIdentifier, userId);
        return Response.ok(new ByteArrayInputStream(imageData)).build();
    }

    /**
     * Uploads the profile image to the server
     * @param loginIdentifier The identifier which identifies the user
     * @param profilePicData The profile image data
     * @return The JSON response
     * {"Success": "The profile image has been set."}
     * {"Fail": "The user has been signed out."}
     * @throws IOException if the image data cannot be written to the hard drive
     */
    @Path("/upload")
    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response uploadProfileImage(@CookieParam("loginIdentifier") String loginIdentifier,
                                       @FormDataParam("profilePicture") InputStream profilePicData) throws IOException {
        JSONObject jsonObject = new JSONObject();
        try (final Session session = SessionProvider.getSession()) {
            Transaction transaction = session.beginTransaction();
            UserManagement userManagement = new UserManagement(session);

            // get the user and set the image for the user
            User user = new User();
            user.setLoginIdentifier(loginIdentifier);
            if (userManagement.isExist(user)) {
                user = userManagement.get(loginIdentifier);
                ImageManagement.writeImage(user.getProfilePicture(), profilePicData);
                jsonObject.put("Success", "The profile image has been set.");
            } else {
                jsonObject = JSONResponseGenerator.formSignedOutJSON();
            }

            transaction.commit();
        }
        return Response.ok(jsonObject.toString()).build();
    }

    /**
     * The helper method for loading the user profile image
     * @param loginIdentifier The identifier which identifies the user
     * @param userId The id of the user
     * @return The profile image data
     * @throws IOException if the image cannot be read from the hard drive
     */
    private byte[] loadUserProfileImage(String loginIdentifier, int userId) throws IOException {
        byte[] imageData = new byte[0];
        try (final Session session = SessionProvider.getSession()) {
            Transaction transaction = session.beginTransaction();
            UserManagement userManagement = new UserManagement(session);

            User user = new User();
            user.setLoginIdentifier(loginIdentifier);

            if (userManagement.isExist(user)) {
                if (userId == 0) {
                    user = userManagement.get(loginIdentifier);
                } else {
                    user = userManagement.get(userId);
                }
                String path = user.getProfilePicture();
                imageData = ImageManagement.readImage(path, false);
            }
            transaction.commit();
        }
        return imageData;
    }
}
