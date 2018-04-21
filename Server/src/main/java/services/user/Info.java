package main.java.services.user;

import main.java.configuration.SessionProvider;
import main.java.entities.User;
import main.java.entities.managements.UserManagement;
import main.java.json.JSONResponseGenerator;
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

@Path("/UserServices")
public class Info {
    @Path("/requestUserInfo")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response requestUserInfo(@CookieParam("loginIdentifier") String loginIdentifier) {
        JSONObject jsonObject = getUserInfo(loginIdentifier, 0);
        return Response.ok(jsonObject.toString()).build();
    }

    @Path("/requestUserInfoById/{targetId}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response requestUserInfoById(@CookieParam("loginIdentifier") String loginIdentifier,
                                        @PathParam("targetId") int id) {
        JSONObject jsonObject = getUserInfo(loginIdentifier, id);
        return Response.ok(jsonObject.toString()).build();
    }

    @Path("/isEmailExisted")
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
        } catch (Exception e) {
            e.printStackTrace();
            jsonObject = JSONResponseGenerator.formUnknownExceptionJSON(e);
        }
        return Response.ok(jsonObject.toString()).build();
    }

    @Path("/isPhoneExisted")
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
        } catch (Exception e) {
            e.printStackTrace();
            jsonObject = JSONResponseGenerator.formUnknownExceptionJSON(e);
        }
        return Response.ok(jsonObject.toString()).build();
    }

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
        } catch (HibernateException e) {
            jsonObject = JSONResponseGenerator.formHibernateExceptionJSON(e);
        } catch (Exception e) {
            jsonObject = JSONResponseGenerator.formUnknownExceptionJSON(e);
        }
        return jsonObject;
    }
}
