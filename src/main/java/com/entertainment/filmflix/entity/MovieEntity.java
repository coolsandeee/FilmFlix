package com.entertainment.filmflix.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "tb_movie")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class MovieEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @NotBlank
    @Column(unique = true)
    private String name;

    private String description;
    private String language;
    private String director;
    private String genre;
    private Integer releaseYear;
    private Integer durationMinutes;
    private Double rating;

    @JsonIgnore
    @ManyToMany(mappedBy = "subscribedMovieEntities")
    private Set<UserEntity> subscribers = new HashSet<>();
}
