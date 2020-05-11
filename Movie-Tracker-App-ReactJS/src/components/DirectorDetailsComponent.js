import React from "react";
import Axios from "axios";
import { Modal, Form, Row, Col, Button } from "react-bootstrap";

class DirectorDetailsComponent extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      director: {
        id: "",
        name: "",
        surname: "",
        birthDate: "",
        birthPlace: ""
      }
    };
  }

  componentDidMount() {
    if (this.props.director !== undefined)
      this.setState({ director: this.props.director });
  }

  handleInputChange = event => {
    event.persist();
    this.setState(prevState => ({
      director: {
        ...prevState.director,
        [event.target.name]: event.target.value
      }
    }));
  };

  handleSaveButtonClick = event => {
    const token = "Bearer " + localStorage.getItem("token");
    if (this.props.director.id !== undefined)
      Axios.put(
        "http://localhost:8080/directors/" + this.state.director.id,
        this.state.director,
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
    else
      Axios.post("http://localhost:8080/directors", this.state.director, {
        headers: { Authorization: token }
      })
        .then(response => {
          console.log(response.data.message);
          this.props.hideModal();
        })
        .catch(err => {
          console.log(err);
        });
  };

  render() {
    return (
      <Modal show={true} onHide={this.props.hideModal}>
        <Modal.Header closeButton>
          <Modal.Title>Director</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          <Form>
            <Form.Group as={Row}>
              <Form.Label column sm={4}>
                Name:{" "}
              </Form.Label>
              <Col sm={8}>
                <Form.Control
                  type="text"
                  name="name"
                  defaultValue={this.state.director.name}
                  onChange={this.handleInputChange}
                />
              </Col>
            </Form.Group>
            <Form.Group as={Row}>
              <Form.Label column sm={4}>
                Surname:{" "}
              </Form.Label>
              <Col sm={8}>
                <Form.Control
                  type="text"
                  name="surname"
                  defaultValue={this.state.director.surname}
                  onChange={this.handleInputChange}
                />
              </Col>
            </Form.Group>
            <Form.Group as={Row}>
              <Form.Label column sm={4}>
                Birth Place:{" "}
              </Form.Label>
              <Col sm={8}>
                <Form.Control
                  type="text"
                  name="birthPlace"
                  defaultValue={this.state.director.birthPlace}
                  onChange={this.handleInputChange}
                />
              </Col>
            </Form.Group>
            <Form.Group as={Row}>
              <Form.Label column sm={4}>
                Birth Date:{" "}
              </Form.Label>
              <Col sm={8}>
                <Form.Control
                  type="date"
                  name="birthDate"
                  defaultValue={this.state.director.birthDate}
                  onChange={this.handleInputChange}
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

export default DirectorDetailsComponent;
