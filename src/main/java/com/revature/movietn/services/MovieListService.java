package com.revature.movietn.services;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.revature.movietn.dtos.requests.NewMovieListRequest;
import com.revature.movietn.dtos.responses.MovieListResponse;
import com.revature.movietn.entities.MovieList;
import com.revature.movietn.entities.User;
import com.revature.movietn.repositories.MovieListRepository;
import com.revature.movietn.utils.custom_exceptions.ResourceConflictException;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class MovieListService {
    private final MovieListRepository movieListRepository;

    /**
     * Saves new movie list to db. Before creating a new movie list there is a
     * validation check to make sure that the movie list doesn't already exist in
     * the db. This check is made to ensure that duplicate movie lists are not
     * created.
     * 
     * @param req the NewMovieListRequest object with movie list information
     * @return the MovieListResponse object
     */
    public MovieListResponse saveMovieLIst(NewMovieListRequest req) {
        // transform name to lowercase
        String name = req.getName().toLowerCase();

        // check if movie lists exists to avoid duplicates
        Optional<MovieList> foundMovieList = movieListRepository.findByNameAndUserId(name,
                req.getUserId());
        if (foundMovieList.isPresent()) {
            throw new ResourceConflictException("User already owns this movie list.");
        }

        // make user
        User user = new User();
        user.setId(req.getUserId());

        // make movie list
        MovieList movieList = new MovieList(name, user);

        // save movie list
        return new MovieListResponse(movieListRepository.save(movieList));
    }

    /**
     * Finds all movie lists that belong to a user using the userId.
     * 
     * @param userId the user id
     * @return a Set of MovieListResponse objects
     */
    public Set<MovieListResponse> findAllByUserId(String userId) {
        // get all movie lists for user
        Set<MovieList> movieLists = movieListRepository.findAllByUserId(userId);
        Set<MovieListResponse> movieListResponses = new HashSet<>();

        // transform data into dto set
        for (MovieList movieList : movieLists) {
            // transform name to have uppercase first letter of each word.
            String[] words = movieList.getName().split(" ");
            for (int index = 0; index < words.length; index++) {
                words[index] = StringUtils.capitalize(words[index]);
            }
            movieList.setName(String.join(" ", words));

            // add movie response
            movieListResponses.add(new MovieListResponse(movieList));
        }

        // return set
        return movieListResponses;
    }
}
