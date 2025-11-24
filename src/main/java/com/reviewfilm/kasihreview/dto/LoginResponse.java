package com.reviewfilm.kasihreview.dto;

public class LoginResponse {
    private boolean success;
    private String message;
    private MovieGoerDTO user;

    public LoginResponse() {}

    public LoginResponse(boolean success, String message, MovieGoerDTO user) {
        this.success = success;
        this.message = message;
        this.user = user;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public MovieGoerDTO getUser() {
        return user;
    }

    public void setUser(MovieGoerDTO user) {
        this.user = user;
    }
}