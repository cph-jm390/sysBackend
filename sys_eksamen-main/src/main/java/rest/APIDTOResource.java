package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import facades.APIManager;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import javax.ws.rs.Path;

@Path("apicall")
public class APIDTOResource {
    public static final APIManager apiManager = new APIManager();
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    @GET
    @RolesAllowed("user")
    @Produces({MediaType.APPLICATION_JSON})
    public Response getAll() throws Exception {
        return Response.ok().entity(GSON.toJson(apiManager.createFactDTOFromAPI())).build();
    }




        @POST
        @Consumes(MediaType.APPLICATION_JSON)
        public void handlePostRequest(String inputData) {
            // Process the input data
            System.out.println("Received input: " + inputData);
        }

    }

