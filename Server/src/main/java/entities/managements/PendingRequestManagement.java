package main.java.entities.managements;

import main.java.entities.PendingRequest;
import main.java.entities.Product;
import main.java.entities.User;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;

/**
 * The class is for managing purchasing requests stored in the database
 */
public class PendingRequestManagement {

    private final Session session;

    /**
     * Creates the purchasing request manager
     * @param session The session for interacting with the database
     */
    public PendingRequestManagement(Session session) {
        this.session = session;
    }

    /**
     * Gets one purchasing request by the id
     * @param id The id of the purchasing request
     * @return The purchasing request object
     */
    public PendingRequest get(int id) {
        return session.load(PendingRequest.class, id);
    }

    /**
     * Adds one purchasing request to the database
     * @param pendingRequest The purchasing request object which needs to be added
     */
    public void add(PendingRequest pendingRequest) {
        session.save(pendingRequest);
    }

    /**
     * Removes one purchasing request from the database by the object itself
     * @param pendingRequest The purchasing request that needs to be removed
     */
    public void remove(PendingRequest pendingRequest) {
        session.delete(pendingRequest);
    }

    /**
     * Removes all purchasing requests for one product by the product id
     * @param idProduct The id of the product
     */
    public void remove(int idProduct) {
        Query query = session.createQuery("delete PendingRequest where idProduct=" + idProduct);
        query.executeUpdate();
    }

    /**
     * Determine if the request has existed in the database
     * @param pendingRequest The purchasing request object
     * @return True if the request exists in the database. False if the request does not exist in the database.
     */
    public boolean isExist(PendingRequest pendingRequest) {
        boolean isExist = false;
        if (pendingRequest.getIdSeller() != null &&
                pendingRequest.getIdBuyer() != null &&
                pendingRequest.getIdProduct() != null) {
            List result = session.createQuery("from PendingRequest where idSeller=" + pendingRequest.getIdSeller() +
                    " and idBuyer=" + pendingRequest.getIdBuyer() +
                    " and idProduct=" + pendingRequest.getIdProduct()).list();
            if (result.size() > 0) {
                isExist = true;
            }
        } else if (pendingRequest.getIdPendingRequest() != null) {
            pendingRequest = session.get(PendingRequest.class, pendingRequest.getIdPendingRequest());
            if (pendingRequest != null) {
                isExist = true;
            }
        }
       return isExist;
    }

    /**
     * Sets basic information for one purchasing request
     * @param pendingRequest The purchasing request that needs to be set
     * @param buyer The buyer of the purchasing request
     * @param seller The seller of the purchasing request
     * @param product The product which needs to be sold
     * @param note The note of the purchasing request
     * @param offerPrice The offer price of the request
     */
    public static void setPendingRequestBasicInfo(PendingRequest pendingRequest, User buyer, User seller, Product product,
                                                  String note, double offerPrice) {
        pendingRequest.setIdBuyer(buyer.getIdUser());
        pendingRequest.setIdSeller(seller.getIdUser());
        pendingRequest.setIdProduct(product.getIdProduct());
        pendingRequest.setNote(note);
        pendingRequest.setOfferPrice(offerPrice);
    }

}
