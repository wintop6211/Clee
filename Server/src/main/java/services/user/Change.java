package main.java.services.user;

import main.java.configuration.SessionProvider;
import main.java.entities.User;
import main.java.entities.managements.UserManagement;
import main.java.json.JSONResponseGenerator;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.json.JSONObject;

import javax.ws.rs.Consumes;
import javax.ws.rs.CookieParam;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * The class contains services for changing user information stored in the database.
 */
@Path("/user/change")
public class Change {
    /**
     * Changes the user name
     *
     * @param loginIdentifier The identifier which identifies the user
     * @param name            The new name of the user
     * @return The JSON response object.
     * {"Success": "The information has been set."}
     * {"Fail": "The user has been signed out."}
     */
    @Path("/name")
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response changeName(@CookieParam("loginIdentifier") String loginIdentifier,
                               @FormParam("name") String name) {
        JSONObject jsonObject = changeInfo(loginIdentifier, new NameSetter(), name);
        return Response.ok(jsonObject.toString()).build();
    }

    /**
     * TODO: implement the service
     */
    @Path("/phone")
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response changePhone() {
        return null;
    }

    /**
     * Changes the user email address
     *
     * @param loginIdentifier The identifier which identifies the user
     * @param email           The new email address of the user
     * @return The JSON response object
     * {"Success": "The information has been set."}
     * {"Fail": "The user has been signed out."}
     */
    @Path("/email")
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response changeEmailAddress(@CookieParam("loginIdentifier") String loginIdentifier,
                                       @FormParam("email") String email) {
        JSONObject jsonObject = changeInfo(loginIdentifier, new EmailSetter(), email);
        return Response.ok(jsonObject.toString()).build();
    }

    /**
     * Changes the user gender
     *
     * @param loginIdentifier The identifier which identifies the user
     * @param gender          The new gender of the user
     * @return The JSON response object
     * {"Success": "The information has been set."}
     * {"Fail": "The user has been signed out."}
     */
    @Path("/gender")
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response changeGender(@CookieParam("loginIdentifier") String loginIdentifier,
                                 @FormParam("gender") int gender) {
        JSONObject jsonObject = changeInfo(loginIdentifier, new GenderSetter(), gender);
        return Response.ok(jsonObject.toString()).build();
    }

    /**
     * Setting user information interface.
     */
    private interface InfoSetter {
        /**
         * Sets the user information
         *
         * @param user      The user object
         * @param attribute The new value which will be set for the user
         */
        void set(User user, Object attribute);
    }

    /**
     * The class is for setting user name
     */
    private class NameSetter implements InfoSetter {
        /**
         * Sets the user name
         *
         * @param user      The user object
         * @param attribute The new name of the user
         */
        @Override
        public void set(User user, Object attribute) {
            user.setName((String) attribute);
        }
    }

    /**
     * The class is for setting user email address
     */
    private class EmailSetter implements InfoSetter {
        /**
         * Sets user email address
         *
         * @param user      The user object
         * @param attribute The new email address of the user
         */
        @Override
        public void set(User user, Object attribute) {
            user.setEmailAddress((String) attribute);
        }
    }

    /**
     * The class is for setting the gender of the user
     */
    private class GenderSetter implements InfoSetter {
        /**
         * Sets user gender
         *
         * @param user      The user object
         * @param attribute The new gender value of the user
         */
        @Override
        public void set(User user, Object attribute) {
            user.setGender((Integer) attribute);
        }
    }

    /**
     * Changes the user information
     *
     * @param loginIdentifier The identifier which identifies the user
     * @param setter          The attribute setter
     * @param info            The new data which needs to be set for the user
     * @return The JSON response object
     * {"Success": "The information has been set."}
     * {"Fail": "The user has been signed out."}
     */
    private JSONObject changeInfo(String loginIdentifier, InfoSetter setter, Object info) {
        JSONObject jsonObject = new JSONObject();
        try (final Session session = SessionProvider.getSession()) {
            Transaction transaction = session.beginTransaction();

            UserManagement userManagement = new UserManagement(session);

            User user = new User();
            user.setLoginIdentifier(loginIdentifier);

            if (userManagement.isExist(user)) {
                user = userManagement.get(loginIdentifier);
                setter.set(user, info);
                jsonObject.put("Success", "The information has been set.");
            } else {
                jsonObject = JSONResponseGenerator.formSignedOutJSON();
            }

            transaction.commit();
        }
        return jsonObject;
    }
}
