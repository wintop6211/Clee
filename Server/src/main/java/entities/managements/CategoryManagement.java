package main.java.entities.managements;

import org.hibernate.Session;

public class CategoryManagement {

    private final Session session;

    public CategoryManagement(Session session) {
        this.session = session;
    }


}
