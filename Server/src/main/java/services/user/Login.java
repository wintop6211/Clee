package main.java.services.user;

import main.java.configuration.SessionProvider;
import main.java.entities.User;
import main.java.entities.managements.UserManagement;
import main.java.json.JSONResponseGenerator;
import main.java.status.manager.LoginStatusManager;
import org.apache.commons.codec.digest.DigestUtils;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.json.JSONObject;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.CookieParam;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * The class contains web services for login users
 */
@Path("/user")
public class Login {

    @Context
    HttpServletResponse response;

    /**
     * Checks if the user has logged in
     * @param loginIdentifier The identifier which identifies the user
     * @return The JSON response object
     * {"Success": "True"}
     * {"Success": "False"}
     */
    @Path("/login/check")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response isLoggedIn(@CookieParam("loginIdentifier") String loginIdentifier) {
        JSONObject jsonObject;
        try (final Session session = SessionProvider.getSession()) {
            Transaction transaction = session.beginTransaction();
            UserManagement userManagement = new UserManagement(session);

            // determine if the user exists
            User user = new User();
            user.setLoginIdentifier(loginIdentifier);
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
     * Login the user
     * @param emailAddress The email address
     * @param password The password
     * @return The JSON response object
     * {"Success": "The user has been authenticated."}
     * {"Fail": "The username and password are not correct."}
     */
    @Path("/login")
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response login(@FormParam("emailAddress") String emailAddress,
                          @FormParam("password") String password) {
        password = DigestUtils.sha256Hex(password); // Hash the password
        JSONObject jsonObject;
        try (final Session session = SessionProvider.getSession()) {
            Transaction transaction = session.beginTransaction();
            UserManagement userManagement = new UserManagement(session);

            User user = new User();
            user.setEmailAddress(emailAddress);

            if (userManagement.isExist(user)) {
                user = userManagement.getByEmail(emailAddress);
                // determine if the user's password is equal to the password provided
                if (user.getPassword().equals(password)) {
                    jsonObject = JSONResponseGenerator.formUserAuthenticatedJSON();
                    LoginStatusManager.refreshLoginCookieStatus(response, session, user);
                } else {
                    jsonObject = JSONResponseGenerator.formUserNameAndPasswordAreNotCorrectJSON();
                }
            } else {
                jsonObject = JSONResponseGenerator.formUserNameAndPasswordAreNotCorrectJSON();
            }

            transaction.commit();
        }
        return Response.ok(jsonObject.toString()).build();
    }

    /**
     * Logs out the user
     * @param loginIdentifier The identifier which identifies the user
     * @return The JSON response object
     * {"Success": "You are signed out."}
     * {"Fail": "The user has been signed out."}
     */
    @Path("/logout")
    @POST
    public Response logout(@CookieParam("loginIdentifier") String loginIdentifier) {
        JSONObject jsonObject = new JSONObject();
        try (final Session session = SessionProvider.getSession()) {
            Transaction transaction = session.beginTransaction();
            UserManagement userManagement = new UserManagement(session);

            // reset the user's login identifier
            User user = new User();
            user.setLoginIdentifier(loginIdentifier);
            if (userManagement.isExist(user)) {
                user = userManagement.get(loginIdentifier);
                user.setLoginIdentifier(null);
                userManagement.set(user);
                jsonObject.put("Success", "You are signed out.");
            } else {
                jsonObject = JSONResponseGenerator.formSignedOutJSON();
            }

            transaction.commit();
        }
        return Response.ok(jsonObject.toString()).build();
    }
}
