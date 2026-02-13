package com.entertainment.filmflix.service;

import com.entertainment.filmflix.dto.request.MovieRequestDTO;
import com.entertainment.filmflix.dto.response.GenericResponse;
import com.entertainment.filmflix.dto.response.MovieResponseDTO;
import com.entertainment.filmflix.entity.MovieEntity;
import com.entertainment.filmflix.exception.NoDataException;
import com.entertainment.filmflix.repository.movie.MovieRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static com.entertainment.filmflix.constants.ApplicationConstants.NO_MOVIES_EXIST;

@Service
public class MovieService {
    private final MovieRepository movieRepository;
    private Logger log = LoggerFactory.getLogger(MovieService.class);

    public MovieService(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    public GenericResponse<List<String>> addMoviesToFilmFlix(Optional<List<MovieRequestDTO>> movieRequestDTOs){
        if (movieRequestDTOs.isEmpty()) {
            String message = "Input has no movie to add to the library. Please add at least one movie.";
            log.error(message);
            throw new NoDataException(message);
        }

        GenericResponse<List<String>> response = new GenericResponse<>();
        List<String> newMovieNamesToAdd = movieRequestDTOs.get().stream()
                .map(dto -> dto.getName())
                .toList();

        Optional<List<MovieEntity>> existingMovies = movieRepository.findByNameIn(newMovieNamesToAdd);
        List<MovieRequestDTO> finalList = new ArrayList<>();
        List<String> tempList = null;

        if (existingMovies.isEmpty()){
            log.debug("All {} movies are new. Proceeding with adding to the FilmFlix library.", newMovieNamesToAdd);
        } else if (newMovieNamesToAdd.size() == existingMovies.get().size()){
            String message = "All movies in the input already exist in the FilmFlix library. Please add at least one new movie to add to the library.";
            log.debug(message);
            response.setMessage(message);
            response.setResponse(new ArrayList<>());
            return response;
        }else {
            List<String> existingMovieNames = existingMovies.get()
                    .stream()
                    .map(MovieEntity::getName)
                    .toList();
            tempList = new ArrayList<>(newMovieNamesToAdd);
            tempList.removeAll(existingMovieNames);

            List<String> finalNewMovieNamesToAddAfterRemovingExistingMovies = tempList;
            for (MovieRequestDTO dto : movieRequestDTOs.get()){
                if (finalNewMovieNamesToAddAfterRemovingExistingMovies.contains(dto.getName())){
                    finalList.add(dto);
                }
            }

//            finalList = movieRequestDTOs.get()
//                    .stream()
//                    .filter(dto -> dto.getName().equals(finalNewMovieNamesToAddAfterRemovingExistingMovies.stream().f().get()))
//                    .toList();
            log.debug("Following movies already exist in the FilmFlix library and will not be added again: {}", finalList.stream().map(movie->movie.getName()).toList());
        }

        log.debug("Total number of movies to add to the FilmFlix library are {}", finalList.size());

        List<MovieEntity> movieEntitiesToSave = new ArrayList<>();
        finalList.stream().forEach(movieRequestDTO -> {
            MovieEntity movieEntity = new MovieEntity();
            movieEntity.setName(movieRequestDTO.getName());
            movieEntity.setDescription(movieRequestDTO.getDescription());
            movieEntity.setLanguage(movieRequestDTO.getLanguage());
            movieEntity.setDirector(movieRequestDTO.getDirector());
            movieEntity.setGenre(movieRequestDTO.getGenre());
            movieEntity.setReleaseYear(movieRequestDTO.getReleaseYear());
            movieEntity.setDurationMinutes(movieRequestDTO.getDurationMinutes());
            movieEntity.setRating(movieRequestDTO.getRating());
            movieEntitiesToSave.add(movieEntity);
        });

        movieRepository.saveAll(movieEntitiesToSave);

        String message = "Total number of movies successfully added to the FilmFlix library are " + finalList.size();
        log.debug(message);
        response.setMessage(message);
        response.setResponse(finalList.stream().map(movie->movie.getName()).toList());
        return response;
    }

    public MovieResponseDTO getMovie(MovieRequestDTO movieRequestDTO){
        Optional<MovieEntity> movieEntityOptional = getMovieDetails(movieRequestDTO);
        if (movieEntityOptional.isEmpty()) {
            String message = "Movie with name " + movieRequestDTO.getName() + " does not exist.";
            log.error(message);
            throw new NoDataException(message);
        }

        MovieEntity movieEntity = movieEntityOptional.get();

        MovieResponseDTO movieResponseDTO = new MovieResponseDTO();
        movieResponseDTO.setId(movieEntity.getId());
        movieResponseDTO.setName(movieEntity.getName());
        movieResponseDTO.setDescription(movieEntity.getDescription());
        movieResponseDTO.setLanguage(movieEntity.getLanguage());
        movieResponseDTO.setDirector(movieEntity.getDirector());
        movieResponseDTO.setGenre(movieEntity.getGenre());
        movieResponseDTO.setReleaseYear(movieEntity.getReleaseYear());
        movieResponseDTO.setDurationMinutes(movieEntity.getDurationMinutes());
        movieResponseDTO.setRating(movieEntity.getRating());

        return movieResponseDTO;
    }

    public List<MovieResponseDTO> getMovies(){
        List<MovieEntity> movieEntities = movieRepository.findAll();
        List<MovieResponseDTO> allMovies = new ArrayList<>();

        if (movieEntities.isEmpty()){
            log.error(NO_MOVIES_EXIST);
            return allMovies;
        }

        for (MovieEntity movieEntity : movieEntities) {
            MovieResponseDTO movieResponseDTO = new MovieResponseDTO();
            movieResponseDTO.setId(movieEntity.getId());
            movieResponseDTO.setName(movieEntity.getName());
            movieResponseDTO.setDescription(movieEntity.getDescription());
            movieResponseDTO.setLanguage(movieEntity.getLanguage());
            movieResponseDTO.setDirector(movieEntity.getDirector());
            movieResponseDTO.setGenre(movieEntity.getGenre());
            movieResponseDTO.setReleaseYear(movieEntity.getReleaseYear());
            movieResponseDTO.setDurationMinutes(movieEntity.getDurationMinutes());
            movieResponseDTO.setRating(movieEntity.getRating());
            allMovies.add(movieResponseDTO);
        }

        return allMovies;
    }

    private Optional<MovieEntity> getMovieDetails(MovieRequestDTO movieRequestDTO) {
        return movieRepository.findByName(movieRequestDTO.getName());
    }

    @Transactional
    public int deleteMoviesFromFilmFlix(Optional<Set<MovieRequestDTO>> movieRequestDTOs){
        if (movieRequestDTOs.isEmpty()) {
            String message = "Input has no movie to delete from the library. Please mention at least one movie.";
            log.error(message);
            throw new NoDataException(message);
        }

        Set<MovieRequestDTO> moviesToDelete = movieRequestDTOs.get();
        log.debug("Total number of movies to delete from the FilmFlix library are {}", moviesToDelete.size());

        Set<String> movieNamesToDelete = moviesToDelete
                .stream()
                .map(dto -> dto.getName())
                .collect(Collectors.toSet());

        int totalNumberOfMoviesDeleted = movieRepository.deleteByNameIn(movieNamesToDelete);
        log.debug("Total number of movies deleted from the FilmFlix library are {}", totalNumberOfMoviesDeleted);
        return totalNumberOfMoviesDeleted;
    }

}
