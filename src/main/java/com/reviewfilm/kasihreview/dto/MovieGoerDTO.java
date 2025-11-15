package com.reviewfilm.kasihreview.dto;

public class MovieGoerDTO {
    private int id;
    private String username;
    private String email;
    private String profilePicture;

    public MovieGoerDTO() {}

    public MovieGoerDTO(int id, String username, String email, String profilePicture) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.profilePicture = profilePicture;
    }

    public int getId() { 
        return id; 
    }

    public void setId(int id) { 
        this.id = id; 
    }

    public String getUsername() { 
        return username; 
    }

    public void setUsername(String username) { 
        this.username = username; 
    }

    public String getEmail() { 
        return email; 
    }

    public void setEmail(String email) { 
        this.email = email; 
    }

    public String getProfilePicture() { 
        return profilePicture; 
    }

    public void setProfilePicture(String profilePicture) { 
        this.profilePicture = profilePicture; 
    }
}