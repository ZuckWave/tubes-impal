package com.reviewfilm.kasihreview.model;

import java.time.LocalDateTime; 
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "reviews")
public class Review {
    @Id
    private int reviewId;

    private int rating;
    private String reviewText;

    private boolean isSpoiler;
    private LocalDateTime createdAt = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "user_id")
    private MovieGoer movieGoer;

    @ManyToOne
    @JoinColumn(name = "movie_id")
    private Movies movie;

    @OneToMany(mappedBy = "review", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReviewVotes> votes;

    public Review() {}

    public Review(int reviewId, int rating, String reviewText, boolean isSpoiler, MovieGoer movieGoer, Movies movie, List<ReviewVotes> votes) {
        this.reviewId = reviewId;
        this.rating = rating;
        this.reviewText = reviewText;
        this.isSpoiler = isSpoiler;
        this.movieGoer = movieGoer;
        this.movie = movie;
        this.votes = votes;
    }

    public int getReviewId() { 
        return reviewId; 
    }

    public void setReviewId(int reviewId) { 
        this.reviewId = reviewId; 
    }

    public int getRating() { 
        return rating; 
    }
    
    public void setRating(int rating) { 
        this.rating = rating; 
    }

    public String getReviewText() { 
        return reviewText; 
    }

    public void setReviewText(String reviewText) { 
        this.reviewText = reviewText; 
    }

    public boolean getIsSpoiler() { 
        return isSpoiler; 
    }

    public void setIsSpoiler(boolean isSpoiler) { 
        this.isSpoiler = isSpoiler; 
    }

    public LocalDateTime getCreatedAt() { 
        return createdAt; 
    
    }

    public void setCreatedAt(LocalDateTime createdAt) { 
        this.createdAt = createdAt; 
    }

    public MovieGoer getMovieGoer() { 
        return movieGoer; 
    }

    public void setMovieGoer(MovieGoer movieGoer) { 
        this.movieGoer = movieGoer; 
    }

    public Movies getMovie() { 
        return movie; 
    }
    public void setMovie(Movies movie) { 
        this.movie = movie; 
    }

    public List<ReviewVotes> getVotes() { 
        return votes; 
    }

    public void setVotes(List<ReviewVotes> votes) { 
        this.votes = votes; 
    }
}
