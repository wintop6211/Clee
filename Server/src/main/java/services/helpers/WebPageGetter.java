package main.java.services.helpers;

import javax.servlet.ServletContext;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class WebPageGetter {
    public static FileInputStream getWebPage(ServletContext context, String webPage) throws IOException {
        String base = context.getRealPath(webPage);
        File f = new File(base);
        return new FileInputStream(f);
    }
}
