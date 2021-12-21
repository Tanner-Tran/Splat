package com.splat.data.DAO;
import com.splat.data.DTO.RestaurantDTO;
import org.jdbi.v3.core.Jdbi;

import java.util.Optional;

public class RestaurantDAO {

    private final Jdbi jdbi;

    public RestaurantDAO(Jdbi jdbi) {
        this.jdbi = jdbi;
    }

    public Optional<RestaurantDTO> getRestaurantByName(String restName) {
        return jdbi.withHandle(h ->
                h.createQuery("SELECT * FROM Restaurant WHERE restName =:restName")
                        .bind("restName", restName)
                        .mapToBean(RestaurantDTO.class)
                        .findOne());
    }

    public void addRestaurant(String restName) {
        jdbi.useHandle(h ->
            h.createUpdate("INSERT INTO Restaurant(restName) values (:restName)")
                    .bind("restName", restName)
                    .execute()
        );
    }
}