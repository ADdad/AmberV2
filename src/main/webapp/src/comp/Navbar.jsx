import React, { Component } from "react";
import SearchAppBar from "./SearchAppBar";
import { withRouter } from 'react-router-dom';
class Navbar extends Component {
    constructor(props) {
        super(props);
        this.state = {
            tempVal : false,
            roles : []
        };
        rerenderNav = rerenderNav.bind(this);
    }

    componentWillMount() {
        rerenderNav()
    }


  render() {

    return (
      <React.Fragment>
        <SearchAppBar roles={this.state.roles} tempVal={this.state.tempVal} />
      </React.Fragment>
    );
  }
}

export function rerenderNav() {
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

export default Navbar;
