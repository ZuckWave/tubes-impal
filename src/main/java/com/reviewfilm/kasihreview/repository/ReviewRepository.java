package com.reviewfilm.kasihreview.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.reviewfilm.kasihreview.model.MovieGoer;
import com.reviewfilm.kasihreview.model.Movies;
import com.reviewfilm.kasihreview.model.Review;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Integer> {
    List<Review> findByMovie(Movies movie);

    List<Review> findByMovieGoer(MovieGoer movieGoer);

    List<Review> findByReviewTextContainingIgnoreCase(String text);

}
