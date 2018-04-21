package main.java.status.manager;

import main.java.entities.User;
import org.hibernate.Session;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

public class LoginStatusManager {
    public static void refreshLoginCookieStatus(HttpServletResponse response, Session session, User user) {
        String uuid = LoginStatusManager.resetLoginIdentifier(session, user);
        Cookie cookie = LoginStatusManager.createLoginIdentifierCookie(uuid);
        response.addCookie(cookie);
    }

    public static String resetLoginIdentifier(Session session, User user) {
        UUID uuid = UUID.randomUUID();
        String randomUUIDString = uuid.toString();
        user.setLoginIdentifier(randomUUIDString);
        session.update(user);
        return randomUUIDString;
    }

    private static Cookie createLoginIdentifierCookie(String uuid) {
        // create the session cookie
        Cookie cookie = new Cookie("loginIdentifier", uuid);
        cookie.setPath("/");
        cookie.setSecure(true);
        cookie.setMaxAge(3600);
        return cookie;
    }
}
