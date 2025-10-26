package com.reviewfilm.kasihreview.model;

import java.util.List; 

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;

@Entity
public class MovieGoer {
    @Id
    private int userId;

    private String username;
    private String password;
    private String bio;
    private String fullName;
    private String avatarUrl;

    @OneToMany(mappedBy = "movieGoer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Review> reviews;

    @OneToOne(mappedBy = "movieGoer", cascade = CascadeType.ALL)
    private Watchlist watchlist;

    public MovieGoer() {

    }

    public MovieGoer(int userId, String username, String password, String bio, String fullName, String avatarUrl, List<Review> reviews, Watchlist watchlist) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.bio = bio;
        this.fullName = fullName;
        this.avatarUrl = avatarUrl;
        this.reviews = reviews;
        this.watchlist = watchlist;
    }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getBio() { return bio; }
    public void setBio(String bio) { this.bio = bio; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getAvatarUrl() { return avatarUrl; }
    public void setAvatarUrl(String avatarUrl) { this.avatarUrl = avatarUrl; }

    public List<Review> getReviews() { return reviews; }
    public void setReviews(List<Review> reviews) { this.reviews = reviews; }

    public Watchlist getWatchlist() { return watchlist; }
    public void setWatchlist(Watchlist watchlist) { this.watchlist = watchlist; }
}
