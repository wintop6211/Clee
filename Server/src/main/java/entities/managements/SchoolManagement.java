package main.java.entities.managements;

import main.java.entities.School;
import org.hibernate.Session;

import java.util.List;

public class SchoolManagement {

    final Session session;

    public SchoolManagement(Session session) {
        this.session = session;
    }

    public School get(int idSchool) {
        return session.load(School.class, idSchool);
    }

    public School get(String name) {
        List schools = session.createQuery("from School where name='" + name + "'").list();
        return (School) schools.get(0);
    }

    public void add(School school) {
        session.save(school);
    }

    public void set(School school) {
        session.update(school);
    }

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

    // TODO: have not implemented
    public School remove(School school) {
        return null;
    }
}
