package dpg.springboot.microservice.moviecatalogservice.resources;

import dpg.springboot.microservice.moviecatalogservice.models.CatelogItem;
import dpg.springboot.microservice.moviecatalogservice.models.Movie;
import dpg.springboot.microservice.moviecatalogservice.models.Rating;
import dpg.springboot.microservice.moviecatalogservice.models.UserRating;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import java.lang.reflect.ParameterizedType;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("catalog")
public class MovieCatalogResource {

    @Autowired
    public RestTemplate restTemplate;

    @Autowired
    public WebClient.Builder webClientBuilder;

    @RequestMapping("/{userId}")
    public List<CatelogItem> getCatelog(@PathVariable ("userId") String userId){

/**
 * when every time getCatelog() call it to create RestTemplate again and again.
 * To avoid that, create Bean from RestTemplate and Autowired as above.
 * Now RestTemplate work as singleton. So comment it as follows
 */
//        RestTemplate restTemplate = new RestTemplate();

//        get all rated movie ids
//        List<Rating> ratings = Arrays.asList(
//                new Rating("1234",4),
//                new Rating("5678",3)
//        );
        /**
         * get above hard corded ratings list from ratingDataService.
         * List of Rating objects cannot cast with this code format, but we can try as follow.
         * But it is burned issue. So it is provide the path for new object implementation
         *
         */
        //restTemplate.getForObject("http://localhost:8083/ratingsdata"+userId, ParameterizedType<List<Rating>>); //compile error
        UserRating userRating = restTemplate.getForObject("http://localhost:8083/ratingsdata/users/" + userId, UserRating.class);

        return userRating.getUserRating().stream().map(rating -> {
           /**
            * we gare going to implement webClient rather than RestTemplate.            *
            */
            //        for each movieId, call movieInfoService and get the details
           Movie movie = restTemplate.getForObject("http://localhost:8082/movies/"+ rating.getMovieId(), Movie.class);
           /**
            * implement using webClient
            */
//           Movie movie = webClientBuilder.build() //builder pattern
//                   .get() // get request
//                   .uri("http://localhost:8082/movies/" + rating.getMovieId()) // url need to access
//                   .retrieve() // go to the fetch
//                   .bodyToMono(Movie.class) // reactive programming it's like a promise that it will get that what you want when it fill the object, Asynchronous
//                   .block(); //till get the list of movies

            //        put them all together
            return new CatelogItem(movie.getName(),"Desc Test",rating.getRating());
       })
               .collect(Collectors.toList());



//        return Collections.singletonList(new CatelogItem("Sooriya Arana","Test Des",4));
    }
}
