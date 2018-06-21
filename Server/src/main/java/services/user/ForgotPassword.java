package main.java.services.user;

import main.java.configuration.SessionProvider;
import main.java.email.EmailController;
import main.java.entities.User;
import main.java.entities.managements.UserManagement;
import main.java.services.helpers.PathManager;
import main.java.status.manager.LoginStatusManager;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.json.JSONObject;

import javax.mail.MessagingException;
import javax.servlet.ServletContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.CookieParam;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static main.java.services.helpers.WebPageGetter.getWebPage;

/**
 * The class contains web services for retrieving back user's password
 */
@Path("/user/password")
public class ForgotPassword {

    @Context
    ServletContext context;
    @Context
    HttpServletResponse response;

    /**
     * Sends the forgot password email which contains the link for resetting the password
     * @param emailAddress The email address of the user
     * @return The JSON response object
     * {"Success": "The email has been sent."}
     * {"Fail": "The user does not exist."}
     * @throws MessagingException The error which will be thrown when the email cannot be delivered
     */
    @Path("/forgot")
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response forgotPassword(@FormParam("emailAddress") String emailAddress) throws MessagingException {
        JSONObject jsonObject = new JSONObject();
        try (final Session session = SessionProvider.getSession()) {
            Transaction transaction = session.beginTransaction();
            UserManagement userManagement = new UserManagement(session);

            User user = new User();
            user.setEmailAddress(emailAddress);
            if (userManagement.isExist(user)) {
                user = userManagement.getByEmail(emailAddress);
                String uuid = LoginStatusManager.resetLoginIdentifier(session, user);
                EmailController.sendForgotPasswordEmail(emailAddress, uuid);
                jsonObject.put("Success", "The email has been sent.");
            } else {
                jsonObject.put("Fail", "The user does not exist.");
            }

            transaction.commit();
        }
        return Response.ok(jsonObject.toString()).build();
    }

    /**
     * Retrieves the resetting password HTML page
     * @param loginIdentifier The identifier which temporarily identifies the user
     * @return The HTML web page
     * @throws IOException if the HTML page file cannot be found
     */
    @Path("/change/{loginIdentifier}")
    @GET
    @Produces(MediaType.TEXT_HTML)
    public InputStream changePassword(@PathParam("loginIdentifier") String loginIdentifier) throws IOException {
        InputStream inputStream;
        try (final Session session = SessionProvider.getSession()) {
            Transaction transaction = session.beginTransaction();

            UserManagement userManagement = new UserManagement(session);

            User user = new User();
            user.setLoginIdentifier(loginIdentifier);

            if (userManagement.isExist(user)) {
                createChangePasswordCookie(loginIdentifier);
                inputStream = getWebPage(context, "/PasswordChange/ChangePassword.html");
            } else {
                inputStream = getWebPage(context, "/PasswordChange/ExpiredLinkWarning.html");
            }

            transaction.commit();
        }
        return inputStream;
    }

    /**
     * Resets the user record in the database. Also, stores the result cookie on the client side
     * @param loginIdentifier The identifier which identifies the user
     * @param password The new password for the user
     * @return The JSON object response
     * {"link": "<the link to for getting the result web page>"}
     */
    @Path("/change")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response changePasswordPersistently(@CookieParam("loginIdentifier") String loginIdentifier,
                                               @FormParam("password") String password) {
        Cookie cookie;
        try (final Session session = SessionProvider.getSession()) {
            Transaction transaction = session.beginTransaction();
            List users = session.createQuery("from User where loginIdentifier='" + loginIdentifier + "'").list();
            if (users.size() == 1) {
                User user = (User) users.get(0);
                user.setPassword(password);
                session.update(user);
                cookie = new Cookie("changedPassword", "successful");
                user.setLoginIdentifier(null);
            } else {
                cookie = new Cookie("changedPassword", "expired");
            }
            transaction.commit();
        } catch (Exception e) {
            cookie = new Cookie("changedPassword", "error");
        }

        response.addCookie(cookie);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("link", PathManager.getURL("services/user/changePassword/finish"));
        return Response.ok(jsonObject.toString()).build();
    }

    /**
     * Display the result page for the user.
     * @param changedPassword The result of resetting the password for the user
     * @return The response web page
     * @throws IOException if the web page cannot be found
     */
    @Path("/change/finish")
    @GET
    @Produces(MediaType.TEXT_HTML)
    public InputStream successChangePassword(@CookieParam("changedPassword") String changedPassword) throws IOException {
        InputStream inputStream;
        switch (changedPassword) {
            case "successful":
                inputStream = getWebPage(context, "/PasswordChange/ChangePasswordSuccess.html");
                break;
            case "usr-non-exist":
                inputStream = getWebPage(context, "/UserDoesNotExist.html");
                break;
            case "error":
                // error happened.
                inputStream = getWebPage(context, "/ExceptionThrown.html");
                break;
            default:
                inputStream = getWebPage(context, "/PasswordChange/ExpiredLinkWarning.html");
        }
        return inputStream;
    }

    /**
     * Creates the cookie for temporarily identifying the user
     * @param loginIdentifier The identifier which identifies the user
     */
    private void createChangePasswordCookie(String loginIdentifier) {
        Cookie cookie = new Cookie("loginIdentifier", loginIdentifier);
        cookie.setPath(PathManager.getChangePasswordCookiePath("services/UserServices/changePassword"));
        response.addCookie(cookie);
    }
}
