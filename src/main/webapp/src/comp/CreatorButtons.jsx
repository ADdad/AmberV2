import React, { Component } from "react";
import { withRouter } from "react-router-dom";
class CreatorButtons extends Component {
  state = {
    buttons: [],
    noButtons: false
  };

  handleClick = name => {
    console.log(name);
    if (name === "Edit") {
      let path = "/order/edit/" + this.props.requestId;
      this.props.history.push(path);
    } else {
      fetch("/request", {
        method: "PUT",
        body: JSON.stringify({
          status: name,
          executorId: null,
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

      this.props.history.push("/dashboard");
    }
  };

  componentDidMount() {
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
        localButtons.push({ value: "Return equipment", type: "success" });
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
