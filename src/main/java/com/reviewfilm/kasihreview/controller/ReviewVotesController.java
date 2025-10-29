package com.reviewfilm.kasihreview.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.reviewfilm.kasihreview.model.ReviewVotes;
import com.reviewfilm.kasihreview.repository.ReviewVotesRepository;

@RestController
@RequestMapping("/api/reviewvotes")
public class ReviewVotesController {

    @Autowired
    private ReviewVotesRepository votesRepo;

    @GetMapping
    public List<ReviewVotes> getAllVotes() {
        return votesRepo.findAll();
    }

    @DeleteMapping("/{id}")
    public String deleteVote(@PathVariable int id) {
        votesRepo.deleteById(id);
        return "Vote deleted";
    }
}
