package com.splat.data.DAO;

import org.jdbi.v3.core.Jdbi;

public class AccountDAO {

    private final Jdbi jdbi;

    public AccountDAO(Jdbi jdbi) {
        this.jdbi = jdbi;
    }

    public void addAccount(String id) {
        jdbi.useHandle(h ->
                h.createUpdate("INSERT INTO Account(id) values (:id)")
                        .bind("id", id)
                        .execute());
    }
}
