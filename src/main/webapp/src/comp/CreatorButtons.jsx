import React, { Component } from "react";
import { withRouter } from "react-router-dom";
class CreatorButtons extends Component {
  state = {
    buttons: [],
    noButtons: false
  };

  handleClick = name => {
    if (name == "refund") {
      let path = "/order/create/refund/" + this.props.requestId;
      this.props.history.push(path);
      return;
    }
    if (name === "Edit") {
      let path = "/order/edit/" + this.props.requestId;
      this.props.history.push(path);
    } else {
      fetch("/request", {
        method: "PATCH",
        body: JSON.stringify({
          status: name,
          executorId: this.props.executorId,
          requestId: this.props.requestId,
          userId: null,
          commentText: null
        }),
        headers: {
          "Content-Type": "application/json"
        }
      })
        .then(response => response.json())
        .then(data => {
          console.log("Good");
        })
        .catch(error => {
          console.error("Error:", error);
        });
      this.props.history.push("/dashboard");
    }
  };

  componentDidMount() {
    
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
        localButtons.push({ value: "Edit", type: "success", status: "Edit" });
        localButtons.push({
          value: "Cancel",
          type: "danger",
          status: "Canceled"
        });
        break;
      }
      case "Rejected": {
        localButtons.push({ value: "Edit", type: "success", status: "Edit" });
        localButtons.push({
          value: "Cancel",
          type: "danger",
          status: "Canceled"
        });
        break;
      }
      case "In progress": {
        localButtons.push({
          value: "Cancel",
          type: "danger",
          status: "Canceled"
        });
        break;
      }
      case "On hold": {
        localButtons.push({
          value: "Cancel",
          type: "danger",
          status: "Canceled"
        });
        break;
      }
      case "Delivering": {
        localButtons.push({
          value: "Confirm receiption",
          type: "success",
          status: "Completed"
        });
        break;
      }
      case "Completed": {
        localButtons.push({
          value: "Return equipment",
          type: "success",
          status: "refund"
        });
        break;
      }
      default:
        this.setState({ noButtons: true });
    }
    this.setState({ buttons: localButtons });
  };

  render() {
    if (this.props.status === "Canceled") return <h3>Order canceled</h3>;
    if (this.state.noButtons) return <h3>Order on reviewing</h3>;
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

export default withRouter(CreatorButtons);
