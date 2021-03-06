package main.java.services.product.purchase;

import main.java.configuration.SessionProvider;
import main.java.entities.Device;
import main.java.entities.PendingRequest;
import main.java.entities.Product;
import main.java.entities.User;
import main.java.entities.managements.PendingRequestManagement;
import main.java.entities.managements.UserManagement;
import main.java.json.JSONResponseGenerator;
import main.java.notification.APNConnector;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.CookieParam;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Collection;
import java.util.concurrent.ExecutionException;

/**
 * The class contains web services for canceling purchase requests for users.
 */
@Path("/product/request")
public class Cancel {

    @Context
    HttpServletRequest request;

    private enum UserType {
        BUYER,
        SELLER
    }

    /**
     * Cancels the purchase request for the seller.
     *
     * @param loginIdentifier The identifier which identifies the user
     * @param requestId       The id of the purchase request
     * @return {"Success": "The pending request is canceled."}
     * {"Fail": "The request does not exist."}
     * {"Fail": "The user has not been verified."}
     * {"Fail": "The user has been signed out."}
     * @throws InterruptedException     if the current thread was interrupted while waiting
     * @throws ExecutionException       if the computation threw an exception
     * @throws NoSuchAlgorithmException if the JVM does not support elliptic curve keys
     * @throws InvalidKeyException      if the signed key is invalid
     * @throws IOException              if the key file cannot be found
     */
    @Path("/cancel/seller")
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response cancelPurchaseRequestForSeller(@CookieParam("loginIdentifier") String loginIdentifier,
                                                   @FormParam("requestId") int requestId)
            throws InterruptedException, ExecutionException, NoSuchAlgorithmException, InvalidKeyException, IOException {
        JSONObject jsonObject = cancelPurchaseRequest(UserType.SELLER, loginIdentifier, requestId);
        return Response.ok(jsonObject.toString()).build();
    }

    /**
     * Cancels the purchase request for the buyer.
     *
     * @param loginIdentifier The identifier which identifies the user
     * @param requestId       The id of the purchase request
     * @return {"Success": "The pending request is canceled."}
     * {"Fail": "The request does not exist."}
     * {"Fail": "The user has not been verified."}
     * {"Fail": "The user has been signed out."}
     * @throws InterruptedException     if the current thread was interrupted while waiting
     * @throws ExecutionException       if the computation threw an exception
     * @throws NoSuchAlgorithmException if the JVM does not support elliptic curve keys
     * @throws InvalidKeyException      if the signed key is invalid
     * @throws IOException              if the key file cannot be found
     */
    @Path("/cancel/buyer")
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response cancelPurchaseRequestForBuyer(@CookieParam("loginIdentifier") String loginIdentifier,
                                                  @FormParam("requestId") int requestId)
            throws InterruptedException, ExecutionException, NoSuchAlgorithmException, InvalidKeyException, IOException {
        JSONObject jsonObject = cancelPurchaseRequest(UserType.BUYER, loginIdentifier, requestId);
        return Response.ok(jsonObject.toString()).build();
    }

    /**
     * Cancels the purchase request for the user
     * @param userType Buyer or Seller
     * @param loginIdentifier The identifier which identifies the user
     * @param requestId The id of the purchase request
     * @return The JSON response which will be returned to the client side
     * @throws NoSuchAlgorithmException if the JVM does not support elliptic curve keys
     * @throws InvalidKeyException if the signed key is invalid
     * @throws IOException if the key file cannot be found
     * @throws ExecutionException if the computation threw an exception
     * @throws InterruptedException if the current thread was interrupted while waiting
     */
    private JSONObject cancelPurchaseRequest(UserType userType, String loginIdentifier, int requestId)
            throws NoSuchAlgorithmException, InvalidKeyException, IOException, ExecutionException, InterruptedException {
        JSONObject jsonObject = new JSONObject();
        try (final Session session = SessionProvider.getSession()) {
            Transaction transaction = session.beginTransaction();

            UserManagement userManagement = new UserManagement(session);
            PendingRequestManagement pendingRequestManagement = new PendingRequestManagement(session);

            User user = new User();
            user.setLoginIdentifier(loginIdentifier);
            PendingRequest pendingRequest = new PendingRequest();
            pendingRequest.setIdPendingRequest(requestId);

            if (userManagement.isExist(user)) {
                user = userManagement.get(loginIdentifier);
                if (user.getVerified() == 1) {
                    if (pendingRequestManagement.isExist(pendingRequest)) {
                        pendingRequest = pendingRequestManagement.get(requestId);
                        pendingRequestManagement.remove(pendingRequest);
                        // Sends the notification
                        APNConnector connector = APNConnector.getAPNConnectorFromSession(request);
                        switch (userType) {
                            case BUYER:
                                User seller = pendingRequest.getUserByIdSeller();
                                Collection<Device> sellerDevices = seller.getDevicesByIdUser();
                                for (Device device : sellerDevices) {
                                    String idDevice = device.getIdDevice();
                                    Product product = pendingRequest.getProductByIdProduct();
                                    connector.sendCancelRequestNotification(idDevice, user.getName(), product.getName());
                                }
                                break;
                            case SELLER:
                                User buyer = pendingRequest.getUserByIdBuyer();
                                Collection<Device> buyerDevices = buyer.getDevicesByIdUser();
                                for (Device device : buyerDevices) {
                                    String idDevice = device.getIdDevice();
                                    Product product = pendingRequest.getProductByIdProduct();
                                    connector.sendCancelRequestNotification(idDevice, user.getName(), product.getName());
                                }
                                break;
                        }
                        jsonObject.put("Success", "The pending request is canceled.");
                    } else {
                        jsonObject.put("Fail", "The request does not exist.");
                    }
                } else {
                    jsonObject.put("Fail", "The user has not been verified.");
                }
            } else {
                jsonObject = JSONResponseGenerator.formSignedOutJSON();
            }

            transaction.commit();
        }
        return jsonObject;
    }
}
