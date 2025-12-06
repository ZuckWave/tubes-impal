package com.reviewfilm.kasihreview.dto;

public class VoteRequestDTO {
    private String voteType;
    private int userId;

    public VoteRequestDTO() {}

    public VoteRequestDTO(String voteType, int userId) {
        this.voteType = voteType;
        this.userId = userId;
    }

    public String getVoteType() {
        return voteType;
    }

    public void setVoteType(String voteType) {
        this.voteType = voteType;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}