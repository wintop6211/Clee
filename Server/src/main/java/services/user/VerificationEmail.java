package main.java.services.user;

import main.java.configuration.SessionProvider;
import main.java.email.EmailController;
import main.java.entities.User;
import main.java.json.JSONResponseGenerator;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.json.JSONObject;

import javax.mail.MessagingException;
import javax.servlet.ServletContext;
import javax.ws.rs.CookieParam;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static main.java.services.helpers.WebPageGetter.getWebPage;

@Path("/UserServices")
public class VerificationEmail {

    @Context
    ServletContext context;

    @Path("/verifyEmailAddress/{loginIdentifier}")
    @GET
    @Produces(MediaType.TEXT_HTML)
    public InputStream verifyEmailAddress(@PathParam("loginIdentifier") String loginIdentifier) {
        FileInputStream fileInputStream = null;
        try (final Session session = SessionProvider.getSession()) {
            Transaction transaction = session.beginTransaction();
            List users = session.createQuery("from User where loginIdentifier='" + loginIdentifier + "'").list();
            if (users.size() == 1) {
                User user = (User) users.get(0);
                if (user.getVerified() == 1) {
                    fileInputStream = getWebPage(context, "/EmailConfirmationWebPages/AlreadyActivated.html");
                } else {
                    user.setVerified(1);
                    fileInputStream = getWebPage(context, "/EmailConfirmationWebPages/ConfirmEmail.html");
                }
            } else {
                fileInputStream = getWebPage(context, "/EmailConfirmationWebPages/ExpiredLinkWarning.html");
            }
            transaction.commit();
        } catch (Exception e) {
            try {
                fileInputStream = getWebPage(context, "/ExceptionThrown.html");
            } catch (IOException tempE) {
                tempE.printStackTrace();
            }
        }
        return fileInputStream;
    }

    @Path("/sendVerificationEmail")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response sendVerificationEmail(@CookieParam("loginIdentifier") String loginIdentifier) {
        JSONObject jsonObject = new JSONObject();
        try (final Session session = SessionProvider.getSession()) {
            Transaction transaction = session.beginTransaction();
            List users = session.createQuery("from User where loginIdentifier='" + loginIdentifier + "'").list();
            if (users.size() == 1) {
                User user = (User) users.get(0);
                String email = user.getEmailAddress();
                EmailController.sendVerificationEmail(email, loginIdentifier);
                jsonObject.put("Success", "The email has been sent.");
            } else {
                jsonObject = JSONResponseGenerator.formSignedOutJSON();
            }
            transaction.commit();
        } catch (MessagingException e) {
            jsonObject = JSONResponseGenerator.formMessagingExceptionJSON(e);
        } catch (Exception e) {
            jsonObject = JSONResponseGenerator.formUnknownExceptionJSON(e);
        }
        return Response.ok(jsonObject.toString()).build();
    }
}
