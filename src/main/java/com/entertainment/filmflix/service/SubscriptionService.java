package com.entertainment.filmflix.service;

import com.entertainment.filmflix.dto.request.MovieRequestDTO;
import com.entertainment.filmflix.dto.request.UserRequestDTO;
import com.entertainment.filmflix.dto.response.GenericResponse;
import com.entertainment.filmflix.entity.MovieEntity;
import com.entertainment.filmflix.entity.UserEntity;
import com.entertainment.filmflix.exception.NoDataException;
import com.entertainment.filmflix.repository.movie.MovieRepository;
import com.entertainment.filmflix.repository.user.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static com.entertainment.filmflix.constants.ApplicationConstants.MOVIES_DO_NOT_EXIST_IN_THE_FILM_FLIX_LIBRARY;
import static com.entertainment.filmflix.constants.ApplicationConstants.USER_SUCCESSFULLY_SUBSCRIBED;

@Service
public class SubscriptionService {
    private Logger log = LoggerFactory.getLogger(SubscriptionService.class);

    private final UserRepository userRepository;
    private final MovieRepository movieRepository;

    public SubscriptionService(UserRepository userRepository, MovieRepository movieRepository) {
        this.userRepository = userRepository;
        this.movieRepository = movieRepository;
    }

    public GenericResponse<List<String>> addUserSubscription(UserRequestDTO userRequestDTO, Set<MovieRequestDTO> movieRequestDTOs){
        Optional<UserEntity> userOptional = userRepository.findByEmail(userRequestDTO.getEmail());
        GenericResponse<List<String>> response = new GenericResponse<>();

        if (userOptional.isEmpty()) {
            String message = "User with email ID " + userRequestDTO.getEmail() + " does not exist.";
            log.error(message);
            response.setMessage(message);
            return response;
        }

        UserEntity userEntity = userOptional.get();

        if (movieRequestDTOs.isEmpty()) {
            String message = "Input has no movie to subscribe to. Please mention at least one movie.";
            log.error(message);
            response.setMessage(message);
            return response;
        }

        List<String> movieNamesToSubscribeTo = movieRequestDTOs.stream()
                .map(dto -> dto.getName())
                .toList();

        Optional<List<MovieEntity>> validMoviesToSubscribe = movieRepository.findByNameIn(movieNamesToSubscribeTo);
        if(validMoviesToSubscribe.isPresent() && validMoviesToSubscribe.get().isEmpty()){
            String message = MOVIES_DO_NOT_EXIST_IN_THE_FILM_FLIX_LIBRARY;
            log.error(message);
            response.setMessage(message);
            response.setResponse(movieNamesToSubscribeTo);
            return response;
        }

        List<String> validMovieNamesToSubscribeTo =
                validMoviesToSubscribe.get()
                .stream()
                .map(MovieEntity::getName)
                .toList();

        List<String> finalList = new ArrayList<>(movieNamesToSubscribeTo);
        if (finalList.size() > validMovieNamesToSubscribeTo.size()){
            log.info("Invalid movie names to subscribe to are: " + finalList.removeAll(validMovieNamesToSubscribeTo));
        }

        userEntity.subscribeMovie(validMoviesToSubscribe.get());
        userRepository.save(userEntity);

        response.setMessage(userRequestDTO.getEmail() + " " + USER_SUCCESSFULLY_SUBSCRIBED);
        response.setResponse(validMovieNamesToSubscribeTo);
        return response;
    }

}
