package com.reviewfilm.kasihreview.dto;

import java.util.List;

public class WatchlistDTO {
    private int watchlistId;
    private int userId;
    private String username;
    private List<MovieInWatchlistDTO> movies;

    public WatchlistDTO() {}

    public WatchlistDTO(int watchlistId, int userId, String username, List<MovieInWatchlistDTO> movies) {
        this.watchlistId = watchlistId;
        this.userId = userId;
        this.username = username;
        this.movies = movies;
    }

    public int getWatchlistId() {
        return watchlistId;
    }

    public void setWatchlistId(int watchlistId) {
        this.watchlistId = watchlistId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<MovieInWatchlistDTO> getMovies() {
        return movies;
    }

    public void setMovies(List<MovieInWatchlistDTO> movies) {
        this.movies = movies;
    }

    // Inner class untuk movie
    public static class MovieInWatchlistDTO {
        private int movieId;
        private String movieTitle;
        private String posterUrl;
        private int releaseYear;
        private float avgRating;

        public MovieInWatchlistDTO() {}

        public MovieInWatchlistDTO(int movieId, String movieTitle, String posterUrl, int releaseYear, float avgRating) {
            this.movieId = movieId;
            this.movieTitle = movieTitle;
            this.posterUrl = posterUrl;
            this.releaseYear = releaseYear;
            this.avgRating = avgRating;
        }

        public int getMovieId() {
            return movieId;
        }

        public void setMovieId(int movieId) {
            this.movieId = movieId;
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

        public int getReleaseYear() {
            return releaseYear;
        }

        public void setReleaseYear(int releaseYear) {
            this.releaseYear = releaseYear;
        }

        public float getAvgRating() {
            return avgRating;
        }

        public void setAvgRating(float avgRating) {
            this.avgRating = avgRating;
        }
    }
}