package com.reviewfilm.kasihreview.model;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "movies")
public class Movies {

    @Id
    private int movieId;

    private String title;
    private int releaseYear;

    @ElementCollection
    private List<String> genre;

    @Column(length = 1000)
    private String description;

    private String posterUrl;
    private float avgRating;

    @OneToMany(mappedBy = "movie", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Review> reviews;

    public Movies() {

    }

    public Movies(int movieId, String title, int releaseYear, List<String> genre, String description, String posterUrl, float avgRating, List<Review> reviews) {
        this.movieId = movieId;
        this.title = title;
        this.genre = genre;
        this.releaseYear = releaseYear;
        this.description = description;
        this.posterUrl = posterUrl;
        this.avgRating = avgRating;
        this.reviews = reviews;
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
}
