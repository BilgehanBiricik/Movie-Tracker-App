package com.obss.jss.project13_1;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.obss.jss.project13_1.model.Director;
import com.obss.jss.project13_1.model.Movie;
import com.obss.jss.project13_1.model.User;
import com.obss.jss.project13_1.respository.DirectorRepository;
import com.obss.jss.project13_1.respository.MovieRepository;
import com.obss.jss.project13_1.respository.UserRepository;

@SpringBootApplication
public class Project131Application implements CommandLineRunner {

	@Autowired
	private MovieRepository mr;

	@Autowired
	private DirectorRepository dr;

	@Autowired
	private UserRepository ur;

	public static void main(String[] args) {
		SpringApplication.run(Project131Application.class, args);
	}

	@Override
	public void run(String... args) throws ParseException {

		SimpleDateFormat format = new SimpleDateFormat("dd MM yyyy");
		User user1 = new User("admin", new BCryptPasswordEncoder().encode("123"), "Bilgehan", "Biricik",
				"admin@movietracker.com", "ROLE_ADMIN");
		User user2 = new User("user", new BCryptPasswordEncoder().encode("123"), "Bilgehan", "Biricik",
				"user@movietracker.com", "ROLE_USER");
		ur.save(user1);
		ur.save(user2);
		Director d1 = new Director("Michael", "Bay", new Date(2000), "USA");
		Director d2 = new Director("Quentin", "Tarantino", new Date(2000), "USA");
		dr.save(d1);
		dr.save(d2);
		mr.save(new Movie("Bad Boys", format.parse("01 02 2018"), 6.9, 120, "Action", d1, user1));
		mr.save(new Movie("Transformers", format.parse("01 02 2018"), 7.1, 144, "Action", d1, user1));
		mr.save(new Movie("Kill Bill", format.parse("01 02 2018"), 8.1, 120, "Action", d2, user1));
		mr.save(new Movie("Pulp Fiction", format.parse("01 02 2018"), 8.9, 154, "Drama, Crime", d2, user1));
	}

}
