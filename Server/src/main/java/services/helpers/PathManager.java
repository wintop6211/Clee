package main.java.services.helpers;

public class PathManager {
    private static String localAddress = "http://localhost:8080/";
    private static String cloudAddress = "http://174.138.45.173:8080/CollegeBuyerServer-1.0-SNAPSHOT/";
    private static String localDirectory = "/Users/liangy/Desktop/CollegeBuyer/CollegeBuyerServer/";
    private static String cloudDirectory = "/usr/share/CollegeBuyer/";
    private static String changePasswordCookieLocalPath = "/";
    private static String changePasswordCookieCloudPath = "/CollegeBuyerServer-1.0-SNAPSHOT/";
    private static String localKeyAddress = "/Users/liangy/Documents/CollegeBuyer/AuthKey_T85K849GX8.p8";
    private static String cloudKeyAddress = "/usr/share/CollegeBuyer/APNKey/AuthKey_T85K849GX8.p8";

    private static boolean isLocal = true;

    public static String getLink(String service) {
        String tempRoot;
        if (isLocal) {
            tempRoot = localAddress;
        } else {
            tempRoot = cloudAddress;
        }
        return tempRoot + service;
    }

    public static String getImagesDirectory(String relativePath) {
        String tempRoot;
        if (isLocal) {
            tempRoot = localDirectory;
        } else {
            tempRoot = cloudDirectory;
        }
        return tempRoot + relativePath;
    }

    public static String getChangePasswordCookiePath(String relativePath) {
        String tempRoot;
        if (isLocal) {
            tempRoot = changePasswordCookieLocalPath;
        } else {
            tempRoot = changePasswordCookieCloudPath;
        }
        return tempRoot + relativePath;
    }

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
