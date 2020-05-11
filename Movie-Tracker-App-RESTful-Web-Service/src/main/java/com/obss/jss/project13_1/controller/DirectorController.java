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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.obss.jss.project13_1.model.Director;
import com.obss.jss.project13_1.model.Movie;
import com.obss.jss.project13_1.service.DirectorService;
import com.obss.jss.project13_1.service.MovieService;
import com.obss.jss.project13_1.util.ResponseMessage;

@RestController
@CrossOrigin(origins = { "http://localhost:3000" })
@RequestMapping("/directors")
public class DirectorController {

	@Autowired
	private DirectorService directorService;

	@Autowired
	private MovieService movieService;

	@GetMapping
	public ResponseEntity<?> search(@RequestParam(required = false) String name) {
		if (name == null)
			return new ResponseEntity<List<Director>>(directorService.getAllDirectors(), HttpStatus.OK);

		List<Director> directors = directorService.getDirectorsByName(name);
		if (directors.isEmpty())
			return new ResponseEntity<ResponseMessage>(new ResponseMessage("Director or directors not found"),
					HttpStatus.OK);
		return new ResponseEntity<List<Director>>(directors, HttpStatus.OK);
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> getDirector(@PathVariable(required = false) Long id) {
		if (id == null)
			return new ResponseEntity<ResponseMessage>(new ResponseMessage("ID cannot be empty"), HttpStatus.OK);
		Director director = directorService.getDirectorById(id);
		if (director == null)
			return new ResponseEntity<ResponseMessage>(new ResponseMessage("Director not found"), HttpStatus.OK);
		return new ResponseEntity<Director>(director, HttpStatus.OK);
	}

	@PostMapping
	public ResponseEntity<ResponseMessage> add(@RequestBody Director director) {
		if (director == null)
			return new ResponseEntity<ResponseMessage>(new ResponseMessage("Director cannot be null"),
					HttpStatus.BAD_REQUEST);
		directorService.addDirector(director);
		return new ResponseEntity<ResponseMessage>(new ResponseMessage("Director added successfully"), HttpStatus.OK);

	}

	@DeleteMapping("/{id}")
	public ResponseEntity<ResponseMessage> delete(@PathVariable Long id) {
		if (id == null)
			return new ResponseEntity<ResponseMessage>(new ResponseMessage("Director ID cannot be null"),
					HttpStatus.BAD_REQUEST);
		if (directorService.getDirectorById(id) == null)
			return new ResponseEntity<ResponseMessage>(new ResponseMessage("Director not found"), HttpStatus.OK);
		directorService.deleteDirectorById(id);
		return new ResponseEntity<ResponseMessage>(new ResponseMessage("Director deleted successfully"), HttpStatus.OK);

	}

	@PutMapping("/{id}")
	public ResponseEntity<ResponseMessage> update(@PathVariable Long id, @RequestBody Director director) {
		Director dr = directorService.getDirectorById(id);
		if (dr == null)
			return new ResponseEntity<ResponseMessage>(new ResponseMessage("Director not found"), HttpStatus.OK);
		directorService.updateDirector(director, dr);
		return new ResponseEntity<ResponseMessage>(new ResponseMessage("Director updated"), HttpStatus.OK);

	}

	@GetMapping("/{id}/movies")
	public ResponseEntity<?> getDirectorMovies(@PathVariable Long id) {
		List<Movie> movies = movieService.getMoviesByDirectorId(id);
		if (id == null || movies.isEmpty())
			return new ResponseEntity<ResponseMessage>(new ResponseMessage("Not Found"), HttpStatus.OK);
		return new ResponseEntity<List<Movie>>(movies, HttpStatus.OK);
	}

}
