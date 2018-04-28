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
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static main.java.services.product.purchase.APNConnection.getAPNConnectorFromSession;

@Path("/ProductServices")
public class Request {

    @Context
    HttpServletRequest request;

    @Path("/requestPurchasingItem")
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response purchaseItem(@CookieParam("loginIdentifier") String loginIdentifier,
                                 @FormParam("offerPrice") double offerPrice,
                                 @FormParam("note") String note,
                                 @FormParam("itemId") int itemId)
            throws InvalidKeyException, NoSuchAlgorithmException, IOException, ExecutionException, InterruptedException {
        JSONObject jsonObject = new JSONObject();
        try (final Session session = SessionProvider.getSession()) {
            Transaction transaction = session.beginTransaction();

            UserManagement userManagement = new UserManagement(session);
            ProductManagement productManagement = new ProductManagement(session);
            PendingRequestManagement pendingRequestManagement = new PendingRequestManagement(session);

            User user = new User();
            user.setLoginIdentifier(loginIdentifier);
            Product product = new Product();
            product.setIdProduct(itemId);

            if (userManagement.isExist(user)) {
                user = userManagement.get(loginIdentifier);
                if (user.getVerified() == 1) {
                    if (productManagement.isExist(product)) {
                        product = productManagement.get(itemId);
                        if (product.getSold() != 1) {
                            User seller = product.getUserByIdSeller();
                            if (!seller.getIdUser().equals(user.getIdUser())) {
                                PendingRequest pendingRequest = new PendingRequest();
                                pendingRequest.setIdBuyer(user.getIdUser());
                                pendingRequest.setIdSeller(seller.getIdUser());
                                pendingRequest.setIdProduct(itemId);
                                if (!pendingRequestManagement.isExist(pendingRequest)) {
                                    // create the pending request
                                    pendingRequest = new PendingRequest();
                                    PendingRequestManagement.setPendingRequestBasicInfo(pendingRequest, user, seller, product, note, offerPrice);
                                    pendingRequestManagement.add(pendingRequest);
                                    // send the notification to the seller
                                    APNConnector connector = getAPNConnectorFromSession(request);
                                    Collection devices = seller.getDevicesByIdUser();
                                    for (Object object : devices) {
                                        Device device = (Device) object;
                                        connector.sendPurchaseRequestNotification(device.getIdDevice(), user.getName());
                                    }
                                    jsonObject.put("Success", "The product is pending for you.");
                                } else {
                                    jsonObject.put("Fail", "You have sent the purchase request for the item.");
                                }
                            } else {
                                jsonObject.put("Fail", "You cannot purchase your own product.");
                            }
                        } else {
                            jsonObject.put("Fail", "The product has been sold.");
                        }
                    } else {
                        jsonObject.put("Fail", "The product has been removed by the seller.");
                    }
                } else {
                    jsonObject.put("Fail", "The user has not been verified.");
                }
            } else {
                jsonObject = JSONResponseGenerator.formSignedOutJSON();
            }

            transaction.commit();
        }
        return Response.ok(jsonObject.toString()).build();
    }

    @Path("/getRequestNumber/{itemId}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRequestNumber(@PathParam("itemId") int itemId) {
        JSONObject jsonObject = new JSONObject();
        try (final Session session = SessionProvider.getSession()) {
            Transaction transaction = session.beginTransaction();
            List pendingRequests = session.createQuery("from PendingRequest where idProduct=" + itemId).list();
            int size = pendingRequests.size();
            jsonObject.put("Success", size);
            transaction.commit();
        }
        return Response.ok(jsonObject.toString()).build();
    }
}
