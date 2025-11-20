package com.reviewfilm.kasihreview.model;

import java.util.ArrayList;
import java.util.List;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "movies")
public class Movies {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int movieId;

    private String title;
    private int releaseYear;

    @ElementCollection
    @JoinTable(
    name = "movie_genre",
    joinColumns = @JoinColumn(name = "movie_id")
    )
    @Column(name = "genre")
    private List<String> genre = new ArrayList<>();


    @Column(length = 1000)
    private String description;

    private String posterUrl;
    private float avgRating;

    @OneToMany(mappedBy = "movie", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference("movie-reviews")
    private List<Review> reviews;

    @ManyToMany(mappedBy = "movies")
    @JsonIgnore
    private List<Watchlist> watchlists = new ArrayList<>();

    public Movies() {}

    public Movies(String title, int releaseYear, List<String> genre, String description, String posterUrl, float avgRating) {
        this.title = title;
        this.releaseYear = releaseYear;
        this.genre = genre;
        this.description = description;
        this.posterUrl = posterUrl;
        this.avgRating = avgRating;
    }

    public int getMovieId() { 
        return movieId; 
    }
    
    public void setMovieId(int movieId) { 
        this.movieId = movieId; 
    }

    public String getTitle() { 
        return title; 
    }

    public void setTitle(String title) { 
        this.title = title; 
    }

    public int getReleaseYear() { 
        return releaseYear; 
    }

    public void setReleaseYear(int releaseYear) { 
        this.releaseYear = releaseYear; 
    }

    public List<String> getGenre() { 
        return genre; 
    }
    
    public void setGenre(List<String> genre) { 
        this.genre = genre; 
    }

    public String getDescription() { 
        return description; 
    }

    public void setDescription(String description) { 
        this.description = description; 
    }

    public String getPosterUrl() { 
        return posterUrl; 
    }

    public void setPosterUrl(String posterUrl) { 
        this.posterUrl = posterUrl; 
    }

    public float getAvgRating() { 
        return avgRating; 
    }

    public void setAvgRating(float avgRating) { 
        this.avgRating = avgRating; 
    }

    public List<Review> getReviews() { 
        return reviews; 
    }

    public void setReviews(List<Review> reviews) { 
        this.reviews = reviews; 
    }

    public List<Watchlist> getWatchlists() {
        return watchlists;
    }

    public void setWatchlists(List<Watchlist> watchlists) {
        this.watchlists = watchlists;
    }
}