package main.java.services.helpers;

import javax.servlet.ServletContext;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * The class for getting the html page
 */
public class WebPageGetter {
    /**
     * Gets the web page
     *
     * @param context The context for getting the real path of the html file
     * @param webPage The path to the web page
     * @return The input stream of the web page
     * @throws IOException if the html file cannot be found
     */
    public static FileInputStream getWebPage(ServletContext context, String webPage) throws IOException {
        String base = context.getRealPath(webPage);
        File f = new File(base);
        return new FileInputStream(f);
    }
}
