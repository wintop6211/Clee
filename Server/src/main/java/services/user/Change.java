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

@Path("/UserServices/change")
public class Change {
    @Path("/name")
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response changeName(@CookieParam("loginIdentifier") String loginIdentifier,
                               @FormParam("name") String name) {
        JSONObject jsonObject = changeInfo(loginIdentifier, new NameSetter(), name);
        return Response.ok(jsonObject.toString()).build();
    }

    @Path("/phone")
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response changePhone() {
        return null;
    }

    @Path("/email")
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response changeEmailAddress(@CookieParam("loginIdentifier") String loginIdentifier,
                                       @FormParam("email") String email) {
        JSONObject jsonObject = changeInfo(loginIdentifier, new EmailSetter(), email);
        return Response.ok(jsonObject.toString()).build();
    }

    @Path("/gender")
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response changeGender(@CookieParam("loginIdentifier") String loginIdentifier,
                                 @FormParam("gender") int gender) {
        JSONObject jsonObject = changeInfo(loginIdentifier, new GenderSetter(), gender);
        return Response.ok(jsonObject.toString()).build();
    }

    private interface InfoSetter {
        void set(User user, Object attribute);
    }

    private class NameSetter implements InfoSetter {
        @Override
        public void set(User user, Object attribute) {
            user.setName((String) attribute);
        }
    }

    private class EmailSetter implements InfoSetter {
        @Override
        public void set(User user, Object attribute) {
            user.setEmailAddress((String) attribute);
        }
    }

    private class GenderSetter implements InfoSetter {
        @Override
        public void set(User user, Object attribute) {
            user.setGender((Integer) attribute);
        }
    }

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
