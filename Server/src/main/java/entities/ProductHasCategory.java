package main.java.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "Product_has_Category", schema = "CollegeBuyer", catalog = "")
@IdClass(ProductHasCategoryPK.class)
public class ProductHasCategory {
    private Integer productIdProduct;
    private Integer categoryIdCategory;
    private Product productByProductIdProduct;
    private Category categoryByCategoryIdCategory;

    @Id
    @Column(name = "Product_idProduct")
    public Integer getProductIdProduct() {
        return productIdProduct;
    }

    public void setProductIdProduct(Integer productIdProduct) {
        this.productIdProduct = productIdProduct;
    }

    @Id
    @Column(name = "Category_idCategory")
    public Integer getCategoryIdCategory() {
        return categoryIdCategory;
    }

    public void setCategoryIdCategory(Integer categoryIdCategory) {
        this.categoryIdCategory = categoryIdCategory;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ProductHasCategory that = (ProductHasCategory) o;

        if (productIdProduct != null ? !productIdProduct.equals(that.productIdProduct) : that.productIdProduct != null)
            return false;
        if (categoryIdCategory != null ? !categoryIdCategory.equals(that.categoryIdCategory) : that.categoryIdCategory != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = productIdProduct != null ? productIdProduct.hashCode() : 0;
        result = 31 * result + (categoryIdCategory != null ? categoryIdCategory.hashCode() : 0);
        return result;
    }

    @ManyToOne
    @JoinColumn(name = "Product_idProduct", referencedColumnName = "idProduct", nullable = false, insertable=false, updatable=false)
    public Product getProductByProductIdProduct() {
        return productByProductIdProduct;
    }

    public void setProductByProductIdProduct(Product productByProductIdProduct) {
        this.productByProductIdProduct = productByProductIdProduct;
    }

    @ManyToOne
    @JoinColumn(name = "Category_idCategory", referencedColumnName = "idCategory", nullable = false, insertable=false, updatable=false)
    public Category getCategoryByCategoryIdCategory() {
        return categoryByCategoryIdCategory;
    }

    public void setCategoryByCategoryIdCategory(Category categoryByCategoryIdCategory) {
        this.categoryByCategoryIdCategory = categoryByCategoryIdCategory;
    }
}
