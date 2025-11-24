package com.reviewfilm.kasihreview.controller;

import java.util.Optional;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping; 
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.reviewfilm.kasihreview.dto.LoginRequest;
import com.reviewfilm.kasihreview.dto.LoginResponse;
import com.reviewfilm.kasihreview.dto.MovieGoerDTO;
import com.reviewfilm.kasihreview.exception.DuplicateResourceException;
import com.reviewfilm.kasihreview.exception.ResourceNotFoundException;
import com.reviewfilm.kasihreview.exception.ValidationException;
import com.reviewfilm.kasihreview.model.MovieGoer;
import com.reviewfilm.kasihreview.model.Movies;
import com.reviewfilm.kasihreview.model.Review;
import com.reviewfilm.kasihreview.model.Watchlist;
import com.reviewfilm.kasihreview.repository.MovieGoerRepository;
import com.reviewfilm.kasihreview.repository.MoviesRepository;
import com.reviewfilm.kasihreview.repository.ReviewRepository;
import com.reviewfilm.kasihreview.repository.WatchlistRepository;

@RestController
@RequestMapping("/api/moviegoers")
public class MovieGoerController {

    @Autowired
    private MovieGoerRepository movieGoerRepo;

    @Autowired
    private ReviewRepository reviewRepo;

    @Autowired
    private MoviesRepository moviesRepo;

    @Autowired
    private WatchlistRepository watchlistRepo;

    private MovieGoerDTO convertToDTO(MovieGoer movieGoer) {
        if (movieGoer == null) return null;
        
        MovieGoerDTO dto = new MovieGoerDTO();
        dto.setId(movieGoer.getUserId());
        dto.setUsername(movieGoer.getUsername());
        dto.setEmail(null); 
        dto.setProfilePicture(movieGoer.getAvatarUrl());
        
        return dto;
    }

    @GetMapping
    public ResponseEntity<List<MovieGoerDTO>> getAllMovieGoers() {
        List<MovieGoerDTO> dtoList = movieGoerRepo.findAll().stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
        return ResponseEntity.ok(dtoList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MovieGoerDTO> getMovieGoerById(@PathVariable int id) {
        MovieGoer movieGoer = movieGoerRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("MovieGoer", "id", id));
        return ResponseEntity.ok(convertToDTO(movieGoer));
    }

    @PostMapping(consumes = "application/json", produces = "application/json")
    public ResponseEntity<MovieGoerDTO> createMovieGoer(@RequestBody MovieGoer movieGoer) {
        if (movieGoer.getUsername() == null || movieGoer.getUsername().trim().isEmpty()) {
            throw new ValidationException("Username cannot be empty");
        }
        if (movieGoer.getUsername().length() < 3) {
            throw new ValidationException("Username must be at least 3 characters");
        }
        
        if (movieGoer.getPassword_hash() == null || movieGoer.getPassword_hash().trim().isEmpty()) {
            throw new ValidationException("Password cannot be empty");
        }
        if (movieGoer.getPassword_hash().length() < 6) {
            throw new ValidationException("Password must be at least 6 characters");
        }
        
        MovieGoer saved = movieGoerRepo.save(movieGoer);
        return ResponseEntity.status(HttpStatus.CREATED).body(convertToDTO(saved));
    }

   @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
    if (loginRequest.getUsername() == null || loginRequest.getUsername().trim().isEmpty()) {
        throw new ValidationException("Username cannot be empty");
    }
    
    if (loginRequest.getPassword() == null || loginRequest.getPassword().trim().isEmpty()) {
        throw new ValidationException("Password cannot be empty");
    }
    
    Optional<MovieGoer> movieGoerOpt = movieGoerRepo.findByUsernameAndPassword(
        loginRequest.getUsername(), 
        loginRequest.getPassword()
    );
    
    if (!movieGoerOpt.isPresent()) {
        LoginResponse response = new LoginResponse(false, "Username atau password salah", null);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }
    
    MovieGoer movieGoer = movieGoerOpt.get();
    MovieGoerDTO userDTO = convertToDTO(movieGoer);
    
    LoginResponse response = new LoginResponse(true, "Login successful", userDTO);
    return ResponseEntity.ok(response);
}

