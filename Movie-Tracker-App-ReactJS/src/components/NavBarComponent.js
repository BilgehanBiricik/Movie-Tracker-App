import React from "react";
import { Navbar, Nav } from "react-bootstrap";
import Axios from "axios";

class NavBarComponent extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      isAdmin: false
    };
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
  }

  handleLogoutLink = () => {
    localStorage.removeItem("token");
  };

  render() {
    return (
      <Navbar bg="dark" variant="dark" sticky="top">
        <Navbar.Brand href="/">Movie Tracker App</Navbar.Brand>
        <Nav className="mr-auto">
          <Nav.Link href="/movies">Movies</Nav.Link>
          <Nav.Link href="/directors">Directors</Nav.Link>
          <Nav.Link href="/user/watched-movies">Watched Movies</Nav.Link>
          <Nav.Link href="/user/favorite-movies">Favorite Movies</Nav.Link>
          {this.state.isAdmin && (
            <Nav.Link href="/omdb">Get Movies From IMDb</Nav.Link>
          )}
        </Nav>
        <Nav>
          <Nav.Link href="/login" onClick={this.handleLogoutLink}>
            Logout
          </Nav.Link>
        </Nav>
      </Navbar>
    );
  }
}

export default NavBarComponent;
