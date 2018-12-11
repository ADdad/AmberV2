import React, { Component } from "react";

class CreateEquipment extends Component {
  constructor(props) {
    super(props);
    this.state = {
      userRoles: [],
      isLoading: false
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
}

export default CreateEquipment;
