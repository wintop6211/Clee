package main.java.configuration;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/**
 * Created by TianTuoYou on 7/12/17.
 */
public class SessionProvider {

    private static SessionFactory ourSessionFactory = null;

    public static Session getSession() throws HibernateException {
        if (ourSessionFactory == null) {
            Configuration configuration = new Configuration();
            configuration.configure();
            ourSessionFactory = configuration.buildSessionFactory();
        }
        return ourSessionFactory.openSession();
    }
}