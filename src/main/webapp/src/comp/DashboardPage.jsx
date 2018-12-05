import React, { Component } from "react";
import Pagination from "react-js-pagination";
import PropTypes from "prop-types";
import { withStyles } from "@material-ui/core/styles";
import List from "@material-ui/core/List";
import ListItem from "@material-ui/core/ListItem";
import ListItemSecondaryAction from "@material-ui/core/ListItemSecondaryAction";
import ListItemText from "@material-ui/core/ListItemText";
import Checkbox from "@material-ui/core/Checkbox";
import IconButton from "@material-ui/core/IconButton";
import DeleteIcon from "@material-ui/icons/Delete";
import Visibility from "@material-ui/icons/Visibility";
import ListSubheader from "@material-ui/core/ListSubheader";

class DashboardPage extends Component {
  state = {
    checked: [],
    userId: null,
    userRoles: ["ROLE_KEEPER"],
    activePage: 1,
    postStyle: false,
    itemsPerPage: 25,
    isLoading: false,
    doubleList: false,
    createdListSize: 0,
    createdRequests: [],
    executingActivePage: 1,
    executingListSize: 0,
    executingRequests: []
  };

  componentDidMount() {
    fetch("/userinfo")
      .then(response => response.json())
      .then(data => {
        let doubleListLocal = true;
        if (data.roles.includes("ROLE_USER") && data.roles.length > 1)
          doubleListLocal = true;
        else doubleListLocal = false;
        this.setState({
          userId: data.id,
          userRoles: data.roles,
          doubleList: doubleListLocal,
          isLoading: false
        });
        this.downloadCreatedRequestsPaginated(1);
        this.downloadExecutingRequestsPaginated(1);
      })
      .catch(error => console.log(error));
  }

  downloadCreatedRequestsPaginated = page => {
    fetch(`/user/requests/created/${page}`, {
      method: "GET"
    })
      .then(response => response.json())
      .then(data => {
        console.log(data);
        this.setState({
          createdRequests: data.requests,
          createdListSize: data.requestsCount,
          isLoading: false
        });
      })
      .catch(error => console.log(error));
  };

  downloadExecutingRequestsPaginated = page => {
    fetch(`/user/requests/executing/${page}`, {
      method: "GET"
    })
      .then(response => response.json())
      .then(data => {
        console.log(data);
        this.setState({
          executingRequests: data.requests,
          executingListSize: data.requestsCount,
          isLoading: false
        });
      })
      .catch(error => console.log(error));
  };

  handleRemoveRequest = requestId => {
    fetch("/request", {
      method: "PATCH",
      body: JSON.stringify({
        status: "Canceled",
        requestId: requestId,
        userId: null,
        commentText: null
      }),
      headers: {
        "Content-Type": "application/json"
      }
    })
      .then(response => response.json())
      .then(data => {
        this.downloadCreatedRequestsPaginated(this.state.activePage);
        console.log(data);
      })
      .catch(error => {
        console.error("Error:", error);
      });
  };

  handleShowRequest = requestId => {
    this.props.history.push("order/" + requestId);
  };

  renderCreatedRequest = request => {
    return (
      <ListItem
        key={request.id}
        role={undefined}
        dense
        button
        onClick={this.handleToggle(request.id)}
      >
        <Checkbox
          checked={this.state.checked.indexOf(request.id) !== -1}
          tabIndex={-1}
          disableRipple
        />
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
          <IconButton
            aria-label="Comments"
            onClick={() => this.handleRemoveRequest(request.id)}
          >
            <DeleteIcon />
          </IconButton>
        </ListItemSecondaryAction>
      </ListItem>
    );
  };

  renderExecutingRequest = request => {
    return (
      <ListItem
        key={request.id}
        role={undefined}
        dense
        button
        onClick={this.handleToggle(request.id)}
      >
        <Checkbox
          checked={this.state.checked.indexOf(request.id) !== -1}
          tabIndex={-1}
          disableRipple
        />
        <ListItemText
          className="col-md-4"
          primary={request.title.substr(0, 17)}
          secondary={request.description.substr(0, 30)}
        />
        <ListItemText primary={request.typeId} className="col-md-2" />
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

  handleToggle = value => () => {
    const { checked } = this.state;
    const currentIndex = checked.indexOf(value);
    const newChecked = [...checked];

    if (currentIndex === -1) {
      newChecked.push(value);
    } else {
      newChecked.splice(currentIndex, 1);
    }

    this.setState({
      checked: newChecked
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
        <List className="col-md-12">{createdRequests}</List>
        <div className="form-row">
          <div className="form-group mx-auto">
            <Pagination
              activePage={this.state.activePage}
              itemsCountPerPage={this.state.itemsPerPage}
              totalItemsCount={this.state.createdListSize}
              pageRangeDisplayed={5}
              onChange={this.downloadCreatedRequestsPaginated}
            />
          </div>
        </div>
      </div>
    );
  };

  renderExecutingRequests = () => {
    let executingRequests = this.state.executingRequests.map(u =>
      this.renderExecutingRequest(u)
    );
    return (
      <div
        className={
          this.state.doubleList ? "col-md-6 form-group" : "col-md-12 form-group"
        }
      >
        <List className="col-md-12">{executingRequests}</List>
        <div className="form-row">
          <div className="form-group mx-auto">
            <Pagination
              activePage={this.state.executingActivePage}
              itemsCountPerPage={this.state.itemsPerPage}
              totalItemsCount={this.state.executingListSize}
              pageRangeDisplayed={5}
              onChange={this.downloadExecutingRequestsPaginated}
            />
          </div>
        </div>
      </div>
    );
  };

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
        <div className="container-fluid">
          <div className="form-row">
            {this.state.userRoles.includes("ROLE_USER") &&
              this.renderCreatedRequests()}
            {(this.state.userRoles.includes("ROLE_ADMIN") ||
              this.state.userRoles.includes("ROLE_KEEPER")) &&
              this.renderExecutingRequests()}
          </div>
        </div>
      </React.Fragment>
    );
  }
}

export default DashboardPage;
