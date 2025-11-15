package com.reviewfilm.kasihreview.dto;

public class MoviesDTO {
    private int movieId;
    private String title;
    private String genre;
    private String description;
    private double rating;
    private String posterUrl;

    public MoviesDTO() {}

    public MoviesDTO(int movieId, String title, String genre, String description, double rating, String posterUrl) {
        this.movieId = movieId;
        this.title = title;
        this.genre = genre;
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

    public String getGenre() { 
        return genre; 
    }

    public void setGenre(String genre) { 
        this.genre = genre; 
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