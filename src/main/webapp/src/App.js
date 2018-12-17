import React, { Component } from "react";
// import Header from "./comp/Header";
import Order from "./comp/Order";
import RegForm from "./comp/RegForm";
import LogInForm from "./comp/LogInForm";
import AdminPage from "./comp/AdminPage";
import Navbar from "./comp/Navbar";
import ErrorPage from "./comp/ErrorPage";
import ReportsPage from "./comp/ReportsPage";
// import PrivateRoute from "./PrivateRoute";
import "./App.css";
import { Route, BrowserRouter, Redirect, Switch } from "react-router-dom";
import CreateOrder from "./comp/CreateOrder";
import OrderReview from "./comp/OrderReview";
import OrderEdit from "./comp/OrderEdit";
import AdminPageNew from "./comp/AdminPageNew";
import DashboardPage from "./comp/DashboardPage";
import SearchAppBar from "./comp/SearchAppBar";
import EquipmentAdding from "./comp/EquipmentAdding";
import CreateEquipment from "./comp/CreateEquipment";
import ArchivePage from "./comp/ArchivePage";
import UsersRequests from "./comp/UsersRequests";

const fakeAuth = {
  isAuthenticated: true,
  authenticate(cb) {
    this.isAuthenticated = true;
    setTimeout(cb, 100); // fake async
  },
  signout(cb) {
    this.isAuthenticated = false;
    setTimeout(cb, 100); // fake async
  }
};
const PrivateRoute = ({ component: Component, yesno: a, ...rest }) => (
  <Route
    {...rest}
    render={props =>
      fakeAuth.isAuthenticated ? (
        <Component {...props} />
      ) : (
        <Redirect
          to={{ pathname: "/login", state: { from: props.location } }}
        />
      )
    }
  />
);

class App extends Component {
  constructor(props) {
    super(props);

    this.state = {
      chars: "",
      errors: { mail: false, password: false, names: false, all: false },
      loading: false,
      callback: false,
      attachments: [1, 1, 2, 3]
    };
  }

  componentDidMount() {
    this.setState({ loading: true });
    // fetch("http://localhost:8080/login");
  }
  callbk = a => {
    // fakeAuth.isAuthenticated = a.status;
    this.setState({ callback: a.status });
  };
  f() {
    return this.state.callback;
  }
  render() {
    return (
      <BrowserRouter>
        <div>
          <Navbar />
          <br />
          <Switch>
            <Route
              path="/login"
              render={() => <LogInForm isAuth={this.callbk} />}
            />
            <Route path="/registration" component={RegForm} />
            <Route path="/admin" component={AdminPageNew} />
            <Route path="/order/edit/:requestId" component={OrderEdit} />
            <Route
              path="/order/create/:type/:requestId?"
              component={CreateOrder}
            />
            <Route path="/user/:userId" component={UsersRequests} />
            <Route path="/order/:requestId" component={OrderReview} />
            <Route path="/dashboard" component={DashboardPage} />
            <Route path="/archive" component={ArchivePage} />
            <Route path="/reports" component={ReportsPage} />
            <Route path="/equipment/add" component={EquipmentAdding} />
            <Route path="/equipment/create" component={CreateEquipment} />

            <Route component={ErrorPage} />
          </Switch>
        </div>
      </BrowserRouter>
    );
  }
}

export default App;
