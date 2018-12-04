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
    userRoles: [],
    activePage: 1,
    postStyle: false,
    itemsPerPage: 25,
    users: [],
    systemRoles: [],
    usersToUpdate: [],
    isLoading: false,
    listSize: 0,
    requests: [
      {
        id: "jiodj",
        title: "MyTItle",
        description: "MyDeklsjkldjl",
        status: "Status",
        type: "TYpe"
      }
    ]
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
        console.log(data);
      })
      .catch(error => {
        console.error("Error:", error);
      });
  };

  handleShowRequest = requestId => {
    this.props.history.push("order/" + requestId);
  };

  renderRequest = request => {
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
        <ListItemText primary={request.title} />
        <ListItemText primary={request.description.substr(0, 30)} />
        <ListItemSecondaryAction>
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

  render() {
    let requests = this.state.requests.map(u => this.renderRequest(u));
    if (this.state.isLoading) {
      return <p>Loading ...</p>;
    }

    return <List className="col-md-6">{requests}</List>;

    // return (
    //   <React.Fragment>
    //     <br />
    //     <br />
    //     <MyList />
    //     <br />
    //     <div className="col-md-12">
    //       {/* {requests} */}
    //       <div className="form-row">
    //         <div className="form-group mx-auto">
    //           <Pagination
    //             activePage={this.state.activePage}
    //             itemsCountPerPage={this.state.itemsPerPage}
    //             totalItemsCount={this.state.listSize}
    //             pageRangeDisplayed={5}
    //             onChange={this.handlePageChange}
    //           />
    //         </div>
    //       </div>
    //     </div>
    //   </React.Fragment>
    // );
  }
}

export default DashboardPage;
