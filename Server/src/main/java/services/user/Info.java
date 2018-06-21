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
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * The class contains web services for retrieving the user information from the database
 */
@Path("/user")
public class Info {
    /**
     * Gets the user information by using the login identifier
     * @param loginIdentifier The identifier which identifies the user
     * @return The JSON response object
     * For example:
     *
     */
    @Path("/get")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response requestUserInfo(@CookieParam("loginIdentifier") String loginIdentifier) {
        JSONObject jsonObject = getUserInfo(loginIdentifier, 0);
        return Response.ok(jsonObject.toString()).build();
    }

    /**
     * Gets the user information by using user's id
     * @param loginIdentifier The identifier which identifies the user
     * @param id The id of the user whom you are looking for
     * @return The JSON response object
     */
    @Path("/get/{targetId}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response requestUserInfoById(@CookieParam("loginIdentifier") String loginIdentifier,
                                        @PathParam("targetId") int id) {
        JSONObject jsonObject = getUserInfo(loginIdentifier, id);
        return Response.ok(jsonObject.toString()).build();
    }

    /**
     * Checks if the email has existed in the database
     * @param emailAddress The email address which needs to be checked
     * @return The JSON response object
     * {"Success": "True"}
     * {"Success": "False"}
     */
    @Path("/email/check")
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    public Response isEmailExisted(@FormParam("emailAddress") String emailAddress) {
        JSONObject jsonObject;
        try (final Session session = SessionProvider.getSession()) {
            Transaction transaction = session.beginTransaction();
            UserManagement userManagement = new UserManagement(session);

            User user = new User();
            user.setEmailAddress(emailAddress);
            if (userManagement.isExist(user)) {
                jsonObject = JSONResponseGenerator.formTrueJSON();
            } else {
                jsonObject = JSONResponseGenerator.formFalseJSON();
            }

            transaction.commit();
        }
        return Response.ok(jsonObject.toString()).build();
    }

    /**
     * Checks if the phone has existed in the database
     * @param phone The phone number which needs to be checked
     * @return The JSON response object
     * {"Success": "True"}
     * {"Success": "False"}
     */
    @Path("/phone/check")
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    public Response isPhoneExisted(@FormParam("phone") String phone) {
        JSONObject jsonObject;
        try (final Session session = SessionProvider.getSession()) {
            Transaction transaction = session.beginTransaction();
            UserManagement userManagement = new UserManagement(session);

            User user = new User();
            user.setPhone(phone);
            if (userManagement.isExist(user)) {
                jsonObject = JSONResponseGenerator.formTrueJSON();
            } else {
                jsonObject = JSONResponseGenerator.formFalseJSON();
            }

            transaction.commit();
        }
        return Response.ok(jsonObject.toString()).build();
    }

    /**
     * Gets the user information
     * @param loginIdentifier The identifier which identifies the user
     * @param userId The id of the user
     * @return The JSON response object
     * {
     *      "Success": {
     *          "id": 4,
     *          "name": "Harry Liang",
     *          "email": "thisisemail@gmail.com",
     *          "gender": 0 (this is a number, and how to handle this number is determined on the client side),
     *          "phone": "4141234567"
     *      }
     * }
     * {"Fail": "The user has been signed out."}
     */
    private JSONObject getUserInfo(String loginIdentifier, int userId) {
        JSONObject jsonObject = new JSONObject();
        try (final Session session = SessionProvider.getSession()) {
            Transaction transaction = session.beginTransaction();
            UserManagement userManagement = new UserManagement(session);

            // get the corresponding user
            User user = new User();
            user.setLoginIdentifier(loginIdentifier);
            if (userManagement.isExist(user)) {
                if (userId == 0) {
                    user = userManagement.get(loginIdentifier);
                } else {
                    user = userManagement.get(userId);
                }
                JSONObject userInfo = JSONResponseGenerator.formUserBasicInfoJSON(session, user);
                jsonObject.put("Success", userInfo);
            } else {
                jsonObject = JSONResponseGenerator.formSignedOutJSON();
            }

            transaction.commit();
        }
        return jsonObject;
    }
}
