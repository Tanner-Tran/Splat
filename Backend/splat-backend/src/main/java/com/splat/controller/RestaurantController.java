package com.splat.controller;

import com.splat.data.DAO.RestaurantDAO;
import com.splat.data.DTO.Error;
import com.splat.data.DTO.RestaurantDTO;


import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Optional;


@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Path("/restaurants")
public class RestaurantController {

    private final RestaurantDAO restaurantDAO;

    public RestaurantController(RestaurantDAO restaurantDAO){this.restaurantDAO = restaurantDAO;}

    @GET
    public Response getRestaurant(@QueryParam("restName") String restName) {
        // Fetch the account from the database
        Optional<RestaurantDTO> restaurant = restaurantDAO.getRestaurantByName(restName.toLowerCase().trim());

        if (restaurant.isPresent()) {
            return Response
                .ok()
                .entity(restaurant.get())
                .build();
        } else {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
    }
}
