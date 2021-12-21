package com.splat.data.DTO.response;

import com.splat.data.DTO.ReviewDTO;

import java.text.DecimalFormat;
import java.util.List;

public class GetReviewsResponse {
    private List<ReviewDTO> reviews;
    private String average;

    public GetReviewsResponse(List<ReviewDTO> reviews) {
        this.reviews = reviews;
        double calc = 0;

        for (ReviewDTO review : reviews) {
            calc += review.getRating();
        }

        double rawAverage = calc / reviews.size();
        DecimalFormat df = new DecimalFormat("#.##");
        this.average = df.format(rawAverage);
    }

    public String getAverage() {
        return average;
    }

    public void setAverage(String average) {
        this.average = average;
    }

    public void setReviews(List<ReviewDTO> reviews) {
        this.reviews = reviews;
    }

    public List<ReviewDTO> getReviews() {
        return reviews;
    }
}
