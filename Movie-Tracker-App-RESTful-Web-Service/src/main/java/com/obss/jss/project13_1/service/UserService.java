package com.obss.jss.project13_1.service;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.core.userdetails.User.UserBuilder;
import org.springframework.stereotype.Service;

import com.obss.jss.project13_1.model.Movie;
import com.obss.jss.project13_1.model.User;
import com.obss.jss.project13_1.respository.MovieRepository;
import com.obss.jss.project13_1.respository.UserRepository;
import com.obss.jss.project13_1.util.JwtTokenUtil;

@Service
public class UserService implements UserDetailsService {

	@Autowired
	MovieRepository movieRepository;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Autowired
	private UserRepository userRepository;

	public User getUser(String username) {
		return userRepository.findByUsername(username);
	}

	public boolean addUser(User user) {
		if (user == null)
			return false;
		user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
		userRepository.save(user);
		return true;
	}

	public List<User> getAllUsers() {
		return userRepository.findAll();
	}

	public boolean deleteUserByUsername(String username, String requestTokenHeader) {
		User admin = getUserFromRequestTokenHeader(requestTokenHeader);
		User user = getUser(username);

		if (username.equals(admin.getUsername()))
			return false;

		user.setWatchedMovies(null);
		user.setFavoriteMovies(null);

		userRepository.deleteById(user.getId());
		return true;
	}

	public void updateUserDetails(User newUser, User oldUser) {
		if (newUser.getUsername() != null)
			oldUser.setUsername(newUser.getUsername());
		if (newUser.getFirstName() != null)
			oldUser.setFirstName(newUser.getFirstName());
		if (newUser.getLastName() != null)
			oldUser.setLastName(newUser.getLastName());
		if (newUser.getEmail() != null)
			oldUser.setEmail(newUser.getEmail());
		if (newUser.getMovies() != null)
			oldUser.setMovies(newUser.getMovies());
		if (newUser.getPassword() != null)
			oldUser.setPassword(newUser.getPassword());
		if (newUser.getRole() != null)
			oldUser.setRole(newUser.getRole());
		userRepository.save(oldUser);
	}

	public List<User> getUserByFirstNameAndLastName(String firstName, String lastName) {
		return userRepository.findByFirstNameAndLastName(firstName, lastName);
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.findByUsername(username);
		UserBuilder builder = null;
		if (user != null) {
			builder = org.springframework.security.core.userdetails.User.withUsername(username);
			builder.password(user.getPassword());
			builder.authorities(user.getRole());
		} else {
			throw new UsernameNotFoundException("User not found.");
		}
		return builder.build();
	}

	public boolean addMovieToWatchedList(Long id, String requestTokenHeader) {
		User user = getUserFromRequestTokenHeader(requestTokenHeader);
		Movie movie = movieRepository.findById(id).get();
		Set<Movie> wm = user.getWatchedMovies();
		if (wm.contains(movie))
			return false;
		wm.add(movie);
		userRepository.save(user);
		return true;
	}

	public List<Movie> getWatchedMoviesByToken(String requestTokenHeader) {
		User user = getUserFromRequestTokenHeader(requestTokenHeader);
		return movieRepository.findByUsersW(user);
	}

	public boolean removeMovieFromWatchedList(Long id, String requestTokenHeader) {
		User user = getUserFromRequestTokenHeader(requestTokenHeader);
		Movie movie = movieRepository.findById(id).get();
		Set<Movie> wm = user.getWatchedMovies();
		if (!wm.contains(movie))
			return false;
		wm.remove(movie);
		userRepository.save(user);
		return true;
	}

	public boolean addMovieToFavoriteList(Long id, String requestTokenHeader) {
		User user = getUserFromRequestTokenHeader(requestTokenHeader);
		Movie movie = movieRepository.findById(id).get();
		Set<Movie> fm = user.getFavoriteMovies();
		if (fm.contains(movie))
			return false;
		fm.add(movie);
		userRepository.save(user);
		return true;
	}

	public List<Movie> getFavoriteMoviesByToken(String requestTokenHeader) {
		User user = getUserFromRequestTokenHeader(requestTokenHeader);
		return movieRepository.findByUsersF(user);
	}

	public boolean removeMovieFromFavoriteList(Long id, String requestTokenHeader) {
		User user = getUserFromRequestTokenHeader(requestTokenHeader);
		Movie movie = movieRepository.findById(id).get();
		Set<Movie> fm = user.getFavoriteMovies();
		if (!fm.contains(movie))
			return false;
		fm.remove(movie);
		userRepository.save(user);
		return true;
	}

	private User getUserFromRequestTokenHeader(String token) {
		token = token.substring(7);
		return userRepository.findByUsername(jwtTokenUtil.getUsernameFromToken(token));
	}

}
