import React, { Component } from "react";
import Pagination from "react-js-pagination";
import { withStyles } from "@material-ui/core/styles";
import List from "@material-ui/core/List";
import ListItem from "@material-ui/core/ListItem";
import ListItemSecondaryAction from "@material-ui/core/ListItemSecondaryAction";
import ListItemText from "@material-ui/core/ListItemText";
import Checkbox from "@material-ui/core/Checkbox";
import IconButton from "@material-ui/core/IconButton";
import DeleteIcon from "@material-ui/icons/Delete";
import Visibility from "@material-ui/icons/Visibility";
import Button from "@material-ui/core/Button";
import CircularProgress from "@material-ui/core/CircularProgress";
import AsyncSelect from "react-select/lib/Async";
import { debounce } from "lodash";

class UsersRequests extends Component {
  state = {
    createdChecked: [],
    executingChecked: [],
    userId: null,
    userRoles: [],
    activePage: 1,
    itemsPerPage: 25,
    isLoading: false,
    doubleList: false,
    createdRequests: [],
    directedUserId: null
  };

  componentWillMount() {
    fetch("/userinfo")
      .then(response => response.json())
      .then(data => {
        this.setState({
          userId: data.id,
          userRoles: data.roles,
          directedUserId: this.props.match.params.userId,
          isLoading: false
        });
        this.downloadCreatedRequestsPaginated(1);
      })
      .catch(error => console.log(error));
  }

  downloadCreatedRequestsPaginated = page => {
    fetch(`/user/requests/direct/${page}?userId=${this.state.directedUserId}`, {
      method: "GET"
    })
      .then(response => response.json())
      .then(data => {
        this.setState({
          createdRequests: data.requests,
          usersListSize: data.requestsCount,
          activePage: page,
          isLoading: false
        });
      })
      .catch(error => console.log(error));
  };

  handleShowRequest = requestId => {
    this.props.history.push("/order/" + requestId);
  };

  renderCreatedRequest = request => {
    return (
      <ListItem
        key={request.id}
        role={undefined}
        dense
        button
        divider
        onClick={this.handleToggleCreated(request.id)}
      >
        <ListItemText
          className="col-md-4"
          primary={request.title.substr(0, 17)}
          secondary={request.description.substr(0, 30)}
        />
        <ListItemText primary={request.status} className="col-md-2" />
        <ListItemText
          className="col-md-2 ml-0 p-0"
          primary={request.creationDate.substr(0, 10)}
          secondary={request.creationDate.substr(11, 5).replace("T", "/")}
        />
        <ListItemText
          className="col-md-2 m-0 p-0"
          primary={request.modifiedDate.substr(0, 10)}
          secondary={request.modifiedDate.substr(11, 5).replace("T", "/")}
        />
        <ListItemText primary={"  "} />
        <ListItemSecondaryAction className="col-md-2 m-0 p-0">
          <IconButton
            aria-label="Comments"
            onClick={() => this.handleShowRequest(request.id)}
          >
            <Visibility />
          </IconButton>
        </ListItemSecondaryAction>
      </ListItem>
    );
  };

  handleToggleCreated = value => () => {
    const { createdChecked } = this.state;
    const currentIndex = createdChecked.indexOf(value);
    const newChecked = [...createdChecked];

    if (currentIndex === -1) {
      newChecked.push(value);
    } else {
      newChecked.splice(currentIndex, 1);
    }

    this.setState({
      createdChecked: newChecked
    });
  };

  renderCreatedRequests = () => {
    let createdRequests = this.state.createdRequests.map(u =>
      this.renderCreatedRequest(u)
    );
    return (
      <div
        className={
          this.state.doubleList ? "col-md-6 form-group" : "col-md-12 form-group"
        }
      >
        <h4>Created requests</h4>
        <List className="col-md-12">{createdRequests}</List>
        <div className="form-row">
          <div className="form-group mx-auto">
            <Pagination
              activePage={this.state.activePage}
              itemsCountPerPage={this.state.itemsPerPage}
              totalItemsCount={this.state.usersListSize}
              pageRangeDisplayed={5}
              onChange={this.downloadCreatedRequestsPaginated}
            />
          </div>
        </div>
      </div>
    );
  };

  renderHelloMessage = () => {
    return (
      <React.Fragment>
        <br />
        <br />
        <br />
        <br />
        <div style={{ display: "flex", justifyContent: "center" }}>
          <div>
            <h4 className="text-muted">This user still don`t have orders</h4>
          </div>
        </div>
        <div style={{ display: "flex", justifyContent: "center" }}>
          <Button
            variant="contained"
            color="primary"
            onClick={this.handleDashboard}
          >
            Dashboard
          </Button>
        </div>
      </React.Fragment>
    );
  };

  handleDashboard = () => {
    this.props.history.push("/dashboard");
  };

  renderAsyncCreatorSelect = () => {
    return (
      <div
        className={
          this.state.doubleList ? "col-md-6 form-group" : "col-md-12 form-group"
        }
      >
        <AsyncSelect
          cacheOptions
          defaultOptions={this.getRequestsOptions(this.state.createdRequests)}
          loadOptions={this.loadCreatorOptions}
          onChange={this.chosenItem}
        />
      </div>
    );
  };

  loadCreatorOptions = (input, callback) => {
    if (!input || input.length < 1) {
      return callback(this.getRequestsOptions(this.state.createdRequests));
    }
    return fetch(
      `/user/requests?search=${input}&userId=${this.state.directedUserId}`
    )
      .then(response => {
        return response.json();
      })
      .then(json => {
        let options = [];
        options = this.getRequestsOptions(json.requests);
        return callback(options);
      });
  };

  getRequestsOptions = requests => {
    let requestsLocal = requests;
    let result = [];
    requestsLocal.map(i =>
      result.push({ label: this.requestName(i), value: i.id, type: "request" })
    );
    return result;
  };

  requestName = request => {
    return (
      request.title.substr(0, 20) +
      ", " +
      request.description.substr(0, 20) +
      ", " +
      request.status
    );
  };

  chosenItem = item => {
    if (typeof item != "undefined") {
      if (item.type == "request") {
        this.props.history.push(`order/${item.value}`);
      }
      if (item.type == "user") {
        this.props.history.push(`user/${item.value}`);
      }
    }
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
    if (this.state.usersListSize == null) {
      return this.renderLoader();
    }
    return (
      <React.Fragment>
        <div className="container-fluid">
          {this.state.usersListSize < 1 && this.renderHelloMessage()}
          <div className="form-row">
            {(this.state.userRoles.filter(role => role.name === "ROLE_KEEPER")
              .length > 0 ||
              this.state.userRoles.filter(role => role.name === "ROLE_ADMIN")
                .length > 0) &&
              this.state.usersListSize > 0 &&
              this.renderAsyncCreatorSelect()}
          </div>
          <div className="form-row">
            {(this.state.userRoles.filter(role => role.name === "ROLE_KEEPER")
              .length > 0 ||
              this.state.userRoles.filter(role => role.name === "ROLE_ADMIN")
                .length > 0) &&
              this.state.usersListSize > 0 &&
              this.renderCreatedRequests()}
          </div>
        </div>
      </React.Fragment>
    );
  }
}

export default UsersRequests;
