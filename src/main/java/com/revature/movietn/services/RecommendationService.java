package com.revature.movietn.services;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.revature.movietn.dtos.requests.GetMovieRecommendations;
import com.revature.movietn.dtos.responses.MovieResponse;
import com.revature.movietn.entities.Movie;
import com.revature.movietn.entities.Review;
import com.revature.movietn.utils.custom_exceptions.ResourceNotFoundException;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class RecommendationService {
    private final MovieService movieService;
    private final ReviewService reviewService;

    public Set<MovieResponse> getMovieRecommendations(String movieId, int amount, GetMovieRecommendations req) {
        Review userReview;
        List<Review> reviews;
        try {
            // get user review for movie
            userReview = reviewService.findByUserIdAndMovieId(req.getUserId(), movieId);

            // calculate min and max rating range
            BigDecimal min = userReview.getRating().subtract(BigDecimal.valueOf(1.5));
            BigDecimal max = userReview.getRating().add(BigDecimal.valueOf(1.5));
            min = min.compareTo(new BigDecimal("0.00")) < 0 ? new BigDecimal("0.00") : min;
            max = max.compareTo(new BigDecimal("10.00")) > 0 ? new BigDecimal("10.00") : max;

            try {
                /**
                 * get reviews with descending order between min and max ratings excluding
                 * user's review
                 */
                reviews = reviewService.findByMovieIdAndRatingBetweenAndUserIdNotSorted(movieId,
                        min, max, req.getUserId());
            } catch (ResourceNotFoundException e) {
                // reviews in range not found
                reviews = new ArrayList<>();
            }

        } catch (ResourceNotFoundException e1) {
            // user review not found
            try {
                // get reviews for movie in descending order
                reviews = reviewService.findAllByMovieIdSorted(movieId);
            } catch (ResourceNotFoundException e2) {
                // no reviews for movie
                reviews = new ArrayList<>();
            }
        }

        // time to create movie responses
        Set<MovieResponse> movieResponses = new HashSet<>();
        // no reviews found
        if (reviews.isEmpty()) {
            // get top rated movies
            List<Movie> movies = movieService
                    .findAllTopRatedMoviesSorted();

            // create movie set
            int counter = 0;
            for (Movie movie : movies) {
                if (counter == amount) {
                    break;
                }

                // do not give user a movie that they have reviewed
                if (reviewService.userHasLeftReview(req.getUserId(), movie.getId())) {
                    continue;
                }

                // add movie
                movieResponses.add(new MovieResponse(movie));

                counter++;
            }
        } else {
            // create map that is made up of (movieId : counter) pairs
            Map<Movie, Integer> map = new LinkedHashMap<>();
            for (Review review : reviews) {
                String userId = review.getUser().getId();
                try {
                    /**
                     * get reviews for user in descending order limited to amount not including
                     * current movie id
                     */
                    List<Review> subReviews = reviewService.findAllByUserIdAndMovieIdNotSorted(userId, movieId, amount);
                    for (Review subReview : subReviews) {
                        Movie subMovie = subReview.getMovie();
                        // do not give user a movie that they have reviewed
                        if (reviewService.userHasLeftReview(req.getUserId(), subMovie.getId())) {
                            continue;
                        }

                        if (map.containsKey(subMovie)) {
                            map.put(subMovie, map.get(subMovie) + 1);
                        } else {
                            map.put(subMovie, 1);
                        }
                    }

                } catch (ResourceNotFoundException e) {
                    // no reviews found for user
                    continue;
                }
            }

            /*
             * map is empty because user has made a review for each movie reviewed by the
             * other users. Top rated movies are used instead.
             */
            if (map.isEmpty()) {
                // get top rated movies
                List<Movie> movies = movieService
                        .findAllTopRatedMoviesSorted();

                // create movie set
                int counter = 0;
                for (Movie movie : movies) {
                    if (counter == amount) {
                        break;
                    }

                    // do not give user a movie that they have reviewed
                    if (reviewService.userHasLeftReview(req.getUserId(), movie.getId())) {
                        continue;
                    }

                    // add movie
                    movieResponses.add(new MovieResponse(movie));

                    counter++;
                }
            }
            // map is full of movies that user hasn't reviewed yet
            else {
                // sort map with most rated movies on top
                List<Entry<Movie, Integer>> list = new ArrayList<>(map.entrySet());
                list.sort(Entry.comparingByValue());

                // iterate over sorted list till amount specified
                int counter = 0;
                for (Entry<Movie, Integer> entry : list) {
                    if (counter == amount) {
                        break;
                    }

                    if (entry.getKey().getId().equals(movieId)) {
                        continue;
                    }

                    movieResponses.add(new MovieResponse(entry.getKey()));

                    counter++;
                }
            }
        }

        // if no recommendations could be made
        if (movieResponses.isEmpty()) {
            throw new ResourceNotFoundException("No recommendations available.");
        }

        return movieResponses;
    }
}
