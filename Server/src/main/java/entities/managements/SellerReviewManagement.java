package main.java.entities.managements;

import main.java.entities.SellerReview;
import org.hibernate.Session;

public class SellerReviewManagement {

    private final Session session;

    public SellerReviewManagement(Session session) {
        this.session = session;
    }

    public void add(SellerReview sellerReview) {
        session.save(sellerReview);
    }
}
