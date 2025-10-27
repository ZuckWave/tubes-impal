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

    @GetMapping
    public List<MovieGoer> getAllMovieGoers() {
        return movieGoerRepo.findAll();
    }

    @GetMapping("/{id}")
    public MovieGoer getMovieGoerById(@PathVariable int id) {
        return movieGoerRepo.findById(id).orElse(null);
    }

    @PostMapping
    public MovieGoer createMovieGoer(@RequestBody MovieGoer movieGoer) {
        return movieGoerRepo.save(movieGoer);
    }

    @PutMapping("/{id}")
    public MovieGoer updateMovieGoer(@PathVariable int id, @RequestBody MovieGoer updated) {
        MovieGoer mg = movieGoerRepo.findById(id).orElse(null);
        if (mg != null) {
            mg.setUsername(updated.getUsername());
            mg.setPassword(updated.getPassword());
            mg.setBio(updated.getBio());
            mg.setFullName(updated.getFullName());
            mg.setAvatarUrl(updated.getAvatarUrl());
            return movieGoerRepo.save(mg);
        }
        return null;
    }

    @DeleteMapping("/{id}")
    public String deleteMovieGoer(@PathVariable int id) {
        movieGoerRepo.deleteById(id);
        return "User deleted";
    }

    @PostMapping("/{id}/review")
    public Review buatReview(@PathVariable int id, @RequestBody Review review) {
        MovieGoer user = movieGoerRepo.findById(id).orElse(null);
        if (user == null) return null;

        review.setMovieGoer(user);
        return reviewRepo.save(review);
    }

    @PostMapping("/{id}/watchlist/{movieId}")
    public String saveMovieToWatchlist(@PathVariable int id, @PathVariable int movieId) {
        MovieGoer user = movieGoerRepo.findById(id).orElse(null);
        Movies movie = moviesRepo.findById(movieId).orElse(null);
        if (user == null || movie == null) return "Not found";

        Watchlist wl = user.getWatchlist();
        if (wl == null) {
            wl = new Watchlist();
            wl.setWatchlistId(id);
            wl.setMovieGoer(user);
    }

    wl.getMovies().add(movie);
    watchlistRepo.save(wl);
    return "Movie added to watchlist";
    }

    @DeleteMapping("/{id}/watchlist/{movieId}")
    public String removeMovieFromWatchlist(@PathVariable int id, @PathVariable int movieId) {
    MovieGoer user = movieGoerRepo.findById(id).orElse(null);
    if (user == null || user.getWatchlist() == null) 
        return "User or watchlist not found";
        
    user.getWatchlist().getMovies().removeIf(m -> m.getMovieId() == movieId);
    movieGoerRepo.save(user);
    return "Movie removed from watchlist";
    }

}