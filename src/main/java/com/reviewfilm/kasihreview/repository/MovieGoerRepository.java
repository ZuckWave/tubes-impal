package com.reviewfilm.kasihreview.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.reviewfilm.kasihreview.model.MovieGoer;

@Repository
public interface MovieGoerRepository extends JpaRepository<MovieGoer, Integer> {
    MovieGoer findByUsername(String username);
}