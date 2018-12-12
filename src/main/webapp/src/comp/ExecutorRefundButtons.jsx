import React, { Component } from "react";
import { withRouter } from "react-router-dom";

class ExecutorRefundButtons extends Component {
  constructor(props) {
    super(props);
    this.state = {
      buttons: [],
      confirmation: false,
      noButtons: false,
      adminStates: ["Opened", "On reviewing"],
      keeperStates: ["Waiting for equipment"],
      comment: null,
      commentStatus: "",
      buttonChange: false
    };
  }

  handleReject = () => {
    if (this.commentValidation()) {
      fetch("/request", {
        method: "PATCH",
        body: JSON.stringify({
          status: this.state.commentStatus,
          executorId: null,
          requestId: this.props.requestId,
          userId: this.props.userId,
          commentText: this.state.comment
        }),
        headers: {
          "Content-Type": "application/json"
        }
      })
        .then(response => response.json())
        .then(data => {
          this.setState({ buttonChange: true });
          this.props.history.push("/dashboard");
          console.log(data);
        })
        .catch(error => {
          console.error("Error:", error);
        });
    }
  };

  handleClick = name => {
    if (name === "Waiting for equipment" && this.props.executorId == null) {
      console.log("worked");
      this.props.validateComment("Chose executor");
    } else {
      if (name === "Rejected") {
        this.setState({ confirmation: true, commentStatus: name });
      } else {
        this.setState({ buttonChange: true });
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
            console.log(data);
          })
          .catch(error => {
            console.error("Error:", error);
          });
        if (name !== "On reviewing") this.props.history.push("/dashboard");
        else window.location.reload();
      }
    }
  };

  componentDidMount() {
    this.initButtons();
  }

  getButtonClasses = type => {
    let classes = "form-group col-md-3 btn btn-lg btn-outline-";
    return classes + type;
  };

  commentValidation = () => {
    if (this.state.comment == null || this.state.comment.trim() == "") {
      this.props.validateComment("Comment is empty");
      return false;
    }
    return true;
  };

  initButtons = () => {
    let status = this.props.status;
    console.log("Init buttons", status);
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
          status: "Waiting for equipment"
        });
        localButtons.push({
          value: "Reject",
          type: "danger",
          status: "Rejected"
        });
        break;
      }
      case "Waiting for equipment": {
        localButtons.push({
          value: "Equipment have taken",
          type: "success",
          status: "Completed"
        });
        break;
      }
      default: {
        this.setState({ noButtons: true });
      }
    }
    this.setState({ buttons: localButtons });
  };

  handleBack = () => {
    this.setState({ confirmation: false });
    // let path = "/order/view/" + this.props.requestId;
    // this.props.history.push(path);
  };

  commentField = () => {
    let backDirection =
      this.state.commentStatus === "Rejected" ? "review" : "progress";
    return (
      <React.Fragment>
        <div className="form-row">
          <div className="form-group col-md-8">
            <label>Comment</label>
            <textarea
              className="form-control"
              id="exampleFormControlTextarea1"
              rows="5"
              onChange={p => this.setState({ comment: p.target.value })}
            />
          </div>
        </div>
        <div className="form-row">
          <button
            className={this.getButtonClasses("success")}
            onClick={() => this.handleBack()}
          >
            Back to {backDirection}
          </button>
          <button
            className={this.getButtonClasses("danger")}
            onClick={() => this.handleReject()}
          >
            {this.state.commentStatus}
          </button>
        </div>
      </React.Fragment>
    );
  };

  render() {
    if (
      (!(
        this.props.userRoles.filter(role => role === "ROLE_ADMIN").length > 0 &&
        this.state.adminStates.includes(this.props.status)
      ) &&
        !(
          this.props.userRoles.filter(role => role === "ROLE_KEEPER").length >
            0 && this.state.keeperStates.includes(this.props.status)
        )) ||
      this.props.status === "Canceled"
    )
      return <h3 />;

    if (this.state.confirmation) {
      return this.commentField();
    }

    if (this.state.noButtons) return <h3 />;
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

export default withRouter(ExecutorRefundButtons);
