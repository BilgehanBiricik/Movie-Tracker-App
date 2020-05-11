import React from "react";
import NavBarComponent from "../components/NavBarComponent";
import Axios from "axios";
// import ListMoviesComponent from "../components/ListMoviesComponent";

class HomePage extends React.Component {
  componentDidMount() {
    if (localStorage.getItem("token")) {
      const token = "Bearer " + localStorage.getItem("token");
      Axios.get("http://localhost:8080/users/token", {
        headers: { Authorization: token }
      })
        .then(response => {
          console.log(response.data);
        })
        .catch(err => {
          console.log(err);
          this.props.history.push("/login");
        });
    } else this.props.history.push("/login");
  }

  render() {
    return (
      <>
        <NavBarComponent />
      </>
    );
  }
}

export default HomePage;
