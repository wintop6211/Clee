package main.java.services.user;

import main.java.configuration.SessionProvider;
import main.java.entities.SellerReview;
import main.java.entities.User;
import main.java.entities.managements.SellerReviewManagement;
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
 * The class contains web services for rating users
 */
@Path("/user")
public class Rate {
    /**
     * Rates the user when he/she acts as the seller
     * @param loginIdentifier The identifier which identifies the user
     * @param sellerId The id of the seller
     * @param rate The rate which will be given to the user
     * @return The JSON response
     * {"Success": "The rate has been posted."}
     * {"Fail": "The user has been signed out."}
     */
    @Path("/rate/seller")
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response rateSeller(@CookieParam("loginIdentifier") String loginIdentifier,
                               @FormParam("sellerId") int sellerId,
                               @FormParam("rate") double rate) {
        JSONObject jsonObject = new JSONObject();
        try (final Session session = SessionProvider.getSession()) {
            Transaction transaction = session.beginTransaction();

            UserManagement userManagement = new UserManagement(session);
            SellerReviewManagement sellerReviewManagement = new SellerReviewManagement(session);

            User user = new User();
            user.setLoginIdentifier(loginIdentifier);

            if (userManagement.isExist(user)) {
                SellerReview sellerReview = new SellerReview();
                sellerReview.setUserIdUser(sellerId);
                sellerReview.setStars(rate);
                sellerReviewManagement.add(sellerReview);
                jsonObject.put("Success", "The rate has been posted.");
            } else {
                jsonObject = JSONResponseGenerator.formSignedOutJSON();
            }

            transaction.commit();
        }
        return Response.ok(jsonObject).build();
    }

    /**
     * TODO: implement the service
     * Rates the user when he/she acts as the buyer
     * @return The JSON response
     */
    @Path("/rate/buyer")
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response rateBuyer() {
        return null;
    }
}
