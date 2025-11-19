package com.reviewfilm.kasihreview.model;

import java.util.List; 

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "moviegoer")
public class MovieGoer {
    @Id
    private int userId;

    private String username;
    private String bio;
    private String fullName;
    private String password_hash;
    private String salt;
    private String avatarUrl;

    @OneToMany(mappedBy = "movieGoer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Review> reviews;

    @OneToOne(mappedBy = "movieGoer", cascade = CascadeType.ALL)
    private Watchlist watchlist;

    public MovieGoer() {

    }

    public MovieGoer(int userId, String username, String bio, String fullName, String password_hash, String salt, String avatarUrl, List<Review> reviews, Watchlist watchlist) {
        this.userId = userId;
        this.username = username;
        this.bio = bio;
        this.fullName = fullName;
        this.password_hash = password_hash;
        this.salt = salt;
        this.avatarUrl = avatarUrl;
        this.reviews = reviews;
        this.watchlist = watchlist;
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

    public String getBio() { 
        return bio; 
    
    }

    public void setBio(String bio) { 
        this.bio = bio; 
    }

    public String getFullName() { 
        return fullName; 
    }

    public void setFullName(String fullName) { 
        this.fullName = fullName; 
    }

    public String getAvatarUrl() { 
        return avatarUrl; 
    }

    public void setAvatarUrl(String avatarUrl) { 
        this.avatarUrl = avatarUrl; 
    }

    public List<Review> getReviews() { 
        return reviews; 
    }
    public void setReviews(List<Review> reviews) { 
        this.reviews = reviews; 
    }

    public Watchlist getWatchlist() { 
        return watchlist; 
    }
    public void setWatchlist(Watchlist watchlist) { 
        this.watchlist = watchlist; 
    }

    public String getPassword_hash() {
        return password_hash;
    }

    public void setPassword_hash(String password_hash) {
        this.password_hash = password_hash;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getSalt() {
        return salt;
    }

}
