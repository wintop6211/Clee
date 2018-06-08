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

/**
 * The class for managing products stored in the database
 */
public class ProductManagement {

    private final Session session;

    /**
     * Creates the product manager
     *
     * @param session The session for interacting with the database
     */
    public ProductManagement(Session session) {
        this.session = session;
    }

    /**
     * Update the corresponding row in the product table in the database.
     *
     * @param product The product object
     */
    public void set(Product product) {
        session.update(product);
    }

    /**
     * Sets the category of the product
     *
     * @param product          The product object that needs to be updated
     * @param categoryInString The category of the product in string format
     */
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

    /**
     * Gets one product by the id
     *
     * @param id The id of the product
     * @return The product object fetched from the database
     */
    public Product get(int id) {
        return session.load(Product.class, id);
    }

    /**
     * Adds one product object into the database
     *
     * @param product The product object that needs to be added
     */
    public void add(Product product) {
        session.save(product);
    }

    /**
     * Removes one product from the database
     *
     * @param product The product object that needs to be removed
     */
    public void remove(Product product) {
        removeAllPendingRequests(session, product);
        removeProductFromCategories(session, product);
        removeProductImages(product);
        session.delete(product);
    }

    /**
     * Determine if the product exists in the database or not
     *
     * @param product The product object that needs to be checked
     * @return True if the product exists. False if the product does not exist.
     */
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

    /**
     * Removes all mappings between the product and its related categories
     *
     * @param session The session for interacting with the database
     * @param product The product whose mappings needs to be removed
     */
    private static void removeProductFromCategories(Session session, Product product) {
        Collection<ProductHasCategory> productHasCategories = product.getProductHasCategoriesByIdProduct();
        for (ProductHasCategory productHasCategory : productHasCategories) {
            session.delete(productHasCategory);
        }
    }

    /**
     * Remove all pending requests for one product
     *
     * @param session The session for interacting with the database
     * @param product The product whose pending requests need to be removed
     */
    private static void removeAllPendingRequests(Session session, Product product) {
        Collection<PendingRequest> pendingRequests = product.getPendingRequestsByIdProduct();
        for (PendingRequest pendingRequest : pendingRequests) {
            session.delete(pendingRequest);
        }
    }

    /**
     * Remove all product images
     *
     * @param product The product whose images need to be removed
     */
    private static void removeProductImages(Product product) {
        try {
            String path = product.getPictures();
            File file = new File(path);
            FileUtils.deleteDirectory(file);
        } catch (Exception e) {
            System.err.println("Files do not exist.");
        }
    }

    /**
     * Get the category by its name in string
     *
     * @param session          The session for interacting with the database
     * @param categoryInString The category in string
     * @return The category object
     */
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

    /**
     * Sets the product's basic information
     *
     * @param product     The product object
     * @param seller      The seller of the product
     * @param name        The name of the product
     * @param description The description of the product
     * @param price       The price of the product
     * @param condition   The condition (quality level) of the product
     */
    public static void setProductBasicInfo(Product product, User seller, String name, String description, double price,
                                           int condition) {
        product.setName(name);
        product.setPrice(price);
        product.setDescription(description);
        product.setCondition(condition);
        product.setSold(0);
        product.setSchoolIdSchool(seller.getSchoolIdSchool());
        product.setPictures("none");
        product.setIdSeller(seller.getIdUser());
        product.setView(0);
    }
}
