import React, { Component } from "react";
import { withRouter } from "react-router-dom";
import { RadioGroup, Radio } from "react-radio-group";
import { Checkbox, CheckboxGroup } from "react-checkbox-group";
class CreateOrder extends Component {
  constructor(props) {
    super(props);
    this.state = {
      title: "",
      myItems: [
        { id: "2das", name: "model1, producer1" },
        { id: "5das", name: "model2, producer1" }
      ],
      resultItems: [{ itemId: 0, quantity: 0 }],
      warehouses: [1, 2, 3, 4, 5],
      warehouse: 0,
      type: "",
      description: "",
      attachments: [],
      optionalAttributes: [
        {
          name: "TestRadio",
          type: "radio",
          multiple: true,
          immutable: false,
          values: ["Test1", "Test2", "Test3"]
        },
        {
          name: "TestCheckbox",
          type: "checkbox",
          multiple: true,
          immutable: false,
          values: ["Test1", "Test2", "Test3"]
        },
        {
          name: "TestDate",
          type: "date",
          multiple: false,
          immutable: false,
          values: []
        },
        {
          name: "TestText",
          type: "Text",
          multiple: false,
          immutable: false,
          values: []
        }
      ],
      oAttributesValues: []
    };
  }

  resultItemEdit = (i, e) => {
    let readyItems = this.state.resultItems.slice();
    let editObject = readyItems[i];
    editObject.itemId = e.target.value;
    readyItems[i] = editObject;
    this.setState({ resultItems: readyItems });
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

  handleAdd = () => {
    let readyItems = [
      ...this.state.resultItems.slice(),
      { itemId: 0, quantity: 0 }
    ];
    this.setState({ resultItems: readyItems });
  };

  handleDelete = () => {
    let readyItems = this.state.resultItems.slice(0, -1);
    this.setState({ resultItems: readyItems });
  };

  handleCancel = () => {
    this.props.history.push("/dashboard");
  };

  handleAddAddAttachment = event => {
    console.log(event.target.files[0]);
    const files1 = Array.from(event.target.files);
    this.setState({ attachments: files1 });
  };
  handleSubmit = () => {
    fetch("/postOrder", {
      method: "POST",
      body: JSON.stringify(this.state),
      headers: {
        "Content-Type": "application/json"
      }
    })
      .then(res => res.json())
      .then(response => console.log("Success:", JSON.stringify(response)))
      .catch(error => console.error("Error:", error));
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
          globalOptionalFields[i].immutable,
          i
        )
      );
    }

    let items = [];
    for (let i = 0; i < this.state.resultItems.length; i++) {
      items.push(
        <div key={i} className="form-row">
          <div className="form-group col-md-8">
            <label>Item</label>
            <select
              className="form-control"
              onChange={e => this.resultItemEdit(i, e)}
            >
              {this.state.myItems.map(p => (
                <option key={p.id} value={p.id}>
                  {p.name}
                </option>
              ))}
            </select>
          </div>
          <div className="form-group col-md-4">
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
            <br />
            <br />
            <h2>Title: </h2>
            <input
              className="form-control col-md-4"
              onChange={p => this.setState({ status: p.target.value })}
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
                  onChange={e => this.setState({ warehouse: e.target.value })}
                >
                  {this.state.warehouses.map(p => (
                    <option key={p} value={p}>
                      {p}
                    </option>
                  ))}
                </select>
              </div>
            </div>
            {items}
            <button
              onClick={() => this.handleAdd()}
              className="form-group col-md-3 btn btn-lg btn-outline-primary"
            >
              Add
            </button>
            <button
              onClick={() => this.handleDelete()}
              className={
                this.state.num <= 0
                  ? "form-group col-md-3 btn btn-lg btn-outline-danger disabled"
                  : "form-group col-md-3 btn btn-lg btn-outline-danger"
              }
            >
              Delete
            </button>

            <div className="form-row">
              <button className="form-group col-md-3 btn btn-lg btn-outline-default">
                Add file{" "}
                <input
                  type="file"
                  multiple
                  hidden
                  onChange={e => this.handleAddAddAttachment(e)}
                />
              </button>
            </div>
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
