package com.splat.controller;

import com.google.firebase.auth.FirebaseAuthException;
import com.splat.data.DTO.Error;
import com.splat.data.DTO.request.AccountRequest;
import com.splat.service.AccountsService;


import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Path("/accounts")
public class AccountsController {

    private final AccountsService accountService;

    public AccountsController(AccountsService accountService) {
        this.accountService = accountService;
    }

//    /**
//     * Endpoint to get an account, or return 404 if it doesn't exist.
//     */
//    @Path("/{id}")
//    @GET
//    public Response getAccount(@PathParam("id") int id) {
//        // Fetch the account from the database
//        Optional<AccountDTO> account = accountDAO.getAccount(id);
//
//        if (account.isPresent()) {
//            AccountResponse response = new AccountResponse(account.get());
//            return Response
//                .ok()
//                .entity(response)
//                .build();
//        } else {
//            throw new WebApplicationException(Response.Status.NOT_FOUND);
//        }
//    }


    @Path("/register")
    @POST
    public Response registerAccount(AccountRequest accountRequest) {
        Error error = new Error();
        try {
            accountService.registerAccount(accountRequest);
        }
        catch (FirebaseAuthException e) {
            error.setError(e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(error).build();
        }
        return Response.status(Response.Status.OK).build();
    }
}
