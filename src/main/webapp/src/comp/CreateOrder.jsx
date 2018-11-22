import React, { Component, Fragment } from "react";
import { withRouter } from "react-router-dom";
import { RadioGroup, Radio } from "react-radio-group";
import { Checkbox, CheckboxGroup } from "react-checkbox-group";
import Attachments from "./Attachments";
import Dropzone from "react-dropzone";
import Select from "react-select";
import AsyncSelect from "react-select/lib/Async";

class CreateOrder extends Component {
  constructor(props) {
    super(props);
    this.state = {
      initialized: false,
      userId: "",
      orderId: "",
      alert: "",
      title: "",
      myItems: [],
      resultItems: [],
      viewItems: [],
      warehouses: [
        { id: "djsal", adress: "dsajkljlk" },
        { id: "djz", adress: "kdl" }
      ],
      warehouseId: "",
      type: "",
      description: "",
      attachments: [],
      optionalAttributes: [
        {
          id: "ejklwq",
          name: "TestRadio",
          type: "radio",
          multiple: true,
          mandatory: true,
          immutable: false,
          values: ["Test1", "Test2", "Test3"]
        },
        {
          id: "2233",
          name: "TestCheckbox",
          type: "select",
          multiple: true,
          mandatory: false,
          immutable: false,
          values: ["Test1", "Test2", "Test3"]
        },
        {
          id: "dka",
          name: "TestDate",
          type: "date",
          multiple: false,
          mandatory: false,
          immutable: false,
          values: []
        },
        {
          id: "djlkjfdl",
          name: "TestText",
          type: "Text",
          multiple: false,
          mandatory: false,
          immutable: false,
          values: []
        }
      ],
      oAttributesValues: [],
      resultOptionalAttributes: [],
      alertFiles: ""
    };
  }

  hasMandatory = () => {
    for (let i = 0; i < this.state.optionalAttributes.length; i++) {
      if (this.state.optionalAttributes[i].mandatory) return true;
    }
    return false;
  };

