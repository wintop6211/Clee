package main.java.json;

import main.java.services.helpers.RatingCalculator;
import main.java.entities.PendingRequest;
import main.java.entities.Product;
import main.java.entities.User;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.List;

public class JSONResponseGenerator {
    public static JSONObject formUserHasExistedJSON() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("Fail", "The user has existed.");
        return jsonObject;
    }

    public static JSONObject formMessagingExceptionJSON(MessagingException e) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("Error", "The email cannot be sent to the destination.");
        jsonObject.put("ErrorCode", 5);
        jsonObject.put("ErrorDetail", e.toString());
        return jsonObject;
    }

    public static JSONObject formHibernateExceptionJSON(HibernateException e) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("Error", "Errors happen on interacting with the database.");
        jsonObject.put("ErrorCode", 2);
        jsonObject.put("ErrorDetail", e.toString());
        return jsonObject;
    }

    public static JSONObject formImageIOExceptionWhenWriteJSON(IOException e) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("Error", "The image cannot be saved to server disk.");
        jsonObject.put("ErrorCode", 3);
        jsonObject.put("ErrorDetail", e.toString());
        return jsonObject;
    }

    public static JSONObject formImageIOExceptionWhenReadJSON(IOException e) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("Error", "The image cannot be read from the server disk.");
        jsonObject.put("ErrorDetail", e.toString());
        return jsonObject;
    }

    public static JSONObject formWebPageIOExceptionJSON(IOException e) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("Error", "The web page source cannot be found from the server.");
        jsonObject.put("ErrorDetail", e.toString());
        return jsonObject;
    }

    public static JSONObject formUnknownExceptionJSON(Exception e) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("Error", e.toString());
        return jsonObject;
    }

    public static JSONObject formSignedOutJSON() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("Fail", "The user has been signed out.");
        return jsonObject;
    }

    public static JSONObject formAnotherUserUsingEmailOrPhoneJSON() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("Fail", "The another user is using the same email address or phone number.");
        return jsonObject;
    }

    public static JSONObject formUserNameAndPasswordAreNotCorrectJSON() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("Fail", "The username and password are not correct.");
        return jsonObject;
    }

    public static JSONObject formUserAuthenticatedJSON() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("Success", "The user has been authenticated.");
        return jsonObject;
    }

    public static JSONObject formBasicItemInfoJSON(Session session, Product product) {
        JSONObject itemJSON = new JSONObject();
        itemJSON.put("id", product.getIdProduct());
        itemJSON.put("name", product.getName());
        itemJSON.put("price", product.getPrice());
        itemJSON.put("condition", product.getCondition());
        itemJSON.put("status", product.getSold());
        itemJSON.put("description", product.getDescription());
        User seller = product.getUserByIdSeller();
        itemJSON.put("seller", JSONResponseGenerator.formUserBasicInfoJSON(session, seller));
        return itemJSON;
    }

    /**
     * Forms user information in JSON format.
     * @param session The session for interacting with the database
     * @param user The user object
     * @return The JSON format information
     * For example:
     * {
     *     "id": 4,
     *     "name": "Harry Liang",
     *     "email": "thisisemail@gmail.com",
     *     "gender": 0 (this is a number, and how to handle this number is determined on the client side),
     *     "phone": "4141234567"
     * }
     */
    public static JSONObject formUserBasicInfoJSON(Session session, User user) {
        JSONObject userJSON = new JSONObject();
        userJSON.put("id", user.getIdUser());
        userJSON.put("name", user.getName());
        userJSON.put("email", user.getEmailAddress());
        userJSON.put("gender", user.getGender());
        userJSON.put("phone", user.getPhone());

        List sellerRatings = session.createQuery("select avg(stars) from SellerReview where userIdUser=" + user.getIdUser()).list();
        Double sellerRating = (Double) sellerRatings.get(0);
        List buyerRatings = session.createQuery("select avg(stars) from BuyerReview where userIdUser=" + user.getIdUser()).list();
        Double buyerRating = (Double) buyerRatings.get(0);
        if (sellerRating != null) {
            sellerRating = RatingCalculator.roundToHalf(sellerRating);
            userJSON.put("sellerRating", sellerRating);
        } else {
            userJSON.put("sellerRating", -1);
        }
        if (buyerRating != null) {
            buyerRating = RatingCalculator.roundToHalf(buyerRating);
            userJSON.put("buyerRating", buyerRating);
        } else {
            userJSON.put("buyerRating", -1);
        }

        userJSON.put("verified", user.getVerified());
        userJSON.put("school", user.getSchoolBySchoolIdSchool().getName());
        return userJSON;
    }

    /**
     * Forms request information in JSON format.
     * @param session The session for interacting with the database
     * @param request The purchasing request object
     * @return The JSON format information
     * For example:
     * {
     *     "id": 5,
     *     "price": 200,
     *     "note": "Please send me an email if you want to schedule a time",
     *     "buyer": {
     *          "id": 4,
     *          "name": "Harry Liang",
     *          "email": "thisisemail@gmail.com",
     *          "gender": 0 (this is a number, and how to handle this number is determined on the client side),
     *          "phone": "4141234567"
     *     }
     * }
     */
    public static JSONObject formRequestJSON(Session session, PendingRequest request) {
        User buyer = request.getUserByIdBuyer();
        JSONObject buyerJSON = JSONResponseGenerator.formUserBasicInfoJSON(session, buyer);
        JSONObject requestJSON = new JSONObject();
        requestJSON.put("id", request.getIdPendingRequest());
        requestJSON.put("price", request.getOfferPrice());
        if (request.getNote() == null) {
            requestJSON.put("note", "");
        } else {
            requestJSON.put("note", request.getNote());
        }
        requestJSON.put("buyer", buyerJSON);
        return requestJSON;
    }

    public static JSONArray formItemListJSON(Session session, List items) {
        JSONArray itemsJSON = new JSONArray();
        for (Object object : items) {
            Product product = (Product) object;
            JSONObject itemJSON = JSONResponseGenerator.formBasicItemInfoJSON(session, product);
            itemsJSON.put(itemJSON);
        }
        return itemsJSON;
    }

    public static JSONObject formTrueJSON() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("Success", "True");
        return jsonObject;
    }

    public static JSONObject formFalseJSON() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("Success", "False");
        return jsonObject;
    }
}
