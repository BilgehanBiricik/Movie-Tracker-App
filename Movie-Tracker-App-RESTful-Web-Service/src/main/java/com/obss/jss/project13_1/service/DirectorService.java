package com.obss.jss.project13_1.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.obss.jss.project13_1.model.Director;
import com.obss.jss.project13_1.model.Movie;
import com.obss.jss.project13_1.respository.DirectorRepository;

@Service
public class DirectorService {

	@Autowired
	private DirectorRepository directorRepository;
	
	@Autowired
	private MovieService movieService;
	
	public Director getDirectorById(Long id) {
		if (directorRepository.findById(id).isPresent())
			return directorRepository.findById(id).get();
		return null;
	}
	
	public List<Director> getAllDirectors() {
		return directorRepository.findAll();
	}
	
	public boolean addDirector(Director director) {
		if (director == null)
			return false;
		directorRepository.save(director);
		return true;
	}
	
	public void deleteDirectorById(Long id) {
		
		List<Movie> movies = movieService.getMoviesByDirectorId(id);
		
		for (Movie movie : movies) {
			movieService.deleteMovieById(movie.getId());
		}
		
		directorRepository.deleteById(id);
	}
	
	public void updateDirector(Director newDirector, Director oldDirector) {
		if (newDirector.getName() != null) oldDirector.setName(newDirector.getName());
		if (newDirector.getSurname() != null) oldDirector.setSurname(newDirector.getSurname());
		if (newDirector.getBirthDate() != null) oldDirector.setBirthDate(newDirector.getBirthDate());
		if (newDirector.getBirthPlace() != null) oldDirector.setBirthPlace(newDirector.getBirthPlace());
		directorRepository.save(oldDirector);
	}
	
	public List<Director> getDirectorsByName(String name) {
		return directorRepository.findByName(name);
	}
	
	public Director getDirectorByNameAndSurname(String name, String surname) {
		return directorRepository.findByNameAndSurname(name, surname);
	}
	
}
