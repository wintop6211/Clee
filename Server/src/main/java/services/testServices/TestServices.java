package main.java.services.testServices;

import main.java.entities.managements.ImageManagement;
import org.glassfish.jersey.media.multipart.FormDataParam;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

@Path("/test")
public class TestServices {
    @Path("/getImage")
    @GET
    @Produces("image/jpeg")
    public Response getImage() throws IOException {
        byte[] image = ImageManagement.readImage("/Users/liangy/Desktop/image.jpeg", true);
        return Response.ok(new ByteArrayInputStream(image)).build();
    }

    @Path("/putImage")
    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response putImage(@FormDataParam("image") InputStream inputStream) throws IOException {
        ImageManagement.writeImage("/Users/liangy/Desktop/image.jpeg", inputStream);
        return Response.ok("ok").build();
    }

    @Path("/exception")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response exception() throws RuntimeException {
        throw new RuntimeException();
    }
}
