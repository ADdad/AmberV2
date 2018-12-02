import React, { Component } from "react";
import Pagination from "react-js-pagination";
import Select from "react-select";

class AdminPageNew extends Component {
  state = {
    userId: null,
    userRoles: [],
    activePage: 1,
    itemsPerPage: 25,
    users: [
      {
        id: 21,
        firstName: "User",
        secondName: "Nouser",
        email: "Sobaka@jkdl.cm",
        roles: [
          { id: "1", name: "ROLE_ADMIN" },
          { id: "2", name: "ROLE_KEEPER" }
        ]
      },
      {
        id: 22,
        firstName: "User2",
        secondName: "Nouser2",
        email: "Sobaka@jkdl.cm2",
        roles: [{ id: "2", name: "ROLE_KEEPER" }]
      }
    ],
    systemRoles: [
      { id: "1", name: "ROLE_ADMIN" },
      { id: "2", name: "ROLE_KEEPER" }
    ],
    usersToUpdate: [],
    isLoading: false
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
    this.handlePageChange(1);
  }

  rolesOptions = roles => {
    let localRoles = [...roles];
    let rolesOptions = [];
    localRoles.map(r =>
      rolesOptions.push({ label: r.name.substr(5), value: r.id })
    );
    return rolesOptions;
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
    if (this.state.usersToUpdate.length > 0) {
      fetch("/users", {
        method: "POST",
        body: JSON.stringify({
          users: this.state.usersToUpdate
        }),
        headers: {
          "Content-Type": "application/json"
        }
      }).then(r => r.json())
          .then(d => console.log(d))
          .catch(error => console.log("error.....", error))
    }
  };

  handleUserChange = (selectArg, userData) => {
    let usersToUpdateLocal = [...this.state.usersToUpdate];
    let index = usersToUpdateLocal.findIndex(p => p.userId == selectArg);
    let rolesId = [];
    userData.map(d => rolesId.push(d.value));
    if (index == -1) {
      usersToUpdateLocal.push({ userId: selectArg, roles: rolesId });
    } else {
      usersToUpdateLocal[index] = {
        userId: selectArg,
        roles: rolesId
      };
    }
    this.setState({ usersToUpdate: usersToUpdateLocal });
    console.log(this.state.usersToUpdate);
  };

  renderUser = user => {
    return (
      <div className="form-row border rounded m-2 col-md-12">
        <p>
          {user.firstName} {user.secondName}
          {","} {user.email}
        </p>
        <div className="form-group col-md-10 mt-auto">
          <Select
            defaultValue={this.rolesOptions(user.roles, user.id)}
            isMulti
            name={user.id}
            options={this.rolesOptions(this.state.systemRoles, user.id)}
            className="basic-multi-select form-group"
            classNamePrefix="select"
            onChange={e => this.handleUserChange(user.id, e)}
            //onChange={this.handleUserChange}
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
          users: data.users,
          systemRoles: data.systemRoles
        });
      })
      .catch(error => console.log(error));
  };

  render() {
    let user = this.state.users.map(u => this.renderUser(u));
    if (this.state.isLoading) {
      return <p>Loading ...</p>;
    }
    return (
      <React.Fragment>
          <br/>
        <br/>
        <br/>
          <button className="btn btn-outline-success m-2 "
                  onClick={this.updateUsers} disabled>
              POST
          </button>
        <div className="col-md-6">
          {user}
          <div className="form-row">
            <div className="form-group mx-auto">
              <Pagination
                activePage={this.state.activePage}
                itemsCountPerPage={this.state.itemsPerPage}
                totalItemsCount={75}
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

export default AdminPageNew;
