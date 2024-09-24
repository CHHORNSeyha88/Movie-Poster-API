package com.movieflex.MovieFlex_Api.service;

import com.movieflex.MovieFlex_Api.Dto.MovieDto;
import com.movieflex.MovieFlex_Api.Dto.MoviePageResponse;
import com.movieflex.MovieFlex_Api.entities.Movies;
import com.movieflex.MovieFlex_Api.exception.FileExistsException;
import com.movieflex.MovieFlex_Api.exception.MovieNotFoundException;
import com.movieflex.MovieFlex_Api.repository.MoviesRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Service
public class MovieServiceImpl implements MovieService {
    private final MoviesRepository movieRepository;

    private final FileService fileService;

    @Value("${project.poster}")
    private String path;

    @Value("${base.url}")
    private String baseUrl;

    public MovieServiceImpl(MoviesRepository movieRepository, FileService fileService) {
        this.movieRepository = movieRepository;
        this.fileService = fileService;
    }

    @Override
    public MovieDto addMovie(MovieDto movieDto, MultipartFile file) throws IOException {
        // 1. upload the file
        if (Files.exists(Paths.get(path + File.separator + file.getOriginalFilename()))) {
            throw new FileExistsException("File already exist!!");
        }
        String uploadedFileName = fileService.uploadFile(path, file);

        // 2. set the value of field 'poster' as filename
        movieDto.setPoster(uploadedFileName);

        // 3. map dto to Movie object
        Movies movie = new Movies(
                null,
                movieDto.getTitle(),
                movieDto.getDirector(),
                movieDto.getStudio(),
                movieDto.getMovieCast(),
                movieDto.getReleaseYear(),
                movieDto.getPoster()
        );

        // 4. save the movie object -> saved Movie object
        Movies savedMovie = movieRepository.save(movie);

        // 5. generate the posterUrl
        String posterUrl = baseUrl + "/file/" + uploadedFileName;

        // 6. map Movie object to DTO object and return it
        MovieDto response = new MovieDto(
                savedMovie.getMovieId(),
                savedMovie.getTitle(),
                savedMovie.getDirector(),
                savedMovie.getStudio(),
                savedMovie.getMovieCast(),
                savedMovie.getReleaseYear(),
                savedMovie.getPoster(),
                posterUrl
        );

        return response;
    }
    @Override
    public MovieDto getMovie(Integer movieId) {
        //1, check the data in db and if exits fetch the the data of given id
      Movies movies =   movieRepository.findById(movieId).orElseThrow(()-> new MovieNotFoundException("Movie not found! "));
//        2.generate url
        String posterUrl = baseUrl + "/file/" + movies.getPoster();

        //3 map dto obj and return it
        MovieDto response = new MovieDto(

                movies.getMovieId(),
                movies.getTitle(),
                movies.getDirector(),
                movies.getStudio(),
                movies.getMovieCast(),
                movies.getReleaseYear(),
                movies.getPoster(),
                posterUrl
        );
        return response;
    }

    @Override
    public List<MovieDto> getAllMovies() {
        //1. fetch all the data from DB
        List<Movies> movies = movieRepository.findAll();

        List<MovieDto> movieDtos = new ArrayList<>();

        //2. interate through the list , generate the posterUrl each of Movie obj
        // , map to MovieDto obj
        for(Movies movie : movies) {
            String posterUrl = baseUrl + "/file/" + movie.getPoster();
            MovieDto movieDto = new MovieDto(

                    movie.getMovieId(),
                    movie.getTitle(),
                    movie.getDirector(),
                    movie.getStudio(),
                    movie.getMovieCast(),
                    movie.getReleaseYear(),
                    movie.getPoster(),
                    posterUrl
            );
            movieDtos.add(movieDto);

        }


        return movieDtos;
    }

