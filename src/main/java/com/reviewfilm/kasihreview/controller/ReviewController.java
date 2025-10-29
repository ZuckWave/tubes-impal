package com.reviewfilm.kasihreview.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.reviewfilm.kasihreview.model.Review;
import com.reviewfilm.kasihreview.model.ReviewVotes;
import com.reviewfilm.kasihreview.repository.ReviewRepository;
import com.reviewfilm.kasihreview.repository.ReviewVotesRepository;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    @Autowired
    private ReviewRepository reviewRepo;

    @Autowired
    private ReviewVotesRepository votesRepo;

    @GetMapping
    public List<Review> getAllReviews() {
        return reviewRepo.findAll();
    }

    @GetMapping("/{id}")
    public Review getReviewById(@PathVariable int id) {
        return reviewRepo.findById(id).orElse(null);
    }

    @PostMapping
    public Review createReview(@RequestBody Review review) {
        return reviewRepo.save(review);
    }

    @PostMapping("/{id}/vote")
    public ReviewVotes voteReview(@PathVariable int id, @RequestBody ReviewVotes vote) {
        Review review = reviewRepo.findById(id).orElse(null);
        if (review == null) return null;
        vote.setReview(review);
        return votesRepo.save(vote);
    }

    @DeleteMapping("/{id}")
    public String deleteReview(@PathVariable int id) {
        reviewRepo.deleteById(id);
        return "Review deleted";
    }
}
