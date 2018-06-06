package main.java.configuration;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/**
 * The class is for providing 'session' to callers. The session is used for communicating with
 * the database.
 * The class used the singleton pattern. The configuration will only be configured once.
 * If some changes on the configuration needs to be applied, feel free to modify this class.
 * Also, Server/src/main/resources/hibernate.cfg.xml is the place for changing configurations.
 * You can set configurations either by code, or by the .xml file. Hibernate documentation can give you more
 * details about this
 */
public class SessionProvider {

    private static SessionFactory ourSessionFactory = null;

    /**
     * This method is for getting one session which connects with the database
     *
     * @return database session
     * @throws HibernateException When the hibernate cannot set up the connection with the database, this exception
     *                            will be thrown
     */
    public static Session getSession() throws HibernateException {
        if (ourSessionFactory == null) {
            Configuration configuration = new Configuration();
            configuration.configure();
            ourSessionFactory = configuration.buildSessionFactory();
        }
        return ourSessionFactory.openSession();
    }
}