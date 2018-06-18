package main.java.services.school;

import main.java.configuration.SessionProvider;
import main.java.entities.User;
import main.java.entities.managements.ImageManagement;
import main.java.entities.managements.UserManagement;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.ws.rs.CookieParam;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import java.io.ByteArrayInputStream;
import java.io.IOException;

/**
 * The class contains services for managing school logos
 */
@Path("/user")
public class Logo {
    /**
     * The service is for getting the school logo for the user.
     *
     * @param loginIdentifier The identifier which identifies the user
     * @return The school logo image raw data
     * @throws IOException if the school image cannot be found
     */
    @Path("/get/school/image")
    @GET
    @Produces("image/jpeg")
    public Response getSchoolLogo(@CookieParam("loginIdentifier") String loginIdentifier) throws IOException {
        byte[] image = new byte[0];
        try (final Session session = SessionProvider.getSession()) {
            Transaction transaction = session.beginTransaction();

            UserManagement userManagement = new UserManagement(session);

            // get the user's school logo
            User user = new User();
            user.setLoginIdentifier(loginIdentifier);

            if (userManagement.isExist(user)) {
                user = userManagement.get(loginIdentifier);
                image = ImageManagement.getUserSchoolImage(user);
            }

            transaction.commit();
        }
        return Response.ok(new ByteArrayInputStream(image)).build();
    }
}
