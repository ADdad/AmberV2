import React, { Component } from "react";
import ReactPaginate from "react-paginate";
class AdminPage extends Component {
  state = {
    userId: null,
    userEmail: null,
    userFirstName: null,
    userSecondName: null,
    userRoles: [],
    isLoading: true
  };

  handleLogout = () => {
    fetch("/logout")
      .then(response => document.location.reload())
      .catch(error => console.log(error));
  };

  componentDidMount() {
    fetch("/userinfo")
      .then(response => response.json())
      .then(data => {
        this.setState({
          userId: data.id,
          userEmail: data.email,
          userFirstName: data.firstName,
          userSecondName: data.secondName,
          userRoles: data.roles,
          isLoading: false
        });
      })
      .catch(error => console.log(error));
  }

  render() {
    if (this.state.isLoading) {
      return <p>Loading ...</p>;
    }
    return (
      <React.Fragment>
        <br />
        <br />
        <br />
        <br />

        <div className="container">
          <button
            className="btn btn-outline-danger m-2 logout-btn"
            onClick={this.handleLogout}
          >
            Logout
          </button>
          <h1>Welcome to your dashboard</h1>
          User id:
          <br />
          <label>{this.state.userId}</label>
          <br />
          User email:
          <br />
          <label>{this.state.userEmail}</label>
          <br />
          User name:
          <br />
          <label>{this.state.userFirstName}</label>
          <br />
          User surname:
          <br />
          <label>{this.state.userSecondName}</label>
          <br />
          User`s roles:
          <br />
          {this.state.userRoles.map(function(role, index) {
            return <label key={index}>{role + " "}</label>;
          })}
        </div>
      </React.Fragment>
    );
  }
}

export default AdminPage;
