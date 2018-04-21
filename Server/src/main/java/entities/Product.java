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
import javax.persistence.OneToMany;
import java.sql.Timestamp;
import java.util.Collection;

@Entity
public class Product {
    private Integer idProduct;
    private String name;
    private String description;
    private Double price;
    private String pictures;
    private Integer sold;
    private Integer condition;
    private Integer schoolIdSchool;
    private Integer idSeller;
    private Integer idBuyer;
    private Integer view;
    private Timestamp date;
    private Collection<PendingRequest> pendingRequestsByIdProduct;
    private School schoolBySchoolIdSchool;
    private User userByIdSeller;
    private User userByIdBuyer;
    private Collection<ProductHasCategory> productHasCategoriesByIdProduct;

    @Id
    @Column(name = "idProduct")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer getIdProduct() {
        return idProduct;
    }

    public void setIdProduct(Integer idProduct) {
        this.idProduct = idProduct;
    }

    @Basic
    @Column(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Basic
    @Column(name = "description")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Basic
    @Column(name = "price")
    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    @Basic
    @Column(name = "pictures")
    public String getPictures() {
        return pictures;
    }

    public void setPictures(String pictures) {
        this.pictures = pictures;
    }

    @Basic
    @Column(name = "sold")
    public Integer getSold() {
        return sold;
    }

    public void setSold(Integer sold) {
        this.sold = sold;
    }

    @Basic
    @Column(name = "`condition`")
    public Integer getCondition() {
        return condition;
    }

    public void setCondition(Integer condition) {
        this.condition = condition;
    }

    @Basic
    @Column(name = "School_idSchool")
    public Integer getSchoolIdSchool() {
        return schoolIdSchool;
    }

    public void setSchoolIdSchool(Integer schoolIdSchool) {
        this.schoolIdSchool = schoolIdSchool;
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
    @Column(name = "view")
    public Integer getView() {
        return view;
    }

    public void setView(Integer view) {
        this.view = view;
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

        Product product = (Product) o;

        if (idProduct != null ? !idProduct.equals(product.idProduct) : product.idProduct != null) return false;
        if (name != null ? !name.equals(product.name) : product.name != null) return false;
        if (description != null ? !description.equals(product.description) : product.description != null) return false;
        if (price != null ? !price.equals(product.price) : product.price != null) return false;
        if (pictures != null ? !pictures.equals(product.pictures) : product.pictures != null) return false;
        if (sold != null ? !sold.equals(product.sold) : product.sold != null) return false;
        if (condition != null ? !condition.equals(product.condition) : product.condition != null) return false;
        if (schoolIdSchool != null ? !schoolIdSchool.equals(product.schoolIdSchool) : product.schoolIdSchool != null)
            return false;
        if (idSeller != null ? !idSeller.equals(product.idSeller) : product.idSeller != null) return false;
        if (idBuyer != null ? !idBuyer.equals(product.idBuyer) : product.idBuyer != null) return false;
        if (view != null ? !view.equals(product.view) : product.view != null) return false;
        if (date != null ? !date.equals(product.date) : product.date != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = idProduct != null ? idProduct.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (price != null ? price.hashCode() : 0);
        result = 31 * result + (pictures != null ? pictures.hashCode() : 0);
        result = 31 * result + (sold != null ? sold.hashCode() : 0);
        result = 31 * result + (condition != null ? condition.hashCode() : 0);
        result = 31 * result + (schoolIdSchool != null ? schoolIdSchool.hashCode() : 0);
        result = 31 * result + (idSeller != null ? idSeller.hashCode() : 0);
        result = 31 * result + (idBuyer != null ? idBuyer.hashCode() : 0);
        result = 31 * result + (view != null ? view.hashCode() : 0);
        result = 31 * result + (date != null ? date.hashCode() : 0);
        return result;
    }

    @OneToMany(mappedBy = "productByIdProduct")
    public Collection<PendingRequest> getPendingRequestsByIdProduct() {
        return pendingRequestsByIdProduct;
    }

    public void setPendingRequestsByIdProduct(Collection<PendingRequest> pendingRequestsByIdProduct) {
        this.pendingRequestsByIdProduct = pendingRequestsByIdProduct;
    }

    @ManyToOne
    @JoinColumn(name = "School_idSchool", referencedColumnName = "idSchool", nullable = false, insertable=false, updatable=false)
    public School getSchoolBySchoolIdSchool() {
        return schoolBySchoolIdSchool;
    }

    public void setSchoolBySchoolIdSchool(School schoolBySchoolIdSchool) {
        this.schoolBySchoolIdSchool = schoolBySchoolIdSchool;
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
    @JoinColumn(name = "idBuyer", referencedColumnName = "idUser", insertable=false, updatable=false)
    public User getUserByIdBuyer() {
        return userByIdBuyer;
    }

    public void setUserByIdBuyer(User userByIdBuyer) {
        this.userByIdBuyer = userByIdBuyer;
    }

    @OneToMany(mappedBy = "productByProductIdProduct")
    public Collection<ProductHasCategory> getProductHasCategoriesByIdProduct() {
        return productHasCategoriesByIdProduct;
    }

    public void setProductHasCategoriesByIdProduct(Collection<ProductHasCategory> productHasCategoriesByIdProduct) {
        this.productHasCategoriesByIdProduct = productHasCategoriesByIdProduct;
    }
}
