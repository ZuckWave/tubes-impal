package com.reviewfilm.kasihreview.dto;

import java.time.LocalDateTime;

public class VoteDTO {
    private int voteId;
    private int movieGoerId;
    private String voterName;
    private int reviewId;
    private String voteType;
    private LocalDateTime createdAt;

    public VoteDTO() {}

    public VoteDTO(int voteId, int movieGoerId, String voterName, int reviewId, 
                   String voteType, LocalDateTime createdAt) {
        this.voteId = voteId;
        this.movieGoerId = movieGoerId;
        this.voterName = voterName;
        this.reviewId = reviewId;
        this.voteType = voteType;
        this.createdAt = createdAt;
    }

    public int getVoteId() {
        return voteId;
    }

    public void setVoteId(int voteId) {
        this.voteId = voteId;
    }

    public int getMovieGoerId() {
        return movieGoerId;
    }

    public void setMovieGoerId(int movieGoerId) {
        this.movieGoerId = movieGoerId;
    }

    public String getVoterName() {
        return voterName;
    }

    public void setVoterName(String voterName) {
        this.voterName = voterName;
    }

    public int getReviewId() {
        return reviewId;
    }

    public void setReviewId(int reviewId) {
        this.reviewId = reviewId;
    }

    public String getVoteType() {
        return voteType;
    }

    public void setVoteType(String voteType) {
        this.voteType = voteType;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}