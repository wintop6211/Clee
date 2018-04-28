package main.java.services.product.purchase;

import main.java.configuration.SessionProvider;
import main.java.entities.Device;
import main.java.entities.PendingRequest;
import main.java.entities.Product;
import main.java.entities.User;
import main.java.entities.managements.PendingRequestManagement;
import main.java.entities.managements.ProductManagement;
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

import static main.java.services.product.purchase.APNConnection.getAPNConnectorFromSession;

@Path("/product/request")
public class Confirm {

    @Context
    HttpServletRequest request;

    @Path("/confirm")
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response confirmPurchaseRequest(@CookieParam("loginIdentifier") String loginIdentifier,
                                           @FormParam("requestId") int requestId)
            throws NoSuchAlgorithmException, InvalidKeyException,
            IOException, ExecutionException, InterruptedException {
        JSONObject jsonObject = new JSONObject();
        try (final Session session = SessionProvider.getSession()) {
            Transaction transaction = session.beginTransaction();

            UserManagement userManagement = new UserManagement(session);
            PendingRequestManagement pendingRequestManagement = new PendingRequestManagement(session);
            ProductManagement productManagement = new ProductManagement(session);

            User user = new User();
            user.setLoginIdentifier(loginIdentifier);

            if (userManagement.isExist(user)) {
                PendingRequest pendingRequest = new PendingRequest();
                pendingRequest.setIdPendingRequest(requestId);
                if (pendingRequestManagement.isExist(pendingRequest)) {
                    pendingRequest = pendingRequestManagement.get(requestId);
                    Product product = pendingRequest.getProductByIdProduct();
                    int buyerId = pendingRequest.getIdBuyer();
                    int sellerId = pendingRequest.getIdSeller();
                    product.setIdBuyer(buyerId);
                    product.setIdSeller(sellerId);
                    product.setSold(1);
                    productManagement.set(product);
                    // delete all pending requests
                    int idProduct = product.getIdProduct();
                    pendingRequestManagement.remove(idProduct);
                    APNConnector connector = getAPNConnectorFromSession(request);
                    Collection<Device> devices = pendingRequest.getUserByIdBuyer().getDevicesByIdUser();
                    String sellerName = pendingRequest.getUserByIdSeller().getName();
                    for (Device device : devices) {
                        String deviceId = device.getIdDevice();
                        connector.sendConfirmRequestNotification(deviceId, sellerName, product.getName());
                    }
                    jsonObject.put("Success", "The request is confirmed.");
                } else {
                    jsonObject.put("Fail", "The request was canceled by the buyer.");
                }
            } else {
                jsonObject = JSONResponseGenerator.formSignedOutJSON();
            }

            transaction.commit();
        }
        return Response.ok(jsonObject.toString()).build();
    }
}
