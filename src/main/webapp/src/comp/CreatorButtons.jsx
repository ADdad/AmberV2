import React, { Component } from "react";
import { withRouter } from "react-router-dom";
class CreatorButtons extends Component {
  state = {
    buttons: [],
    noButtons: false
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
        localButtons.push({ value: "Edit", type: "success" });
        localButtons.push({ value: "Cancel", type: "danger" });
        break;
      }
      case "In progress": {
        localButtons.push({ value: "Cancel", type: "danger" });
        break;
      }
      case "On hold": {
        localButtons.push({ value: "Cancel", type: "danger" });
        break;
      }
      case "Delivering": {
        localButtons.push({ value: "Confirm receiption", type: "success" });
        break;
      }
      case "Confirmed": {
        localButtons.push({ value: "Return equipment", type: "success" });
        break;
      }
      default:
        this.setState({ noButtons: true });
    }
    this.setState({ buttons: localButtons });
  };

  render() {
    if (this.state.noButtons) return <h3>Order on reviewing</h3>;
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

export default withRouter(CreatorButtons);
