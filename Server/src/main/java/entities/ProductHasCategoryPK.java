package main.java.entities;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;

public class ProductHasCategoryPK implements Serializable {
    private Integer productIdProduct;
    private Integer categoryIdCategory;

    @Column(name = "Product_idProduct")
    @Id
    public Integer getProductIdProduct() {
        return productIdProduct;
    }

    public void setProductIdProduct(Integer productIdProduct) {
        this.productIdProduct = productIdProduct;
    }

    @Column(name = "Category_idCategory")
    @Id
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

        ProductHasCategoryPK that = (ProductHasCategoryPK) o;

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
}
