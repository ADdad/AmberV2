import React, { Component } from "react";
import SearchAppBar from "./SearchAppBar";
import { withRouter } from 'react-router-dom';
class Navbar extends Component {
    constructor(props) {
        super(props);
        this.state = {
            tempVal : false,
            roles : [],
            userMail : "",
            userName : ""
        };
        rerenderNav = rerenderNav.bind(this);
    }

    componentWillMount() {
        rerenderNav()
    }


  render() {

    return (
      <React.Fragment>
        <SearchAppBar userMail={this.state.userMail} userName={this.state.userName} roles={this.state.roles}
                      tempVal={this.state.tempVal} />
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
                roles : data.roles.map(role => role.name),
                userMail : data.email,
                userName : data.firstName
            });
        })
        .catch(error => {
            this.setState({ tempVal: false });
        });
}

export default Navbar;
