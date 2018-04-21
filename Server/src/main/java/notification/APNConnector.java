package main.java.notification;

import com.turo.pushy.apns.ApnsClient;
import com.turo.pushy.apns.ApnsClientBuilder;
import com.turo.pushy.apns.PushNotificationResponse;
import com.turo.pushy.apns.auth.ApnsSigningKey;
import com.turo.pushy.apns.util.ApnsPayloadBuilder;
import com.turo.pushy.apns.util.SimpleApnsPushNotification;
import io.netty.util.concurrent.Future;
import main.java.services.helpers.PathManager;

import java.io.File;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class APNConnector {

    private ApnsClient client;

    public APNConnector() throws InvalidKeyException, NoSuchAlgorithmException, IOException {
        this.client = new ApnsClientBuilder()
                .setSigningKey(ApnsSigningKey.loadFromPkcs8File(
                        new File(PathManager.getAPNKeyAddress()),
                        "6Y6N7WNU3G", "T85K849GX8")).setApnsServer(ApnsClientBuilder.DEVELOPMENT_APNS_HOST
                )
                .build();
    }

    public void sendPurchaseRequestNotification(String deviceToken, String seller) throws Exception {
        if (deviceToken != null) {
            String title = "Purchase Request";
            String body = "The purchase request from " + seller;
            sendNotification(deviceToken, title, body);
        }
    }

    public void sendConfirmRequestNotification(String deviceToken, String seller, String product) throws Exception {
        if (deviceToken != null) {
            String title = "Request confirmed";
            String body = product + " has confirmed by " + seller;
            sendNotification(deviceToken, title, body);
        }
    }

    public void sendCancelRequestNotification(String deviceToken, String user, String product) throws Exception {
        if (deviceToken != null) {
            String title = "Request Canceled";
            String body = user + " canceled request for " + product;
            sendNotification(deviceToken, title, body);
        }
    }

    public void closeConnection() throws InterruptedException {
        Future<Void> closeFuture = client.close();
        closeFuture.await();
    }

    private void sendNotification(String deviceToken, String title, String body) throws Exception {
        ApnsPayloadBuilder payloadBuilder = new ApnsPayloadBuilder();
        payloadBuilder.setAlertTitle(title);
        payloadBuilder.setAlertBody(body);
        String payload = payloadBuilder.buildWithDefaultMaximumLength();
        SimpleApnsPushNotification pushNotification = new SimpleApnsPushNotification(deviceToken, "com.ColleNet.CollegeBuyer", payload);
        Future<PushNotificationResponse<SimpleApnsPushNotification>> sendNotificationFuture = client.sendNotification(pushNotification);
        sendNotificationFuture.get();
    }
}
