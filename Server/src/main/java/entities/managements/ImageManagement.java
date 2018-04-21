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

public class ImageManagement {
    public static byte[] loadItemImage(Product product, int itemImageIndex, boolean isHighResolution) throws IOException {
        String path = product.getPictures() + itemImageIndex + ".jpeg";
        return readImage(path, isHighResolution);
    }

    public static byte[] loadItemCoverImage(Product product) throws IOException {
        String path = product.getPictures();
        String coverPagePath = path + "0.jpeg";
        return ImageManagement.readImage(coverPagePath, false);
    }

    public static byte[] getUserSchoolImage(User user) throws IOException {
        String logoPath = user.getSchoolBySchoolIdSchool().getLogo();
        return readImage(logoPath, true);
    }

    public static void writeImage(String destination, InputStream imageData) throws IOException {
        BufferedImage image = ImageIO.read(imageData);
        ImageIO.write(image, "jpeg", new File(destination));
    }

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
