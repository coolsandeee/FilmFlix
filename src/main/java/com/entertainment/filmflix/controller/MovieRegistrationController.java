package com.entertainment.filmflix.controller;

import com.entertainment.filmflix.dto.request.MovieRequestDTO;
import com.entertainment.filmflix.dto.response.GenericResponse;
import com.entertainment.filmflix.dto.response.Response;
import com.entertainment.filmflix.dto.response.MovieResponseDTO;
import com.entertainment.filmflix.service.MovieService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static com.entertainment.filmflix.constants.ApplicationConstants.FAILED_TO_ADD_MOVIES;
import static com.entertainment.filmflix.constants.ApplicationConstants.FAILED_TO_CREATE_USER_WITH_EMAIL_ID;

@RestController
@RequestMapping("/movies")
@Validated
public class MovieRegistrationController {

    private final MovieService movieService;

    public MovieRegistrationController(MovieService movieService) {
        this.movieService = movieService;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response<List<String>>> addMovie(@Valid @RequestBody List<MovieRequestDTO> movieRequestDTOs){
        GenericResponse<List<String>> response = movieService.addMoviesToFilmFlix(Optional.of(movieRequestDTOs));

        if (response.getResponse().size() > 0) {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(response);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(response);
        }

    }

    @GetMapping(value = "/{movieName}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response<MovieResponseDTO>> getMovie(@PathVariable String movieName){
        MovieRequestDTO userRequestDTO = new MovieRequestDTO();
        userRequestDTO.setName(movieName);

        MovieResponseDTO userDetails = movieService.getMovie(userRequestDTO);

        GenericResponse response = new GenericResponse();
        response.setMessage("Movie details retrieved");
        response.setResponse(userDetails);

        return ResponseEntity.status(HttpStatus.OK)
                .body(response);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response<List<MovieResponseDTO>>> getMovies(){
        List<MovieResponseDTO> allMovies = movieService.getMovies();
        if (allMovies.isEmpty()){
            GenericResponse response = new GenericResponse();
            response.setMessage("No users exist.");

            return ResponseEntity.status(HttpStatus.NO_CONTENT)
                    .body(response);
        }

        GenericResponse response = new GenericResponse();
        response.setMessage("Movie details retrieved.");
        response.setResponse(allMovies);

        return ResponseEntity.status(HttpStatus.OK)
                .body(response);
    }

    @DeleteMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response<String>> deleteMovies(@RequestBody Set<MovieRequestDTO> moviesToDelete){
        int deletedMoviesCount = movieService.deleteMoviesFromFilmFlix(Optional.of(moviesToDelete));

        Response response = new GenericResponse();
        response.setMessage("Total number of movies deleted: " + deletedMoviesCount);

        return ResponseEntity.status(HttpStatus.OK)
                .body(response);
    }

}
