package main.java.services.helpers;

/**
 * The class for managing root path of all files used by the server
 */
public class PathManager {
    private static String localURL = "http://localhost:8080/";
    private static String cloudURL = "http://174.138.45.173:8080/CollegeBuyerServer-1.0-SNAPSHOT/";
    private static String localFileDirectory = "/Users/liangy/Desktop/CollegeBuyer/CollegeBuyerServer/";
    private static String cloudFileDirectory = "/usr/share/CollegeBuyer/";
    private static String changePasswordCookieLocalPath = "/";
    private static String changePasswordCookieCloudPath = "/CollegeBuyerServer-1.0-SNAPSHOT/";
    private static String localKeyAddress = "/Users/liangy/Documents/CollegeBuyer/AuthKey_T85K849GX8.p8";
    private static String cloudKeyAddress = "/usr/share/CollegeBuyer/APNKey/AuthKey_T85K849GX8.p8";

    private static boolean isLocal = true; // The flag toggles if the path should be cloud path or not

    /**
     * Get the URL for the web service
     *
     * @param service The sub path of the web service
     * @return The full URL
     */
    public static String getURL(String service) {
        String tempRoot;
        if (isLocal) {
            tempRoot = localURL;
        } else {
            tempRoot = cloudURL;
        }
        return tempRoot + service;
    }

    /**
     * Gets the directory for the image
     *
     * @param relativePath The relative path of the image
     * @return The path of the image
     */
    public static String getImagesDirectory(String relativePath) {
        String tempRoot;
        if (isLocal) {
            tempRoot = localFileDirectory;
        } else {
            tempRoot = cloudFileDirectory;
        }
        return tempRoot + relativePath;
    }

    /**
     * Gets the change password cookie URL
     *
     * @param relativePath The path which indicates which service will accept the cookie
     * @return The cookie path
     */
    public static String getChangePasswordCookiePath(String relativePath) {
        String tempRoot;
        if (isLocal) {
            tempRoot = changePasswordCookieLocalPath;
        } else {
            tempRoot = changePasswordCookieCloudPath;
        }
        return tempRoot + relativePath;
    }

    /**
     * Get the APN signed key file root address
     *
     * @return The path of the APN signed key file
     */
    public static String getAPNKeyAddress() {
        String address;
        if (isLocal) {
            address = localKeyAddress;
        } else {
            address = cloudKeyAddress;
        }
        return address;
    }
}
