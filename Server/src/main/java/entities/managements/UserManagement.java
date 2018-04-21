package main.java.entities.managements;

import main.java.entities.User;
import org.hibernate.Session;

import java.util.List;

public class UserManagement {

    final Session session;

    public UserManagement(Session session) {
        this.session = session;
    }

    public User get(int idUser) {
        return session.load(User.class, idUser);
    }

    public User get(String loginIdentifier) {
        List users = session.createQuery("from User where loginIdentifier='" + loginIdentifier + "'").list();
        return (User) users.get(0);
    }

    public User getByEmail(String email) {
        List users = session.createQuery("from User where emailAddress='" + email + "'").list();
        return (User) users.get(0);
    }

    public void set(User user) {
        session.update(user);
    }

    public void add(User user) {
        session.save(user);
    }

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

    public static void setUserBasicInfo(User user, String name, String emailAddress, String password,
                                        int gender, String phone, int schoolId) {
        user.setName(name);
        user.setEmailAddress(emailAddress);
        user.setGender(gender);
        user.setPhone(phone);
        user.setSchoolIdSchool(schoolId);
        user.setPassword(password);
        user.setVerified(0);
    }
}
