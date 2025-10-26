package com.reviewfilm.kasihreview.model;

import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

public class ReviewVotes {
    @Id
    private int voteId;

    private String voteType; 

    @ManyToOne
    @JoinColumn(name = "review_id")
    private Review review;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private MovieGoer voter;

    public ReviewVotes() {}

    public ReviewVotes(int voteId, String voteType, Review review, MovieGoer voter) {
        this.voteId = voteId;
        this.voteType = voteType;
        this.review = review;
        this.voter = voter;
    }

    public int getVoteId() { return voteId; }
    public void setVoteId(int voteId) { this.voteId = voteId; }

    public String getVoteType() { return voteType; }
    public void setVoteType(String voteType) { this.voteType = voteType; }

    public Review getReview() { return review; }
    public void setReview(Review review) { this.review = review; }

    public MovieGoer getVoter() { return voter; }
    public void setVoter(MovieGoer voter) { this.voter = voter; }
}
