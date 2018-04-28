package main.java.services.product;

import main.java.configuration.SessionProvider;
import main.java.entities.Category;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/category")
public class CategoryServices {
    @Path("/get/all")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllCategories() {
        Transaction transaction;
        JSONObject jsonObject = new JSONObject();
        try (final Session session = SessionProvider.getSession()) {
            transaction = session.beginTransaction();
            List categories = session.createQuery("FROM Category").list();
            JSONArray jsonArray = new JSONArray();
            for (Object object : categories) {
                JSONObject tempJsonObj = new JSONObject();
                Category category = (Category) object;
                String name = category.getName();
                tempJsonObj.put("category", name);
                jsonArray.put(tempJsonObj);
            }
            jsonObject.put("categories", jsonArray);
            transaction.commit();
        }
        return Response.ok(jsonObject.toString()).build();
    }
}
