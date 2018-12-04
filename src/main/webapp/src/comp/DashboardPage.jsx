import React, { Component } from "react";
import Pagination from "react-js-pagination";
import Select from "react-select";

class DashboardPage extends Component {
  state = {
    userId: null,
    userRoles: [],
    activePage: 1,
    postStyle: false,
    itemsPerPage: 25,
    users: [],
    systemRoles: [],
    usersToUpdate: [],
    isLoading: false,
    listSize: 0
  };

  componentDidMount() {
    fetch("/userinfo")
      .then(response => response.json())
      .then(data => {
        this.setState({
          userId: data.id,
          userRoles: data.roles,
          isLoading: false
        });
      })
      .catch(error => console.log(error));
  }

  handleRemoveUser = userId => {
    this.handleUserChange(userId, []);
  };

  rolesOptions = (roles, userId) => {
    let localRoles = [...roles];
    let rolesOptions = [];
    localRoles.map(r =>
      rolesOptions.push({
        label: r.name.substr(5),
        value: r.id,
        userId: userId
      })
    );
    return rolesOptions;
  };

  updateUsers = () => {
    console.log("User to update: ", this.state.usersToUpdate);
    if (this.state.usersToUpdate.length > 0) {
      fetch("/users", {
        method: "POST",
        body: JSON.stringify({
          users: this.state.usersToUpdate
        }),
        headers: {
          "Content-Type": "application/json"
        }
      })
        .then(r => r.json())
        .then(d => {
          console.log(d);
          this.setState({ usersToUpdate: [], postStyle: true });
        })
        .catch(error => console.log("error.....", error));
    }
  };

  handleUserChange = (userId, userData) => {
    let usersToUpdateLocal = [...this.state.usersToUpdate];
    let index = usersToUpdateLocal.findIndex(p => p.userId == userId);
    let rolesId = [];
    userData.map(d => rolesId.push(d.value));
    if (index == -1) {
      usersToUpdateLocal.push({ userId: userId, roles: rolesId });
    } else {
      usersToUpdateLocal[index] = {
        userId: userId,
        roles: rolesId
      };
    }
    this.setState({ usersToUpdate: usersToUpdateLocal });
    console.log(this.state.usersToUpdate);
  };

  renderRequest = request => {
    request = {
      title: "MyTitle",
      description:
        "Mdlasjlkadjalskjdlkasjdlkasjdlkasjdlkjdlaksjdklasjdlkasjdlkas"
    };
    return (
      <div className={"form-row border rounded m-2 col-md-" + this.state.colum}>
        <p>{request.title + " " + request.description.substr(0, 50)}</p>
        <div className="form-group col-md-2 mt-auto">
          <button
            onClick={() => this.handleRemoveUser(user.id)}
            className="form-group btn btn-lg btn-outline-danger"
          >
            Remove
          </button>
        </div>
      </div>
    );
  };

  renderUser = user => {
    return (
      <div className="form-row border rounded m-2 col-md-12">
        <p>
          {user.firstName} {user.secondName}
          {","} {user.email}
        </p>
        <div className="form-row col-md-10 mt-auto">
          <Select
            defaultValue={this.rolesOptions(user.roles, user.id)}
            isMulti
            name={user.id}
            options={this.rolesOptions(this.state.systemRoles, user.id)}
            className="basic-multi-select form-group"
            classNamePrefix="select"
            onChange={e => this.handleUserChange(user.id, e)}
          />
        </div>
        <div className="form-group col-md-2 mt-auto">
          <button
            onClick={() => this.handleRemoveUser(user.id)}
            className="form-group btn btn-lg btn-outline-danger"
          >
            Remove
          </button>
        </div>
      </div>
    );
  };

  handlePageChange = pageNumber => {
    const convertNumber = pageNumber - 1;
    fetch(`/users/${convertNumber}`, {
      method: "GET"
    })
      .then(response => response.json())
      .then(data => {
        console.log(data);
        this.setState({
          activePage: pageNumber,
          users: data.list
        });
      })
      .catch(error => console.log(error));
  };

  render() {
    let requests = this.state.request.map(u => this.renderRequest(u));
    if (this.state.isLoading) {
      return <p>Loading ...</p>;
    }
    return (
      <React.Fragment>
        <br />
        <br />
        <br />
        <div className="col-md-12">
          {requests}
          <div className="form-row">
            <div className="form-group mx-auto">
              <Pagination
                activePage={this.state.activePage}
                itemsCountPerPage={this.state.itemsPerPage}
                totalItemsCount={this.state.listSize}
                pageRangeDisplayed={5}
                onChange={this.handlePageChange}
              />
            </div>
          </div>
        </div>
      </React.Fragment>
    );
  }
}

export default DashboardPage;
