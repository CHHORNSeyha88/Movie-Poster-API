package com.movieflex.MovieFlex_Api.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Set;
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Movies {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer movieId;
    @Column(nullable = false,length = 200)
    @NotBlank(message = "Please Enter The Title of Movie's title")
    private String title;

    @Column(nullable = false,length = 200)
    @NotBlank(message = "Please Enter The Title of Movie's director")
    private String director;

    @Column(nullable = false,length = 200)
    @NotBlank(message = "Please Enter The Title of Movie's studio")
    private String studio;


    @ElementCollection
    @CollectionTable(name = "movie_cast")
    private Set<String> movieCast;

    @Column(nullable = false,length = 200)
//    @NotBlank(message = "Please Enter The Title of Movie's release year!")
    @NotNull
    private Integer releaseYear;

    @Column(nullable = false)
    @NotBlank(message = "Please Enter The Title of Movie's title")
    private String poster;


}
