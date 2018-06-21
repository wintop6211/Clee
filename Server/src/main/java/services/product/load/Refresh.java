package main.java.services.product.load;

import main.java.status.manager.ItemOffsetRecorder;
import org.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Since the item offset is stored in the session when the client loads items, the item offset recorder status
 * should be refreshed when the user refreshes their pages.
 */
@Path("/product/refresh")
public class Refresh {

    @Context
    HttpServletRequest request;

    /**
     * Refresh the offset for simply loading items.
     * @return {"Success": "The offset has been reset."}
     * or the error will be thrown
     */
    @Path("/load")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response refreshLoading() {
        JSONObject jsonObject = resetItemOffset(ItemOffsetRecorder.ALL_ITEM_OFFSET_K);
        return Response.ok(jsonObject.toString()).build();
    }

    /**
     * Refresh the offset for one specific category
     * @param categoryInString The category in string format
     * @return {"Success": "The offset has been reset."}
     * or the error will be thrown
     */
    @Path("/load/{category}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response refreshLoading(@PathParam("category") String categoryInString) {
        JSONObject jsonObject = resetItemOffset(categoryInString);
        return Response.ok(jsonObject.toString()).build();
    }

    /**
     * Refresh the offset for searching items.
     * @return {"Success": "The offset has been reset."}
     * or the error will be thrown
     */
    @Path("/search")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response refreshLoadSearchingItems() {
        JSONObject jsonObject = resetItemOffset(ItemOffsetRecorder.SEARCH_ITEM_OFFSET_K);
        return Response.ok(jsonObject.toString()).build();
    }

    /**
     * Refresh the offset for loading selling items.
     * @return {"Success": "The offset has been reset."}
     * or the error will be thrown
     */
    @Path("/load/selling")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response refreshLoadingSellingItems() {
        JSONObject jsonObject = resetItemOffset(ItemOffsetRecorder.SELLING_ITEM_OFFSET_K);
        return Response.ok(jsonObject.toString()).build();
    }

    /**
     * Refresh the offset for loading bought items.
     * @return {"Success": "The offset has been reset."}
     * or the error will be thrown
     */
    @Path("/load/bought")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response refreshLoadingBoughtItems() {
        JSONObject jsonObject = resetItemOffset(ItemOffsetRecorder.BOUGHT_ITEM_OFFSET_K);
        return Response.ok(jsonObject.toString()).build();
    }

    /**
     * Refresh the offset for loading requested items.
     * @return {"Success": "The offset has been reset."}
     * or the error will be thrown
     */
    @Path("/load/requested")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response refreshLoadingRequestedItems() {
        JSONObject jsonObject = resetItemOffset(ItemOffsetRecorder.REQUESTED_ITEM_OFFSET_K);
        return Response.ok(jsonObject.toString()).build();
    }

    /**
     * Resets the item offset
     * @param offsetIdentifier The identifier which identifies what kind of offset it is
     * @return {"Success": "The offset has been reset."}
     * or the error will be thrown
     */
    private JSONObject resetItemOffset(String offsetIdentifier) {
        ItemOffsetRecorder.resetItemOffset(request, offsetIdentifier);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("Success", "The offset has been reset.");
        return jsonObject;
    }
}
