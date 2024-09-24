package com.movieflex.MovieFlex_Api.repository;

import com.movieflex.MovieFlex_Api.entities.Movies;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MoviesRepository extends JpaRepository<Movies, Integer> {


}
