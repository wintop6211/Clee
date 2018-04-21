package main.java.entities.managements;

import main.java.entities.PendingRequest;
import main.java.entities.Category;
import main.java.entities.Product;
import main.java.entities.ProductHasCategory;
import main.java.entities.ProductHasCategoryPK;
import main.java.entities.User;
import org.apache.commons.io.FileUtils;
import org.hibernate.Session;

import java.io.File;
import java.util.Collection;
import java.util.List;

public class ProductManagement {

    private final Session session;

    public ProductManagement(Session session) {
        this.session = session;
    }

    public void set(Product product) {
        session.update(product);
    }

    public void setCategory(Product product, String categoryInString) {
        Category category = getCategoryByName(session, categoryInString);
        ProductHasCategoryPK productHasCategoryPK = new ProductHasCategoryPK();
        productHasCategoryPK.setCategoryIdCategory(category.getIdCategory());
        productHasCategoryPK.setProductIdProduct(product.getIdProduct());
        ProductHasCategory productHasCategory = session.get(ProductHasCategory.class, productHasCategoryPK);
        if (productHasCategory == null) {
            productHasCategory = new ProductHasCategory();
            productHasCategory.setCategoryIdCategory(category.getIdCategory());
            productHasCategory.setProductIdProduct(product.getIdProduct());
        }
        session.save(productHasCategory);
    }

    public Product get(int id) {
        return session.load(Product.class, id);
    }

    public void add(Product product) {
        session.save(product);
    }

    public void remove(Product product) {
        removeAllPendingRequests(session, product);
        removeProductFromCategories(session, product);
        removeProductImages(product);
        session.delete(product);
    }

    public boolean isExist(Product product) {
        boolean isExist = false;
        if (product.getIdProduct() != null) {
            product = session.get(Product.class, product.getIdProduct());
            if (product != null) {
                isExist = true;
            }
        }
        return isExist;
    }

    private static void removeProductFromCategories(Session session, Product product) {
        Collection<ProductHasCategory> productHasCategories = product.getProductHasCategoriesByIdProduct();
        for (ProductHasCategory productHasCategory : productHasCategories) {
            session.delete(productHasCategory);
        }
    }

    private static void removeAllPendingRequests(Session session, Product product) {
        Collection<PendingRequest> pendingRequests = product.getPendingRequestsByIdProduct();
        for (PendingRequest pendingRequest : pendingRequests) {
            session.delete(pendingRequest);
        }
    }

    private static void removeProductImages(Product product) {
        try {
            String path = product.getPictures();
            File file = new File(path);
            FileUtils.deleteDirectory(file);
        } catch (Exception e) {
            System.err.println("Files do not exist.");
        }
    }

    private Category getCategoryByName(Session session, String categoryInString) {
        List categories = session.createQuery("FROM Category WHERE name = '" + categoryInString + "'").list();
        Category category;
        if (categories.isEmpty()) {
            category = new Category();
            category.setName(categoryInString);
            session.save(category);
        } else {
            category = (Category) categories.get(0);
        }
        return category;
    }

    public static void setProductBasicInfo(Product product, User seller, String name, String description, double price,
                                           int condition, int sold) {
        product.setName(name);
        product.setPrice(price);
        product.setDescription(description);
        product.setCondition(condition);
        product.setSold(sold);
        product.setSchoolIdSchool(seller.getSchoolIdSchool());
        product.setPictures("none");
        product.setIdSeller(seller.getIdUser());
        product.setView(0);
    }
}
