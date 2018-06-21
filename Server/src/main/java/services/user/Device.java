package main.java.services.user;

import main.java.configuration.SessionProvider;
import main.java.entities.User;
import main.java.entities.managements.DeviceManagement;
import main.java.entities.managements.UserManagement;
import main.java.notification.APNConnector;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
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

/**
 * The class contains services for managing devices information in the database
 */
@Path("/user/device")
public class Device {

    @Context
    HttpServletRequest request;

    /**
     * Uploads the device to the database
     * @param deviceID The id of the mobile device
     * @return The JSON response object
     * {"Success": "The device has been uploaded."}
     */
    @Path("/upload")
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response uploadDevice(@FormParam("deviceID") String deviceID) {
        JSONObject jsonObject = new JSONObject();
        try (final Session session = SessionProvider.getSession()) {
            Transaction transaction = session.beginTransaction();
            DeviceManagement deviceManagement = new DeviceManagement(session);

            // add the device into the database
            main.java.entities.Device device = new main.java.entities.Device();
            device.setIdDevice(deviceID);
            if (!deviceManagement.isExist(device)) {
                deviceManagement.add(device);
            }
            jsonObject.put("Success", "The device has been uploaded.");

            transaction.commit();
        }
        return Response.ok(jsonObject.toString()).build();
    }

    /**
     * Binds the device with the user
     * @param loginIdentifier The identifier which identifies the user
     * @param deviceID The id of the device
     * @return The JSON response object
     * {"Success": "The device has been bind with the user."}
     * {"Fail": "The user has not logged in yet."}
     */
    @Path("/bind")
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response bindDevice(@CookieParam("loginIdentifier") String loginIdentifier,
                               @FormParam("deviceID") String deviceID) {
        JSONObject jsonObject = new JSONObject();
        try (final Session session = SessionProvider.getSession()) {
            Transaction transaction = session.beginTransaction();
            UserManagement userManagement = new UserManagement(session);
            DeviceManagement deviceManagement = new DeviceManagement(session);

            User user = new User();
            user.setLoginIdentifier(loginIdentifier);
            if (userManagement.isExist(user)) {
                user = userManagement.get(loginIdentifier);
                main.java.entities.Device device = deviceManagement.get(deviceID);
                device.setUserIdUser(user.getIdUser());
                deviceManagement.set(device);
                jsonObject.put("Success", "The device has been bind with the user.");
            } else {
                jsonObject.put("Fail", "The user has not logged in yet.");
            }

            transaction.commit();
        }
        return Response.ok(jsonObject.toString()).build();
    }

    /**
     * Set up the connection with APN
     *
     * @return The JSON response object
     * {"Success": "The connection is set up."}
     * @throws InvalidKeyException      if the signed key is invalid
     * @throws NoSuchAlgorithmException if the JVM does not support elliptic curve keys
     * @throws IOException              if the key file cannot be found
     */
    @Path("/connectAPN")
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response connectAPN() throws InvalidKeyException, NoSuchAlgorithmException, IOException {
        JSONObject jsonObject = new JSONObject();
        APNConnector.getAPNConnectorFromSession(request);
        jsonObject.put("Success", "The connection is set up.");
        return Response.ok(jsonObject.toString()).build();
    }

    /**
     * Close the APN connection
     * @return The JSON response object
     * @throws InterruptedException if the current thread was interrupted while waiting
     */
    @Path("/disconnectAPN")
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response disconnectAPN() throws InterruptedException {
        JSONObject jsonObject = new JSONObject();
        disconnectAPNConnection();
        jsonObject.put("Success", "The connection is disconnected.");
        return Response.ok(jsonObject.toString()).build();
    }

    /**
     * The helper method for disconnection the APN connection
     * @throws InterruptedException if the current thread was interrupted while waiting
     */
    private void disconnectAPNConnection() throws InterruptedException {
        HttpSession session = request.getSession(false);
        if (session != null) {
            APNConnector connector = (APNConnector) session.getAttribute("APNConnection");
            connector.closeConnection();
        }
    }
}
