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
      userId: "",
      alert: "",
      title: "",
      myItems: [
        { id: "2das", model: "model1", producer: "producer1", country: "Ukr" },
        { id: "5das", model: "model2", producer: "producer1", country: "Ukr" }
      ],
      resultItems: [],
      viewItems: [],
      warehouses: [
        { id: "djsal", adress: "dsajkljlk" },
        { id: "djz", adress: "kdl" }
      ],
      warehouse: "",
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
          type: "checkbox",
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
      resultOptionalAttributes: []
    };
  }

  componentDidMount() {
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
          isLoading: false
        });
      })
      .catch(error => console.error("Error:", error));
  }

  handleSubmit = () => {
    const readyAttributes = this.compileAdditionalAttributes();
    fetch("/request/save", {
      method: "POST",
      body: JSON.stringify({
        creatorId: this.state.userId,
        title: this.state.title,
        description: this.state.description,
        type: this.state.type,
        warehouseId: this.state.warehouse,
        items: this.state.resultItems,
        attributes: readyAttributes
      }),
      headers: {
        "Content-Type": "application/json"
      }
    })
      .then(res => {
        if (res.status == 200) {
          this.setState({ alert: "Succesfuly ordered" });
          console.log("Success:", JSON.stringify(res));
          console.log(res.status);
        } else {
          this.setState({ alert: "Something went wrong" });
          console.log(JSON.stringify(res));
        }
      })
      .catch(error => {
        console.error("Error:", error);
        this.setState({ alert: "Bad idea" });
      });
  };

  compileAdditionalAttributes = () => {
    let readyAttributes = [];
    for (let i = 0; i < this.state.attributes; i++) {
      readyAttributes.push({
        id: this.state.attributes[i].id,
        value: this.state.oAttributesValues[i]
      });
    }
    return readyAttributes;
  };

  getItemsOptions1 = () => {
    let items = this.state.myItems.slice();
    let result = [];
    items.map(i => result.push({ label: this.itemName(i), value: i.id }));
    return result;
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
    immutable,
    index
  ) => {
    switch (type) {
      case "select": {
        return (
          <div className="form-row">
            <div className="form-group">
              <label>{name}: </label>
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

  returnButton = () => {
    return (
      <button
        className="form-group col-md-3 btn btn-lg btn-outline-success"
        onClick={this.context.router.history.push(`/dashboard`)}
      >
        To main page
      </button>
    );
  };

  handleClearFiles = () => {
    this.setState({ attachments: [] });
  };

  handleCancel = () => {
    this.props.history.push("/dashboard");
  };

  onPreviewDrop = files => {
    this.setState({
      attachments: this.state.attachments.concat(files)
    });
  };

  searchEquipment = inputValue => {
    console.log(inputValue);
    let result = [];
    fetch(`/request/equipment/find/${inputValue}`)
      .then(res => res.json())
      .then(response => {
        console.log(response);
        result = response.equipment;
      })
      .catch(error => console.error("Error:", error));
    console.log(result);
    return this.getItemsOptions(result);
  };

  loadOptions = (input, callback) => {
    if (!input) {
      return callback([
        { label: "Delhi", value: "DEL" },
        { label: "Mumbai", value: "MUM" }
      ]);
    }
    console.log(input);
    return fetch(`/request/equipment/find/${input}`)
      .then(response => {
        return response.json();
      })
      .then(json => {
        let options = [];
        options = this.getItemsOptions(json.equipment);
        console.log(json.equipment);
        console.log("DATA:", options);
        return callback(options);
      });
  };

  render() {
    // if (this.state.resultItems.length === 0) {
    //   this.setState({
    //     resultItems: [{ id: this.state.myItems[0].id, quantity: 0 }]
    //   });
    // }
    const globalOptionalFields = [...this.state.optionalAttributes];
    let localOptionalFields = [];
    for (let i = 0; i < this.state.optionalAttributes.length; i++) {
      localOptionalFields.push(
        this.renderAdditionalAttribute(
          globalOptionalFields[i].type,
          globalOptionalFields[i].name,
          globalOptionalFields[i].multiple,
          globalOptionalFields[i].values,
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
            <label>{this.state.alert}</label>
            {/*this.returnButton*/}
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
              <div className="form-group">
                <label>Warehouse</label>
                <select
                  className="form-control"
                  onChange={p => this.setState({ warehouse: p.target.value })}
                >
                  {this.state.warehouses.map(p => (
                    <option key={p.id} value={p.id}>
                      {p.adress}
                    </option>
                  ))}
                </select>
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
            <div className="form-row">
              <div className="form-group col-md-11">
                <AsyncSelect
                  cacheOptions
                  defaultOptions
                  loadOptions={this.loadOptions}
                  onChange={this.addItem}
                />
              </div>
            </div>
            <div className="form-row">
              <div className="form-group ">
                <Dropzone
                  accept="image/*,.doc,.pdf"
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
