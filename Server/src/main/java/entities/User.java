package main.java.entities;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.Collection;

@Entity
public class User {
    private Integer idUser;
    private String emailAddress;
    private String password;
    private Integer gender;
    private String profilePicture;
    private String name;
    private String phone;
    private Integer schoolIdSchool;
    private Integer verified;
    private String loginIdentifier;
    private Collection<BuyerReview> buyerReviewsByIdUser;
    private Collection<Device> devicesByIdUser;
    private Collection<PendingRequest> pendingRequestsByIdUser;
    private Collection<PendingRequest> pendingRequestsByIdUser_0;
    private Collection<Product> productsByIdUser;
    private Collection<Product> productsByIdUser_0;
    private Collection<Report> reportsByIdUser;
    private Collection<SellerReview> sellerReviewsByIdUser;
    private School schoolBySchoolIdSchool;

    @Id
    @Column(name = "idUser")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer getIdUser() {
        return idUser;
    }

    public void setIdUser(Integer idUser) {
        this.idUser = idUser;
    }

    @Basic
    @Column(name = "emailAddress")
    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    @Basic
    @Column(name = "password")
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Basic
    @Column(name = "gender")
    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    @Basic
    @Column(name = "profilePicture")
    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
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
    @Column(name = "phone")
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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
    @Column(name = "verified")
    public Integer getVerified() {
        return verified;
    }

    public void setVerified(Integer verified) {
        this.verified = verified;
    }

    @Basic
    @Column(name = "loginIdentifier")
    public String getLoginIdentifier() {
        return loginIdentifier;
    }

    public void setLoginIdentifier(String loginIdentifier) {
        this.loginIdentifier = loginIdentifier;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (idUser != null ? !idUser.equals(user.idUser) : user.idUser != null) return false;
        if (emailAddress != null ? !emailAddress.equals(user.emailAddress) : user.emailAddress != null) return false;
        if (password != null ? !password.equals(user.password) : user.password != null) return false;
        if (gender != null ? !gender.equals(user.gender) : user.gender != null) return false;
        if (profilePicture != null ? !profilePicture.equals(user.profilePicture) : user.profilePicture != null)
            return false;
        if (name != null ? !name.equals(user.name) : user.name != null) return false;
        if (phone != null ? !phone.equals(user.phone) : user.phone != null) return false;
        if (schoolIdSchool != null ? !schoolIdSchool.equals(user.schoolIdSchool) : user.schoolIdSchool != null)
            return false;
        if (verified != null ? !verified.equals(user.verified) : user.verified != null) return false;
        if (loginIdentifier != null ? !loginIdentifier.equals(user.loginIdentifier) : user.loginIdentifier != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = idUser != null ? idUser.hashCode() : 0;
        result = 31 * result + (emailAddress != null ? emailAddress.hashCode() : 0);
        result = 31 * result + (password != null ? password.hashCode() : 0);
        result = 31 * result + (gender != null ? gender.hashCode() : 0);
        result = 31 * result + (profilePicture != null ? profilePicture.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (phone != null ? phone.hashCode() : 0);
        result = 31 * result + (schoolIdSchool != null ? schoolIdSchool.hashCode() : 0);
        result = 31 * result + (verified != null ? verified.hashCode() : 0);
        result = 31 * result + (loginIdentifier != null ? loginIdentifier.hashCode() : 0);
        return result;
    }

    @OneToMany(mappedBy = "userByUserIdUser")
    public Collection<BuyerReview> getBuyerReviewsByIdUser() {
        return buyerReviewsByIdUser;
    }

    public void setBuyerReviewsByIdUser(Collection<BuyerReview> buyerReviewsByIdUser) {
        this.buyerReviewsByIdUser = buyerReviewsByIdUser;
    }

    @OneToMany(mappedBy = "userByUserIdUser")
    public Collection<Device> getDevicesByIdUser() {
        return devicesByIdUser;
    }

    public void setDevicesByIdUser(Collection<Device> devicesByIdUser) {
        this.devicesByIdUser = devicesByIdUser;
    }

    @OneToMany(mappedBy = "userByIdSeller")
    public Collection<PendingRequest> getPendingRequestsByIdUser() {
        return pendingRequestsByIdUser;
    }

    public void setPendingRequestsByIdUser(Collection<PendingRequest> pendingRequestsByIdUser) {
        this.pendingRequestsByIdUser = pendingRequestsByIdUser;
    }

    @OneToMany(mappedBy = "userByIdBuyer")
    public Collection<PendingRequest> getPendingRequestsByIdUser_0() {
        return pendingRequestsByIdUser_0;
    }

    public void setPendingRequestsByIdUser_0(Collection<PendingRequest> pendingRequestsByIdUser_0) {
        this.pendingRequestsByIdUser_0 = pendingRequestsByIdUser_0;
    }

    @OneToMany(mappedBy = "userByIdSeller")
    public Collection<Product> getProductsByIdUser() {
        return productsByIdUser;
    }

    public void setProductsByIdUser(Collection<Product> productsByIdUser) {
        this.productsByIdUser = productsByIdUser;
    }

    @OneToMany(mappedBy = "userByIdBuyer")
    public Collection<Product> getProductsByIdUser_0() {
        return productsByIdUser_0;
    }

    public void setProductsByIdUser_0(Collection<Product> productsByIdUser_0) {
        this.productsByIdUser_0 = productsByIdUser_0;
    }

    @OneToMany(mappedBy = "userByUserIdUser")
    public Collection<Report> getReportsByIdUser() {
        return reportsByIdUser;
    }

    public void setReportsByIdUser(Collection<Report> reportsByIdUser) {
        this.reportsByIdUser = reportsByIdUser;
    }

    @OneToMany(mappedBy = "userByUserIdUser")
    public Collection<SellerReview> getSellerReviewsByIdUser() {
        return sellerReviewsByIdUser;
    }

    public void setSellerReviewsByIdUser(Collection<SellerReview> sellerReviewsByIdUser) {
        this.sellerReviewsByIdUser = sellerReviewsByIdUser;
    }

    @ManyToOne
    @JoinColumn(name = "School_idSchool", referencedColumnName = "idSchool", nullable = false, insertable=false, updatable=false)
    public School getSchoolBySchoolIdSchool() {
        return schoolBySchoolIdSchool;
    }

    public void setSchoolBySchoolIdSchool(School schoolBySchoolIdSchool) {
        this.schoolBySchoolIdSchool = schoolBySchoolIdSchool;
    }
}
