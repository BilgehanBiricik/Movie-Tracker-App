import React from "react";
import { Modal, Button, Row, Form, Col } from "react-bootstrap";
import Axios from "axios";

class MovieDetailsComponent extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      movie: {
        id: "",
        name: "",
        imdbRating: "",
        genre: "",
        releaseDate: "",
        duration: "",
        director: { id: "" }
      },
      directors: [],
      directorId: 1
    };
  }

  componentDidMount() {
    if (this.props.movie !== undefined)
      this.setState({ movie: this.props.movie });
    const token = "Bearer " + localStorage.getItem("token");
    Axios.get("http://localhost:8080/directors", {
      headers: { Authorization: token }
    })
      .then(response => {
        console.log(response.data);
        this.setState({ directors: response.data });
        if (this.props.movie.director !== undefined)
          this.setState({ directorId: this.props.movie.director.id });
      })
      .catch(err => {
        console.log(err);
      });
  }

  handleSaveButtonClick = event => {
    this.setState(
      prevState => ({
        movie: {
          ...prevState.movie,
          director: {
            id: this.state.directorId
          }
        }
      }),
      () => {
        console.log(this.state.movie);
        console.log(this.state.movie.director.id);
        const token = "Bearer " + localStorage.getItem("token");
        if (this.props.movie.id !== undefined) {
          Axios.put(
            "http://localhost:8080/movies/" + this.state.movie.id,
            this.state.movie,
            {
              headers: { Authorization: token }
            }
          )
            .then(response => {
              console.log(response.data.message);
              this.props.hideModal();
            })
            .catch(err => {
              console.log(err);
            });
        } else {
          Axios.post("http://localhost:8080/movies", this.state.movie, {
            headers: { Authorization: token }
          })
            .then(response => {
              console.log(response.data.message);
              this.props.hideModal();
            })
            .catch(err => {
              console.log(err);
            });
        }
      }
    );
  };

  handleDirectorChange = event => {
    this.setState({ directorId: event.target.value });
  };

  handleMovieInputChange = event => {
    event.persist();
    this.setState(prevState => ({
      movie: {
        ...prevState.movie,
        [event.target.name]: event.target.value
      }
    }));
    console.log(event.target.name + ": " + event.target.value);
  };

  render() {
    return (
      <Modal show={true} onHide={this.props.hideModal}>
        <Modal.Header closeButton>
          <Modal.Title>Movie Details</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          <Form>
            <Form.Group as={Row}>
              <Form.Label column sm={4}>
                Title:
              </Form.Label>
              <Col sm={8}>
                <Form.Control
                  type="text"
                  name="name"
                  defaultValue={this.state.movie.name}
                  onChange={this.handleMovieInputChange}
                />
              </Col>
            </Form.Group>
            <Form.Group as={Row}>
              <Form.Label column sm={4}>
                Director:
              </Form.Label>
              <Col sm={8}>
                <Form.Control
                  as="select"
                  value={this.state.directorId}
                  onChange={this.handleDirectorChange}
                >
                  {this.state.directors.map(director => (
                    <option key={director.id} value={director.id}>
                      {director.name} {director.surname}
                    </option>
                  ))}
                </Form.Control>
              </Col>
            </Form.Group>
            <Form.Group as={Row}>
              <Form.Label column sm={4}>
                IMDb Rating:
              </Form.Label>
              <Col sm={8}>
                <Form.Control
                  type="text"
                  name="imdbRating"
                  defaultValue={this.state.movie.imdbRating}
                  onChange={this.handleMovieInputChange}
                />
              </Col>
            </Form.Group>
            <Form.Group as={Row}>
              <Form.Label column sm={4}>
                Duration:
              </Form.Label>
              <Col sm={8}>
                <Form.Control
                  type="text"
                  name="duration"
                  defaultValue={this.state.movie.duration}
                  onChange={this.handleMovieInputChange}
                />
              </Col>
            </Form.Group>
            <Form.Group as={Row}>
              <Form.Label column sm={4}>
                Genre:
              </Form.Label>
              <Col sm={8}>
                <Form.Control
                  type="text"
                  name="genre"
                  defaultValue={this.state.movie.genre}
                  onChange={this.handleMovieInputChange}
                />
              </Col>
            </Form.Group>
            <Form.Group as={Row}>
              <Form.Label column sm={4}>
                Release Date:
              </Form.Label>
              <Col sm={8}>
                <Form.Control
                  type="date"
                  name="releaseDate"
                  defaultValue={this.state.movie.releaseDate}
                  onChange={this.handleMovieInputChange}
                />
              </Col>
            </Form.Group>
          </Form>
        </Modal.Body>
        <Modal.Footer>
          <Button variant="secondary" onClick={this.props.hideModal}>
            Close
          </Button>
          <Button variant="primary" onClick={this.handleSaveButtonClick}>
            Save
          </Button>
        </Modal.Footer>
      </Modal>
    );
  }
}

export default MovieDetailsComponent;
