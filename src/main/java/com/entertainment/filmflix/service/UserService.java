package com.entertainment.filmflix.service;

import com.entertainment.filmflix.dto.request.UserRequestDTO;
import com.entertainment.filmflix.dto.response.MovieResponseDTO;
import com.entertainment.filmflix.dto.response.UserResponseDTO;
import com.entertainment.filmflix.entity.MovieEntity;
import com.entertainment.filmflix.entity.UserEntity;
import com.entertainment.filmflix.exception.FilmFlixException;
import com.entertainment.filmflix.exception.NoDataException;
import com.entertainment.filmflix.repository.user.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static com.entertainment.filmflix.constants.ApplicationConstants.*;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    private Logger log = LoggerFactory.getLogger(UserService.class);

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public String addUser(UserRequestDTO userRequestDTO){
        if (getUserDetails(userRequestDTO).isPresent()) {
            String message = "User with email ID " + userRequestDTO.getEmail() + " already exists.";
            log.error(message);
            throw new FilmFlixException(message);
        }

        UserEntity userEntity = new UserEntity();
        userEntity.setEmail(userRequestDTO.getEmail());
        userEntity.setEmail(userRequestDTO.getEmail());
        userEntity.setGivenName(userRequestDTO.getGivenName());
        userEntity.setPassword(encoder.encode(userRequestDTO.getPassword()));

        UserEntity newUserEntity = userRepository.save(userEntity);
        if (newUserEntity == null) {
            String message = FAILED_TO_CREATE_USER_WITH_EMAIL_ID + userRequestDTO.getEmail();
            log.error(message);
            return message;
        }
        String message = "User with email ID " + userRequestDTO.getEmail() + " created successfully.";
        log.debug(message);
        return message;
    }

    public UserResponseDTO getUser(UserRequestDTO userRequestDTO){
        Optional<UserEntity> userEntityOptional = getUserDetails(userRequestDTO);
        if (!userEntityOptional.isPresent()) {
            String message = "User with email ID " + userRequestDTO.getEmail() + " does not exists.";
            log.error(message);
            throw new NoDataException(message);
        }

        UserEntity userEntity = userEntityOptional.get();
        UserResponseDTO userResponseDTO = new UserResponseDTO();
        List<MovieResponseDTO> subscribesMovies = new ArrayList<>();

        userResponseDTO.setUserId(userEntity.getUserId());
        userResponseDTO.setGivenName(userEntity.getGivenName());
        userResponseDTO.setEmail(userEntity.getEmail());
        Set<MovieEntity> subscribedMovieEntities = userEntity.getSubscribedMovieEntities();
        for (MovieEntity movieEntity : subscribedMovieEntities){
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
            subscribesMovies.add(movieResponseDTO);
        }

        userResponseDTO.setSubscribedMovies(subscribesMovies);
        return userResponseDTO;
    }

    public List<UserResponseDTO> getUsers(){
        List<UserEntity> userEntities = userRepository.findAll();
        List<UserEntity> tmpUserEntities = new ArrayList<>(userEntities);
        List<UserResponseDTO> allUsers = new ArrayList<>();

        if (userEntities.isEmpty()){
            log.error(NO_USERS_EXIST);
            return allUsers;
        }

        for (UserEntity userEntity : tmpUserEntities) {
            UserResponseDTO userResponseDTO = new UserResponseDTO();

            userResponseDTO.setUserId(userEntity.getUserId());
            userResponseDTO.setGivenName(userEntity.getGivenName());
            userResponseDTO.setEmail(userEntity.getEmail());
            Set<MovieEntity> subscribedMovieEntities = new HashSet<>(userEntity.getSubscribedMovieEntities());
            List<MovieResponseDTO> subscribedMovies =
                    subscribedMovieEntities
                            .stream()
                            .map(movieEntity -> {
                                MovieResponseDTO dto = new MovieResponseDTO();
                                dto.setId(movieEntity.getId());
                                dto.setName(movieEntity.getName());
                                dto.setDescription(movieEntity.getDescription());
                                dto.setLanguage(movieEntity.getLanguage());
                                dto.setDirector(movieEntity.getDirector());
                                dto.setGenre(movieEntity.getGenre());
                                dto.setReleaseYear(movieEntity.getReleaseYear());
                                dto.setDurationMinutes(movieEntity.getDurationMinutes());
                                dto.setRating(movieEntity.getRating());
                                return dto;
                            })
                            .toList();

            userResponseDTO.setSubscribedMovies(subscribedMovies);
            allUsers.add(userResponseDTO);
        }

        return allUsers;
    }

    @Transactional
    public String deleteUser(UserRequestDTO userRequestDTO){
        Optional<UserEntity> userEntityOptional = getUserDetails(userRequestDTO);
        if (!userEntityOptional.isPresent()) {
            String message = "User with email ID " + userRequestDTO.getEmail() + " does not exists.";
            log.error(message);
            throw new FilmFlixException(message);
        }

        UserEntity userEntity = userEntityOptional.get();
        userEntity.getSubscribedMovieEntities().clear();

        userRepository.delete(userEntity);

        String message = "User with email ID " + userRequestDTO.getEmail() + " deleted successfully.";
        log.debug(message);
        return message;
    }


    private Optional<UserEntity> getUserDetails(UserRequestDTO userRequestDTO) {
        return userRepository.findByEmail(userRequestDTO.getEmail());
    }
}
