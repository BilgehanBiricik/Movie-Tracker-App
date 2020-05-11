import React from "react";
import Axios from "axios";
import { Form, Button, Container, Alert } from "react-bootstrap";

class LoginPage extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      username: "",
      password: "",
      alertBox: false
    };
  }

  handleInputChange = event => {
    this.setState({ [event.target.name]: event.target.value });
  };

  handleFormSubmit = event => {
    const username = this.state.username;
    const password = this.state.password;
    Axios.post("http://localhost:8080/authenticate", { username, password })
      .then(response => {
        console.log("token: " + response.data.jwtToken);
        localStorage.setItem("token", response.data.jwtToken);
        this.setState({ alertBox: false });
        this.props.history.push("/home");
      })
      .catch(err => {
        console.log(err);
        this.setState({ alertBox: true });
      });
    event.preventDefault();
  };

  render() {
    return (
      <Container style={{ width: 500, marginTop: 100 }}>
        <Form onSubmit={this.handleFormSubmit}>
          <Form.Group>
            <Form.Label>Username</Form.Label>
            <Form.Control
              name="username"
              type="text"
              placeholder="Enter Username"
              onChange={this.handleInputChange}
            />
          </Form.Group>
          <Form.Group>
            <Form.Label>Password</Form.Label>
            <Form.Control
              name="password"
              type="password"
              placeholder="Enter Password"
              onChange={this.handleInputChange}
            />
          </Form.Group>
          <Button variant="primary" type="submit">
            Login
          </Button>
        </Form>
        <Alert
          show={this.state.alertBox}
          style={{ marginTop: 10 }}
          variant="danger"
        >
          Username or password is invalid.
        </Alert>
      </Container>
    );
  }
}

export default LoginPage;
