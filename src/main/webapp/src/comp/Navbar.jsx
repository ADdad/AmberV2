import React, { Component } from "react";
import SearchAppBar from "./SearchAppBar";
class Navbar extends Component {
  state = {
    tempVal: false
  };

  componentWillMount = () => {
      let tempVal = false;
      fetch("/userinfo")
          .then(response => response.json())
          .then(data => {
              this.setState({ tempVal: true });
          })
          .catch(error => {
              this.setState({ tempVal: false });
          });

  }
  render() {

    return (
      <React.Fragment>
        <SearchAppBar tempVal={this.state.tempVal} />
      </React.Fragment>
    );
  }
}

export default Navbar;
