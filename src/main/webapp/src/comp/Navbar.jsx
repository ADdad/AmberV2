import React, { Component } from "react";
import SearchAppBar from "./SearchAppBar";
class Navbar extends Component {
  state = {
    tempVal: false,
      roles:[]
  };

  componentWillMount = () => {
      let tempVal = false;
      fetch("/userinfo")
          .then(response => response.json())
          .then(data => {
              this.setState({
                  tempVal: true,
                  roles : data.roles.map(role => role.name)
              });
          })
          .catch(error => {
              this.setState({ tempVal: false });
          });

  }
  render() {

    return (
      <React.Fragment>
        <SearchAppBar roles={this.state.roles} tempVal={this.state.tempVal} />
      </React.Fragment>
    );
  }
}

export default Navbar;
