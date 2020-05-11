import React from "react";
import {
  Container,
  Card,
  FormControl,
  Button,
  Form,
  Table,
  Alert
} from "react-bootstrap";
import NavBarComponent from "../components/NavBarComponent";
import Axios from "axios";

class OmdbPage extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      movieImdbId: "",
      movie: { director: {} },
      message: ""
    };
  }

  componentDidMount() {
    console.log(this.state.movie);
    if (localStorage.getItem("token")) {
      const token = "Bearer " + localStorage.getItem("token");
      Axios.get("http://localhost:8080/users/token", {
        headers: { Authorization: token }
      })
        .then(response => {
          console.log(response.data);
          if (response.data.role !== "[ROLE_ADMIN]") {
            this.props.history.goBack();
          }
        })
        .catch(err => {
          console.log(err);
          this.props.history.push("/login");
        });
    } else this.props.history.push("/login");
  }

  handleMovieImdbIdChange = event => {
    this.setState({ [event.target.name]: event.target.value });
  };

  handleGetMovieButtonClick = event => {
    const token = "Bearer " + localStorage.getItem("token");
    Axios.get("http://localhost:8080/omdb?id=" + this.state.movieImdbId, {
      headers: { Authorization: token }
    })
      .then(response => {
        if (response.data.message) console.log(response.data.message);
        else
          this.setState({ movie: response.data }, () => {
            console.log(this.state.movie);
          });
      })
      .catch(err => {
        console.log(err);
      });
  };

  handleSaveButtonClick = event => {
    const token = "Bearer " + localStorage.getItem("token");
    Axios.post("http://localhost:8080/omdb", this.state.movie, {
      headers: { Authorization: token }
    })
      .then(response => {
        this.setState({ message: response.data.message });
      })
      .catch(err => {
        console.log(err);
      });
  };

  render() {
    return (
      <>
        <NavBarComponent />
        <Container style={{ marginTop: 40 }}>
          <Card style={{ marginBottom: 20 }}>
            <Card.Body>
              <Form
                inline
                style={{
                  width: "340px",
                  marginLeft: "auto",
                  marginRight: "auto"
                }}
              >
                <FormControl
                  type="text"
                  name="movieImdbId"
                  placeholder="Enter movie ID"
                  className="mr-sm-2"
                  onChange={this.handleMovieImdbIdChange}
                />
                <Button
                  variant="outline-success"
                  onClick={this.handleGetMovieButtonClick}
                >
                  Get Movie
                </Button>
              </Form>
            </Card.Body>
          </Card>
          {this.state.movie.name !== undefined && (
            <Container>
              <Table bordered hover>
                <tbody>
                  <tr>
                    <td>
                      <strong>Title: </strong>
                    </td>
                    <td>{this.state.movie.name}</td>
                  </tr>
                  <tr>
                    <td>
                      <strong>Director: </strong>{" "}
                    </td>
                    <td>
                      {this.state.movie.director.name}{" "}
                      {this.state.movie.director.surname}
                    </td>
                  </tr>
                  <tr>
                    <td>
                      <strong>IMDb Rating: </strong>
                    </td>
                    <td>{this.state.movie.imdbRating} / 10</td>
                  </tr>
                  <tr>
                    <td>
                      <strong>Duration: </strong>
                    </td>
                    <td>{this.state.movie.duration} min</td>
                  </tr>
                  <tr>
                    <td>
                      <strong>Genre: </strong>
                    </td>
                    <td>{this.state.movie.genre}</td>
                  </tr>
                  <tr>
                    <td>
                      <strong>Release Date: </strong>
                    </td>
                    <td>{this.state.movie.releaseDate}</td>
                  </tr>
                </tbody>
              </Table>
              <Button variant="primary" onClick={this.handleSaveButtonClick}>
                Save
              </Button>
              {this.state.message !== "" && (
                <Alert style={{ marginTop: 10 }} variant="success">
                  {this.state.message}
                </Alert>
              )}
            </Container>
          )}
        </Container>
      </>
    );
  }
}

export default OmdbPage;
