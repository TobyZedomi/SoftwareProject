package softwareProject.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;
import softwareProject.business.MovieResponse;
import softwareProject.business.MovieTest;

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

    public List<MovieTest> getMovies(){

       // MovieTest [] movies = restTemplate.getForObject(API_URL, MovieTest[].class);


        ResponseEntity<MovieResponse> response = restTemplate.getForEntity(API_URL+API_KEY, MovieResponse.class);


        return response.getBody().getResults();
    }
}
