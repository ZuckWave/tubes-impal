package com.reviewfilm.kasihreview.dto;

import java.time.LocalDateTime;

public class ReviewDTO {
    private int reviewId;
    private String reviewerName;
    private int movieId;
    private String movieTitle;
    private String content;
    private int rating;
    private boolean isSpoiler;
    private LocalDateTime createdAt;
    private int upvotes;
    private int downvotes;

    public ReviewDTO() {}

    public ReviewDTO(int reviewId, String reviewerName, int movieId, String movieTitle,
                     String content, int rating, boolean isSpoiler, LocalDateTime createdAt,
                     int upvotes, int downvotes) {
        this.reviewId = reviewId;
        this.reviewerName = reviewerName;
        this.movieId = movieId;
        this.movieTitle = movieTitle;
        this.content = content;
        this.rating = rating;
        this.isSpoiler = isSpoiler;
        this.createdAt = createdAt;
        this.upvotes = upvotes;
        this.downvotes = downvotes;
    }

    public int getReviewId() {
        return reviewId;
    }

    public void setReviewId(int reviewId) {
        this.reviewId = reviewId;
    }

    public String getReviewerName() {
        return reviewerName;
    }

    public void setReviewerName(String reviewerName) {
        this.reviewerName = reviewerName;
    }

    public int getMovieId() {
        return movieId;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }

    public String getMovieTitle() {
        return movieTitle;
    }

    public void setMovieTitle(String movieTitle) {
        this.movieTitle = movieTitle;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public boolean isSpoiler() {
        return isSpoiler;
    }

    public void setSpoiler(boolean spoiler) {
        isSpoiler = spoiler;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public int getUpvotes() {
        return upvotes;
    }

    public void setUpvotes(int upvotes) {
        this.upvotes = upvotes;
    }

    public int getDownvotes() {
        return downvotes;
    }

    public void setDownvotes(int downvotes) {
        this.downvotes = downvotes;
    }
}