package com.reviewfilm.kasihreview.controller;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import com.reviewfilm.kasihreview.exception.DuplicateResourceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.reviewfilm.kasihreview.dto.MoviesDTO;
import com.reviewfilm.kasihreview.dto.ReviewDTO;
import com.reviewfilm.kasihreview.dto.VoteDTO;
import com.reviewfilm.kasihreview.exception.ResourceNotFoundException;
import com.reviewfilm.kasihreview.exception.ValidationException;
import com.reviewfilm.kasihreview.model.Movies;
import com.reviewfilm.kasihreview.model.Review;
import com.reviewfilm.kasihreview.model.ReviewVotes;
import com.reviewfilm.kasihreview.repository.MoviesRepository;
import com.reviewfilm.kasihreview.repository.ReviewRepository;
import com.reviewfilm.kasihreview.repository.ReviewVotesRepository;

@RestController
@RequestMapping("/api/movies")
public class MoviesController {

    @Autowired
    private MoviesRepository moviesRepo;

    @Autowired
    private ReviewRepository reviewRepo;

    @Autowired
    private ReviewVotesRepository votesRepo;

    private MoviesDTO convertToDTO(Movies movie) {
        if (movie == null) return null;
        
        MoviesDTO dto = new MoviesDTO();
        dto.setMovieId(movie.getMovieId());
        dto.setTitle(movie.getTitle());
        dto.setGenre(movie.getGenre()); 
        dto.setReleaseYear(movie.getReleaseYear());
        dto.setDescription(movie.getDescription());
        dto.setRating(movie.getAvgRating()); 
        dto.setPosterUrl(movie.getPosterUrl());
        
        return dto;
    }

