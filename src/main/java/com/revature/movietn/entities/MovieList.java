package com.revature.movietn.entities;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "movie_lists")
public class MovieList {
    @Id
    private String id;

    @Column(nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private User user;

    @ManyToMany
    @JoinTable(name = "movie_list_items", joinColumns = @JoinColumn(name = "movie_list_id"), inverseJoinColumns = @JoinColumn(name = "movie_id"))
    @JsonBackReference
    private Set<Movie> movies;

    public MovieList(String name, User user) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.user = user;
        this.movies = new HashSet<>();
    }
}
