import React, { Component } from "react";
import { withRouter } from 'react-router-dom';
import "bootstrap";
import "jquery";
class LogInForm extends Component {
  constructor(props) {
    super(props);

    this.state = {
        mail: "",
        password: "",
        alert: "",
        error: false,
        res: false
    };
  }

  handleMailChange = p => {
    const v = p.target.value;
    // Remembering mail value for submit
    this.setState({ mail: v });
    console.log(this.state.mail);
    // Check regex notation
    /[a-zA-Z0-9._+-]+@[a-zA-Z+-]+\.[a-z]+$/.test(p.target.value)
      ? this.setState({ error: false })
      : this.setState({ error: true });
  };
  handlePasswordChange = p => {
    // Remembering password value for submit
    const v = p.target.value;
    this.setState({ password: v });
  };
  handleSubmit = () => {
    console.log(this.state.mail, "\n", this.state.password);
    var params = new URLSearchParams([['email',this.state.mail],['password',this.state.password]]);
    
    fetch("/perform_login", {
      method: "POST",
      body: params,
      headers: {
        "Content-Type": "application/x-www-form-urlencoded"
      }
    })  //response.json()
      .then(response => {
          const regex = /dashboard$/gm;
          if(regex.test(response.url)) {
              this.props.history.push('/dashboard')
          } else {
              this.setState({ alert: "Fields are not filled propperly" })
          }
      } )
      .catch(error => console.log(error));
    // If any error is still present or password is < 6 printing error
    console.log(this.state.res);
    this.state.error //|| this.state.password.length < 6
      ? this.setState({ alert: "Fields are not filled propperly" })
      : this.setState({ alert: "Logging in..." });
  };
  handleRegistration = () => {
      this.props.history.push('/registration')
  }
  render() {

    // const { from } = this.props.location.state || { from: { pathname: "/" } };
    return (
      <React.Fragment>
        <div className="container">
          <div className="container">
            <br />
            <br />
            <br />
            <br />
            <br />
              <label>Email</label>
            {/* Mail input field */}
            <input
              type="text"
              placeholder="Mail"
              onChange={this.handleMailChange.bind(this)}
              className="form-control"
            />

            <label>{this.state.error ? "Unsupported email form" : ""}</label>
            <br />
            <label>Password</label>
            {/* Password input field */}
            <input
              type="password"
              placeholder="Password"
              className="form-control"
              onChange={this.handlePasswordChange.bind(this)}
            />

            <br />

            {/* Error label for submitting */}
            <label>{this.state.alert}</label>
              <div>
                  {/* Submit button */}
                  <button
                      className="btn btn-outline-primary m-2"
                      onClick={this.handleSubmit}
                  >
                      Login
                  </button>
                  {/* Button to registration */}
                  <button
                      className="btn btn-primary m-2"
                      onClick={this.handleRegistration}
                  >
                      Register
                  </button>
              </div>
          </div>
        </div>
      </React.Fragment>
    );
  }
}

export default withRouter(LogInForm);
