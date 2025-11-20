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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.reviewfilm.kasihreview.dto.WatchlistDTO;
import com.reviewfilm.kasihreview.dto.WatchlistDTO.MovieInWatchlistDTO;
import com.reviewfilm.kasihreview.exception.DuplicateResourceException;
import com.reviewfilm.kasihreview.exception.ResourceNotFoundException;
import com.reviewfilm.kasihreview.model.MovieGoer;
import com.reviewfilm.kasihreview.model.Movies;
import com.reviewfilm.kasihreview.model.Watchlist;
import com.reviewfilm.kasihreview.repository.MovieGoerRepository;
import com.reviewfilm.kasihreview.repository.MoviesRepository;
import com.reviewfilm.kasihreview.repository.WatchlistRepository;

@RestController
@RequestMapping("/api/watchlist")
public class WatchlistController {

    @Autowired
    private WatchlistRepository watchlistRepo;

    @Autowired
    private MovieGoerRepository movieGoerRepo;

    @Autowired
    private MoviesRepository moviesRepo;

    @GetMapping("/user/{userId}")
    public ResponseEntity<WatchlistDTO> getWatchlistByUserId(@PathVariable int userId) {
        Watchlist watchlist = watchlistRepo.findByMovieGoer_UserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Watchlist", "userId", userId));

        WatchlistDTO dto = convertToDTO(watchlist);
        return ResponseEntity.ok(dto);
    }

    @GetMapping
    public ResponseEntity<List<WatchlistDTO>> getAllWatchlists() {
        List<WatchlistDTO> dtoList = watchlistRepo.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(dtoList);
    }

    @PostMapping("/user/{userId}")
    public ResponseEntity<WatchlistDTO> createWatchlistForUser(@PathVariable int userId) {
        MovieGoer movieGoer = movieGoerRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("MovieGoer", "userId", userId));

        if (watchlistRepo.findByMovieGoer_UserId(userId).isPresent()) {
            throw new DuplicateResourceException("Watchlist", "userId", userId);
        }

        Watchlist watchlist = new Watchlist(movieGoer);
        Watchlist saved = watchlistRepo.save(watchlist);
        
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(convertToDTO(saved));
    }

    @PostMapping("/user/{userId}/movie/{movieId}")
    public ResponseEntity<WatchlistDTO> addMovieToWatchlist(
            @PathVariable int userId, 
            @PathVariable int movieId) {
        
        Watchlist watchlist = watchlistRepo.findByMovieGoer_UserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Watchlist", "userId", userId));

        Movies movie = moviesRepo.findById(movieId)
                .orElseThrow(() -> new ResourceNotFoundException("Movie", "movieId", movieId));

        if (watchlist.getMovies().stream().anyMatch(m -> m.getMovieId() == movieId)) {
            throw new DuplicateResourceException("Movie already exists in watchlist");
        }

        watchlist.addMovie(movie);
        Watchlist saved = watchlistRepo.save(watchlist);

        return ResponseEntity.ok(convertToDTO(saved));
    }

    @DeleteMapping("/user/{userId}/movie/{movieId}")
    public ResponseEntity<WatchlistDTO> removeMovieFromWatchlist(
            @PathVariable int userId, 
            @PathVariable int movieId) {
        
        Watchlist watchlist = watchlistRepo.findByMovieGoer_UserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Watchlist", "userId", userId));

        Movies movie = moviesRepo.findById(movieId)
                .orElseThrow(() -> new ResourceNotFoundException("Movie", "movieId", movieId));

        watchlist.removeMovie(movie);
        Watchlist saved = watchlistRepo.save(watchlist);

        return ResponseEntity.ok(convertToDTO(saved));
    }

    @DeleteMapping("/user/{userId}")
    public ResponseEntity<String> deleteWatchlist(@PathVariable int userId) {
        Watchlist watchlist = watchlistRepo.findByMovieGoer_UserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Watchlist", "userId", userId));

        watchlistRepo.delete(watchlist);
        return ResponseEntity.ok("Watchlist deleted successfully");
    }

    private WatchlistDTO convertToDTO(Watchlist watchlist) {
        List<MovieInWatchlistDTO> movieDTOs = watchlist.getMovies().stream()
                .map(movie -> new MovieInWatchlistDTO(
                    movie.getMovieId(),
                    movie.getTitle(),
                    movie.getPosterUrl(),
                    movie.getReleaseYear(),
                    movie.getAvgRating()
                ))
                .collect(Collectors.toList());

        return new WatchlistDTO(
            watchlist.getWatchlistId(),
            watchlist.getMovieGoer().getUserId(),
            watchlist.getMovieGoer().getUsername(),
            movieDTOs
        );
    }
}