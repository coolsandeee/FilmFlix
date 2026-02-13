package com.entertainment.filmflix.dto.response;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MovieResponseDTO {
    private Long id;
    private String name;
    private String description;
    private String language;
    private String director;
    private String genre;
    private Integer releaseYear;
    private Integer durationMinutes;
    private Double rating;
}
