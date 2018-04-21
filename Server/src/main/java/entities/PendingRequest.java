package main.java.entities;

import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.sql.Timestamp;

@Entity
public class PendingRequest {
    private Integer idPendingRequest;
    private Integer idSeller;
    private Integer idBuyer;
    private Integer idProduct;
    private Double offerPrice;
    private String note;
    private Timestamp date;
    private User userByIdSeller;
    private User userByIdBuyer;
    private Product productByIdProduct;

    @Id
    @Column(name = "idPendingRequest")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer getIdPendingRequest() {
        return idPendingRequest;
    }

    public void setIdPendingRequest(Integer idPendingRequest) {
        this.idPendingRequest = idPendingRequest;
    }

    @Basic
    @Column(name = "idSeller")
    public Integer getIdSeller() {
        return idSeller;
    }

    public void setIdSeller(Integer idSeller) {
        this.idSeller = idSeller;
    }

    @Basic
    @Column(name = "idBuyer")
    public Integer getIdBuyer() {
        return idBuyer;
    }

    public void setIdBuyer(Integer idBuyer) {
        this.idBuyer = idBuyer;
    }

    @Basic
    @Column(name = "idProduct")
    public Integer getIdProduct() {
        return idProduct;
    }

    public void setIdProduct(Integer idProduct) {
        this.idProduct = idProduct;
    }

    @Basic
    @Column(name = "offer_price")
    public Double getOfferPrice() {
        return offerPrice;
    }

    public void setOfferPrice(Double offerPrice) {
        this.offerPrice = offerPrice;
    }

    @Basic
    @Column(name = "note")
    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    @Basic
    @Column(name = "date")
    @CreationTimestamp
    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PendingRequest that = (PendingRequest) o;

        if (idPendingRequest != null ? !idPendingRequest.equals(that.idPendingRequest) : that.idPendingRequest != null)
            return false;
        if (idSeller != null ? !idSeller.equals(that.idSeller) : that.idSeller != null) return false;
        if (idBuyer != null ? !idBuyer.equals(that.idBuyer) : that.idBuyer != null) return false;
        if (idProduct != null ? !idProduct.equals(that.idProduct) : that.idProduct != null) return false;
        if (offerPrice != null ? !offerPrice.equals(that.offerPrice) : that.offerPrice != null) return false;
        if (note != null ? !note.equals(that.note) : that.note != null) return false;
        if (date != null ? !date.equals(that.date) : that.date != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = idPendingRequest != null ? idPendingRequest.hashCode() : 0;
        result = 31 * result + (idSeller != null ? idSeller.hashCode() : 0);
        result = 31 * result + (idBuyer != null ? idBuyer.hashCode() : 0);
        result = 31 * result + (idProduct != null ? idProduct.hashCode() : 0);
        result = 31 * result + (offerPrice != null ? offerPrice.hashCode() : 0);
        result = 31 * result + (note != null ? note.hashCode() : 0);
        result = 31 * result + (date != null ? date.hashCode() : 0);
        return result;
    }

    @ManyToOne
    @JoinColumn(name = "idSeller", referencedColumnName = "idUser", nullable = false, insertable=false, updatable=false)
    public User getUserByIdSeller() {
        return userByIdSeller;
    }

    public void setUserByIdSeller(User userByIdSeller) {
        this.userByIdSeller = userByIdSeller;
    }

    @ManyToOne
    @JoinColumn(name = "idBuyer", referencedColumnName = "idUser", nullable = false, insertable=false, updatable=false)
    public User getUserByIdBuyer() {
        return userByIdBuyer;
    }

    public void setUserByIdBuyer(User userByIdBuyer) {
        this.userByIdBuyer = userByIdBuyer;
    }

    @ManyToOne
    @JoinColumn(name = "idProduct", referencedColumnName = "idProduct", nullable = false, insertable=false, updatable=false)
    public Product getProductByIdProduct() {
        return productByIdProduct;
    }

    public void setProductByIdProduct(Product productByIdProduct) {
        this.productByIdProduct = productByIdProduct;
    }
}
