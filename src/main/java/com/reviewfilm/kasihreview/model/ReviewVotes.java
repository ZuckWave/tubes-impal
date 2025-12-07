package com.reviewfilm.kasihreview.model;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "review_votes")
public class ReviewVotes {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "vote_id")
    private int voteId;
    
    @ManyToOne
    @JoinColumn(name = "review_id", nullable = false)
    @JsonBackReference("review-votes")
    private Review review;
    
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)  
    @JsonBackReference("voter-votes")
    private MovieGoer voter;
    
    @Column(name = "vote_type", nullable = false, length = 10)
    private String voteType;

    public ReviewVotes() {}
    
    public int getVoteId() {
        return voteId;
    }
    
    public void setVoteId(int voteId) {
        this.voteId = voteId;
    }
    
    public Review getReview() {
        return review;
    }
    
    public void setReview(Review review) {
        this.review = review;
    }
    
    public MovieGoer getVoter() {
        return voter;
    }
    
    public void setVoter(MovieGoer voter) {
        this.voter = voter;
    }
    
    public String getVoteType() {
        return voteType;
    }
    
    public void setVoteType(String voteType) {
        this.voteType = voteType;
    }
}