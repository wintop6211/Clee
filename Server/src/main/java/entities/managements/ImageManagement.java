package main.java.entities.managements;

import main.java.entities.Product;
import main.java.entities.User;
import org.imgscalr.Scalr;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * The class is for reading and writing images
 */
public class ImageManagement {
    /**
     * Loads one image for the item
     *
     * @param product          The product object
     * @param itemImageIndex   The index of the image
     * @param isHighResolution True if the high resolution image needs to be returned
     *                         False if the low resolution image needs to be returned
     * @return The image data as the byte array
     * @throws IOException if the image cannot be found
     */
    public static byte[] loadItemImage(Product product, int itemImageIndex, boolean isHighResolution) throws IOException {
        String path = product.getPictures() + itemImageIndex + ".jpeg";
        return readImage(path, isHighResolution);
    }

    /**
     * Loads the cover page image for the item
     *
     * @param product The product object
     * @return The image data as the byte array
     * @throws IOException if the image cannot be found
     */
    public static byte[] loadItemCoverImage(Product product) throws IOException {
        String path = product.getPictures();
        String coverPagePath = path + "0.jpeg";
        return ImageManagement.readImage(coverPagePath, false);
    }

    /**
     * Gets the school logo for the user
     *
     * @param user The user object
     * @return The school logo data as the byte array
     * @throws IOException if the image cannot be found
     */
    public static byte[] getUserSchoolImage(User user) throws IOException {
        String logoPath = user.getSchoolBySchoolIdSchool().getLogo();
        return readImage(logoPath, true);
    }

    /**
     * Writes the image to the destination
     *
     * @param destination The destination of the image
     * @param imageData   The image data as the input stream
     * @throws IOException if the path is invalid
     */
    public static void writeImage(String destination, InputStream imageData) throws IOException {
        BufferedImage image = ImageIO.read(imageData);
        ImageIO.write(image, "jpeg", new File(destination));
    }

    /**
     * Reads one image from the source
     *
     * @param source           Where the image is at
     * @param isHighResolution True if the high resolution image needs to be returned
     *                         False if the low resolution image needs to be returned
     * @return The image data as the byte array
     * @throws IOException if the image cannot be found
     */
    public static byte[] readImage(String source, boolean isHighResolution) throws IOException {
        File file = new File(source);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        if (file.exists()) {
            BufferedImage image = ImageIO.read(file);
            if (!isHighResolution) {
                image = Scalr.resize(image, 500);
            }
            ImageIO.write(image, "jpeg", outputStream);
        }
        return outputStream.toByteArray();
    }
}
