import React, { Component } from "react";
import Select from "react-select";
import AsyncSelect from "react-select/lib/Async";
import { debounce } from "lodash";
import { withRouter } from "react-router-dom";

class EquipmentAdding extends Component {
  constructor(props) {
    super(props);
    this.state = {
      userRoles: [],
      warehouses: [],
      warehouseId: "",
      viewItems: [],
      resultItems: [],
      myItems: [],
      alert: "",
      equipment: [],
      initialized: false
    };
  }

  componentDidMount() {
    this.setState({ isLoading: true });
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
    fetch(`/equipment`, {
      method: "GET"
    })
      .then(response => response.json())
      .then(data => {
        console.log(data);
        this.setState({
          myItems: data.equipment,
          initialized: true
        });
      })
      .catch(error => console.log(error));
    fetch(`/warehouse`, {
      method: "GET"
    })
      .then(response => response.json())
      .then(data => {
        console.log(data);
        this.setState({
          warehouses: data.warehouses
        });
      })
      .catch(error => console.log(error));
  }

  itemName = item => {
    let strName = item.model + ", " + item.producer + ", " + item.country;
    return strName;
  };

  resultQuantityEdit = (i, e) => {
    let readyItems = this.state.resultItems.slice();
    let editObject = readyItems[i];
    editObject.quantity = e.target.value > -1 ? e.target.value : 0;
    readyItems[i] = editObject;
    this.setState({ resultItems: readyItems });
  };
  getItemsOptions = items => {
    let itemsLocal = items;
    let result = [];
    itemsLocal.map(i => result.push({ label: this.itemName(i), value: i.id }));
    return result;
  };

  loadOptions = (input, callback) => {
    console.log(input);
    if (!input || input.length < 1) {
      return callback(this.getItemsOptions(this.state.myItems));
    }

    return fetch(`/request/equipment/find/${input}`)
      .then(response => {
        return response.json();
      })
      .then(json => {
        let options = [];
        options = this.getItemsOptions(json.equipment);
        return callback(options);
      });
  };

  handleRemove = index => {
    let resItemsLocal = this.state.resultItems;
    let viewItemsLocal = this.state.viewItems;
    resItemsLocal.splice(index, 1);
    viewItemsLocal.splice(index, 1);
    this.setState({ resultItems: resItemsLocal, viewItems: viewItemsLocal });
  };

  handleRemoveAll = () => {
    this.setState({ resultItems: [], viewItems: [] });
  };

  getWarehouseOptions = () => {
    let res = [];
    this.state.warehouses.map(w => res.push({ value: w.id, label: w.adress }));
    return res;
  };
  handleWarehouseChange = selectedWarehouse => {
    this.setState({ warehouseId: selectedWarehouse.value });
  };

  validate = () => {
    let resultItemsLocal = this.state.resultItems;
    let localAlert = "";
    let validated = true;
    if (
      typeof this.state.warehouseId === "undefined" ||
      this.state.warehouseId == null ||
      this.state.warehouseId == ""
    ) {
      validated = false;
      localAlert += "Chose warehouse\n";
    }
    if (resultItemsLocal.length < 1) {
      localAlert += "Enter some items\n";
      validated = false;
    } else {
      for (let i = 0; i < resultItemsLocal.length; i++) {
        if (resultItemsLocal[i].quantity < 1) {
          localAlert += "Quantity of items cant be less than 1\n";
          validated = false;
          break;
        }
      }
    }
    let newAlert = localAlert.split("\n").map((item, i) => (
      <p key={i} className="text-danger">
        {item}
      </p>
    ));
    this.setState({ alert: newAlert });
    return validated;
  };

  handleSubmit = () => {
    if (this.validate()) {
      fetch("/equipment/list", {
        method: "POST",
        body: JSON.stringify({
          warehouseId: this.state.warehouseId,
          items: this.state.resultItems
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
          console.error("Error:", error);
          this.handleCancel();
        });
    } else {
      window.scrollTo(0, 0);
    }
  };

  initReactSelect = () => {
    if (this.state.initialized)
      return (
        <div className="form-row">
          <div className="form-group col-md-12">
            <AsyncSelect
              cacheOptions
              defaultOptions={this.getItemsOptions(this.state.myItems)}
              loadOptions={debounce(this.loadOptions, 500)}
              onChange={this.addItem}
            />
          </div>
        </div>
      );
  };

  addItem = item => {
    let resItemsLocal = this.state.resultItems;
    let viewItemsLocal = this.state.viewItems;
    let index = resItemsLocal.findIndex(p => p.id === item.value);
    if (index == -1) {
      resItemsLocal.push({ id: item.value, quantity: 0 });
      viewItemsLocal.push(item.label);
    }
    this.setState({ resultItems: resItemsLocal, viewItems: viewItemsLocal });
  };

  handleCancel = () => {
    this.props.history.push("/dashboard");
  };

  render() {
    let items = [];
    for (let i = 0; i < this.state.resultItems.length; i++) {
      items.push(
        <div key={i} className="form-row border rounded m-2 col-md-11">
          <div className="form-group col-md-8 mt-auto">
            <p>{this.state.viewItems[i]}</p>
          </div>
          <div className="form-group col-md-2 mt-auto">
            <label>Quantity</label>
            <input
              id="quantity"
              className="form-control"
              type="number"
              min="0"
              step="1"
              value={this.state.resultItems[i].quantity}
              data-bind="value:replyNumber"
              onChange={e => this.resultQuantityEdit(i, e)}
            />
          </div>
          <div className="form-group col-md-2 mt-auto">
            <button
              onClick={() => this.handleRemove(i)}
              className="btn btn-lg btn-outline-danger"
            >
              Remove
            </button>
          </div>
        </div>
      );
    }

    return (
      <React.Fragment>
        <div className="container">
          <br />
          <h2>Adding equipment</h2>
          {this.state.alert}
          <br />
          <div className="form-row">
            <div className="form-group col-md-8">
              <label>Warehouse</label>
              <Select
                onChange={this.handleWarehouseChange}
                options={this.getWarehouseOptions()}
              />
            </div>
          </div>
          <div className="form-row">
            <div className="form-group col-md-9">
              <h3>Items</h3>
            </div>
            <div className="form-group col-md-3">
              <button
                onClick={() => this.handleRemoveAll()}
                className="btn btn-lg btn-outline-danger"
              >
                Remove all
              </button>
            </div>
          </div>
          {items}
          {this.initReactSelect()}
          <div className="form-row">
            <button
              className="form-group col-md-3 btn btn-lg btn-outline-success"
              onClick={() => this.handleSubmit()}
            >
              Add equipment
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

export default withRouter(EquipmentAdding);
