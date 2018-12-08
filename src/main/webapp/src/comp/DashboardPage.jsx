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
import Button from "@material-ui/core/Button";

class DashboardPage extends Component {
  state = {
    createdChecked: [],
    executingChecked: [],
    userId: null,
    userRoles: [],
    activePage: 1,
    postStyle: false,
    itemsPerPage: 25,
    isLoading: false,
    doubleList: false,
    createdListSize: 0,
    createdRequests: [],
    executingActivePage: 1,
    executingListSize: 0,
    executingRequests: [],
    executorDoubleList: false,
    createdDoubleList: false
  };

  componentDidMount() {
    fetch("/userinfo")
      .then(response => response.json())
      .then(data => {
        console.log(data);
        let doubleListLocal = true;
        if (
          data.roles.filter(role => role.name === "ROLE_USER").length > 0 &&
          data.roles.length > 1
        )
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
        let doubleList = true;
        if (data.requestsCount < 1) doubleList = false;
        this.setState({
          createdRequests: data.requests,
          createdListSize: data.requestsCount,
          activePage: page,
          createdDoubleList: doubleList,
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
        let doubleList = true;
        if (data.requestsCount < 1) doubleList = false;
        console.log(data);
        this.setState({
          executingRequests: data.requests,
          executingListSize: data.requestsCount,
          executorDoubleList: doubleList,
          executingActivePage: page,
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
        <Checkbox
          checked={this.state.createdChecked.indexOf(request.id) !== -1}
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
        divider
        onClick={this.handleToggleExecuting(request.id)}
      >
        <Checkbox
          checked={this.state.executingChecked.indexOf(request.id) !== -1}
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

  handleToggleExecuting = value => () => {
    const { executingChecked } = this.state;
    const currentIndex = executingChecked.indexOf(value);
    const newChecked = [...executingChecked];

    if (currentIndex === -1) {
      newChecked.push(value);
    } else {
      newChecked.splice(currentIndex, 1);
    }

    this.setState({
      executingChecked: newChecked
    });
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
    console.log(this.state.createdListSize);
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

  checkDoubleList = () => {
    return this.state.executorDoubleList && this.state.createdDoubleList;
  };

  renderExecutingRequests = () => {
    console.log(this.state.executingListSize);
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

  createRequestButton = () => {
    return (
      <Button
        variant="contained"
        color="primary"
        onClick={this.handleOrderRequest}
      >
        Order items
      </Button>
    );
  };

  createCancelManyButton = () => {
    return (
      <Button
        variant="contained"
        color="primary"
        onClick={this.handleCancelManyButton}
      >
        Cancel seleced
      </Button>
    );
  };

  handleCancelManyButton = () => {
    if (this.state.createdChecked.length > 0) {
      fetch("/request/list", {
        method: "PATCH",
        body: JSON.stringify({
          status: "Canceled",
          requests: this.state.createdChecked
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
    }
  };

  createReportsButton = () => {
    return (
      <Button
        variant="contained"
        color="primary"
        onClick={this.handleReportsPage}
      >
        Reports
      </Button>
    );
  };

  handleOrderRequest = () => {
    this.props.history.push("/order/create/order");
  };

  handleReportsPage = () => {
    this.props.history.push("/reports");
  };

  render() {
    if (this.checkDoubleList() != this.state.doubleList) {
      this.setState({ doubleList: this.checkDoubleList() });
    }
    if (this.state.isLoading) {
      return <p>Loading ...</p>;
    }
    return (
      <React.Fragment>
        <div className="container-fluid">
          <div className="from-row d-flex justify-content-between">
            <div>
              {this.state.userRoles.filter(role => role.name === "ROLE_USER")
                .length > 0 && this.createRequestButton()}
              {this.state.userRoles.filter(role => role.name === "ROLE_USER")
                .length > 0 &&
                this.state.createdChecked.length > 0 &&
                this.createCancelManyButton()}
            </div>
            {(this.state.userRoles.filter(role => role.name === "ROLE_KEEPER")
              .length > 0 ||
              this.state.userRoles.filter(role => role.name === "ROLE_ADMIN")
                .length > 0) &&
              this.createReportsButton()}
          </div>
          <div className="form-row">
            {this.state.userRoles.filter(role => role.name === "ROLE_USER")
              .length > 0 &&
              this.state.createdListSize > 0 &&
              this.renderCreatedRequests()}
            {(this.state.userRoles.filter(role => role.name === "ROLE_KEEPER")
              .length > 0 ||
              this.state.userRoles.filter(role => role.name === "ROLE_ADMIN")
                .length > 0) &&
              this.state.executingListSize > 0 &&
              this.renderExecutingRequests()}
          </div>
        </div>
      </React.Fragment>
    );
  }
}

export default DashboardPage;
