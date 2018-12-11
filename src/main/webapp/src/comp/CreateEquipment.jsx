import React, { Component } from "react";

class CreateEquipment extends Component {
  constructor(props) {
    super(props);
    this.state = {
      userRoles: [],
      isLoading: false,
      alert: "",
      model: "",
      producer: "",
      country: ""
    };
  }

  componentWillMount() {
    fetch("/userinfo")
      .then(response => response.json())
      .then(data => {
        this.setState({
          userRoles: data.roles.map(role => role.name),
          isLoading: false
        });
        if (!this.state.userRoles.includes("ROLE_ADMIN"))
          this.props.history.push("/errorpage");
      })
      .catch(error => console.log(error));
  }

  handleSubmit = () => {
    if (this.validate()) {
      fetch("/equipment", {
        method: "POST",
        body: JSON.stringify({
          id: null,
          model: this.state.model,
          producer: this.state.producer,
          country: this.state.country
        }),
        headers: {
          "Content-Type": "application/json"
        }
      })
        .then(response => response.json())
        .then(data => {
          this.handleCancel();
        })
        .catch(error => {
          this.handleCancel();
          console.error("Error:", error);
          this.setState({ alert: "Bad idea" });
        });
    } else {
      window.scrollTo(0, 0);
    }
  };

  handleCancel = () => {
    this.props.history.push("/dashboard");
  };

  validate = () => {
    let localAlert = "";
    let validated = true;
    if (
      typeof this.state.model === "undefined" ||
      this.state.model == null ||
      this.state.model == ""
    ) {
      validated = false;
      localAlert += "Enter model\n";
    }
    if (
      typeof this.state.producer === "undefined" ||
      this.state.producer == null ||
      this.state.producer == ""
    ) {
      validated = false;
      localAlert += "Enter producer\n";
    }
    if (
      typeof this.state.country === "undefined" ||
      this.state.country == null ||
      this.state.country == ""
    ) {
      validated = false;
      localAlert += "Enter country\n";
    }
    let newAlert = localAlert.split("\n").map((item, i) => (
      <p key={i} className="text-danger">
        {item}
      </p>
    ));
    this.setState({ alert: newAlert });
    return validated;
  };

  render() {
    return (
      <React.Fragment>
        <div className="container">
          <br />
          <h2>Register equipment</h2>
          {this.state.alert}
          <br />
          <div className="form-row">
            <div className="form-group col-md-10">
              <label>Model name</label>
              <input
                className="form-control col-md-6"
                onChange={p => this.setState({ model: p.target.value })}
              />
            </div>
          </div>
          <div className="form-row">
            <div className="form-group col-md-10">
              <label>Producer</label>
              <input
                className="form-control col-md-6"
                onChange={p => this.setState({ producer: p.target.value })}
              />
            </div>
          </div>
          <div className="form-row">
            <div className="form-group col-md-10">
              <label>Country</label>
              <input
                className="form-control col-md-6"
                onChange={p => this.setState({ country: p.target.value })}
              />
            </div>
          </div>
          <br />
          <br />
          <div className="form-row">
            <button
              className="form-group col-md-3 btn btn-lg btn-outline-success"
              onClick={() => this.handleSubmit()}
            >
              Create equipment
            </button>
            <button
              className="form-group col-md-3 btn btn-lg btn-outline-danger"
              onClick={() => this.handleCancel()}
            >
              Cancel
            </button>
          </div>
        </div>
      </React.Fragment>
    );
  }
}

export default CreateEquipment;
