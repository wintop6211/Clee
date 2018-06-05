package main.java.entities.managements;

import main.java.entities.BuyerReview;
import org.hibernate.Session;

/**
 * The class for managing buyer reviews stored in the database
 */
public class BuyerReviewManagement {

    private final Session session; // The session for interacting with the database

    /**
     * Creates the buyer review manager
     * @param session The session for interacting with the database
     */
    public BuyerReviewManagement(Session session) {
        this.session = session;
    }

    /**
     * Inserts one buyer review into the database
     * @param buyerReview The buyer review which needs to be inserted
     */
    public void add(BuyerReview buyerReview) {
        session.save(buyerReview);
    }
}
