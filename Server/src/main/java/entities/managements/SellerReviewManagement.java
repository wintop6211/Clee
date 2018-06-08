package main.java.entities.managements;

import main.java.entities.SellerReview;
import org.hibernate.Session;

/**
 * The class for managing seller reviews stored in the database
 */
public class SellerReviewManagement {

    private final Session session;

    /**
     * Creates the seller review manager
     *
     * @param session The session for interacting with the database
     */
    public SellerReviewManagement(Session session) {
        this.session = session;
    }

    /**
     * Adds one seller review object to the database
     *
     * @param sellerReview The seller review object that needs to be added
     */
    public void add(SellerReview sellerReview) {
        session.save(sellerReview);
    }
}
