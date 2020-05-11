import React from "react";
import NavBarComponent from "../components/NavBarComponent";
import { Container, Card } from "react-bootstrap";
import ListMoviesComponent from "../components/ListMoviesComponent";

class FavoriteMoviesPage extends React.Component {
  render() {
    return (
      <>
        <NavBarComponent />
        <Container style={{ marginTop: 40 }}>
          <h2>Favorite Movies</h2>
          <Card>
            <Card.Body>
              <ListMoviesComponent path="users/favorite-list/" favoriteMovies />
            </Card.Body>
          </Card>
        </Container>
      </>
    );
  }
}

export default FavoriteMoviesPage;
