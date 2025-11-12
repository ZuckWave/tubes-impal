package com.reviewfilm.kasihreview.dto;

public class ReviewDTO {
    private int id;
    private String reviewerName;
    private String content;
    private int rating;
    private int upvotes;
    private int downvotes;

    public ReviewDTO() {}

    public ReviewDTO(int id, String reviewerName, String content, int rating, int upvotes, int downvotes) {
        this.id = id;
        this.reviewerName = reviewerName;
        this.content = content;
        this.rating = rating;
        this.upvotes = upvotes;
        this.downvotes = downvotes;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getReviewerName() { return reviewerName; }
    public void setReviewerName(String reviewerName) { this.reviewerName = reviewerName; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public int getRating() { return rating; }
    public void setRating(int rating) { this.rating = rating; }

    public int getUpvotes() { return upvotes; }
    public void setUpvotes(int upvotes) { this.upvotes = upvotes; }

    public int getDownvotes() { return downvotes; }
    public void setDownvotes(int downvotes) { this.downvotes = downvotes; }
}