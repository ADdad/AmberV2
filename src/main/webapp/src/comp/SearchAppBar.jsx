import React, { Component } from "react";
import AppBar from "@material-ui/core/AppBar";
import Toolbar from "@material-ui/core/Toolbar";
import Typography from "@material-ui/core/Typography";
import { fade } from "@material-ui/core/styles/colorManipulator";
import { withStyles } from "@material-ui/core/styles";
import Button from "@material-ui/core/Button";
import { withRouter } from "react-router-dom";
import MenuItem from "@material-ui/core/MenuItem";
import Menu from "@material-ui/core/Menu";

const styles = theme => ({
  root: {
    width: "100%"
  },
  grow: {
    flexGrow: 1
  },
  menuButton: {
    marginLeft: -12,
    marginRight: 20
  },
  title: {
    display: "none",
    [theme.breakpoints.up("sm")]: {
      display: "block"
    }
  },
  search: {
    position: "relative",
    borderRadius: theme.shape.borderRadius,
    backgroundColor: fade(theme.palette.common.white, 0.15),
    "&:hover": {
      backgroundColor: fade(theme.palette.common.white, 0.25)
    },
    marginLeft: 0,
    width: "100%",
    [theme.breakpoints.up("sm")]: {
      marginLeft: theme.spacing.unit,
      width: "auto"
    }
  },
  searchIcon: {
    width: theme.spacing.unit * 9,
    height: "100%",
    position: "absolute",
    pointerEvents: "none",
    display: "flex",
    alignItems: "center",
    justifyContent: "center"
  },
  inputRoot: {
    color: "inherit",
    width: "100%"
  },
  inputInput: {
    paddingTop: theme.spacing.unit,
    paddingRight: theme.spacing.unit,
    paddingBottom: theme.spacing.unit,
    paddingLeft: theme.spacing.unit * 10,
    transition: theme.transitions.create("width"),
    width: "100%",
    [theme.breakpoints.up("sm")]: {
      width: 120,
      "&:focus": {
        width: 200
      }
    }
  }
});

const selectedStyle = {
  muiItemSelected: {
    backgroundColor: "#3f51b5"
  }
};
const ITEM_HEIGHT = 48;

