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

@Path("/product/refresh")
public class Refresh {

    @Context
    HttpServletRequest request;

    @Path("/load")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response refreshLoading() {
        JSONObject jsonObject = resetItemOffset(ItemOffsetRecorder.ALL_ITEM_OFFSET);
        return Response.ok(jsonObject.toString()).build();
    }

    @Path("/load/{category}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response refreshLoading(@PathParam("category") String categoryInString) {
        JSONObject jsonObject = resetItemOffset(categoryInString);
        return Response.ok(jsonObject.toString()).build();
    }

    @Path("/search")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response refreshLoadSearchingItems() {
        JSONObject jsonObject = resetItemOffset(ItemOffsetRecorder.SEARCH_ITEM_OFFSET);
        return Response.ok(jsonObject.toString()).build();
    }

    @Path("/load/selling")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response refreshLoadingSellingItems() {
        JSONObject jsonObject = resetItemOffset(ItemOffsetRecorder.SELLING_ITEM_OFFSET);
        return Response.ok(jsonObject.toString()).build();
    }

    @Path("/load/bought")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response refreshLoadingBoughtItems() {
        JSONObject jsonObject = resetItemOffset(ItemOffsetRecorder.BOUGHT_ITEM_OFFSET);
        return Response.ok(jsonObject.toString()).build();
    }

    @Path("/load/requested")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response refreshLoadingRequestedItems() {
        JSONObject jsonObject = resetItemOffset(ItemOffsetRecorder.REQUESTED_ITEM_OFFSET);
        return Response.ok(jsonObject.toString()).build();
    }

    private JSONObject resetItemOffset(String offsetIdentifier) {
        ItemOffsetRecorder.resetItemOffset(request, offsetIdentifier);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("Success", "The offset has been reset.");
        return jsonObject;
    }
}
