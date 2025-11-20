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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.reviewfilm.kasihreview.dto.MovieGoerDTO;
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

    // Convert Entity to DTO
    private MovieGoerDTO convertToDTO(MovieGoer movieGoer) {
        if (movieGoer == null) return null;
        
        MovieGoerDTO dto = new MovieGoerDTO();
        dto.setId(movieGoer.getUserId());
        dto.setUsername(movieGoer.getUsername());
        // Sesuaikan dengan field di entity MovieGoer
        // Jika tidak ada email, bisa set null atau field lain
        dto.setEmail(null); // atau movieGoer.getEmail() jika ada
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
        MovieGoer movieGoer = movieGoerRepo.findById(id).orElse(null);
        if (movieGoer == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(convertToDTO(movieGoer));
    }

    @PostMapping
    public ResponseEntity<MovieGoerDTO> createMovieGoer(@RequestBody MovieGoer movieGoer) {
        MovieGoer saved = movieGoerRepo.save(movieGoer);
        return ResponseEntity.status(HttpStatus.CREATED).body(convertToDTO(saved));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MovieGoerDTO> updateMovieGoer(@PathVariable int id, @RequestBody MovieGoer updated) {
        MovieGoer mg = movieGoerRepo.findById(id).orElse(null);
        if (mg == null) {
            return ResponseEntity.notFound().build();
        }
        
        mg.setUsername(updated.getUsername());
        mg.setPassword_hash(updated.getPassword_hash());
        mg.setBio(updated.getBio());
        mg.setFullName(updated.getFullName());
        mg.setAvatarUrl(updated.getAvatarUrl());
        
        MovieGoer savedMg = movieGoerRepo.save(mg);
        return ResponseEntity.ok(convertToDTO(savedMg));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteMovieGoer(@PathVariable int id) {
        if (!movieGoerRepo.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        movieGoerRepo.deleteById(id);
        return ResponseEntity.ok("User deleted");
    }

    @PostMapping("/{id}/review")
    public ResponseEntity<Review> buatReview(@PathVariable int id, @RequestBody Review review) {
        MovieGoer user = movieGoerRepo.findById(id).orElse(null);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        review.setMovieGoer(user);
        Review savedReview = reviewRepo.save(review);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedReview);
    }

    @PostMapping("/{id}/watchlist/{movieId}")
    public ResponseEntity<String> saveMovieToWatchlist(@PathVariable int id, @PathVariable int movieId) {
        MovieGoer user = movieGoerRepo.findById(id).orElse(null);
        Movies movie = moviesRepo.findById(movieId).orElse(null);
        
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
        if (movie == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Movie not found");
        }

        Watchlist wl = user.getWatchlist();
        if (wl == null) {
            wl = new Watchlist();
            wl.setWatchlistId(id);
            wl.setMovieGoer(user);
            user.setWatchlist(wl);
        }

        if (!wl.getMovies().contains(movie)) {
            wl.getMovies().add(movie);
            watchlistRepo.save(wl);
        }
        
        return ResponseEntity.ok("Movie added to watchlist");
    }

    @DeleteMapping("/{id}/watchlist/{movieId}")
    public ResponseEntity<String> removeMovieFromWatchlist(@PathVariable int id, @PathVariable int movieId) {
        MovieGoer user = movieGoerRepo.findById(id).orElse(null);
        
        if (user == null || user.getWatchlist() == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("User or watchlist not found");
        }
        
        boolean removed = user.getWatchlist().getMovies()
            .removeIf(m -> m.getMovieId() == movieId);
        
        if (removed) {
            movieGoerRepo.save(user);
            return ResponseEntity.ok("Movie removed from watchlist");
        }
        
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body("Movie not found in watchlist");
    }
}