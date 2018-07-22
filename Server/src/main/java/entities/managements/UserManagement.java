package main.java.entities.managements;

import main.java.entities.User;
import org.hibernate.Session;

import java.util.List;

/**
 * The class for managing users stored in the database
 */
public class UserManagement {

    final Session session;

    /**
     * Creates the user manager
     *
     * @param session The session for interacting with the database
     */
    public UserManagement(Session session) {
        this.session = session;
    }

    /**
     * Gets the user by the user id from the database
     *
     * @param idUser The id of the user
     * @return The user object
     */
    public User get(int idUser) {
        return session.load(User.class, idUser);
    }

    /**
     * Gets the user by the login identifier from the database
     *
     * @param loginIdentifier The login identifier of the user
     * @return The user object
     */
    public User get(String loginIdentifier) {
        List users = session.createQuery("from User where loginIdentifier='" + loginIdentifier + "'").list();
        return (User) users.get(0);
    }

    /**
     * Gets the user by the user's email from the database
     *
     * @param email The email address of the user
     * @return The user object
     */
    public User getByEmail(String email) {
        List users = session.createQuery("from User where emailAddress='" + email + "'").list();
        return (User) users.get(0);
    }

    /**
     * Updates the corresponding row of the user table in the database
     *
     * @param user The user object that needs to be updated
     */
    public void set(User user) {
        session.update(user);
    }

    /**
     * Adds the user into the database
     *
     * @param user The user object which needs to be added
     */
    public void add(User user) {
        session.save(user);
    }

    /**
     * Determine if the user exists in the database
     *
     * @param user The user which needs to be checked
     * @return True if the user has existed. False if the user does not exist
     */
    public boolean isExist(User user) {
        boolean isExist = false;
        if (user.getIdUser() != null) {
            user = session.get(User.class, user.getIdUser());
            isExist = user != null;
        } else if (user.getEmailAddress() != null) {
            List users = session.createQuery("from User where emailAddress='" + user.getEmailAddress() + "'").list();
            isExist = !users.isEmpty();
        } else if (user.getPhone() != null) {
            List users = session.createQuery("from User where phone='" + user.getPhone() + "'").list();
            isExist = !users.isEmpty();
        } else if (user.getLoginIdentifier() != null) {
            List users = session.createQuery("from User where loginIdentifier='" + user.getLoginIdentifier() + "'").list();
            isExist = !users.isEmpty();
        }
        return isExist;
    }

    /**
     * Sets the user's basic information for the user object
     *
     * @param user         The user object which needs to be updated
     * @param name         The name of the user
     * @param emailAddress The email address of the user
     * @param password     The password of the user
     * @param gender       The gender of the user
     * @param phone        The phone of the user
     * @param schoolId     The school id of the user
     */
    public static void setUserBasicInfo(User user, String name, String emailAddress, String password,
                                        int gender, String phone, int schoolId) {
        user.setName(name);
        user.setEmailAddress(emailAddress);
        user.setGender(gender);
        user.setPhone(phone);
        user.setSchoolIdSchool(schoolId);
        user.setPassword(password);
        user.setVerified(1);
    }
}
