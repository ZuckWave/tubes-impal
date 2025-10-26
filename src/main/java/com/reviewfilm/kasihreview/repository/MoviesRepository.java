package com.reviewfilm.kasihreview.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.reviewfilm.kasihreview.model.Movies;


@Repository
public interface MoviesRepository extends JpaRepository<Movies, Integer>{
    List<Movies> findByTitleContainingIgnoreCase(String title);

    List<Movies> findByGenre(List<String> genre);
    
}
