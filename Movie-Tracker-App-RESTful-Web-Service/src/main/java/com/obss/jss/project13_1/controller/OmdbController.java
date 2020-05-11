package com.obss.jss.project13_1.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JsonParser;
import org.springframework.boot.json.JsonParserFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.obss.jss.project13_1.model.Director;
import com.obss.jss.project13_1.model.Movie;
import com.obss.jss.project13_1.service.DirectorService;
import com.obss.jss.project13_1.service.MovieService;
import com.obss.jss.project13_1.util.ResponseMessage;

@RestController
@CrossOrigin(origins = { "http://localhost:3000" })
@RequestMapping("/omdb")
public class OmdbController {

	@Autowired
	private DirectorService directorService;

	@Autowired
	private MovieService movieService;

	@GetMapping
	public ResponseEntity<?> getFromOmdb(@RequestParam String id,
			@RequestHeader("Authorization") String requestTokenHeader) throws ParseException {
		String resp = new RestTemplate().getForObject("http://www.omdbapi.com/?apikey=ba4e51a1&i=" + id, String.class);

		JsonParser springParser = JsonParserFactory.getJsonParser();
		Map<String, Object> map = springParser.parseMap(resp);

		if (map.containsKey("Error")) {
			return new ResponseEntity<ResponseMessage>(new ResponseMessage(map.get("Error").toString()), HttpStatus.OK);
		}

		Movie movie = new Movie();
		if (!map.get("Title").toString().equals("N/A"))
			movie.setName(map.get("Title").toString());
		if (!map.get("Released").toString().equals("N/A"))
			movie.setReleaseDate(new SimpleDateFormat("dd MMM yyyy").parse(map.get("Released").toString()));
		if (!map.get("imdbRating").toString().equals("N/A"))
			movie.setImdbRating(Double.parseDouble(map.get("imdbRating").toString()));
		if (!map.get("Runtime").toString().equals("N/A"))
			movie.setDuration(Integer.parseInt(map.get("Runtime").toString().split(" ")[0]));
		if (!map.get("Genre").toString().equals("N/A"))
			movie.setGenre(map.get("Genre").toString());

		Director director = new Director(map.get("Director").toString().split(" ")[0],
				map.get("Director").toString().split(" ")[1], new Date(100), "N/A");
		movie.setDirector(director);


		return new ResponseEntity<Movie>(movie, HttpStatus.OK);
	}

	@PostMapping
	public ResponseEntity<?> postToMovie(@RequestBody Movie movie,
			@RequestHeader("Authorization") String requestTokenHeader) {
		if (movie == null)
			return new ResponseEntity<ResponseMessage>(new ResponseMessage("Movie cannot be null"), HttpStatus.OK);

		if (directorService.getDirectorByNameAndSurname(movie.getDirector().getName(),
				movie.getDirector().getSurname()) == null) {
			Director director = new Director(movie.getDirector().getName(), movie.getDirector().getSurname(),
					new Date(100), movie.getDirector().getBirthPlace());
			directorService.addDirector(director);
			movie.setDirector(director);
		} else {
			movie.setDirector(directorService.getDirectorByNameAndSurname(movie.getDirector().getName(),
					movie.getDirector().getSurname()));
		}
		movieService.addMovie(movie, requestTokenHeader);
		return new ResponseEntity<ResponseMessage>(new ResponseMessage("Movie added successfully"), HttpStatus.OK);
	}

}
