package com.movieflex.MovieFlex_Api.service;

import com.movieflex.MovieFlex_Api.Dto.MovieDto;
import com.movieflex.MovieFlex_Api.Dto.MoviePageResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface MovieService {

    MovieDto addMovie(MovieDto movieDto, MultipartFile file) throws IOException;

    MovieDto getMovie(Integer movieId);

    List<MovieDto> getAllMovies();

    MovieDto updateMovie(Integer movieId, MovieDto movieDto, MultipartFile file) throws IOException;
    String deleteMovie(Integer movieId) throws IOException;
    MoviePageResponse getAllMoviesPageWithPagination(Integer pageNumber, Integer pageSize);
    MoviePageResponse getAllMoviesPageWithPaginationAndSorting(Integer pageNumber, Integer pageSize, String sortBy, String dir);


}
