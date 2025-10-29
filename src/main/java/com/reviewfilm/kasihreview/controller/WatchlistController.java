package com.reviewfilm.kasihreview.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.reviewfilm.kasihreview.model.Watchlist;
import com.reviewfilm.kasihreview.repository.WatchlistRepository;

@RestController
@RequestMapping("/api/watchlist")
public class WatchlistController {

    @Autowired
    private WatchlistRepository watchlistRepo;

    @GetMapping
    public List<Watchlist> getAllWatchlists() {
        return watchlistRepo.findAll();
    }

    @GetMapping("/{id}")
    public Watchlist getWatchlistById(@PathVariable int id) {
        return watchlistRepo.findById(id).orElse(null);
    }

    @PostMapping
    public Watchlist createWatchlist(@RequestBody Watchlist wl) {
        return watchlistRepo.save(wl);
    }

    @DeleteMapping("/{id}")
    public String deleteWatchlist(@PathVariable int id) {
        watchlistRepo.deleteById(id);
        return "Watchlist deleted";
    }
}
