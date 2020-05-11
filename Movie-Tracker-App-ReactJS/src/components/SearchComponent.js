import React from "react";
import { Form, FormControl, Button } from "react-bootstrap";

class SearchComponent extends React.Component {
  render() {
    return (
      <Form inline>
        <FormControl
          type="text"
          name="searchText"
          placeholder={"Search " + this.props.searchText}
          className="mr-sm-2"
          onChange={this.props.handleSearchChange}
        />
        <Button
          variant="outline-success"
          onClick={this.props.handleSearchClick}
        >
          Search
        </Button>
      </Form>
    );
  }
}

export default SearchComponent;
