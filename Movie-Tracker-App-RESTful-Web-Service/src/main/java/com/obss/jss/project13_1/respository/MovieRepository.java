package com.obss.jss.project13_1.respository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.obss.jss.project13_1.model.Movie;
import com.obss.jss.project13_1.model.User;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {

	List<Movie> findByNameContaining(String name);

	List<Movie> findByDirectorId(Long id);

	List<Movie> findByUsersW(User user);

	List<Movie> findByUsersF(User user);

}
