import React from "react";
import Axios from "axios";
import {
  Container,
  Card,
  CardDeck,
  ButtonGroup,
  Button
} from "react-bootstrap";
import DirectorDetailsComponent from "./DirectorDetailsComponent";

class ListDirectorsComponent extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      directors: [],
      director: {},
      isAdmin: false,
      showModal: false
    };
  }

  getDirectors(path) {
    const token = "Bearer " + localStorage.getItem("token");
    Axios.get("http://localhost:8080/" + path, {
      headers: { Authorization: token }
    })
      .then(response => {
        console.log(response.data);
        if (response.data.message) alert(response.data.message);
        else this.setState({ directors: response.data });
      })
      .catch(err => {
        console.log(err);
      });
  }

  componentDidUpdate(prevProps, prevState) {
    if (prevProps.path !== this.props.path) {
      console.log("did update:" + this.props.path);
      if (this.props.path !== "") this.getDirectors(this.props.path);
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
    this.getDirectors(this.props.path);
  }

  handleEditButtonClick = event => {
    const token = "Bearer " + localStorage.getItem("token");
    Axios.get("http://localhost:8080/directors/" + event.target.value, {
      headers: { Authorization: token }
    })
      .then(response => {
        console.log(response.data);
        this.setState({ director: response.data }, () => {
          this.setState({ showModal: true });
        });
      })
      .catch(err => {
        console.log(err);
      });
  };

  handleDeleteButtonClick = event => {
    const token = "Bearer " + localStorage.getItem("token");
    const directorId = event.target.value;
    const director = this.state.directors.find(
      m => m.id === parseInt(directorId)
    );
    Axios.delete("http://localhost:8080/directors/" + directorId, {
      headers: { Authorization: token }
    })
      .then(response => {
        console.log(response.data.message);
        this.setState({
          directors: this.state.directors.filter(item => item !== director)
        });
      })
      .catch(err => {
        console.log(err);
      });
  };

  handleCloseModal = event => {
    this.getDirectors("directors/");
    if (this.props.showAddDirectorModal) this.props.hideModal();
    this.setState({ director: {} }, () => {
      this.setState({ showModal: false });
    });
  };

  render() {
    return (
      <Container>
        <CardDeck style={{ justifyContent: "space-evenly" }}>
          {this.state.directors.map(director => (
            <Card
              key={director.id}
              style={{
                marginBottom: 30
              }}
            >
              <Card.Header>
                <Card.Title>
                  <strong>
                    {director.name} {director.surname}
                  </strong>
                </Card.Title>
              </Card.Header>
              <Card.Body>
                <Card.Text>
                  <strong>Birth Date: </strong>
                  {director.birthDate} <br />
                  <strong>Birth Place: </strong>
                  {director.birthPlace} <br />
                </Card.Text>
                <Button
                  style={{
                    marginLeft: "auto",
                    marginRight: "auto",
                    display: "table"
                  }}
                  variant="primary"
                  onClick={this.handleGetDirectorMoviesButtonClick}
                >
                  Get Director Movies
                </Button>
                {this.state.isAdmin && (
                  <ButtonGroup
                    style={{
                      marginTop: 10,
                      marginLeft: "auto",
                      marginRight: "auto",
                      display: "table"
                    }}
                  >
                    <Button
                      value={director.id}
                      variant="warning"
                      onClick={this.handleEditButtonClick}
                    >
                      Edit
                    </Button>
                    <Button
                      value={director.id}
                      variant="danger"
                      onClick={this.handleDeleteButtonClick}
                    >
                      Delete
                    </Button>
                  </ButtonGroup>
                )}
              </Card.Body>
            </Card>
          ))}
        </CardDeck>
        {(this.state.showModal || this.props.showAddDirectorModal) && (
          <DirectorDetailsComponent
            director={this.state.director}
            hideModal={this.handleCloseModal}
          />
        )}
      </Container>
    );
  }
}

export default ListDirectorsComponent;
