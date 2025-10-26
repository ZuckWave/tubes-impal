package com.reviewfilm.kasihreview.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.reviewfilm.kasihreview.model.MovieGoer;
import com.reviewfilm.kasihreview.model.Watchlist;

@Repository
public interface WatchlistRepository extends JpaRepository<Watchlist, Integer>{
    Watchlist findByMovieGoer(MovieGoer movieGoer);
}
