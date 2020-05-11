import React from "react";
import { BrowserRouter, Route, Switch } from "react-router-dom";
import LoginPage from "./pages/LoginPage";
import HomePage from "./pages/HomePage";
import MoviesPage from "./pages/MoviesPage";
import FavoriteMoviesPage from "./pages/FavoriteMoviesPage";
import WatchedMoviesPage from "./pages/WatchedMoviesPage";
import DirectorsPage from "./pages/DirectorsPage";
import OmdbPage from "./pages/OmdbPage";

class MovieTrackerAppRouter extends React.Component {
  render() {
    return (
      <BrowserRouter>
        <Switch>
          <Route exact path="/login" component={LoginPage} />
          <Route exact path="/home" component={HomePage} />
          <Route exact path="/" component={HomePage} />
          <Route exact path="/movies" component={MoviesPage} />
          <Route exact path="/directors" component={DirectorsPage} />
          <Route exact path="/omdb" component={OmdbPage} />
          <Route
            exact
            path="/user/watched-movies"
            component={WatchedMoviesPage}
          />
          <Route
            exact
            path="/user/favorite-movies"
            component={FavoriteMoviesPage}
          />
          <Route render={() => <h2>404 Not Found</h2>} />
        </Switch>
      </BrowserRouter>
    );
  }
}

export default MovieTrackerAppRouter;
