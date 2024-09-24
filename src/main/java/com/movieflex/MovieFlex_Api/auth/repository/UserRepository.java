package com.movieflex.MovieFlex_Api.auth.repository;

import com.movieflex.MovieFlex_Api.auth.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByEmail(String username);
}
