import React, { Component } from "react";
import Pagination from "react-js-pagination";
import Select from "react-select";
import Button from "@material-ui/core/Button";
import CircularProgress from "@material-ui/core/CircularProgress";
import AsyncSelect from "react-select/lib/Async";

class AdminPageNew extends Component {
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
          userRoles: data.roles.map(role => role.name),
          isLoading: false
        });
        if (data.roles.filter(u => u.name == "ROLE_ADMIN").length < 1) {
          this.props.history.push("/errorpage");
        }
      })
      .catch(error => console.log(error));
    fetch(`/users/data`, {
      method: "GET"
    })
      .then(response => response.json())
      .then(data => {
        this.setState({
          systemRoles: data.systemRoles,
          listSize: data.usersCount
        });
      })
      .catch(error => console.log(error));
    this.handlePageChange(1);
  }

  handleRemoveUser = userId => {
    this.handleUserChange(userId, []);
  };

  renderAsyncAdminSelect = () => {
    return (
      <div className="form-row">
        <div className="col-md-12 form-group">
          <AsyncSelect
            cacheOptions
            defaultOptions={this.getUsersOptions(this.state.users)}
            loadOptions={this.loadUsersOptions}
            onChange={this.chosenItem}
          />
        </div>
      </div>
    );
  };

  loadUsersOptions = (input, callback) => {
    if (!input || input.length < 1) {
      return callback(this.getUsersOptions(this.state.users));
    }
    return fetch(`/users?search=${input}`)
      .then(response => {
        return response.json();
      })
      .then(json => {
        let res = this.getUsersOptions(json.list);
        return callback(res);
      });
  };

  getUsersOptions = users => {
    let usersLocal = users;
    let result = [];
    usersLocal.map(i =>
      result.push({ label: this.userName(i), value: i.id, type: "user" })
    );
    return result;
  };

  userName = user => {
    return (
      user.email.substr(0, 20) +
      ", " +
      user.firstName.substr(0, 20) +
      " " +
      user.secondName.substr(0, 20)
    );
  };

  chosenItem = item => {
    if (typeof item != "undefined") {
      if (item.type == "user") {
        this.props.history.push(`user/${item.value}`);
      }
    }
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
        method: "PATCH",
        body: JSON.stringify({
          users: this.state.usersToUpdate
        }),
        headers: {
          "Content-Type": "application/json"
        }
      })
        .then(r => r.json())
        .then(d => {
          this.setState({ usersToUpdate: [] });
        })
        .catch(error => {
          console.log("error.....", error);
          this.setState({ usersToUpdate: [] });
        });
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
  };

  renderEnableButton = userId => {
    return (
      <Button
        variant="contained"
        color="secondary"
        onClick={() => this.handleEnableUser(userId)}
        className="form-group m-2 col-md-2"
      >
        Enable user
      </Button>
    );
  };

  handleEnableUser = userId => {
    fetch(`/users/enable?userId=${userId}&value=true`, {
      method: "GET",
      headers: {
        "Content-Type": "application/json"
      }
    })
      .then(r => r.json())
      .then(d => {
        this.updateUsers();
      })
      .catch(error => {
        this.updateUsers();
        console.log("error.....", error);
      });
  };

  renderUser = user => {
    return (
      <div className="form-row border rounded m-2 col-md-12">
        <div className="form-group m-2 col-md-4">
          <p>
            {user.firstName} {user.secondName}
            {","} {user.email}
          </p>
        </div>
        <div className="form-group col-md-6 mt-auto">
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
        {user.enabled == 0 && this.renderEnableButton(user.id)}
      </div>
    );
  };

  handlePageChange = pageNumber => {
    const convertNumber = pageNumber - 1;
    fetch(`/users/page/${convertNumber}`, {
      method: "GET"
    })
      .then(response => response.json())
      .then(data => {
      
        this.setState({
          activePage: pageNumber,
          users: data.list
        });
      })
      .catch(error => console.log(error));
  };

  renderLoader = () => {
    return (
      <React.Fragment>
        <br />
        <br />
        <br />
        <br />
        <div style={{ display: "flex", justifyContent: "center" }}>
          <CircularProgress />
        </div>
      </React.Fragment>
    );
  };

  render() {
    let user = this.state.users.map(u => this.renderUser(u));
    if (this.state.systemRoles.length < 1) {
      return this.renderLoader();
    }
    return (
      <React.Fragment>
        <div className="col-md-12">
          {this.renderAsyncAdminSelect()}
          <div className="form-row">
            <button
              className={
                this.state.postStyle
                  ? "btn btn-outline-success m-2 disabled form-group mx-auto"
                  : "btn btn-outline-success m-2 form-group mx-auto"
              }
              onClick={this.updateUsers}
            >
              Save changes
            </button>
          </div>
          {user}
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

export default AdminPageNew;
