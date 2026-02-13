package com.entertainment.filmflix.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MovieRequestDTO {
    private Long id;
    @NotBlank(message = "Movie name is required")
    @Size(max=100, message = "Movie name must be a maximum of 100 characters")
    private String name;
    @NotBlank(message = "Movie description is required")
    @Size(max=100, message = "Movie description must be a maximum of 100 characters")
    private String description;
    @NotBlank(message = "Movie language is required")
    @Size(max=50, message = "Movie language must be a maximum of 50 characters")
    private String language;
    private String director;
    @NotBlank(message = "Movie genre is required")
    @Size(max=50, message = "Movie genre must be a maximum of 50 characters")
    private String genre;
    private Integer releaseYear;
    private Integer durationMinutes;
    private Double rating;
}
