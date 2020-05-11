package com.obss.jss.project13_1.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "movies")
public class Movie {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column
	private String name;

	@Column
	@Temporal(TemporalType.DATE)
	private Date releaseDate;

	@Column
	private Double imdbRating;

	@Column
	private Integer duration;

	@Column
	private String genre;

	@ManyToOne
	@JoinColumn
	private Director director;

	@ManyToOne
	@JoinColumn
	private User user;

	@ManyToMany(mappedBy = "watchedMovies")
	@JsonIgnore
	private Set<User> usersW = new HashSet<>();

	@ManyToMany(mappedBy = "favoriteMovies")
	@JsonIgnore
	private Set<User> usersF = new HashSet<>();

	public Movie(String name, Date releaseDate, Double imdbRating, Integer duration, String genre) {
		super();
		this.name = name;
		this.releaseDate = releaseDate;
		this.imdbRating = imdbRating;
		this.duration = duration;
		this.genre = genre;
	}

	public Movie(String name, Date releaseDate, Double imdbRating, Integer duration, String genre, Director director,
			User user) {
		super();
		this.name = name;
		this.releaseDate = releaseDate;
		this.imdbRating = imdbRating;
		this.duration = duration;
		this.genre = genre;
		this.director = director;
		this.user = user;
	}

	public Movie() {
		super();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getReleaseDate() {
		return releaseDate;
	}

	public void setReleaseDate(Date releaseDate) {
		this.releaseDate = releaseDate;
	}

	public Double getImdbRating() {
		return imdbRating;
	}

	public void setImdbRating(Double imdbRating) {
		this.imdbRating = imdbRating;
	}

	public Integer getDuration() {
		return duration;
	}

	public void setDuration(Integer duration) {
		this.duration = duration;
	}

	public String getGenre() {
		return genre;
	}

	public void setGenre(String genre) {
		this.genre = genre;
	}

	public Director getDirector() {
		return director;
	}

	public void setDirector(Director director) {
		this.director = director;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Set<User> getUsersW() {
		return usersW;
	}

	public void setUsersW(Set<User> usersW) {
		this.usersW = usersW;
	}

	public Set<User> getUsersF() {
		return usersF;
	}

	public void setUsersF(Set<User> usersF) {
		this.usersF = usersF;
	}

}