  validate = () => {
    let localValues = this.state.oAttributesValues;
    let resultItemsLocal = this.state.resultItems;
    let localAlert = "";
    let validated = true;
    if (
      typeof this.state.title === "undefined" ||
      this.state.title == null ||
      this.state.title == ""
    ) {
      validated = false;
      localAlert += "Enter title\n";
    }
    if (
      typeof this.state.description === "undefined" ||
      this.state.description == null ||
      this.state.description == ""
    ) {
      validated = false;
      localAlert += "Enter description\n";
    }
    if (
      typeof this.state.warehouseId === "undefined" ||
      this.state.warehouseId == null ||
      this.state.warehouseId == ""
    ) {
      validated = false;
      localAlert += "Chose warehouse\n";
    }
    if (this.hasMandatory() && localValues.length < 1) {
      validated = false;
      localAlert += "Enter mandatory attributes\n";
    }
    for (let i = 0; i < localValues.length; i++) {
      if (
        this.state.optionalAttributes[i].mandatory &&
        (typeof localValues[i] === "undefined" ||
          localValues[i] == null ||
          localValues[i] == "")
      ) {
        validated = false;
        localAlert +=
          "Enter value " + this.state.optionalAttributes[i].name + "\n";
      }
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

  componentDidMount() {
    this.setState({ isLoading: true });
    fetch("/userinfo")
      .then(response => response.json())
      .then(data => {
        this.setState({ userId: data.id });
      })
      .catch(error => console.log(error));
    const type = this.props.match.params.type;
    fetch(`/request/create/${type}`)
      .then(res => res.json())
      .then(response => {
        this.setState({
          optionalAttributes: this.sortAttributes(response.attributes),
          warehouses: response.warehouses,
          myItems: response.equipment,
          type: type,
          warehouse: response.warehouses[0].id,
          isLoading: false,
          initialized: true
        });
      })
      .catch(error => console.error("Error:", error));
  }

  handleSubmit = () => {
    if (this.validate()) {
      const readyAttributes = this.compileAdditionalAttributes();
      fetch("/request", {
        method: "POST",
        body: JSON.stringify({
          creatorId: this.state.userId,
          title: this.state.title,
          description: this.state.description,
          type: this.state.type,
          warehouseId: this.state.warehouseId,
          items: this.state.resultItems,
          attributes: readyAttributes,
          offset: new Date().getTimezoneOffset()
        }),
        headers: {
          "Content-Type": "application/json"
        }
      })
        .then(response => response.json())
        .then(data => {
          if (this.state.attachments.length > 0) {
            this.uploadFiles(data.id);
          } else {
            this.props.history.push("/dashboard");
          }
        })
        .catch(error => {
          console.error("Error:", error);
          this.setState({ alert: "Bad idea" });
        });
    } else {
      window.scrollTo(0, 0);
    }
  };

  uploadFiles = orderId => {
    let files = this.state.attachments;
    let data = new FormData();
    for (let i = 0; i < files.length; i++) {
      data.append("files", files[i]);
    }
    fetch(`/request/create/attachments/${orderId}`, {
      method: "POST",
      body: data
    })
      .then(response => {
        this.setState({ error: "", msg: "Sucessfully uploaded file" });
      })
      .catch(err => {
        this.setState({ error: err });
      });
    this.props.history.push("/dashboard");
  };

  compileAdditionalAttributes = () => {
    let readyAttributes = [];
    for (let i = 0; i < this.state.optionalAttributes.length; i++) {
      readyAttributes.push({
        id: this.state.optionalAttributes[i].id,
        type: this.state.optionalAttributes[i].type,
        value: this.state.oAttributesValues[i]
      });
    }
    return readyAttributes;
  };

  initReactSelect = () => {
    if (this.state.initialized)
      return (
        <div className="form-row">
          <div className="form-group col-md-11">
            <AsyncSelect
              cacheOptions
              defaultOptions={this.getItemsOptions(this.state.myItems)}
              loadOptions={this.loadOptions}
              onChange={this.addItem}
            />
          </div>
        </div>
      );
  };
  getItemsOptions = items => {
    let itemsLocal = items;
    let result = [];
    itemsLocal.map(i => result.push({ label: this.itemName(i), value: i.id }));
    return result;
  };

  sortAttributes = attributes => {
    let localAttributes = attributes;
    localAttributes.sort(function(a, b) {
      return parseInt(a.order) - parseInt(b.order);
    });
    return localAttributes;
  };

  itemName = item => {
    let strName = item.model + ", " + item.producer + ", " + item.country;
    return strName;
  };

  resultItemEdit = item => {
    let myItemsLocal = this.state.resultItems;
    let index = myItemsLocal.findIndex(p => p.id === item.value);
    if (index == -1) myItemsLocal.push({ id: item.value, quantity: 0 });
    this.setState({ myItems: myItemsLocal });
  };
  resultQuantityEdit = (i, e) => {
    let readyItems = this.state.resultItems.slice();
    let editObject = readyItems[i];
    editObject.quantity = e.target.value > -1 ? e.target.value : 0;
    readyItems[i] = editObject;
    this.setState({ resultItems: readyItems });
  };

  renderAdditionalAttribute = (
    type,
    name,
    multiple,
    values,
    mandatory,
    immutable,
    index
  ) => {
    if (mandatory) name += "*";
    switch (type) {
      case "select": {
        if (
          this.state.oAttributesValues.length < 1 ||
          this.state.oAttributesValues[index] == null
        )
          this.changeOptionalValue(index, values[0]);
        return (
          <div className="form-row">
            <div className="form-group">
              <label>{name} </label>
              <select
                className="form-control"
                onChange={p => this.changeOptionalValue(index, p.target.value)}
              >
                {values.map(p => (
                  <option key={p} value={p}>
                    {p}
                  </option>
                ))}
              </select>
            </div>
          </div>
        );
      }
      case "textarea": {
        return (
          <div className="form-row">
            <div className="form-group col-md-8">
              <label>{name}</label>
              <textarea
                className="form-control"
                id="exampleFormControlTextarea1"
                rows="5"
                onChange={p => this.changeOptionalValue(index, p.target.value)}
              />
            </div>
          </div>
        );
      }
      case "radio": {
        return (
          <div className="form-row">
            <div className="form-group">
              <h4>{name}</h4>
              <RadioGroup name={name} onChange={this.changeRadiobuttonValue}>
                {values.map(v => (
                  <label key={v} className="form-group m-2">
                    <Radio value={v + "|" + index} />
                    {v}
                  </label>
                ))}
              </RadioGroup>
            </div>
          </div>
        );
      }
      case "checkbox": {
        return (
          <div className="form-row">
            <div className="form-group">
              <h4>{name}</h4>
              <CheckboxGroup
                checkboxDepth={2}
                name={name}
                onChange={this.handleCheckbox}
              >
                {values.map(v => (
                  <label key={v} className="form-group m-2">
                    <Checkbox value={v + "|" + index} />
                    {v}
                  </label>
                ))}
              </CheckboxGroup>
            </div>
          </div>
        );
      }
      default: {
        return (
          <div className="form-row">
            <div className="form-group">
              <h4>{name}: </h4>
              <input
                className="form-control col-md-12"
                type={type}
                onChange={p => this.changeOptionalValue(index, p.target.value)}
              />
            </div>
          </div>
        );
      }
    }
  };

  changeOptionalValue = (index, value) => {
    let optionalVal = this.state.oAttributesValues.slice();
    optionalVal[index] = value;
    this.setState({ oAttributesValues: optionalVal });
  };
  changeRadiobuttonValue = value => {
    let tempVal = value;
    let temp = tempVal.split("|");
    let resValue = temp[0];
    let index = temp[1];
    this.changeOptionalValue(index, resValue);
  };
  handleCheckbox = value => {
    let checkBoxValues = "";
    let index = 0;
    for (let i = 0; i < value.length; i++) {
      let temp = value[i].split("|");
      checkBoxValues += temp[0] + "|";
      index = temp[1];
    }
    this.changeOptionalValue(index, checkBoxValues);
  };

  getFileExtension1 = filename => {
    return /[.]/.exec(filename) ? /[^.]+$/.exec(filename)[0] : undefined;
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

  handleClearFiles = () => {
    this.setState({ attachments: [] });
  };

  handleCancel = () => {
    this.props.history.push("/dashboard");
  };

  onPreviewDrop = files => {
    if (files.length + this.state.attachments.length > 6) {
      this.setState({
        alertFiles: "You can load only 6 files\n"
      });
    } else {
      this.setState({
        alertFiles: "",
        attachments: this.state.attachments.concat(files)
      });
    }
  };

  getWarehouseOptions = () => {
    let res = [];
    this.state.warehouses.map(w => res.push({ value: w.id, label: w.adress }));
    return res;
  };

  handleWarehouseChange = selectedWarehouse => {
    this.setState({ warehouseId: selectedWarehouse.value });
  };

  loadOptions = (input, callback) => {
    if (!input || input.length < 3) {
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

  render() {
    const globalOptionalFields = [...this.state.optionalAttributes];
    let localOptionalFields = [];
    for (let i = 0; i < this.state.optionalAttributes.length; i++) {
      localOptionalFields.push(
        this.renderAdditionalAttribute(
          globalOptionalFields[i].type,
          globalOptionalFields[i].name,
          globalOptionalFields[i].multiple,
          globalOptionalFields[i].values,
          globalOptionalFields[i].mandatory,
          globalOptionalFields[i].immutable,
          i
        )
      );
    }

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
          <div className="container">
            <br />
            <br />
            <h2>Create order</h2>
            <br />
            {this.state.alert}

            <br />
            <h2>Title: </h2>

            <input
              className="form-control col-md-4"
              onChange={p => this.setState({ title: p.target.value })}
            />
            {/*   <form className="md-form"> */}
            <div className="form-row">
              <div className="form-group col-md-8">
                <label>Description</label>
                <textarea
                  className="form-control"
                  id="exampleFormControlTextarea1"
                  rows="5"
                  onChange={p => this.setState({ description: p.target.value })}
                />
              </div>
            </div>
            <div className="form-row">
              <div className="form-group col-md-5">
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
              <div className="form-group ">
                <p className="text-danger">{this.state.alertFiles}</p>
                <Dropzone
                  accept="image/*, .doc, .pdf, .docx"
                  onDrop={this.onPreviewDrop}
                >
                  Drop an image, get a preview!
                </Dropzone>
                <button
                  onClick={() => this.handleClearFiles()}
                  className={
                    "form-group col-md-10 btn btn-lg btn-outline-danger"
                  }
                >
                  Clear files
                </button>
              </div>
            </div>
            <Attachments attachments={this.state.attachments} />
            {localOptionalFields}
            <div className="form-row">
              <button
                className="form-group col-md-3 btn btn-lg btn-outline-success"
                onClick={() => this.handleSubmit()}
              >
                Send request
              </button>

              <button
                onClick={() => this.handleCancel()}
                className={"form-group col-md-3 btn btn-lg btn-outline-danger"}
              >
                Cancel
              </button>
            </div>
            {/* </form> */}
          </div>
        </div>
      </React.Fragment>
    );
  }
}
export default withRouter(CreateOrder);
