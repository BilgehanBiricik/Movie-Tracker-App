package com.obss.jss.project13_1.respository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.obss.jss.project13_1.model.Director;

@Repository
public interface DirectorRepository extends JpaRepository<Director, Long>{

	List<Director> findByName(String name);
	
	Director findByNameAndSurname(String name, String surname);
	
}
