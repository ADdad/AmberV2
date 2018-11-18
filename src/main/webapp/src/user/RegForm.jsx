import React, { Component } from "react";
// import { Redirect } from "react-router-dom";
class RegForm extends Component {
  state = {
    //   To compare passwords
    chars: "",
    mail: "",
    pass: "",
    name: "",
      confirmPass: "",
    surname: "",
    // Errors for each situation
    errorMail: false,
    errorPass: false,
    errorConfPass: false,
    errorName: false
  };
  handleMailChange = p => {
    this.setState({ mail: p.target.value });
    //   Check if matches regex
    /[a-zA-Z0-9._+-]+@[a-zA-Z+-]+\.[a-z]+$/.test(p.target.value)
      ? this.setState({ errorMail: false })
      : this.setState({ errorMail: true });
  };
  handlePasswordChange = p => {
    //   Check length
    const v = p.target.value;
    this.setState({ chars: v });
    v.length > 6
      ? this.setState({ errorPass: false })
      : this.setState({ errorPass: true });
  };
  handlePasswordConfChange = p => {
    //   Check length and compare with password field
    const v = p.target.value;
    v.length < 6 || v !== this.state.chars
      ? this.setState({ errorConfPass: true })
      : this.setState({ errorConfPass: false, pass: p.target.value, confirmPass: p.target.value });
  };
  writeName = p => {
    this.setState({ name: p.target.value });
    this.handleNamesChange(p);
  };
  writeSurName = p => {
    this.setState({ surname: p.target.value });
    this.handleNamesChange(p);
  };
  handleNamesChange = p => {
    this.setState({ name: p.target.value });
    //   Only lettersa
    // console.log(p.target.value);
    /[a-zA-Z]+/.test(p.target.value)
      ? this.setState({ errorName: false })
      : this.setState({ errorName: true });
  };
    handleSubmit = async () => {
        //   Container for all errors
        const error =
            this.state.errorConfPass ||
            this.state.errorMail ||
            this.state.errorName ||
            this.state.errorPass ||
            this.state.errorSurname;
        console.log(
            this.state.errorConfPass,
            this.state.errorMail,
            this.state.errorName,
            this.state.errorPass
        );
        if (error) {
            this.setState({ alert: "Fields are not filled propperly" });
        } else {

            this.setState({ alert: "Registring..." });
            await fetch("/register", {
                method: "POST",
                body: JSON.stringify({
                    email: this.state.mail,
                    password: this.state.pass,
                    confirmPassword: this.state.confirmPass,
                    firstName: this.state.name,
                    secondName: this.state.surname
                }),
                headers: {
                    "Content-Type": "application/json"
                }
            })
                .then(res => {
                    if(res.status == 200) {
                        this.setState({alert: "Succesfuly registered"})
                        this.setState({ loginStatus: true})
                        console.log("Success:", JSON.stringify(res))
                        console.log(res.status);
                    } else {
                        this.setState({ alert: "You are already registered" })
                    }
                })
                .catch(error => {
                    console.error("Error:", error);
                    this.setState({ alert: "Registration failed" })
                });
        }
        //setTimeout(function(){}, 1000);
        if(this.state.loginStatus){
            this.setState({ alert: "Logging in"})
            var params = new URLSearchParams([['email',this.state.mail],['password',this.state.pass]]);

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
                        this.setState({ alert: "Unknown exception" })
                    }
                } )
                .catch(error => console.log(error));
        }
    };
  render() {
    return (
      <React.Fragment>
        <div className="container">
          <div className="container">
            <br />
            <br />
            <br />
            <br />
            <br />
            <input
              type="text"
              className="form-control"
              placeholder="Mail"
              onChange={this.handleMailChange.bind(this)}
              onFocus={() => this.setState({ errorMail: true })}
            />

            <label>
              {this.state.errorMail ? (
                <font face="Verdana" color="red">
                  Error
                </font>
              ) : (
                "Login"
              )}
            </label>
            <br />
            <input
              type="password"
              className="form-control"
              placeholder="Password"
              onChange={this.handlePasswordChange.bind(this)}
              onFocus={() => this.setState({ errorPass: true })}
              required
            />
            <label>
              {this.state.errorPass ? (
                <font face="Verdana" color="red">
                  Error
                </font>
              ) : (
                "Password"
              )}
            </label>
            <br />

            <input
              type="password"
              className="form-control"
              placeholder="Confirm Password"
              onChange={this.handlePasswordConfChange.bind(this)}
              onFocus={() => this.setState({ errorConfPass: true })}
            />
            <label>
              {this.state.errorConfPass ? (
                <font face="Verdana" color="red">
                  Error
                </font>
              ) : (
                "Confirm Password"
              )}
            </label>
            <br />

            <input
              type="text"
              className="form-control"
              placeholder="Name"
              onChange={this.writeName.bind(this)}
              onFocus={() => this.setState({ errorName: true })}
            />

            <label>
              {this.state.errorName ? (
                <font face="Verdana" color="red">
                  Error
                </font>
              ) : (
                "Name"
              )}
            </label>
            <br />

            <input
              type="text"
              className="form-control"
              placeholder="Surname"
              onChange={this.writeSurName.bind(this)}
              onFocus={() => this.setState({ errorName: true })}
            />
            {this.state.errorName ? (
              <font face="Verdana" color="red">
                Error
              </font>
            ) : (
              "Surname"
            )}

            <button
              className="btn btn-outline-primary m-2"
              onClick={this.handleSubmit}
            >
              Submit
            </button>
            <button
              className="btn btn-outline-default m-2"
              onClick={() =>
                window.confirm("Are you sure?")
                  ? console.log("sure")
                  : console.log("Not sure")
              }
            >
              Cancel
            </button>
            <label>{this.state.alert}</label>
          </div>
        </div>
      </React.Fragment>
    );
  }
}

export default RegForm;
