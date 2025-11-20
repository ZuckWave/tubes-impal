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

import com.reviewfilm.kasihreview.dto.MoviesDTO;
import com.reviewfilm.kasihreview.exception.ResourceNotFoundException;
import com.reviewfilm.kasihreview.exception.ValidationException;
import com.reviewfilm.kasihreview.model.Movies;
import com.reviewfilm.kasihreview.repository.MoviesRepository;

@RestController
@RequestMapping("/api/movies")
public class MoviesController {

    @Autowired
    private MoviesRepository moviesRepo;

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

    @PostMapping
    public ResponseEntity<MoviesDTO> createMovie(@RequestBody Movies movie) {
        if (movie.getTitle() == null || movie.getTitle().trim().isEmpty()) {
            throw new ValidationException("Movie title cannot be empty");
        }
        
        if (movie.getReleaseYear() < 1888 || movie.getReleaseYear() > 2100) {
            throw new ValidationException("Invalid release year. Must be between 1888 and 2100");
        }
        
        if (movie.getAvgRating() < 0 || movie.getAvgRating() > 10) {
            throw new ValidationException("Rating must be between 0 and 10");
        }
        
        Movies saved = moviesRepo.save(movie);
        return ResponseEntity.status(HttpStatus.CREATED).body(convertToDTO(saved));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MoviesDTO> updateMovie(@PathVariable int id, @RequestBody Movies movie) {
        Movies m = moviesRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Movie", "id", id));
        
        // Validasi title
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
            if (movie.getAvgRating() < 0 || movie.getAvgRating() > 10) {
                throw new ValidationException("Rating must be between 0 and 10");
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