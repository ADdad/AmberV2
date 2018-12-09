import React, {Component} from "react";
import PropTypes from "prop-types";
import AppBar from "@material-ui/core/AppBar";
import Toolbar from "@material-ui/core/Toolbar";
import IconButton from "@material-ui/core/IconButton";
import Typography from "@material-ui/core/Typography";
import InputBase from "@material-ui/core/InputBase";
import {fade} from "@material-ui/core/styles/colorManipulator";
import {withStyles} from "@material-ui/core/styles";
import MenuIcon from "@material-ui/icons/Menu";
import SearchIcon from "@material-ui/icons/Search";
import Button from "@material-ui/core/Button";
import {withRouter} from 'react-router-dom';
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
const ITEM_HEIGHT = 48;

class SearchAppBar extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            anchorEl : null,
        }
    }






handleLogout = () => {
    fetch("/logout")
        .then(response => document.location.reload())
        .catch(error => console.log(error));
}

 handleDashboard = () => {
     this.handleClose();
  this.props.history.push("/dashboard");
}
    handleClick = event => {
        this.setState({ anchorEl: event.currentTarget });
    };

    handleClose = () => {
        this.setState({ anchorEl: null });
    };

    handleCreateRegularOrder = () => {
        this.handleClose();
        this.props.history.push("/order/create/order")
    }

    handleCreateReplenishmentOrder = () => {
        this.handleClose();
        this.props.history.push("/order/create/replenishment")
    }

    handleReports = () => {
        this.handleClose();
        this.props.history.push("/reports")
    }

    handleRoleChange = () => {
        this.handleClose();
        this.props.history.push("/admin")
    }

 renderLogout =() => {
    return (
        <MenuItem style={{color : '#ffffff',backgroundColor : '#c2185b'}} onClick={this.handleLogout}>
            Logout
        </MenuItem>
    );
}

 renderDashboard = () => {
  return (
    <MenuItem style={{color : '#ffffff',backgroundColor : '#c2185b'}}  onClick={this.handleDashboard}>
      Dashboard
    </MenuItem>
  );
}
renderUserButtons = () => {
     if(this.props.roles.includes("ROLE_USER")){
         return(
             <MenuItem style={{color : '#ffffff',backgroundColor : '#c2185b'}} onClick={this.handleCreateRegularOrder}>
                 Create regular order
             </MenuItem>
         )
     }
}
renderKeeperButtons = () => {
        if(this.props.roles.includes("ROLE_KEEPER")) {
            return(
                <React.Fragment>
                    <MenuItem style={{color : '#ffffff',backgroundColor : '#c2185b'}} onClick={this.handleCreateReplenishmentOrder}>
                        Create replenishment order
                    </MenuItem>
                    <MenuItem style={{color : '#ffffff',backgroundColor : '#c2185b'}} onClick={this.handleReports}>
                        Reports
                    </MenuItem>
                </React.Fragment>
            )
        }

}
renderAdminButtons = () => {
     if(this.props.roles.includes("ROLE_ADMIN") && !this.props.roles.includes("ROLE_KEEPER")){
         return(
             <React.Fragment>
                 <MenuItem style={{color : '#ffffff',backgroundColor : '#c2185b'}} onClick={this.handleReports}>
                     Reports
                 </MenuItem>
                 <MenuItem style={{color : '#ffffff',backgroundColor : '#c2185b'}} onClick={this.handleRoleChange}>
                     Role change
                 </MenuItem>
             </React.Fragment>
         )
     } else if (this.props.roles.includes("ROLE_ADMIN")) {
         return(
             <MenuItem style={{color : '#ffffff',backgroundColor : '#c2185b'}} onClick={this.handleRoleChange}>
                 Role change
             </MenuItem>
         )
     }
}

renderMenuButtons = () => {
        return (
            <React.Fragment>
                {this.renderDashboard()}
                {this.renderUserButtons()}
                {this.renderKeeperButtons()}
                {this.renderAdminButtons()}
                {this.renderLogout()}
            </React.Fragment>
        )
}
renderMenu = () => {

    if(this.props.tempVal == true){
        const { anchorEl } = this.state;
        return(
        <div>
            <Button
                aria-owns={anchorEl ? 'simple-menu' : undefined}
                aria-haspopup="true"
                onClick={this.handleClick}
                style={{color : '#ffffff',
                    backgroundColor : '#c2185b'}}
            >
                Open Menu
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

                    },
                }}
            >
                {this.renderMenuButtons()}
            </Menu>
        </div>
        )
    } else {
        return(<div></div>)
    }
}



render() {
    const {classes} = this.props;
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
