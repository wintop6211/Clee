package main.java.entities;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class BuyerReview {
    private Integer idBuyerReview;
    private Double stars;
    private String review;
    private Integer userIdUser;
    private User userByUserIdUser;

    @Id
    @Column(name = "idBuyerReview")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer getIdBuyerReview() {
        return idBuyerReview;
    }

    public void setIdBuyerReview(Integer idBuyerReview) {
        this.idBuyerReview = idBuyerReview;
    }

    @Basic
    @Column(name = "stars")
    public Double getStars() {
        return stars;
    }

    public void setStars(Double stars) {
        this.stars = stars;
    }

    @Basic
    @Column(name = "review")
    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    @Basic
    @Column(name = "User_idUser")
    public Integer getUserIdUser() {
        return userIdUser;
    }

    public void setUserIdUser(Integer userIdUser) {
        this.userIdUser = userIdUser;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BuyerReview that = (BuyerReview) o;

        if (idBuyerReview != null ? !idBuyerReview.equals(that.idBuyerReview) : that.idBuyerReview != null)
            return false;
        if (stars != null ? !stars.equals(that.stars) : that.stars != null) return false;
        if (review != null ? !review.equals(that.review) : that.review != null) return false;
        if (userIdUser != null ? !userIdUser.equals(that.userIdUser) : that.userIdUser != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = idBuyerReview != null ? idBuyerReview.hashCode() : 0;
        result = 31 * result + (stars != null ? stars.hashCode() : 0);
        result = 31 * result + (review != null ? review.hashCode() : 0);
        result = 31 * result + (userIdUser != null ? userIdUser.hashCode() : 0);
        return result;
    }

    @ManyToOne
    @JoinColumn(name = "User_idUser", referencedColumnName = "idUser", nullable = false, insertable=false, updatable=false)
    public User getUserByUserIdUser() {
        return userByUserIdUser;
    }

    public void setUserByUserIdUser(User userByUserIdUser) {
        this.userByUserIdUser = userByUserIdUser;
    }
}
