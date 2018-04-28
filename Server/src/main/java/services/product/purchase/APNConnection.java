package main.java.services.product.purchase;

import main.java.notification.APNConnector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class APNConnection {
    public static APNConnector getAPNConnectorFromSession(HttpServletRequest request) throws
            InvalidKeyException, NoSuchAlgorithmException, IOException {
        HttpSession httpSession = request.getSession(false);
        APNConnector connector;
        if (httpSession != null) {
            connector = (APNConnector) httpSession.getAttribute("APNConnection");
        } else {
            httpSession = request.getSession();
            connector = new APNConnector();
            httpSession.setMaxInactiveInterval(3600 * 24);
            httpSession.setAttribute("APNConnection", connector);
        }
        return connector;
    }
}
