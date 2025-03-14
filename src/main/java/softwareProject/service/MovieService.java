package softwareProject.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;
import softwareProject.business.*;

import java.util.Arrays;
import java.util.List;

import static com.fasterxml.jackson.databind.cfg.CoercionInputShape.Array;

@Service
@Slf4j
public class MovieService {

    @Value("${movieDb.apikey}")
    public String API_KEY;
    private final String API_URL = "https://api.themoviedb.org/3/movie/popular?api_key=";


    @Autowired
    private RestTemplate restTemplate;

    /**
     * Get the most popular movies
     * @return the most popular movies
     */
    public List<MovieTest> getMovies(){

       // MovieTest [] movies = restTemplate.getForObject(API_URL, MovieTest[].class);


        ResponseEntity<MovieResponse> response = restTemplate.getForEntity(API_URL+API_KEY, MovieResponse.class);


        return response.getBody().getResults();
    }


    // get videos based on movie id

    /**
     * Get video son movies based on the movie id
     * @param id is the movie id being searched
     * @return different videos based on movie searched
     */

    public List<MovieTrailer> getTrailer(int id){

        ResponseEntity<MovieTrailerResponse> response = restTemplate.getForEntity("https://api.themoviedb.org/3/movie/"+id+"/videos?api_key="+API_KEY, MovieTrailerResponse.class);


        return response.getBody().getResults();
    }


    // get genre of movies


    private final String API_URL1 = "https://api.themoviedb.org/3/genre/movie/list?api_key=";


    /**
     * Get genres from the movie db api
     * @return the list of genre from movie db api
     */
    public List<GenreTest> getGenres(){

        ResponseEntity<GenreResponse> response = restTemplate.getForEntity(API_URL1+API_KEY, GenreResponse.class);


        return response.getBody().getGenres();

    }


    // get movie by genre id

    /**
     * Get movies based the genre
     * @param genre_id is the genre id being searched
     * @return the list of movies based on the genre
     */

    public List<MovieTest> getMoviesByGenre(String genre_id){

        ResponseEntity<MovieResponse> response = restTemplate.getForEntity("https://api.themoviedb.org/3/discover/movie?with_genres="+genre_id+"&api_key="+API_KEY, MovieResponse.class);


        return response.getBody().getResults();
    }



    /// get movie by movie name

    /**
     * search for any movie from the movie db api
     * @param query is the movie being searched
     * @return a list of movies based on the searche
     */

    public List<MovieTest> getMoviesBySearch(String query){

        ResponseEntity<MovieResponse> response = restTemplate.getForEntity("https://api.themoviedb.org/3/search/movie?query="+query+"&api_key="+API_KEY, MovieResponse.class);


        return response.getBody().getResults();
    }

    // get movie by movie id


    public MovieDbByMovieId getMoviesByMovieId(int movieId){

        ResponseEntity<MovieDbByMovieId> response = restTemplate.getForEntity("https://api.themoviedb.org/3/movie/"+movieId+"?"+"&api_key="+API_KEY, MovieDbByMovieId.class);


        return response.getBody();
    }


}
