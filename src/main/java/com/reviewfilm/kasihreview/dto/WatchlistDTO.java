package com.reviewfilm.kasihreview.dto;

public class WatchlistDTO {
    private int id;
    private String movieTitle;
    private String posterUrl;

    public WatchlistDTO() {}

    public WatchlistDTO(int id, String movieTitle, String posterUrl) {
        this.id = id;
        this.movieTitle = movieTitle;
        this.posterUrl = posterUrl;
    }

    public int getId() { 
        return id;
    }

    public void setId(int id) { 
        this.id = id; 
    }

    public String getMovieTitle() { 
        return movieTitle; 
    }

    public void setMovieTitle(String movieTitle) { 
        this.movieTitle = movieTitle; 
    }

    public String getPosterUrl() { 
        return posterUrl; 
    }

    public void setPosterUrl(String posterUrl) { 
        this.posterUrl = posterUrl; 
    }
}