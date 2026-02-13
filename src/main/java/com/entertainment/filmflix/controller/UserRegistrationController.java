package com.entertainment.filmflix.controller;

import com.entertainment.filmflix.dto.request.MovieRequestDTO;
import com.entertainment.filmflix.dto.request.UserRequestDTO;
import com.entertainment.filmflix.dto.response.GenericResponse;
import com.entertainment.filmflix.dto.response.Response;
import com.entertainment.filmflix.dto.response.UserResponseDTO;
import com.entertainment.filmflix.service.SubscriptionService;
import com.entertainment.filmflix.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

import static com.entertainment.filmflix.constants.ApplicationConstants.*;

@RestController
@RequestMapping("/users")
@Validated
public class UserRegistrationController {

    private final UserService userService;
    private final SubscriptionService subscriptionService;

    public UserRegistrationController(UserService userService, SubscriptionService subscriptionService) {
        this.userService = userService;
        this.subscriptionService = subscriptionService;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response<String>> addUser(@Valid @RequestBody UserRequestDTO userRequestDTO){
        String message = userService.addUser(userRequestDTO);

        GenericResponse<String> response = new GenericResponse<>();
        response.setMessage(message);

        if (FAILED_TO_CREATE_USER_WITH_EMAIL_ID.equals(message)) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(response);
        }

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping(value = "/{registeredEmailId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response<UserResponseDTO>> getUser(@PathVariable String registeredEmailId){
        UserRequestDTO userRequestDTO = new UserRequestDTO();
        userRequestDTO.setEmail(registeredEmailId);

        UserResponseDTO userDetails = userService.getUser(userRequestDTO);

        GenericResponse<UserResponseDTO> response = new GenericResponse<>();
        response.setMessage("User details retrieved");
        response.setResponse(userDetails);

        return ResponseEntity.status(HttpStatus.OK)
                .body(response);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response<List<UserResponseDTO>>> getUsers(){
        List<UserResponseDTO> allUsers = userService.getUsers();
        if (allUsers.isEmpty()){
            GenericResponse<List<UserResponseDTO>> response = new GenericResponse<>();
            response.setMessage("No users exist.");

            return ResponseEntity.status(HttpStatus.NO_CONTENT)
                    .body(response);
        }

        GenericResponse<List<UserResponseDTO>> response = new GenericResponse<>();
        response.setMessage("User details retrieved.");
        response.setResponse(allUsers);

        return ResponseEntity.status(HttpStatus.OK)
                .body(response);
    }

    @PostMapping(value = "/{emailId}/subscribe", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response<List<String>>> subscribe(@Valid @PathVariable String emailId, @RequestBody Set<MovieRequestDTO> movieRequestDTOs) {
        UserRequestDTO userRequestDTO = new UserRequestDTO();
        userRequestDTO.setEmail(emailId);
        GenericResponse<List<String>> response = subscriptionService.addUserSubscription(userRequestDTO, movieRequestDTOs);
        if (response.getMessage().contains(USER_SUCCESSFULLY_SUBSCRIBED)) {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(response);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(response);
        }
    }

}
