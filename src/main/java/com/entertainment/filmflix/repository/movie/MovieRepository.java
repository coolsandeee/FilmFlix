package com.entertainment.filmflix.repository.movie;

import com.entertainment.filmflix.entity.MovieEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface MovieRepository extends JpaRepository<MovieEntity, Long> {
    Optional<MovieEntity> findByName(String movieName);
    Optional<List<MovieEntity>> findByNameIn(List<String> movieNames);
    @Modifying
    int deleteByNameIn(Set<String> movieName);
}