    @PutMapping("/{id}")
    public ResponseEntity<MovieGoerDTO> updateMovieGoer(@PathVariable int id, @RequestBody MovieGoer updated) {
        MovieGoer mg = movieGoerRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("MovieGoer", "id", id));
        
        if (updated.getUsername() != null) {
            if (updated.getUsername().trim().isEmpty()) {
                throw new ValidationException("Username cannot be empty");
            }
            if (updated.getUsername().length() < 3) {
                throw new ValidationException("Username must be at least 3 characters");
            }
            mg.setUsername(updated.getUsername());
        }
        
        if (updated.getPassword_hash() != null) {
            if (updated.getPassword_hash().length() < 6) {
                throw new ValidationException("Password must be at least 6 characters");
            }
            mg.setPassword_hash(updated.getPassword_hash());
        }
        
        if (updated.getBio() != null) {
            mg.setBio(updated.getBio());
        }
        if (updated.getFullName() != null) {
            mg.setFullName(updated.getFullName());
        }
        if (updated.getAvatarUrl() != null) {
            mg.setAvatarUrl(updated.getAvatarUrl());
        }
        
        MovieGoer savedMg = movieGoerRepo.save(mg);
        return ResponseEntity.ok(convertToDTO(savedMg));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteMovieGoer(@PathVariable int id) {
        MovieGoer movieGoer = movieGoerRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("MovieGoer", "id", id));
        
        movieGoerRepo.delete(movieGoer);
        return ResponseEntity.ok("User deleted successfully");
    }

    @PostMapping("/{id}/review")
    public ResponseEntity<Review> buatReview(@PathVariable int id, @RequestBody Review review) {
        MovieGoer user = movieGoerRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("MovieGoer", "id", id));

        if (review.getRating() < 1 || review.getRating() > 10) {
            throw new ValidationException("Rating must be between 1 and 10");
        }
        
        if (review.getReviewText() == null || review.getReviewText().trim().isEmpty()) {
            throw new ValidationException("Review text cannot be empty");
        }

        review.setMovieGoer(user);
        Review savedReview = reviewRepo.save(review);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedReview);
    }

    @PostMapping("/{id}/watchlist/{movieId}")
    public ResponseEntity<String> saveMovieToWatchlist(@PathVariable int id, @PathVariable int movieId) {
        MovieGoer user = movieGoerRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("MovieGoer", "id", id));
        
        Movies movie = moviesRepo.findById(movieId)
                .orElseThrow(() -> new ResourceNotFoundException("Movie", "movieId", movieId));

        Watchlist wl = user.getWatchlist();
        if (wl == null) {
            wl = new Watchlist();
            wl.setMovieGoer(user);
            user.setWatchlist(wl);
        }

        if (wl.getMovies().contains(movie)) {
            throw new DuplicateResourceException("Movie already exists in watchlist");
        }

        wl.getMovies().add(movie);
        watchlistRepo.save(wl);
        
        return ResponseEntity.ok("Movie added to watchlist successfully");
    }

    @DeleteMapping("/{id}/watchlist/{movieId}")
    public ResponseEntity<String> removeMovieFromWatchlist(@PathVariable int id, @PathVariable int movieId) {
        MovieGoer user = movieGoerRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("MovieGoer", "id", id));
        
        if (user.getWatchlist() == null) {
            throw new ResourceNotFoundException("Watchlist not found for this user");
        }
        
        boolean removed = user.getWatchlist().getMovies()
            .removeIf(m -> m.getMovieId() == movieId);
        
        if (!removed) {
            throw new ResourceNotFoundException("Movie", "movieId", movieId);
        }
        
        movieGoerRepo.save(user);
        return ResponseEntity.ok("Movie removed from watchlist successfully");
    }
}