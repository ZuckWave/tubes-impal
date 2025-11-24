package com.reviewfilm.kasihreview.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.reviewfilm.kasihreview.model.MovieGoer;

@Repository
public interface MovieGoerRepository extends JpaRepository<MovieGoer, Integer> {
    MovieGoer findByUsername(String username);
    
    @Query("SELECT m FROM MovieGoer m WHERE m.username = :username AND m.password_hash = :password")
    Optional<MovieGoer> findByUsernameAndPassword(@Param("username") String username, @Param("password") String password);
}