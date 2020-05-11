import React from "react";
import NavBarComponent from "../components/NavBarComponent";
import { Container, Card, Row, Col, Button } from "react-bootstrap";
import ListMoviesComponent from "../components/ListMoviesComponent";
import SearchComponent from "../components/SearchComponent";
import Axios from "axios";

class MoviesPage extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      isAdmin: false,
      showAddMovieModal: false,
      searchInputText: "",
      path: "movies/"
    };
  }

  componentDidMount() {
    if (localStorage.getItem("token")) {
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
          this.props.history.push("/login");
        });
    } else this.props.history.push("/login");
  }

  handleAddButtonClick = event => {
    this.setState({ showAddMovieModal: true });
  };

  handleCloseModal = event => {
    this.setState({ path: "" });
    this.setState({ showAddMovieModal: false });
  };

  handleSearchInputChange = event => {
    this.setState({ searchInputText: event.target.value });
    console.log(event.target.value);
  };

  handleSearchButtonClick = event => {
    if (this.state.searchInputText !== "")
      this.setState({ path: "movies?name=" + this.state.searchInputText });
    else this.setState({ path: "movies/" + this.state.searchInputText });
  };

  render() {
    return (
      <>
        <NavBarComponent />
        <Container style={{ marginTop: 40 }}>
          <Card style={{ marginBottom: 20 }}>
            <Card.Body>
              <Row>
                <Col sm={10}>
                  <SearchComponent
                    searchText="Movie"
                    handleSearchChange={this.handleSearchInputChange}
                    handleSearchClick={this.handleSearchButtonClick}
                  />
                </Col>
                {this.state.isAdmin && (
                  <Col sm={2} style={{ textAlign: "right" }}>
                    <Button
                      variant="success"
                      onClick={this.handleAddButtonClick}
                    >
                      Add Movie
                    </Button>
                  </Col>
                )}
              </Row>
            </Card.Body>
          </Card>
          <Container>
            <ListMoviesComponent
              showAddMovieModal={this.state.showAddMovieModal}
              hideModal={this.handleCloseModal}
              path={this.state.path}
            />
          </Container>
        </Container>
      </>
    );
  }
}

export default MoviesPage;
