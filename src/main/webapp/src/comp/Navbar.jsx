import React, { Component } from "react";
import SearchAppBar from "./SearchAppBar";
class Navbar extends Component {
  state = {};
  render() {
    let tempVal = false;
    const currentPath = this.props.location.pathname;
    const regLoginEx = /\w*\/login\b/g;
    const regRegistEx = /\w*\/registration\b/g;
    tempVal = !(regLoginEx.test(currentPath) || regRegistEx.test(currentPath));
    console.log(currentPath);
    return (
      <React.Fragment>
        <SearchAppBar tempVal={tempVal} />
      </React.Fragment>
    );
  }
}

export default Navbar;
