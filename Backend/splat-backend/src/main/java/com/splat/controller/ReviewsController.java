package com.splat.controller;

import com.google.firebase.auth.FirebaseAuthException;
import com.splat.Exceptions.DuplicateReviewException;
import com.splat.data.DTO.Error;
import com.splat.data.DTO.ReviewDTO;
import com.splat.data.DTO.request.AddReviewRequest;
import com.splat.data.DTO.request.DeleteReviewRequest;
import com.splat.data.DTO.response.GetReviewsResponse;
import com.splat.service.ReviewsService;


import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;


@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Path("/reviews")
public class ReviewsController {

    private final ReviewsService reviewsService;

    public ReviewsController(ReviewsService reviewsService) {
        this.reviewsService = reviewsService;
    }

    @Path("/restaurant")
    @GET
    public Response getReviewsByRest(@QueryParam("restName") String restName) {

        List<ReviewDTO> reviews = reviewsService.getReviewsByRest(restName);

        if (!reviews.isEmpty()) {
            return Response
                    .ok()
                    .entity(new GetReviewsResponse(reviews))
                    .build();
        } else {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
    }

    @Path("/personal")
    @GET
    public Response getPersonalReviews(@QueryParam("sessionToken") String sessionToken) {
        Error error = new Error();

        try {
            List<ReviewDTO> reviews = reviewsService.getPersonalReviews(sessionToken);

            if (!reviews.isEmpty()) {
                return Response
                        .ok()
                        .entity(new GetReviewsResponse(reviews))
                        .build();
            } else {
                throw new WebApplicationException(Response.Status.NOT_FOUND);
            }
        }
        catch (FirebaseAuthException e) {
            error.setError(e.getMessage());
            return Response.status(Response.Status.UNAUTHORIZED).entity(error).build();
        }
    }

    @Path("/recent")
    @GET
    public Response getRecentReviews() {

        List<ReviewDTO> reviews = reviewsService.getRecentReviews();

        if (!reviews.isEmpty()) {
            return Response
                    .ok()
                    .entity(new GetReviewsResponse(reviews))
                    .build();
        } else {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
    }

    @Path("/addReview")
    @POST
    public Response addReview(AddReviewRequest addReviewRequest) {
        Error error = new Error();

        if (addReviewRequest.getBodyText().length() > 300 || addReviewRequest.getRestName().length() > 50 || addReviewRequest.getTitle().length() > 50
        || addReviewRequest.getRating() < 0 || addReviewRequest.getRating() > 5) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        try {
            reviewsService.addReview(addReviewRequest);
        }
        catch (FirebaseAuthException | DuplicateReviewException e) {
            error.setError(e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(error).build();
        }
        return Response.status(Response.Status.OK).build();
    }

    @Path("/updateReview")
    @PUT
    public Response updateReview(AddReviewRequest addReviewRequest) {
        Error error = new Error();

        if (addReviewRequest.getBodyText().length() > 300 || addReviewRequest.getRestName().length() > 50 || addReviewRequest.getTitle().length() > 50
                || addReviewRequest.getRating() < 0 || addReviewRequest.getRating() > 5) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        try {
            reviewsService.updateReview(addReviewRequest);
        }
        catch (FirebaseAuthException e) {
            error.setError(e.getMessage());
            return Response.status(Response.Status.UNAUTHORIZED).entity(error).build();
        }
        return Response.status(Response.Status.OK).build();
    }

    @Path("/deleteReview")
    @POST
    public Response deleteReview(DeleteReviewRequest deleteReviewRequest) {
        Error error = new Error();
        try {
            reviewsService.deleteReview(deleteReviewRequest);
        }
        catch (FirebaseAuthException e) {
            error.setError(e.getMessage());
            return Response.status(Response.Status.UNAUTHORIZED).entity(error).build();
        }
        return Response.status(Response.Status.OK).build();
    }
}
