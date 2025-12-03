package com.reviewfilm.kasihreview.controller;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.reviewfilm.kasihreview.dto.ReviewDTO;
import com.reviewfilm.kasihreview.dto.ReviewRequestDTO;
import com.reviewfilm.kasihreview.exception.ResourceNotFoundException;
import com.reviewfilm.kasihreview.exception.ValidationException;
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
        Review review = reviewRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Review", "id", id));
        return ResponseEntity.ok(convertToDTO(review));
    }

   @GetMapping("/user/{userId}")
    public ResponseEntity<List<ReviewDTO>> getReviewsByUserId(@PathVariable int userId) {
        MovieGoer user = movieGoerRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "userId", userId));
        
        List<ReviewDTO> dtoList = reviewRepo.findByMovieGoer(user).stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
        
        return ResponseEntity.ok(dtoList);
    }
    
    @PostMapping
    public ResponseEntity<ReviewDTO> createReview(@RequestBody ReviewRequestDTO request) {
        if (request.getRating() < 1 || request.getRating() > 5) {
            throw new ValidationException("Rating must be between 1 and 5");
        }

        Movies movie = moviesRepo.findById(request.getMovieId())
                .orElseThrow(() -> new ResourceNotFoundException("Movie", "movieId", request.getMovieId()));

        MovieGoer user = movieGoerRepo.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User", "userId", request.getUserId()));

        if (request.getContent() == null || request.getContent().trim().isEmpty()) {
            throw new ValidationException("Review content cannot be empty");
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

    @PatchMapping("/{id}")
    public ResponseEntity<ReviewDTO> updateReview(@PathVariable int id, @RequestBody Map<String, Object> updates) {
        Review review = reviewRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Review", "id", id));
        
        if (updates.containsKey("content")) {
            String content = (String) updates.get("content");
            if (content == null || content.trim().isEmpty()) {
                throw new ValidationException("Review content cannot be empty");
            }
            review.setReviewText(content);
        }
        
        if (updates.containsKey("rating")) {
            Integer rating = (Integer) updates.get("rating");
            if (rating == null || rating < 1 || rating > 5) {
                throw new ValidationException("Rating must be between 1 and 5");
            }
            review.setRating(rating);
        }
        
        if (updates.containsKey("isSpoiler")) {
            Boolean isSpoiler = (Boolean) updates.get("isSpoiler");
            if (isSpoiler != null) {
                review.setIsSpoiler(isSpoiler);
            }
        }
        
        Review saved = reviewRepo.save(review);
        return ResponseEntity.ok(convertToDTO(saved));
    }

    @PostMapping("/{id}/vote")
    public ResponseEntity<String> voteReview(@PathVariable int id, @RequestBody ReviewVotes vote) {
        Review review = reviewRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Review", "id", id));
        
        String voteType = vote.getVoteType();
        if (!"upvote".equalsIgnoreCase(voteType) && !"downvote".equalsIgnoreCase(voteType)) {
            throw new ValidationException("Invalid vote type. Must be 'upvote' or 'downvote'");
        }
        
        vote.setReview(review);
        votesRepo.save(vote);
        
        return ResponseEntity.ok("Vote recorded successfully");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteReview(@PathVariable int id) {
        Review review = reviewRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Review", "id", id));
        
        reviewRepo.delete(review);
        return ResponseEntity.ok("Review deleted successfully");
    }
}