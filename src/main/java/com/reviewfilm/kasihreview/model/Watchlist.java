package com.reviewfilm.kasihreview.model;

import java.util.List;

import jakarta.persistence.Entity;
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
    private int watchlistId;

    @OneToOne
    @JoinColumn(name = "user_id")
    private MovieGoer movieGoer;

    @ManyToMany
    @JoinTable(
        name = "watchlist_movies",
        joinColumns = @JoinColumn(name = "watchlist_id"),
        inverseJoinColumns = @JoinColumn(name = "movie_id")
    )
    private List<Movies> movies;

    public Watchlist() {}

    public Watchlist(int watchlistId, MovieGoer movieGoer, List<Movies> movies) {
        this.watchlistId = watchlistId;
        this.movieGoer = movieGoer;
        this.movies = movies;
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
