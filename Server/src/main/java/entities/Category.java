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
public class Category {
    private Integer idCategory;
    private String name;
    private Collection<ProductHasCategory> productHasCategoriesByIdCategory;

    @Id
    @Column(name = "idCategory")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer getIdCategory() {
        return idCategory;
    }

    public void setIdCategory(Integer idCategory) {
        this.idCategory = idCategory;
    }

    @Basic
    @Column(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Category category = (Category) o;

        if (idCategory != null ? !idCategory.equals(category.idCategory) : category.idCategory != null) return false;
        if (name != null ? !name.equals(category.name) : category.name != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = idCategory != null ? idCategory.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }

    @OneToMany(mappedBy = "categoryByCategoryIdCategory")
    public Collection<ProductHasCategory> getProductHasCategoriesByIdCategory() {
        return productHasCategoriesByIdCategory;
    }

    public void setProductHasCategoriesByIdCategory(Collection<ProductHasCategory> productHasCategoriesByIdCategory) {
        this.productHasCategoriesByIdCategory = productHasCategoriesByIdCategory;
    }
}