    private ReviewDTO convertToReviewDTO(Review review) {
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

    private VoteDTO convertToVoteDTO(ReviewVotes vote) {
        if (vote == null) return null;
        
        VoteDTO dto = new VoteDTO();
        dto.setVoteId(vote.getVoteId());
        
        // Ambil movieGoer dari review
        if (vote.getReview() != null && vote.getReview().getMovieGoer() != null) {
            dto.setMovieGoerId(vote.getReview().getMovieGoer().getUserId());
            dto.setVoterName(vote.getReview().getMovieGoer().getUsername());
        }
        
        if (vote.getReview() != null) {
            dto.setReviewId(vote.getReview().getReviewId());
        }
        
        dto.setVoteType(vote.getVoteType());
        // ReviewVotes tidak punya createdAt, set null atau hapus
        dto.setCreatedAt(null);
        
        return dto;
    }

    // Method untuk menghitung dan memperbarui average rating movie (max 5.0)
    // Method ini public agar bisa dipanggil dari ReviewController
    public void updateMovieAverageRating(int movieId) {
        Movies movie = moviesRepo.findById(movieId)
                .orElseThrow(() -> new ResourceNotFoundException("Movie", "id", movieId));
        
        List<Review> reviews = reviewRepo.findByMovie(movie);
        
        if (reviews.isEmpty()) {
            movie.setAvgRating(0.0f);
        } else {
            double avgRating = reviews.stream()
                    .mapToDouble(Review::getRating)
                    .average()
                    .orElse(0.0);
            
            // Pastikan rating tidak melebihi 5.0
            avgRating = Math.min(avgRating, 5.0);
            
            movie.setAvgRating((float) Math.round(avgRating * 10) / 10); // Round to 1 decimal
        }
        
        moviesRepo.save(movie);
    }

    @GetMapping
    public ResponseEntity<List<MoviesDTO>> getAllMovies() {
        List<MoviesDTO> dtoList = moviesRepo.findAll().stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
        return ResponseEntity.ok(dtoList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MoviesDTO> getMovieById(@PathVariable int id) {
        Movies movie = moviesRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Movie", "id", id));
        return ResponseEntity.ok(convertToDTO(movie));
    }

    @GetMapping("/avgRating/{id}")
    public ResponseEntity<Double> getMovieAvgRatingById(@PathVariable int id) {
        Movies movie = moviesRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Movie", "id", id));
        return ResponseEntity.ok(convertToDTO(movie).getRating());
    }

    @GetMapping("/{id}/reviews")
    public ResponseEntity<List<ReviewDTO>> getReviewsByMovieId(@PathVariable int id) {
        Movies movie = moviesRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Movie", "id", id));
        
        List<ReviewDTO> dtoList = reviewRepo.findByMovie(movie).stream()
            .map(this::convertToReviewDTO)
            .collect(Collectors.toList());
        
        return ResponseEntity.ok(dtoList);
    }

    // FITUR BARU: Get votes by MovieGoer ID for a specific movie
    @GetMapping("/{movieId}/votes/moviegoer/{movieGoerId}")
    public ResponseEntity<List<VoteDTO>> getVotesByMovieGoerId(
            @PathVariable int movieId, 
            @PathVariable int movieGoerId) {
        
        Movies movie = moviesRepo.findById(movieId)
                .orElseThrow(() -> new ResourceNotFoundException("Movie", "id", movieId));
        
        List<Review> movieReviews = reviewRepo.findByMovie(movie);
        
        List<VoteDTO> voteDTOs = movieReviews.stream()
                .flatMap(review -> review.getVotes().stream())
                .filter(vote -> vote.getVoter() != null && 
                               vote.getVoter().getUserId() == movieGoerId)
                .map(this::convertToVoteDTO)
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(voteDTOs);
    }

    // FITUR BARU: Delete vote by vote ID
    @DeleteMapping("/votes/{voteId}")
    public ResponseEntity<String> deleteVoteByVoteId(@PathVariable int voteId) {
        ReviewVotes vote = votesRepo.findById(voteId)
                .orElseThrow(() -> new ResourceNotFoundException("Vote", "id", voteId));
        
        votesRepo.delete(vote);
        return ResponseEntity.ok("Vote deleted successfully");
    }

    // FITUR BARU: Update vote type (upvote/downvote)
    @PatchMapping("/votes/{voteId}")
    public ResponseEntity<VoteDTO> updateVote(@PathVariable int voteId, @RequestBody Map<String, String> updates) {
        ReviewVotes vote = votesRepo.findById(voteId)
                .orElseThrow(() -> new ResourceNotFoundException("Vote", "id", voteId));
        
        if (updates.containsKey("voteType")) {
            String voteType = updates.get("voteType");
            if (!"upvote".equalsIgnoreCase(voteType) && !"downvote".equalsIgnoreCase(voteType)) {
                throw new ValidationException("Invalid vote type. Must be 'upvote' or 'downvote'");
            }
            vote.setVoteType(voteType);
        }
        
        ReviewVotes savedVote = votesRepo.save(vote);
        return ResponseEntity.ok(convertToVoteDTO(savedVote));
    }

    @PostMapping
    public ResponseEntity<MoviesDTO> createMovie(@RequestBody Movies movie) {

        Optional<Movies> moviePost = moviesRepo.findById(movie.getMovieId());

        if (moviePost.isPresent()) {
            throw new DuplicateResourceException("Movie", "id", movie.getMovieId());
        }

        if (movie.getTitle() == null || movie.getTitle().trim().isEmpty()) {
            throw new ValidationException("Movie title cannot be empty");
        }
        
        if (movie.getReleaseYear() < 1888 || movie.getReleaseYear() > 2100) {
            throw new ValidationException("Invalid release year. Must be between 1888 and 2100");
        }
        
        if (movie.getAvgRating() < 0 || movie.getAvgRating() > 5) {
            throw new ValidationException("Rating must be between 0 and 5");
        }
        
        Movies saved = moviesRepo.save(movie);
        return ResponseEntity.status(HttpStatus.CREATED).body(convertToDTO(saved));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MoviesDTO> updateMovie(@PathVariable int id, @RequestBody Movies movie) {
        Movies m = moviesRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Movie", "id", id));
        
        if (movie.getTitle() != null) {
            if (movie.getTitle().trim().isEmpty()) {
                throw new ValidationException("Movie title cannot be empty");
            }
            m.setTitle(movie.getTitle());
        }
        
        if (movie.getReleaseYear() != 0) {
            if (movie.getReleaseYear() < 1888 || movie.getReleaseYear() > 2100) {
                throw new ValidationException("Invalid release year. Must be between 1888 and 2100");
            }
            m.setReleaseYear(movie.getReleaseYear());
        }
        
        if (movie.getAvgRating() != 0) {
            if (movie.getAvgRating() < 0 || movie.getAvgRating() > 5) {
                throw new ValidationException("Rating must be between 0 and 5");
            }
            m.setAvgRating(movie.getAvgRating());
        }
        
        if (movie.getDescription() != null) {
            m.setDescription(movie.getDescription());
        }
        if (movie.getGenre() != null) {
            m.setGenre(movie.getGenre());
        }
        if (movie.getPosterUrl() != null) {
            m.setPosterUrl(movie.getPosterUrl());
        }
        
        Movies savedMovie = moviesRepo.save(m);
        return ResponseEntity.ok(convertToDTO(savedMovie));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteMovie(@PathVariable int id) {
        Movies movie = moviesRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Movie", "id", id));
        
        moviesRepo.delete(movie);
        return ResponseEntity.ok("Movie deleted successfully");
    }
}