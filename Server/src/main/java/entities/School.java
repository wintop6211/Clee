package main.java.entities;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.Collection;

@Entity
public class School {
    private Integer idSchool;
    private String name;
    private String logo;
    private Collection<Product> productsByIdSchool;
    private Collection<User> usersByIdSchool;

    @Id
    @Column(name = "idSchool")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer getIdSchool() {
        return idSchool;
    }

    public void setIdSchool(Integer idSchool) {
        this.idSchool = idSchool;
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
    @Column(name = "logo")
    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        School school = (School) o;

        if (idSchool != null ? !idSchool.equals(school.idSchool) : school.idSchool != null) return false;
        if (name != null ? !name.equals(school.name) : school.name != null) return false;
        if (logo != null ? !logo.equals(school.logo) : school.logo != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = idSchool != null ? idSchool.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (logo != null ? logo.hashCode() : 0);
        return result;
    }

    @OneToMany(mappedBy = "schoolBySchoolIdSchool")
    public Collection<Product> getProductsByIdSchool() {
        return productsByIdSchool;
    }

    public void setProductsByIdSchool(Collection<Product> productsByIdSchool) {
        this.productsByIdSchool = productsByIdSchool;
    }

    @OneToMany(mappedBy = "schoolBySchoolIdSchool")
    public Collection<User> getUsersByIdSchool() {
        return usersByIdSchool;
    }

    public void setUsersByIdSchool(Collection<User> usersByIdSchool) {
        this.usersByIdSchool = usersByIdSchool;
    }
}
