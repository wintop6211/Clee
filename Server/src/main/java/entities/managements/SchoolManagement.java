package main.java.entities.managements;

import main.java.entities.School;
import org.hibernate.Session;

import java.util.List;

/**
 * The class for managing schools stored in the database
 */
public class SchoolManagement {

    final Session session;

    /**
     * Creates the school manager
     *
     * @param session The session for interacting with the database
     */
    public SchoolManagement(Session session) {
        this.session = session;
    }

    /**
     * Gets the school object from the database
     *
     * @param idSchool The id of the school
     * @return The school object
     */
    public School get(int idSchool) {
        return session.load(School.class, idSchool);
    }

    /**
     * Gets the school object from the database
     *
     * @param name The name of the school
     * @return The school object
     */
    public School get(String name) {
        List schools = session.createQuery("from School where name='" + name + "'").list();
        return (School) schools.get(0);
    }

    /**
     * Adds one school object into the database
     *
     * @param school The school object that needs to be added
     */
    public void add(School school) {
        session.save(school);
    }

    /**
     * Updates one school object in the database
     *
     * @param school The school object that needs to be updated
     */
    public void set(School school) {
        session.update(school);
    }

    /**
     * Determine if the school exists in the database
     *
     * @param school The school object that needs to be checked
     * @return True if the school exists. False if the school does not exist
     */
    public boolean isExist(School school) {
        boolean isExist = false;
        if (school.getName() != null) {
            List schools = session.createQuery("from School where name='" + school.getName() + "'").list();
            isExist = !schools.isEmpty();
        } else if (school.getIdSchool() != null) {
            school = session.get(School.class, school.getIdSchool());
            isExist = school != null;
        }
        return isExist;
    }
}
