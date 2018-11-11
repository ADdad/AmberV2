import React, { Component } from "react";
import { withRouter } from "react-router-dom";
class ExecutorButtons extends Component {
  state = {
    buttons: [],
    noButtons: false,
    adminStates: ["Opened", "On reviewing"],
    keeperStates: ["In progress", "On hold", "Delivering"]
  };

  handleClick = name => {
    //this.props.history.push("/dashboard");
  };

  componentDidMount() {
    this.initButtons();
    console.log(this.props.status);
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
        localButtons.push({ value: "Review", type: "success" });
        break;
      }
      case "On reviewing": {
        localButtons.push({ value: "Approve", type: "success" });
        localButtons.push({ value: "Reject", type: "danger" });
        break;
      }
      case "In progress": {
        localButtons.push({ value: "Send", type: "success" });
        localButtons.push({ value: "Hold", type: "danger" });
        break;
      }
      case "On hold": {
        localButtons.push({ value: "Back to execute", type: "success" });
        break;
      }
      case "Delivering": {
        localButtons.push({ value: "Cancel delivering", type: "danger" });
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
        this.props.userRoles.includes("Admin") &&
        this.state.adminStates.includes(this.props.status)
      ) &&
      !(
        this.props.userRoles.includes("Keeper") &&
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
            onClick={this.handleClick(p.value)}
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
