package com.reviewfilm.kasihreview.dto;

public class ReviewRequestDTO {
    private int movieId;
    private int userId;
    private String content;
    private int rating;

    public ReviewRequestDTO() {}

    public ReviewRequestDTO(int movieId, int userId, String content, int rating) {
        this.movieId = movieId;
        this.userId = userId;
        this.content = content;
        this.rating = rating;
    }

    public int getMovieId() { return movieId; }
    public void setMovieId(int movieId) { this.movieId = movieId; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public int getRating() { return rating; }
    public void setRating(int rating) { this.rating = rating; }
}