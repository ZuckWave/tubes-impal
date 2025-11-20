package com.reviewfilm.kasihreview.dto;

import java.util.List;

public class MoviesDTO {
    private int movieId;
    private String title;
    private List<String> genre; 
    private int releaseYear; 
    private String description;
    private double rating;
    private String posterUrl;

    public MoviesDTO() {}

    public MoviesDTO(int movieId, String title, List<String> genre, int releaseYear, 
                     String description, double rating, String posterUrl) {
        this.movieId = movieId;
        this.title = title;
        this.genre = genre;
        this.releaseYear = releaseYear;
        this.description = description;
        this.rating = rating;
        this.posterUrl = posterUrl;
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

    public List<String> getGenre() { 
        return genre; 
    }

    public void setGenre(List<String> genre) { 
        this.genre = genre; 
    }

    public int getReleaseYear() { 
        return releaseYear; 
    }

    public void setReleaseYear(int releaseYear) { 
        this.releaseYear = releaseYear; 
    }

    public String getDescription() { 
        return description; 
    }
    
    public void setDescription(String description) { 
        this.description = description; 
    }

    public double getRating() { 
        return rating; 
    }

    public void setRating(double rating) { 
        this.rating = rating; 
    }

    public String getPosterUrl() { 
        return posterUrl; 
    }

    public void setPosterUrl(String posterUrl) { 
        this.posterUrl = posterUrl; 
    }
}