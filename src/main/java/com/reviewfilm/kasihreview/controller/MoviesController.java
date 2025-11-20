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
        dto.setGenre(movie.getGenre()); // List<String>
        dto.setReleaseYear(movie.getReleaseYear());
        dto.setDescription(movie.getDescription());
        dto.setRating(movie.getAvgRating()); // float -> double
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
        Movies movie = moviesRepo.findById(id).orElse(null);
        if (movie == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(convertToDTO(movie));
    }

    @PostMapping
    public ResponseEntity<MoviesDTO> createMovie(@RequestBody Movies movie) {
        Movies saved = moviesRepo.save(movie);
        return ResponseEntity.status(HttpStatus.CREATED).body(convertToDTO(saved));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MoviesDTO> updateMovie(@PathVariable int id, @RequestBody Movies movie) {
        Movies m = moviesRepo.findById(id).orElse(null);
        if (m == null) {
            return ResponseEntity.notFound().build();
        }
        
        m.setTitle(movie.getTitle());
        m.setDescription(movie.getDescription());
        m.setGenre(movie.getGenre());
        m.setReleaseYear(movie.getReleaseYear());
        m.setPosterUrl(movie.getPosterUrl());
        m.setAvgRating(movie.getAvgRating());
        
        Movies savedMovie = moviesRepo.save(m);
        return ResponseEntity.ok(convertToDTO(savedMovie));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteMovie(@PathVariable int id) {
        if (!moviesRepo.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        moviesRepo.deleteById(id);
        return ResponseEntity.ok("Movie deleted");
    }
}