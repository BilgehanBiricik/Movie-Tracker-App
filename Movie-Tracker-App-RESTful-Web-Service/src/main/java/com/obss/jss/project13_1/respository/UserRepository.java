package com.obss.jss.project13_1.respository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.obss.jss.project13_1.model.Movie;
import com.obss.jss.project13_1.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{
	
	User findByUsername(String username);
	
	User findByUsernameAndPassword(String username, String password);
	
	List<User> findByFirstNameAndLastName(String firstName, String lastName);
	
	List<User> findByWatchedMovies(Movie movie);
	
	List<User> findByFavoriteMovies(Movie movie);
	
}
