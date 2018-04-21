package main.java.entities.managements;

import main.java.entities.PendingRequest;
import main.java.entities.Product;
import main.java.entities.User;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;

public class PendingRequestManagement {

    private final Session session;

    public PendingRequestManagement(Session session) {
        this.session = session;
    }

    public PendingRequest get(int id) {
        return session.load(PendingRequest.class, id);
    }

    public void add(PendingRequest pendingRequest) {
        session.save(pendingRequest);
    }

    public void remove(PendingRequest pendingRequest) {
        session.delete(pendingRequest);
    }

    public void remove(int idProduct) {
        Query query = session.createQuery("delete PendingRequest where idProduct=" + idProduct);
        query.executeUpdate();
    }

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

    public static void setPendingRequestBasicInfo(PendingRequest pendingRequest, User buyer, User seller, Product product,
                                                  String note, double offerPrice) {
        pendingRequest.setIdBuyer(buyer.getIdUser());
        pendingRequest.setIdSeller(seller.getIdUser());
        pendingRequest.setIdProduct(product.getIdProduct());
        pendingRequest.setNote(note);
        pendingRequest.setOfferPrice(offerPrice);
    }

}
