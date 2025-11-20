package com.reviewfilm.kasihreview.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.reviewfilm.kasihreview.dto.ReviewDTO;
import com.reviewfilm.kasihreview.dto.ReviewRequestDTO;
import com.reviewfilm.kasihreview.model.MovieGoer;
import com.reviewfilm.kasihreview.model.Movies;
import com.reviewfilm.kasihreview.model.Review;
import com.reviewfilm.kasihreview.model.ReviewVotes;
import com.reviewfilm.kasihreview.repository.MovieGoerRepository;
import com.reviewfilm.kasihreview.repository.MoviesRepository;
import com.reviewfilm.kasihreview.repository.ReviewRepository;
import com.reviewfilm.kasihreview.repository.ReviewVotesRepository;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    @Autowired
    private ReviewRepository reviewRepo;

    @Autowired
    private ReviewVotesRepository votesRepo;

    @Autowired
    private MoviesRepository moviesRepo;

    @Autowired
    private MovieGoerRepository movieGoerRepo;

    private ReviewDTO convertToDTO(Review review) {
        if (review == null) return null;
        
        ReviewDTO dto = new ReviewDTO();
        dto.setReviewId(review.getReviewId());
        
        if (review.getMovieGoer() != null) {
            dto.setReviewerName(review.getMovieGoer().getUsername());
        }
        
        // Dari Movie
        if (review.getMovie() != null) {
            dto.setMovieId(review.getMovie().getMovieId());
            dto.setMovieTitle(review.getMovie().getTitle());
        }
        
        dto.setContent(review.getReviewText());
        dto.setRating(review.getRating());
        dto.setSpoiler(review.getIsSpoiler());
        dto.setCreatedAt(review.getCreatedAt());
        
        int upvotes = 0;
        int downvotes = 0;
        
        if (review.getVotes() != null) {
            upvotes = (int) review.getVotes().stream()
                .filter(vote -> "upvote".equalsIgnoreCase(vote.getVoteType()))
                .count();
            downvotes = (int) review.getVotes().stream()
                .filter(vote -> "downvote".equalsIgnoreCase(vote.getVoteType()))
                .count();
        }
        
        dto.setUpvotes(upvotes);
        dto.setDownvotes(downvotes);
        
        return dto;
    }

    @GetMapping
    public ResponseEntity<List<ReviewDTO>> getAllReviews() {
        List<ReviewDTO> dtoList = reviewRepo.findAll().stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
        return ResponseEntity.ok(dtoList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReviewDTO> getReviewById(@PathVariable int id) {
        Review review = reviewRepo.findById(id).orElse(null);
        if (review == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(convertToDTO(review));
    }

    @PostMapping
    public ResponseEntity<?> createReview(@RequestBody ReviewRequestDTO request) {
        if (request.getRating() < 1 || request.getRating() > 5) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Rating must be between 1 and 5");
        }

        Movies movie = moviesRepo.findById(request.getMovieId()).orElse(null);
        if (movie == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("Movie not found with id: " + request.getMovieId());
        }

        MovieGoer user = movieGoerRepo.findById(request.getUserId()).orElse(null);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("User not found with id: " + request.getUserId());
        }

        if (request.getContent() == null || request.getContent().trim().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Review content cannot be empty");
        }

        Review review = new Review();
        review.setMovie(movie);
        review.setMovieGoer(user);
        review.setReviewText(request.getContent());
        review.setRating(request.getRating());
        review.setIsSpoiler(request.isSpoiler());
        
        Review saved = reviewRepo.save(review);
        return ResponseEntity.status(HttpStatus.CREATED).body(convertToDTO(saved));
    }

    @PostMapping("/{id}/vote")
    public ResponseEntity<String> voteReview(@PathVariable int id, @RequestBody ReviewVotes vote) {
        Review review = reviewRepo.findById(id).orElse(null);
        if (review == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Review not found");
        }
        
        String voteType = vote.getVoteType();
        if (!"upvote".equalsIgnoreCase(voteType) && !"downvote".equalsIgnoreCase(voteType)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Invalid vote type. Must be 'upvote' or 'downvote'");
        }
        
        vote.setReview(review);
        votesRepo.save(vote);
        
        return ResponseEntity.ok("Vote recorded successfully");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteReview(@PathVariable int id) {
        if (!reviewRepo.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        reviewRepo.deleteById(id);
        return ResponseEntity.ok("Review deleted");
    }
}