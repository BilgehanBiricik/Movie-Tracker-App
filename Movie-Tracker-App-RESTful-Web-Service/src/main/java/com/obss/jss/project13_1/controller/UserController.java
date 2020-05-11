package com.obss.jss.project13_1.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
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
import com.obss.jss.project13_1.model.User;
import com.obss.jss.project13_1.service.MovieService;
import com.obss.jss.project13_1.service.UserService;
import com.obss.jss.project13_1.util.JwtTokenUtil;
import com.obss.jss.project13_1.util.ResponseMessage;

@RestController
@CrossOrigin(origins = { "http://localhost:3000" })
@RequestMapping("/users")
public class UserController {

	@Autowired
	private UserService userService;

	@Autowired
	private MovieService movieService;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@GetMapping
	public ResponseEntity<?> getUsers(@RequestParam(required = false) String username,
			@RequestParam(required = false) String firstName, @RequestParam(required = false) String lastName) {

		if (username != null) {
			User user = userService.getUser(username);
			if (user == null)
				return new ResponseEntity<ResponseMessage>(new ResponseMessage("User or users not found"),
						HttpStatus.OK);
			return new ResponseEntity<User>(user, HttpStatus.OK);

		} else if (firstName != null && lastName != null) {
			List<User> users = userService.getUserByFirstNameAndLastName(firstName, lastName);
			if (users.isEmpty())
				return new ResponseEntity<ResponseMessage>(new ResponseMessage("User or users not found"),
						HttpStatus.OK);
			return new ResponseEntity<List<User>>(users, HttpStatus.OK);
		}

		return new ResponseEntity<List<User>>(userService.getAllUsers(), HttpStatus.OK);
	}

	@PostMapping
	public ResponseEntity<ResponseMessage> add(@RequestBody User user) {
		if (userService.getUser(user.getUsername()) != null)
			return new ResponseEntity<ResponseMessage>(new ResponseMessage("User already exists"), HttpStatus.OK);

		if (!userService.addUser(user))
			return new ResponseEntity<ResponseMessage>(new ResponseMessage("User cannot be null"),
					HttpStatus.BAD_REQUEST);

		return new ResponseEntity<ResponseMessage>(new ResponseMessage("User added successfully"), HttpStatus.OK);
	}

	@DeleteMapping("/{username}")
	public ResponseEntity<ResponseMessage> delete(@PathVariable String username,
			@RequestHeader("Authorization") String requestTokenHeader) {
		if (username == null)
			return new ResponseEntity<ResponseMessage>(new ResponseMessage("User ID cannot be null"),
					HttpStatus.BAD_REQUEST);
		if (userService.getUser(username) == null)
			return new ResponseEntity<ResponseMessage>(new ResponseMessage("User not found"), HttpStatus.OK);
		if (!userService.deleteUserByUsername(username, requestTokenHeader))
			return new ResponseEntity<ResponseMessage>(new ResponseMessage("User cant delete itself"), HttpStatus.OK);
		return new ResponseEntity<ResponseMessage>(new ResponseMessage("User deleted successfully"), HttpStatus.OK);

	}

	@PutMapping("/{username}")
	public ResponseEntity<ResponseMessage> update(@PathVariable String username, @RequestBody User user) {
		User usr = userService.getUser(username);
		if (usr == null)
			return new ResponseEntity<ResponseMessage>(new ResponseMessage("User not found"), HttpStatus.OK);
		userService.updateUserDetails(user, usr);
		return new ResponseEntity<ResponseMessage>(new ResponseMessage("User updated"), HttpStatus.OK);

	}

	@PostMapping("/watched-list/{id}")
	public ResponseEntity<?> addWatchedList(@PathVariable Long id,
			@RequestHeader("Authorization") String requestTokenHeader) {
		if (id == null)
			return new ResponseEntity<ResponseMessage>(new ResponseMessage("ID cannot be null"),
					HttpStatus.BAD_REQUEST);
		if (movieService.getMovieById(id) == null)
			return new ResponseEntity<ResponseMessage>(new ResponseMessage("Movie not found"), HttpStatus.OK);
		if (!userService.addMovieToWatchedList(id, requestTokenHeader))
			return new ResponseEntity<ResponseMessage>(new ResponseMessage("Movie already in list"), HttpStatus.OK);

		return new ResponseEntity<ResponseMessage>(new ResponseMessage("Movie added to watched list"), HttpStatus.OK);
	}

