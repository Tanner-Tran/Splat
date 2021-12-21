package com.splat.data.DAO;
import com.splat.data.DTO.ReviewDTO;
import org.jdbi.v3.core.Jdbi;

import java.util.List;
import java.util.Optional;

public class ReviewDAO {

    private final Jdbi jdbi;

    public ReviewDAO(Jdbi jdbi) {
        this.jdbi = jdbi;
    }

    public Optional<ReviewDTO> getReviewByAuthorAndRestaurant(String author, String restName) {
        return jdbi.withHandle(h ->
            h.createQuery("SELECT * FROM Review WHERE author=:author AND restName =:restName")
                .bind("author", author)
                .bind("restName", restName)
                .mapToBean(ReviewDTO.class)
                .findOne());
    }

    public List<ReviewDTO> getReviewsByAuthor(String author) {
        return jdbi.withHandle(h ->
                h.createQuery("SELECT * FROM Review WHERE author =:author ORDER BY created DESC")
                        .bind("author", author)
                        .mapToBean(ReviewDTO.class)
                        .list());
    }

    public List<ReviewDTO> getReviewsByRestaurant(String restName) {
        return jdbi.withHandle(h ->
                h.createQuery("SELECT * FROM Review WHERE restName =:restName ORDER BY created DESC")
                        .bind("restName", restName)
                        .mapToBean(ReviewDTO.class)
                        .list());
    }

    public List<ReviewDTO> getRecentReviews() {
        return jdbi.withHandle(h ->
                h.createQuery("SELECT top 5 * FROM Review ORDER BY created DESC")
                        .mapToBean(ReviewDTO.class)
                        .list());
    }

    public void addReview(String author, String restName, String title, String bodyText, int rating) {
        jdbi.useHandle(h ->
                    h.createUpdate("INSERT INTO Review(bodyText, rating, restName, author, title) values (:bodyText, :rating, :restName, :author, :title)")
                            .bind("bodyText", bodyText)
                            .bind("rating", rating)
                            .bind("restName", restName)
                            .bind("author", author)
                            .bind("title", title)
                            .execute());
    }

    public void updateReview(String author, String restName, String title, String bodyText, int rating) {
        jdbi.useHandle(h ->
                h.createUpdate("UPDATE Review SET bodyText=:bodyText, rating=:rating, title=:title where author=:author AND restName=:restName")
                        .bind("bodyText", bodyText)
                        .bind("rating", rating)
                        .bind("title", title)
                        .bind("author", author)
                        .bind("restName", restName)
                        .execute());
    }
    public void deleteReview(String author, String restName) {
        jdbi.useHandle(h ->
                h.createUpdate("DELETE FROM Review WHERE author=:author AND restName=:restName")
                .bind("author", author)
                .bind("restName", restName)
                .execute());

    }
}