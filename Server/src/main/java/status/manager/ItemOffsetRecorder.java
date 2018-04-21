package main.java.status.manager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class ItemOffsetRecorder {

    public static final String ALL_ITEM_OFFSET = "allItemOffset";
    public static final String BOUGHT_ITEM_OFFSET = "boughtItemOffset";
    public static final String SELLING_ITEM_OFFSET = "sellingItemOffset";
    public static final String REQUESTED_ITEM_OFFSET = "requestedItemOffset";
    public static final String SEARCH_ITEM_OFFSET = "searchItemOffset";
    public static final int MAX_ITEM_ALLOWED = 1;
    private static final int INACTIVE_TIME = 60 * 60 * 2; // Two hours

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

    public static void updateItemOffset(HttpServletRequest request, String identifier) {
        HttpSession httpSession = request.getSession();
        Integer offset = (Integer) httpSession.getAttribute(identifier);
        if (offset == null) {
            offset = 0;
            httpSession.setMaxInactiveInterval(INACTIVE_TIME);
        }
        httpSession.setAttribute(identifier, offset + MAX_ITEM_ALLOWED);
    }

    public static void resetItemOffset(HttpServletRequest request, String identifier) {
        HttpSession httpSession = request.getSession();
        Integer offset = (Integer) httpSession.getAttribute(identifier);
        if (offset == null) {
            httpSession.setMaxInactiveInterval(INACTIVE_TIME);
        }
        httpSession.setAttribute(identifier, 0);
    }
}
