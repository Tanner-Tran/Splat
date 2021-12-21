package com.splat.service;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import com.splat.Exceptions.DuplicateReviewException;
import com.splat.data.DAO.RestaurantDAO;
import com.splat.data.DAO.ReviewDAO;
import com.splat.data.DTO.ReviewDTO;
import com.splat.data.DTO.request.AddReviewRequest;
import com.splat.data.DTO.request.DeleteReviewRequest;
import org.owasp.html.HtmlPolicyBuilder;
import org.owasp.html.PolicyFactory;

import java.util.List;

public class ReviewsService {
    private final ReviewDAO reviewDAO;
    private final RestaurantDAO restaurantDAO;

    public ReviewsService(ReviewDAO reviewDAO, RestaurantDAO restaurantDAO) {
        this.reviewDAO = reviewDAO;
        this.restaurantDAO = restaurantDAO;
    }

    public void addReview(AddReviewRequest addReviewRequest) throws DuplicateReviewException, FirebaseAuthException {
        FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(addReviewRequest.getSessionToken());
        String uid = decodedToken.getUid();
        String restNameHandled = addReviewRequest.getRestName().toLowerCase().trim();
        String bodyTextHandled = addReviewRequest.getBodyText().trim();
        String titleHandled = addReviewRequest.getTitle().trim();

        if (reviewDAO.getReviewByAuthorAndRestaurant(uid, restNameHandled).isPresent()) {
            throw new DuplicateReviewException("User may only have one review per restaurant");
        }

        if (!restaurantDAO.getRestaurantByName(restNameHandled).isPresent()) {
            restaurantDAO.addRestaurant(restNameHandled);
        }
        reviewDAO.addReview(uid, restNameHandled, titleHandled, bodyTextHandled, addReviewRequest.getRating());
    }

    public void updateReview(AddReviewRequest addReviewRequest) throws FirebaseAuthException {
        FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(addReviewRequest.getSessionToken());
        String uid = decodedToken.getUid();
        String restNameHandled = addReviewRequest.getRestName().toLowerCase().trim();
        String bodyTextHandled = addReviewRequest.getBodyText().trim();
        String titleHandled = addReviewRequest.getTitle().trim();

        reviewDAO.updateReview(uid, restNameHandled, titleHandled, bodyTextHandled, addReviewRequest.getRating());
    }
    public void deleteReview(DeleteReviewRequest deleteReviewRequest) throws FirebaseAuthException {
        FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(deleteReviewRequest.getSessionToken());
        String uid = decodedToken.getUid();

        String restNameHandled = deleteReviewRequest.getRestName().toLowerCase().trim();

        reviewDAO.deleteReview(uid, restNameHandled);
    }
    public List<ReviewDTO> getRecentReviews() {
        List<ReviewDTO> reviews;

        reviews = reviewDAO.getRecentReviews();

        for (ReviewDTO review : reviews) {
            sanitizeReview(review);
        }

        return reviews;
    }

    public List<ReviewDTO> getReviewsByRest(String restName) {
        List<ReviewDTO> reviews;

        reviews = reviewDAO.getReviewsByRestaurant(restName.toLowerCase().trim());

        for (ReviewDTO review : reviews) {
            sanitizeReview(review);
        }

        return reviews;
    }

    public List<ReviewDTO> getPersonalReviews(String sessionToken) throws FirebaseAuthException {

        FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(sessionToken);
        String uid = decodedToken.getUid();
        List<ReviewDTO> reviews;

        reviews = reviewDAO.getReviewsByAuthor(uid);

        for (ReviewDTO review : reviews) {
            sanitizeReview(review);
        }

        return reviews;
    }

    private String sanitizeHTML(String untrustedHTML){
        PolicyFactory policy = new HtmlPolicyBuilder()
                .allowStandardUrlProtocols()
                .toFactory();

        return policy.sanitize(untrustedHTML);
    }
        public void sanitizeReview(ReviewDTO reviewDTO) {
            reviewDTO.setBodyText(sanitizeHTML(reviewDTO.getBodyText()));
            reviewDTO.setRestName(sanitizeHTML(reviewDTO.getRestName()));
            reviewDTO.setAuthor(sanitizeHTML(reviewDTO.getAuthor()));
            reviewDTO.setTitle(sanitizeHTML(reviewDTO.getTitle()));
    }
}
