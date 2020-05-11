import React from "react";
import Axios from "axios";
import MovieDetailsComponent from "./MovieDetailsComponent";
import { Card, ButtonGroup, Button, CardDeck } from "react-bootstrap";

class ListMoviesComponent extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      movies: [],
      movie: {},
      showModal: false,
      isAdmin: false
    };
  }

  getMovies(path) {
    const token = "Bearer " + localStorage.getItem("token");
    Axios.get("http://localhost:8080/" + path, {
      headers: { Authorization: token }
    })
      .then(response => {
        console.log(response.data);
        if (response.data.message) alert(response.data.message);
        else this.setState({ movies: response.data });
      })
      .catch(err => {
        console.log(err);
      });
  }

  componentDidUpdate(prevProps, prevState) {
    if (prevProps.path !== this.props.path) {
      console.log("did update:" + this.props.path);
      if (this.props.path !== "") this.getMovies(this.props.path);
    }
  }

  componentDidMount() {
    const token = "Bearer " + localStorage.getItem("token");
    Axios.get("http://localhost:8080/users/token", {
      headers: { Authorization: token }
    })
      .then(response => {
        console.log(response.data);
        if (response.data.role === "[ROLE_ADMIN]") {
          this.setState({ isAdmin: true });
        }
      })
      .catch(err => {
        console.log(err);
      });
    this.getMovies(this.props.path);
  }

  handleEditButtonClick = event => {
    const token = "Bearer " + localStorage.getItem("token");
    Axios.get("http://localhost:8080/movies/" + event.target.value, {
      headers: { Authorization: token }
    })
      .then(response => {
        console.log(response.data);
        this.setState({ movie: response.data }, () => {
          this.setState({ showModal: true });
        });
      })
      .catch(err => {
        console.log(err);
      });
  };

  handleDeleteButtonClick = event => {
    const token = "Bearer " + localStorage.getItem("token");
    const movieId = event.target.value;
    const movie = this.state.movies.find(m => m.id === parseInt(movieId));
    Axios.delete("http://localhost:8080/" + this.props.path + movieId, {
      headers: { Authorization: token }
    })
      .then(response => {
        console.log(response.data.message);
        this.setState({
          movies: this.state.movies.filter(item => item !== movie)
        });
      })
      .catch(err => {
        console.log(err);
      });
  };

  handleCloseModal = event => {
    this.getMovies("movies/");
    if (this.props.showAddMovieModal) this.props.hideModal();
    this.setState({ movie: {} }, () => {
      this.setState({ showModal: false });
    });
  };

  handleAddWatchedListButtonClick = event => {
    const token = "Bearer " + localStorage.getItem("token");
    Axios.post(
      "http://localhost:8080/users/watched-list/" + event.target.value,
      null,
      {
        headers: { Authorization: token }
      }
    )
      .then(response => {
        if (response.data.message) alert(response.data.message);
      })
      .catch(err => {
        console.log(err);
      });
  };

  handleAddFavoriteListButtonClick = event => {
    const token = "Bearer " + localStorage.getItem("token");
    Axios.post(
      "http://localhost:8080/users/favorite-list/" + event.target.value,
      null,
      {
        headers: { Authorization: token }
      }
    )
      .then(response => {
        if (response.data.message) alert(response.data.message);
      })
      .catch(err => {
        console.log(err);
      });
  };

  render() {
    console.log("render: " + this.props.path);
    const movies = this.state.movies;
    return (
      <>
        <CardDeck style={{ justifyContent: "space-between" }}>
          {movies.map(movie => (
            <Card
              key={movie.id}
              style={{
                marginBottom: 30
              }}
            >
              <Card.Header>
                <Card.Title as="h4">
                  <strong>{movie.name}</strong>
                </Card.Title>
                <Card.Subtitle as="h6">
                  <i>
                    {movie.director.name} {movie.director.surname}
                  </i>
                </Card.Subtitle>
              </Card.Header>
              <Card.Body>
                <Card.Text>
                  <strong>IMDb Rating: </strong>
                  {movie.imdbRating} <br />
                  <strong>Duration: </strong>
                  {movie.duration} min <br />
                  <strong>Genre: </strong>
                  {movie.genre} <br />
                  <strong>Release Date: </strong>
                  {movie.releaseDate}
                  <br />
                </Card.Text>
                {true &&
                  !this.props.watchedMovies &&
                  !this.props.favoriteMovies && (
                    <ButtonGroup
                      style={{
                        marginLeft: "auto",
                        marginRight: "auto",
                        display: "table"
                      }}
                    >
                      <Button
                        value={movie.id}
                        variant="primary"
                        onClick={this.handleAddWatchedListButtonClick}
                      >
                        + Watched List
                      </Button>
                      <Button
                        value={movie.id}
                        variant="primary"
                        onClick={this.handleAddFavoriteListButtonClick}
                      >
                        + Favorite List
                      </Button>
                    </ButtonGroup>
                  )}
                {this.state.isAdmin &&
                  !this.props.watchedMovies &&
                  !this.props.favoriteMovies && (
                    <ButtonGroup
                      style={{
                        marginTop: 10,
                        marginLeft: "auto",
                        marginRight: "auto",
                        display: "table"
                      }}
                    >
                      <Button
                        value={movie.id}
                        variant="warning"
                        onClick={this.handleEditButtonClick}
                      >
                        Edit
                      </Button>
                      <Button
                        value={movie.id}
                        variant="danger"
                        onClick={this.handleDeleteButtonClick}
                      >
                        Delete
                      </Button>
                    </ButtonGroup>
                  )}
                {(this.props.watchedMovies || this.props.favoriteMovies) && (
                  <Button
                    value={movie.id}
                    variant="danger"
                    onClick={this.handleDeleteButtonClick}
                    style={{
                      marginLeft: "auto",
                      marginRight: "auto",
                      display: "table"
                    }}
                  >
                    Remove
                  </Button>
                )}
              </Card.Body>
              <Card.Footer>
                <small className="text-muted">
                  Added by <strong>{movie.user.username}</strong>
                </small>
              </Card.Footer>
            </Card>
          ))}
        </CardDeck>
        {(this.state.showModal || this.props.showAddMovieModal) && (
          <MovieDetailsComponent
            movie={this.state.movie}
            hideModal={this.handleCloseModal}
          />
        )}
      </>
    );
  }
}

export default ListMoviesComponent;
