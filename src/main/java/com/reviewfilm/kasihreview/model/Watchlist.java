package com.reviewfilm.kasihreview.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "watchlist")
public class Watchlist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int watchlistId;

    @OneToOne
    @JoinColumn(name = "user_id")
    @JsonManagedReference("moviegoer-watchlist")
    private MovieGoer movieGoer;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
    name = "watchlist_movies",
    joinColumns = @JoinColumn(name = "watchlist_id"),
    inverseJoinColumns = @JoinColumn(name = "movie_id")
    )
    private List<Movies> movies = new ArrayList<>();
    
    public Watchlist() {}

    public Watchlist(MovieGoer movieGoer) {
        this.movieGoer = movieGoer;
        this.movies = new ArrayList<>();
    }

    public void addMovie(Movies movie) {
        if (!this.movies.contains(movie)) {
            this.movies.add(movie);
            movie.getWatchlists().add(this);
        }
    }

    public void removeMovie(Movies movie) {
        this.movies.remove(movie);
        movie.getWatchlists().remove(this);
    }

    public int getWatchlistId() { 
        return watchlistId; 
    }
    
    public void setWatchlistId(int watchlistId) { 
        this.watchlistId = watchlistId; 
    }

    public MovieGoer getMovieGoer() { 
        return movieGoer; 
    }

    public void setMovieGoer(MovieGoer movieGoer) { 
        this.movieGoer = movieGoer; 
    }

    public List<Movies> getMovies() { 
        return movies; 
    }

    public void setMovies(List<Movies> movies) { 
        this.movies = movies; 
    }
}