package com.reviewfilm.kasihreview.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.reviewfilm.kasihreview.model.MovieGoer;
import com.reviewfilm.kasihreview.model.Review;
import com.reviewfilm.kasihreview.model.ReviewVotes;

public interface ReviewVotesRepository extends JpaRepository<ReviewVotes, Integer>{
    List<ReviewVotes> findByReview(Review review);
    List<ReviewVotes> findByMovieGoer(MovieGoer movieGoer);
}