class SearchAppBar extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      anchorEl: null,
      menuIndex: 1
    };
  }

  handleLogout = () => {
    fetch("/logout")
      .then(response => document.location.reload())
      .catch(error => console.log(error));
  };

  handleDashboard = () => {
    this.handleClose();
    this.props.history.push("/dashboard");
    this.setState({ menuIndex: 1 });
  };

  handleArchive = () => {
    this.handleClose();
    this.props.history.push("/archive");
    this.setState({ menuIndex: 2 });
  };

  handleClick = event => {
    this.setState({ anchorEl: event.currentTarget });
  };

  handleClose = () => {
    this.setState({ anchorEl: null });
  };

  handleCreateRegularOrder = () => {
    this.handleClose();
    this.props.history.push("/order/create/order");
    this.setState({ menuIndex: 3 });
  };

  handleCreateReplenishmentOrder = () => {
    this.handleClose();
    this.props.history.push("/order/create/replenishment");
    this.setState({ menuIndex: 4 });
  };

  handleReports = () => {
    this.handleClose();
    this.props.history.push("/reports");
    this.setState({ menuIndex: 5 });
  };

  handleRegisterEquipment = () => {
    this.handleClose();
    this.props.history.push("/equipment/create");
    this.setState({ menuIndex: 8 });
  };

  handleRoleChange = () => {
    this.handleClose();
    this.props.history.push("/admin");
    this.setState({ menuIndex: 6 });
  };
  handleAddItems = () => {
    this.handleClose();
    this.props.history.push("/equipment/add");
    this.setState({ menuIndex: 7 });
  };

  renderLogout = () => {
    return (
      <MenuItem
        style={{ color: "#ffffff", backgroundColor: "#c2185b" }}
        onClick={this.handleLogout}
      >
        Logout
      </MenuItem>
    );
  };

  renderDashboard = () => {
    return (
      <React.Fragment>
        <MenuItem
          style={{
            color: "#ffffff",
            backgroundColor: "#c2185b",
            ...(this.state.menuIndex === 1 ? selectedStyle.muiItemSelected : {})
          }}
          onClick={this.handleDashboard}
          selected={this.state.menuIndex === 1}
        >
          Dashboard
        </MenuItem>
        <MenuItem
          style={{
            color: "#ffffff",
            backgroundColor: "#c2185b",
            ...(this.state.menuIndex === 2 ? selectedStyle.muiItemSelected : {})
          }}
          onClick={this.handleArchive}
          selected={this.state.menuIndex === 2}
        >
          Archive
        </MenuItem>
      </React.Fragment>
    );
  };
  renderUserButtons = () => {
    if (this.props.roles.includes("ROLE_USER")) {
      return (
        <MenuItem
          style={{
            color: "#ffffff",
            backgroundColor: "#c2185b",
            ...(this.state.menuIndex === 3 ? selectedStyle.muiItemSelected : {})
          }}
          onClick={this.handleCreateRegularOrder}
          selected={this.state.menuIndex === 3}
        >
          Create regular order
        </MenuItem>
      );
    }
  };
  renderKeeperButtons = () => {
    if (this.props.roles.includes("ROLE_KEEPER")) {
      return (
        <React.Fragment>
          <MenuItem
            style={{
              color: "#ffffff",
              backgroundColor: "#c2185b",
              ...(this.state.menuIndex === 4
                ? selectedStyle.muiItemSelected
                : {})
            }}
            onClick={this.handleCreateReplenishmentOrder}
            selected={this.state.menuIndex === 4}
          >
            Create replenishment order
          </MenuItem>
          <MenuItem
            style={{
              color: "#ffffff",
              backgroundColor: "#c2185b",
              ...(this.state.menuIndex === 5
                ? selectedStyle.muiItemSelected
                : {})
            }}
            onClick={this.handleReports}
            selected={this.state.menuIndex === 5}
          >
            Reports
          </MenuItem>
        </React.Fragment>
      );
    }
  };
  renderAdminButtons = () => {
    if (
      this.props.roles.includes("ROLE_ADMIN") &&
      !this.props.roles.includes("ROLE_KEEPER")
    ) {
      return (
        <React.Fragment>
          <MenuItem
            style={{
              color: "#ffffff",
              backgroundColor: "#c2185b",
              ...(this.state.menuIndex === 5
                ? selectedStyle.muiItemSelected
                : {})
            }}
            onClick={this.handleReports}
            selected={this.state.menuIndex === 5}
          >
            Reports
          </MenuItem>
          <MenuItem
            style={{
              color: "#ffffff",
              backgroundColor: "#c2185b",
              ...(this.state.menuIndex === 6
                ? selectedStyle.muiItemSelected
                : {})
            }}
            onClick={this.handleRoleChange}
            selected={this.state.menuIndex === 6}
          >
            Role change
          </MenuItem>
          <MenuItem
            style={{
              color: "#ffffff",
              backgroundColor: "#c2185b",
              ...(this.state.menuIndex === 7
                ? selectedStyle.muiItemSelected
                : {})
            }}
            onClick={this.handleAddItems}
            selected={this.state.menuIndex === 7}
          >
            Add items
          </MenuItem>
          <MenuItem
            style={{
              color: "#ffffff",
              backgroundColor: "#c2185b",
              ...(this.state.menuIndex === 8
                ? selectedStyle.muiItemSelected
                : {})
            }}
            onClick={this.handleRegisterEquipment}
            selected={this.state.menuIndex === 8}
          >
            Register equipment
          </MenuItem>
        </React.Fragment>
      );
    } else if (this.props.roles.includes("ROLE_ADMIN")) {
      return (
        <React.Fragment>
          <MenuItem
            style={{
              color: "#ffffff",
              backgroundColor: "#c2185b",
              ...(this.state.menuIndex === 6
                ? selectedStyle.muiItemSelected
                : {})
            }}
            onClick={this.handleRoleChange}
            selected={this.state.menuIndex === 6}
          >
            Role change
          </MenuItem>
          <MenuItem
            style={{
              color: "#ffffff",
              backgroundColor: "#c2185b",
              ...(this.state.menuIndex === 7
                ? selectedStyle.muiItemSelected
                : {})
            }}
            onClick={this.handleAddItems}
            selected={this.state.menuIndex === 7}
          >
            Add items
          </MenuItem>
          <MenuItem
            style={{
              color: "#ffffff",
              backgroundColor: "#c2185b",
              ...(this.state.menuIndex === 8
                ? selectedStyle.muiItemSelected
                : {})
            }}
            onClick={this.handleRegisterEquipment}
            selected={this.state.menuIndex === 8}
          >
            Register equipment
          </MenuItem>
        </React.Fragment>
      );
    }
  };

  renderMenuButtons = () => {
    return (
      <React.Fragment>
        {this.renderDashboard()}
        {this.renderUserButtons()}
        {this.renderKeeperButtons()}
        {this.renderAdminButtons()}
        {this.renderLogout()}
      </React.Fragment>
    );
  };
  cutNameString = name => {
    if (name.length > 50) {
      return name.substr(0, 50) + "...";
    } else {
      return name;
    }
  };
  renderMenu = () => {
    if (this.props.tempVal == true) {
      const { anchorEl } = this.state;
      let user = this.cutNameString(
        this.props.userName + " " + this.props.userMail
      );
      return (
        <div>
          <Button
            aria-owns={anchorEl ? "simple-menu" : undefined}
            aria-haspopup="true"
            onClick={this.handleClick}
            style={{
              color: "#ffffff",
              backgroundColor: "#c2185b",
              textTransform: "none"
            }}
          >
            {user}
          </Button>
          <Menu
            id="simple-menu"
            anchorEl={anchorEl}
            open={Boolean(anchorEl)}
            onClose={this.handleClose}
            PaperProps={{
              style: {
                maxHeight: ITEM_HEIGHT * 4.5,
                width: 200
              }
            }}
          >
            {this.renderMenuButtons()}
          </Menu>
        </div>
      );
    } else {
      return <div />;
    }
  };

  render() {
    const { classes } = this.props;
    return (
      <div className={classes.root}>
        <AppBar position="static">
          <Toolbar>
            <Typography
              className={classes.title}
              variant="h6"
              color="inherit"
              noWrap
            >
              Amber
            </Typography>

            <div className={classes.grow} />

            {this.renderMenu()}
          </Toolbar>
        </AppBar>
      </div>
    );
  }
}

export default withRouter(withStyles(styles)(SearchAppBar));
