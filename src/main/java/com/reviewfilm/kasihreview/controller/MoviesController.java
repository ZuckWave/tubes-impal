package com.reviewfilm.kasihreview.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.reviewfilm.kasihreview.model.Movies;
import com.reviewfilm.kasihreview.repository.MoviesRepository;

@RestController
@RequestMapping("/api/movies")
public class MoviesController {

    @Autowired
    private MoviesRepository moviesRepo;

    @GetMapping
    public List<Movies> getAllMovies() {
        return moviesRepo.findAll();
    }

    @GetMapping("/{id}")
    public Movies getMovieById(@PathVariable int id) {
        return moviesRepo.findById(id).orElse(null);
    }

    @PostMapping
    public Movies createMovie(@RequestBody Movies movie) {
        return moviesRepo.save(movie);
    }

    @PutMapping("/{id}")
    public Movies updateMovie(@PathVariable int id, @RequestBody Movies movie) {
        Movies m = moviesRepo.findById(id).orElse(null);
        if (m != null) {
            m.setTitle(movie.getTitle());
            m.setDescription(movie.getDescription());
            m.setGenre(movie.getGenre());
            m.setPosterUrl(movie.getPosterUrl());
            m.setAvgRating(movie.getAvgRating());
            return moviesRepo.save(m);
        }
        return null;
    }

    @DeleteMapping("/{id}")
    public String deleteMovie(@PathVariable int id) {
        moviesRepo.deleteById(id);
        return "Movie deleted";
    }
}
