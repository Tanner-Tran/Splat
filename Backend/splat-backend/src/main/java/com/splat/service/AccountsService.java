package com.splat.service;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import com.splat.data.DAO.AccountDAO;
import com.splat.data.DTO.request.AccountRequest;

public class AccountsService {
    private final AccountDAO accountDAO;

    public AccountsService(AccountDAO accountDAO) {
        this.accountDAO = accountDAO;
    }

    public void registerAccount(AccountRequest accountRequest) throws FirebaseAuthException {
        UserRecord.CreateRequest request = new UserRecord.CreateRequest()
                .setEmail(accountRequest.getEmail())
                .setPassword(accountRequest.getPassword());

        UserRecord userRecord = FirebaseAuth.getInstance().createUser(request);
        accountDAO.addAccount(userRecord.getUid());
    }
}
