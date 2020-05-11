import React from "react";
import NavBarComponent from "../components/NavBarComponent";
import { Container, Row, Card, Col, Button } from "react-bootstrap";
import ListDirectorsComponent from "../components/ListDirectorsComponent";
import SearchComponent from "../components/SearchComponent";
import Axios from "axios";

class DirectorsPage extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      isAdmin: false,
      showAddDirectorModal: false,
      searchInputText: "",
      path: "directors/"
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
          if (response.data.role === "[ROLE_ADMIN]")
            this.setState({ isAdmin: true });
        })
        .catch(err => {
          console.log(err);
          this.props.history.push("/login");
        });
    } else this.props.history.push("/login");
  }

  handleAddButtonClick = event => {
    this.setState({ showAddDirectorModal: true });
  };

  handleCloseModal = event => {
    this.setState({ path: "" });
    this.setState({ showAddDirectorModal: false });
  };

  handleSearchInputChange = event => {
    this.setState({ [event.target.name]: event.target.value });
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
                    searchText="Director"
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
                      Add Director
                    </Button>
                  </Col>
                )}
              </Row>
            </Card.Body>
          </Card>
          <Container>
            <ListDirectorsComponent
              showAddDirectorModal={this.state.showAddDirectorModal}
              hideModal={this.handleCloseModal}
              path={this.state.path}
            />
          </Container>
        </Container>
      </>
    );
  }
}

export default DirectorsPage;
