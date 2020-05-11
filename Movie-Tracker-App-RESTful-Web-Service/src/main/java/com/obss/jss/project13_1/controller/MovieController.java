package com.obss.jss.project13_1.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.obss.jss.project13_1.model.Movie;
import com.obss.jss.project13_1.service.MovieService;
import com.obss.jss.project13_1.util.ResponseMessage;

@RestController
@CrossOrigin(origins = { "http://localhost:3000" })
@RequestMapping("/movies")
public class MovieController {

	@Autowired
	private MovieService movieService;

	@GetMapping
	public ResponseEntity<?> search(@RequestParam(required = false) String name) {
		if (name == null)
			return new ResponseEntity<List<Movie>>(movieService.getAllMovies(), HttpStatus.OK);

		List<Movie> movies = movieService.getMoviesByName(name);
		if (movies.isEmpty())
			return new ResponseEntity<ResponseMessage>(new ResponseMessage("Movie or movies not found"), HttpStatus.OK);
		return new ResponseEntity<List<Movie>>(movies, HttpStatus.OK);

	}

	@GetMapping("/{id}")
	public ResponseEntity<?> getMovie(@PathVariable(required = false) Long id) {
		if (id == null)
			return new ResponseEntity<ResponseMessage>(new ResponseMessage("ID cannot be empty"), HttpStatus.OK);
		Movie movie = movieService.getMovieById(id);
		if (movie == null) {
			return new ResponseEntity<ResponseMessage>(new ResponseMessage("Movie not found"), HttpStatus.OK);
		}
		return new ResponseEntity<Movie>(movie, HttpStatus.OK);
	}

	@PostMapping
	public ResponseEntity<ResponseMessage> add(@RequestBody Movie movie,
			@RequestHeader("Authorization") String requestTokenHeader) {
		if (movie == null)
			return new ResponseEntity<ResponseMessage>(new ResponseMessage("Movie cannot be null"),
					HttpStatus.BAD_REQUEST);
		movieService.addMovie(movie, requestTokenHeader);
		return new ResponseEntity<ResponseMessage>(new ResponseMessage("Movie added successfully"), HttpStatus.OK);

	}

	@DeleteMapping("/{id}")
	public ResponseEntity<ResponseMessage> delete(@PathVariable Long id) {
		if (id == null)
			return new ResponseEntity<ResponseMessage>(new ResponseMessage("Movie ID cannot be null"),
					HttpStatus.BAD_REQUEST);
		if (movieService.getMovieById(id) == null)
			return new ResponseEntity<ResponseMessage>(new ResponseMessage("Movie not found"), HttpStatus.OK);
		movieService.deleteMovieById(id);
		return new ResponseEntity<ResponseMessage>(new ResponseMessage("Movie deleted successfully"), HttpStatus.OK);

	}

	@PutMapping("/{id}")
	public ResponseEntity<ResponseMessage> update(@PathVariable Long id, @RequestBody Movie movie) {
		Movie mv = movieService.getMovieById(id);
		if (mv == null)
			return new ResponseEntity<ResponseMessage>(new ResponseMessage("Movie not found"), HttpStatus.NOT_FOUND);
		movieService.updateMovie(movie, mv);
		return new ResponseEntity<ResponseMessage>(new ResponseMessage("Movie updated"), HttpStatus.OK);

	}

}
