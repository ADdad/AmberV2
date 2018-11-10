import React, { Component } from "react";
import { withRouter } from "react-router-dom";
class ExecutorButtons extends Component {
  state = {
    buttons: []
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
      case "On reviewing": {
        localButtons.push({ value: "Cancel delivering", type: "danger" });
        break;
      }
    }
    this.setState({ buttons: localButtons });
  };

  render() {
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
        ;
      </div>
    );
  }
}

export default withRouter(ExecutorButtons);