	@GetMapping("/watched-list")
	public ResponseEntity<?> getWatchedList(@RequestHeader("Authorization") String requestTokenHeader) {
		List<Movie> watchedMovies = userService.getWatchedMoviesByToken(requestTokenHeader);
		if (watchedMovies == null)
			return new ResponseEntity<ResponseMessage>(new ResponseMessage("Watched List is empty"), HttpStatus.OK);
		return new ResponseEntity<List<Movie>>(watchedMovies, HttpStatus.OK);

	}

	@DeleteMapping("/watched-list/{id}")
	public ResponseEntity<?> deleteWatchedList(@PathVariable Long id,
			@RequestHeader("Authorization") String requestTokenHeader) {
		if (id == null)
			return new ResponseEntity<ResponseMessage>(new ResponseMessage("ID cannot be null"),
					HttpStatus.BAD_REQUEST);
		if (movieService.getMovieById(id) == null)
			return new ResponseEntity<ResponseMessage>(new ResponseMessage("Movie not found"), HttpStatus.OK);
		if (!userService.removeMovieFromWatchedList(id, requestTokenHeader))
			return new ResponseEntity<ResponseMessage>(new ResponseMessage("Movie already removed from list"),
					HttpStatus.OK);

		return new ResponseEntity<ResponseMessage>(new ResponseMessage("Movie removed from watched list"),
				HttpStatus.OK);
	}

	@PostMapping("/favorite-list/{id}")
	public ResponseEntity<?> addFavoriteList(@PathVariable Long id,
			@RequestHeader("Authorization") String requestTokenHeader) {
		if (id == null)
			return new ResponseEntity<ResponseMessage>(new ResponseMessage("ID cannot be null"),
					HttpStatus.BAD_REQUEST);
		if (movieService.getMovieById(id) == null)
			return new ResponseEntity<ResponseMessage>(new ResponseMessage("Movie not found"), HttpStatus.OK);
		if (!userService.addMovieToFavoriteList(id, requestTokenHeader))
			return new ResponseEntity<ResponseMessage>(new ResponseMessage("Movie already in list"), HttpStatus.OK);
		return new ResponseEntity<ResponseMessage>(new ResponseMessage("Movie added to favorite list"), HttpStatus.OK);
	}

	@GetMapping("/favorite-list")
	public ResponseEntity<?> getFavoriteList(@RequestHeader("Authorization") String requestTokenHeader) {
		List<Movie> favoriteMovies = userService.getFavoriteMoviesByToken(requestTokenHeader);
		if (favoriteMovies == null)
			return new ResponseEntity<ResponseMessage>(new ResponseMessage("Favorite List is empty"), HttpStatus.OK);
		return new ResponseEntity<List<Movie>>(favoriteMovies, HttpStatus.OK);

	}

	@DeleteMapping("/favorite-list/{id}")
	public ResponseEntity<?> deleteFavoriteList(@PathVariable Long id,
			@RequestHeader("Authorization") String requestTokenHeader) {
		if (id == null)
			return new ResponseEntity<ResponseMessage>(new ResponseMessage("ID cannot be null"),
					HttpStatus.BAD_REQUEST);
		if (movieService.getMovieById(id) == null)
			return new ResponseEntity<ResponseMessage>(new ResponseMessage("Movie not found"), HttpStatus.OK);
		if (!userService.removeMovieFromFavoriteList(id, requestTokenHeader))
			return new ResponseEntity<ResponseMessage>(new ResponseMessage("Movie already removed from list"),
					HttpStatus.OK);
		return new ResponseEntity<ResponseMessage>(new ResponseMessage("Movie removed from favorite list"),
				HttpStatus.OK);
	}

	@GetMapping("/token")
	public ResponseEntity<?> getUserFromJwtToken(@RequestHeader("Authorization") String requestTokenHeader) {
		UserDetails userDetails = jwtTokenUtil.getUserDetailsFromToken(requestTokenHeader.substring(7));
		if (userDetails == null)
			return new ResponseEntity<ResponseMessage>(new ResponseMessage("User not found"), HttpStatus.OK);
		User user = userService.getUser(userDetails.getUsername());

		@SuppressWarnings("unused")
		Object userD = new Object() {
			public final String username = userDetails.getUsername();
			public final String fullName = user.getFirstName() + " " + user.getLastName();
			public final String role = userDetails.getAuthorities().toString();

		};
		return new ResponseEntity<Object>(userD, HttpStatus.OK);
	}

}
