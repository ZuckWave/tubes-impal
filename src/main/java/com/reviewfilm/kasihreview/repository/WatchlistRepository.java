package com.reviewfilm.kasihreview.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.reviewfilm.kasihreview.model.Watchlist;

@Repository
public interface WatchlistRepository extends JpaRepository<Watchlist, Integer> {
    Optional<Watchlist> findByMovieGoer_UserId(int userId);
}