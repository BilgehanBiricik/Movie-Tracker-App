import React from "react";
import NavBarComponent from "../components/NavBarComponent";
import { Container, Card } from "react-bootstrap";
import ListMoviesComponent from "../components/ListMoviesComponent";

class WatchedMoviesPage extends React.Component {
  render() {
    return (
      <>
        <NavBarComponent />
        <Container style={{ marginTop: 40 }}>
          <h2>Watched Movies</h2>
          <Card>
            <Card.Body>
              <ListMoviesComponent path="users/watched-list/" watchedMovies />
            </Card.Body>
          </Card>
        </Container>
      </>
    );
  }
}

export default WatchedMoviesPage;
