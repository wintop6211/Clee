package main.java.status.manager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * The class is for recording item offset when loading items
 */
public class ItemOffsetRecorder {

    public static final String ALL_ITEM_OFFSET_K = "allItemOffset";
    public static final String BOUGHT_ITEM_OFFSET_K = "boughtItemOffset";
    public static final String SELLING_ITEM_OFFSET_K = "sellingItemOffset";
    public static final String REQUESTED_ITEM_OFFSET_K = "requestedItemOffset";
    public static final String SEARCH_ITEM_OFFSET_K = "searchItemOffset";
    public static final int MAX_ITEM_ALLOWED = 1;
    private static final int INACTIVE_TIME = 60 * 60 * 2; // Two hours

    /**
     * Gets the loading offset by using the offset identifier
     * @param request The object which contains request information
     * @param identifier The offset identifier
     * @return The offset for loading items
     */
    public static Integer getItemOffset(HttpServletRequest request, String identifier) {
        HttpSession httpSession = request.getSession();
        Integer offset = (Integer) httpSession.getAttribute(identifier);
        if (offset == null) {
            offset = 0;
            httpSession.setAttribute(identifier, offset);
            httpSession.setMaxInactiveInterval(INACTIVE_TIME);
        }
        return offset;
    }

    /**
     * Updates the item offset by using the offset identifier
     * @param request The object which contains request information
     * @param identifier The offset identifier
     */
    public static void updateItemOffset(HttpServletRequest request, String identifier) {
        HttpSession httpSession = request.getSession();
        Integer offset = (Integer) httpSession.getAttribute(identifier);
        if (offset == null) {
            offset = 0;
            httpSession.setMaxInactiveInterval(INACTIVE_TIME);
        }
        httpSession.setAttribute(identifier, offset + MAX_ITEM_ALLOWED);
    }

    /**
     * Resets the item offset by using the offset identifier
     * @param request The object which contains request information
     * @param identifier The offset identifier
     */
    public static void resetItemOffset(HttpServletRequest request, String identifier) {
        HttpSession httpSession = request.getSession();
        Integer offset = (Integer) httpSession.getAttribute(identifier);
        if (offset == null) {
            httpSession.setMaxInactiveInterval(INACTIVE_TIME);
        }
        httpSession.setAttribute(identifier, 0);
    }
}
