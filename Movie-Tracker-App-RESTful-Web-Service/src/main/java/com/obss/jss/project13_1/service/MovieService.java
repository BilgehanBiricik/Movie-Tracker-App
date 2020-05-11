package com.obss.jss.project13_1.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.obss.jss.project13_1.model.Movie;
import com.obss.jss.project13_1.model.User;
import com.obss.jss.project13_1.respository.MovieRepository;
import com.obss.jss.project13_1.respository.UserRepository;
import com.obss.jss.project13_1.util.JwtTokenUtil;

@Service
public class MovieService {

	@Autowired
	MovieRepository movieRepository;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Autowired
	private UserRepository userRepository;

	public Movie getMovieById(Long id) {
		if (movieRepository.findById(id).isPresent())
			return movieRepository.findById(id).get();
		return null;
	}

	public List<Movie> getAllMovies() {
		return movieRepository.findAll();
	}

	public boolean addMovie(Movie movie, String requestTokenHeader) {
		if (movie == null)
			return false;
		User user = getUserFromRequestTokenHeader(requestTokenHeader);
		movie.setUser(user);
		movieRepository.save(movie);
		return true;
	}

	public void deleteMovieById(Long id) {

		Movie movie = getMovieById(id);

		List<User> usersW = userRepository.findByWatchedMovies(getMovieById(id));
		List<User> usersF = userRepository.findByFavoriteMovies(getMovieById(id));

		for (User user : usersW) {
			user.getWatchedMovies().remove(movie);
			userRepository.save(user);
		}

		for (User user : usersF) {
			user.getFavoriteMovies().remove(movie);
			userRepository.save(user);
		}

		movieRepository.deleteById(id);
	}

	public void updateMovie(Movie newMovie, Movie oldMovie) {
		if (newMovie.getName() != null)
			oldMovie.setName(newMovie.getName());
		if (newMovie.getDirector() != null)
			oldMovie.setDirector(newMovie.getDirector());
		if (newMovie.getDuration() != null)
			oldMovie.setDuration(newMovie.getDuration());
		if (newMovie.getGenre() != null)
			oldMovie.setGenre(newMovie.getGenre());
		if (newMovie.getImdbRating() != null)
			oldMovie.setImdbRating(newMovie.getImdbRating());
		if (newMovie.getReleaseDate() != null)
			oldMovie.setReleaseDate(newMovie.getReleaseDate());
		if (newMovie.getDirector() != null)
			oldMovie.setDirector(newMovie.getDirector());
		movieRepository.save(oldMovie);
	}

	public List<Movie> getMoviesByName(String name) {
		return movieRepository.findByNameContaining(name);
	}

	public List<Movie> getMoviesByDirectorId(Long id) {
		return movieRepository.findByDirectorId(id);
	}

	private User getUserFromRequestTokenHeader(String token) {
		token = token.substring(7);
		return userRepository.findByUsername(jwtTokenUtil.getUsernameFromToken(token));
	}

}