    @Override
    public MovieDto updateMovie(Integer movieId, MovieDto movieDto, MultipartFile file) throws IOException {
        //1, check id if exist
        Movies mv =   movieRepository.findById(movieId).orElseThrow(()-> new MovieNotFoundException("Movie not found! "));

        //2.if files check is null do nothing
        String fileName = mv.getPoster();

        //.if isn't null then delete and upload new file
     if(file != null){
         Files.deleteIfExists(Paths.get(path+File.separator+fileName));
         fileName = fileService.uploadFile(path,file);
     }
        //3.set movieDto posterUrl acrooding step2
       movieDto.setPoster(fileName);
        //4 . map it to movie obj
    Movies movies = new Movies(
            mv.getMovieId(),
            movieDto.getTitle(),
            movieDto.getDirector(),
            movieDto.getStudio(),
            movieDto.getMovieCast(),
            movieDto.getReleaseYear(),
            movieDto.getPoster()
    );
        //5. save the movie obj and return saved movie obj
        Movies Updatemoviesave =   movieRepository.save(movies);
        //6. generate posterUrl for it
        String posterUrl = baseUrl + "/file/" + fileName;
        //7.map to movieDto and save it
    MovieDto respone = new MovieDto(
            movies.getMovieId(),
            movies.getTitle(),
            movies.getDirector(),
            movies.getStudio(),
            movies.getMovieCast(),
            movies.getReleaseYear(),
            movies.getPoster(),
            posterUrl
    );
      return respone;
    }

    @Override
    public String deleteMovie(Integer movieId) throws IOException {
        //1. check id if exist
        Movies mv =   movieRepository.findById(movieId).orElseThrow(()-> new MovieNotFoundException("Movie not found!!"));
       Integer id = mv.getMovieId();
        //2. delte the file
        Files.deleteIfExists(Paths.get(path + File.separator + mv.getPoster()));

        //3. delete the file
        movieRepository.delete(mv);

        return "Movie deleted with id = "+id;
    }

    @Override
    public MoviePageResponse getAllMoviesPageWithPagination(Integer pageNumber, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
//        movieRepository.findAll(pageable);
        Page<Movies> moviesPage =   movieRepository.findAll(pageable);
        List<Movies> movies = moviesPage.getContent();
        List<MovieDto> movieDtos = new ArrayList<>();

        //2. interate through the list , generate the posterUrl each of Movie obj
        // , map to MovieDto obj
        for(Movies movie : movies) {
            String posterUrl = baseUrl + "/file/" + movie.getPoster();
            MovieDto movieDto = new MovieDto(

                    movie.getMovieId(),
                    movie.getTitle(),
                    movie.getDirector(),
                    movie.getStudio(),
                    movie.getMovieCast(),
                    movie.getReleaseYear(),
                    movie.getPoster(),
                    posterUrl
            );
            movieDtos.add(movieDto);

        }
        return new MoviePageResponse(movieDtos,
                pageNumber,
                pageSize,
                moviesPage.getTotalElements(),
                moviesPage.getTotalPages(),
                moviesPage.isLast());
    }

    @Override
    public MoviePageResponse getAllMoviesPageWithPaginationAndSorting(Integer pageNumber, Integer pageSize, String sortBy, String dir) {
        // Default to ascending if dir is null or not "desc"
        Sort sort;
        if (dir == null || !dir.equalsIgnoreCase("desc")) {
            sort = Sort.by(sortBy).ascending();
        } else {
            sort = Sort.by(sortBy).descending();
        }

        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<Movies> moviesPage = movieRepository.findAll(pageable);
        List<Movies> movies = moviesPage.getContent();
        List<MovieDto> movieDtos = new ArrayList<>();

        // Iterate through the list, generate the posterUrl for each Movie object, and map to MovieDto object
        for (Movies movie : movies) {
            String posterUrl = baseUrl + "/file/" + movie.getPoster();
            MovieDto movieDto = new MovieDto(
                    movie.getMovieId(),
                    movie.getTitle(),
                    movie.getDirector(),
                    movie.getStudio(),
                    movie.getMovieCast(),
                    movie.getReleaseYear(),
                    movie.getPoster(),
                    posterUrl
            );
            movieDtos.add(movieDto);
        }

        return new MoviePageResponse(movieDtos,
                pageNumber,
                pageSize,
                moviesPage.getTotalElements(),
                moviesPage.getTotalPages(),
                moviesPage.isLast());
    }


}
