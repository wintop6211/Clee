package main.java.status.manager;

import main.java.entities.User;
import org.hibernate.Session;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

/**
 * The class is for creating and managing cookies
 */
public class LoginStatusManager {
    /**
     * Refreshes the login identifier cookie expire time
     * @param response The object which contains the response information
     * @param session The session for interacting with the database
     * @param user The user object
     */
    public static void refreshLoginCookieStatus(HttpServletResponse response, Session session, User user) {
        String uuid = LoginStatusManager.resetLoginIdentifier(session, user);
        Cookie cookie = LoginStatusManager.createLoginIdentifierCookie(uuid);
        response.addCookie(cookie);
    }

    /**
     * Resets the login identifier for the user
     * @param session The session for interacting with the database
     * @param user The user object
     * @return The login identifier
     */
    public static String resetLoginIdentifier(Session session, User user) {
        UUID uuid = UUID.randomUUID();
        String randomUUIDString = uuid.toString();
        user.setLoginIdentifier(randomUUIDString);
        session.update(user);
        return randomUUIDString;
    }

    /**
     * Creates the login identifier cookie
     * @param uuid The login identifier
     * @return The cookie object
     */
    private static Cookie createLoginIdentifierCookie(String uuid) {
        // create the session cookie
        Cookie cookie = new Cookie("loginIdentifier", uuid);
        cookie.setPath("/");
        cookie.setSecure(true);
        cookie.setMaxAge(3600);
        return cookie;
    }
}
