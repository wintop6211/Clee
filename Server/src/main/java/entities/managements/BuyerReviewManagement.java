package main.java.entities.managements;

import main.java.entities.BuyerReview;
import org.hibernate.Session;

public class BuyerReviewManagement {

    private final Session session;

    public BuyerReviewManagement(Session session) {
        this.session = session;
    }

    public void add(BuyerReview buyerReview) {
        session.save(buyerReview);
    }
}
