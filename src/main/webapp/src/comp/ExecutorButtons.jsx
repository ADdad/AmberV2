import React, { Component } from "react";
import { withRouter } from "react-router-dom";
import Popup from "react-popup";

class ExecutorButtons extends Component {
  state = {
    buttons: [],
    noButtons: false,
    adminStates: ["Opened", "On reviewing"],
    keeperStates: ["In progress", "On hold", "Delivering"]
  };

  handleClick = name => {
    console.log(name);


    fetch("/request", {
      method: "PUT",
      body: JSON.stringify({
        status: name,
        executorId: this.props.executorId,
        requestId: this.props.requestId
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

    //this.props.history.goBack();
  };

  componentDidMount() {
    console.log(this.props.userRoles);
    console.log(this.props.status);
    this.initButtons();
  }

  getButtonClasses = type => {
    let classes = "form-group col-md-3 btn btn-lg btn-outline-";
    return classes + type;
  };

  initButtons = () => {
    let status = this.props.status;
    let localButtons = [];
    switch (status) {
      case "Opened": {
        localButtons.push({
          value: "Review",
          type: "success",
          status: "On reviewing"
        });
        break;
      }
      case "On reviewing": {
        localButtons.push({
          value: "Approve",
          type: "success",
          status: "In progress"
        });
        localButtons.push({
          value: "Reject",
          type: "danger",
          status: "Rejected"
        });
        break;
      }
      case "In progress": {
        localButtons.push({
          value: "Send",
          type: "success",
          status: "Delivering"
        });
        localButtons.push({ value: "Hold", type: "danger", status: "On hold" });
        break;
      }
      case "On hold": {
        localButtons.push({
          value: "Back to execute",
          type: "success",
          status: "In progress"
        });
        break;
      }
      case "Delivering": {
        localButtons.push({
          value: "Cancel delivering",
          type: "danger",
          status: "In progress"
        });
        break;
      }
      default: {
        this.setState({ noButtons: true });
      }
    }
    this.setState({ buttons: localButtons });
  };

  render() {
    if (
      !(
        this.props.userRoles.includes("ROLE_ADMIN") &&
        this.state.adminStates.includes(this.props.status)
      ) &&
      !(
        this.props.userRoles.includes("ROLE_KEEPER") &&
        this.state.keeperStates.includes(this.props.status)
      )
    )
      return <h3>Sorry, you can just view that</h3>;

    if (this.state.noButtons) return <h3>Sorry, you can just view that</h3>;
    const buttonsLoc = this.state.buttons;
    return (
      <div className="form-row">
        {buttonsLoc.map(p => (
          <button
            className={this.getButtonClasses(p.type)}
            onClick={() => this.handleClick(p.status)}
            key={p.value}
          >
            {p.value}
          </button>
        ))}
      </div>
    );
  }
}

export default withRouter(ExecutorButtons);
