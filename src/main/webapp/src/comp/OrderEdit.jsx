import React, { Component, Fragment } from "react";
import { withRouter } from "react-router-dom";
import { RadioGroup, Radio } from "react-radio-group";
import { Checkbox, CheckboxGroup } from "react-checkbox-group";
import Attachments from "./Attachments";
import Dropzone from "react-dropzone";
import Select from "react-select";
import AsyncSelect from "react-select/lib/Async";
import Comment from "./Comment";
import { debounce } from "lodash";

class OrderEdit extends Component {
  constructor(props) {
    super(props);
    this.state = {
      requestId: 0,
      initialized: false,
      userId: "",
      orderId: "",
      alert: "",
      title: "",
      myItems: [],
      resultItems: [],
      viewItems: [],
      warehouses: [],
      warehouseId: "",
      warehouse: 0,
      type: "",
      description: "",
      attachments: [],
      newAttachments: [],
      optionalAttributes: [],
      oAttributesValues: [],
      resultOptionalAttributes: [],
      alertFiles: "",
      comments: null,
      executors: null,
      creationDate: "01.02.1998",
      updatedDate: "01.03.1998",
      status: "",
      equipment: []
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
    if (this.state.type.name === "refund") {
      let localEquipment = [...this.state.equipment];
      if (resultItemsLocal.length > localEquipment.length) {
        localAlert += "You can`t refund more than have\n";
        validated = false;
      } else {
        for (let i = 0; i < resultItemsLocal.length; i++) {
          let index = localEquipment.findIndex(
            p => p.id === resultItemsLocal[i].id
          );
          if (index < 0) {
            localAlert += "You can`t refund items not from order\n";
            validated = false;
            break;
          }
          if (localEquipment[index].quantity < resultItemsLocal[i].quantity) {
            localAlert += "You can`t refund more than have\n";
            validated = false;
            break;
          }
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

  resetEquipment = localEquip => {
    let readyItems = [];
    let viewLoc = [];
    for (let i = 0; i < localEquip.length; i++) {
      readyItems.push({
        id: localEquip[i].id,
        quantity: localEquip[i].quantity
      });
      viewLoc.push(this.itemName(localEquip[i]));
    }
    this.setState({ resultItems: readyItems, viewItems: viewLoc });
  };

  renderResetItemsButton = () => {
    return (
      <div className="form-group col-md-3">
        <button
          onClick={() => this.resetEquipment(this.state.equipment)}
          className="btn btn-lg btn-outline-danger"
        >
          ResetItems
        </button>
      </div>
    );
  };

  componentWillMount() {
    const { requestId } = this.props.match.params;
    this.setState({ requestId: requestId });
    this.loadEquipment(this.state.equipment);
    fetch("/userinfo")
      .then(response => response.json())
      .then(data => {
        let roles = [];
        data.roles.map(r => roles.push(r.name));
        this.setState({
          userId: data.id,
          userRoles: roles,
          requestId: requestId
        });
      })
      .catch(error => console.log(error));

    fetch(`/request/${requestId}`, {
      method: "GET"
    })
      .then(response => response.json())
      .then(data => {
        this.setState({
          title: data.title,
          status: data.status,
          type: data.type,
          description: data.description,
          creationDate: data.creationDate,
          updatedDate: data.modifiedDate,
          warehouse: data.warehouse,
          warehouseId: data.warehouse.id,
          creator: data.creator,
          executor: data.executor,
          attributes: this.sortAttributes(data.attributes),
          comments: data.comments,
          equipment: data.equipment,
          isLoading: false
        });
        if (data.status != "Rejected" && data.status != "Opened")
          this.props.history.push("/dashboard");
        this.loadRefundData(data.type.name);
        this.loadEquipment(data.equipment);
        this.loadAdditionaAtrributes(data.type, data.connectedRequest);
      })
      .catch(error => console.log(error));
  }

  loadRefundData = (type, connectedRequest) => {
    if (type === "refund") {
      fetch(`/request/${connectedRequest}`, {
        method: "GET"
      })
        .then(response => response.json())
        .then(data => {
          this.setState({
            connectedRequest: data.id,
            equipment: data.equipment,
            isLoading: false
          });
          if (
            data.status != "Completed" ||
            data.creator.id != this.state.userId
          )
            this.props.history.push("/dashboard");
          this.loadEquipment(data.equipment);
        })
        .catch(error => console.log(error));
    }
  };

  componentDidMount() {
    this.setState({ isLoading: true });

    fetch(`/attachments/list/${this.props.match.params.requestId}`)
      .then(response => response.json())
      .then(data => {
        this.setState({ attachments: data.listFiles });
      })
      .catch(error => console.log(error));
  }

  loadAdditionaAtrributes = type => {
 
    fetch(`/request/create/${type.name}`)
      .then(res => res.json())
      .then(response => {
        this.setState({
          optionalAttributes: this.sortAttributes(response.attributes),
          warehouses: response.warehouses,
          myItems: response.equipment,
          warehouse: response.warehouses[0].id,
          isLoading: false,
          initialized: true
        });
      })
      .catch(error => console.error("Error:", error));
  };

  handleSubmit = () => {
    if (this.validate()) {
      const readyAttributes = this.compileAdditionalAttributes();
      fetch("/request", {
        method: "PUT",
        body: JSON.stringify({
          requestId: this.state.requestId,
          creatorId: this.state.userId,
          title: this.state.title,
          description: this.state.description,
          type: this.state.type.name,
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
          if (this.state.newAttachments.length > 0) {
            this.uploadFiles(this.state.requestId);
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
    let files = this.state.newAttachments;
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
      let value = this.state.oAttributesValues[i];
      if (
        typeof value !== "undefined" &&
        value != null &&
        value.length > 1 &&
        value.charAt(value.length - 1) === "|"
      ) {
        value = value.substring(0, value.length - 1);
      }
      readyAttributes.push({
        id: this.state.optionalAttributes[i].id,
        type: this.state.optionalAttributes[i].type,
        value: value
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
              loadOptions={debounce(this.loadOptions, 500)}
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

  loadEquipment = localEquip => {
    let localItemsOptions = this.getItemsOptions(localEquip);
    localItemsOptions.map(e => this.addItem(e));

    let readyItems = this.state.resultItems.slice();
    for (let i = 0; i < readyItems.length; i++) {
      readyItems[i].quantity = localEquip[i].quantity;
    }
    this.setState({ resultItems: readyItems });
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

  resultQuantityEdit = (i, e) => {
    let readyItems = this.state.resultItems.slice();
    let editObject = readyItems[i];
    editObject.quantity = e.target.value > -1 ? e.target.value : 0;
    readyItems[i] = editObject;
    this.setState({ resultItems: readyItems });
  };

  renderAdditionalAttribute = (
    id,
    type,
    name,
    multiple,
    values,
    mandatory,
    immutable,
    index
  ) => {
    if (mandatory) name += "*";
    let chosenAttributes = this.state.attributes;
    if (chosenAttributes != null) {
      let index = chosenAttributes.findIndex(p => p.id === id);
      if (index > -1) {
        name += " chosen " + chosenAttributes[index].value.replace(/\|/g, ", ");
        let index2 = this.state.optionalAttributes.findIndex(a => a.id === id);
        if (this.state.oAttributesValues[index2] == null && mandatory) {
          this.changeOptionalValue(index2, chosenAttributes[index].value);
        }
      }
    }

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
    this.setState({ newAttachments: [] });
  };

  handleCancel = () => {
    this.props.history.push("/dashboard");
  };

  onPreviewDrop = files => {
    if (files.length + this.state.newAttachments.length > 6) {
      this.setState({
        alertFiles: "You can load only 6 files\n"
      });
    } else {
      this.setState({
        alertFiles: "",
        newAttachments: this.state.newAttachments.concat(files)
      });
    }
  };

  attachmentsList = () => {
    return (
      <table className="table">
        <thead>
          <tr>
            <th scope="col">File name</th>
            <th scope="col">File size</th>
            <th scope="col">
              <button
                className="btn btn-lg btn-outline-danger"
                onClick={this.handleDownloadAll}
              >
                Download all
              </button>
            </th>
          </tr>
        </thead>
        <tbody>
          {this.state.attachments.map(p => (
            <tr key={p.filename}>
              <td>{p.filename}</td>
              <td>{p.fileSize}</td>
              <td>
                <button
                  className="btn btn-lg btn-outline-primary"
                  onClick={() => {
                    this.handleDownloadFile(p.filename);
                  }}
                >
                  Download
                </button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    );
  };

  handleDownloadFile = name => {
    fetch(`/attachments/${this.state.requestId}?filename=${name}`).then(
      response => {
        const filename = response.headers
          .get("Content-Disposition")
          .split("filename=")[1];
        response.blob().then(blob => {
          let url = window.URL.createObjectURL(blob);
          let a = document.createElement("a");
          a.href = url;
          a.download = filename;
          a.click();
        });
      }
    );
  };

  handleDownloadAll = () => {
    fetch(`/attachments/zip/${this.state.requestId}`).then(response => {
      const filename = response.headers
        .get("Content-Disposition")
        .split("filename=")[1];
      response.blob().then(blob => {
        let url = window.URL.createObjectURL(blob);
        let a = document.createElement("a");
        a.href = url;
        a.download = filename;
        a.click();
      });
    });
  };

  renderComments = () => {
    if (this.state.comments.length > 0) {
      return (
        <div className="form-row">
          <div className="form-group">
            <h2>Comments</h2>
            {this.state.comments.map(comment => (
              <Comment
                key={comment.id}
                date={comment.creationDate.substr(0, 16).replace("T", "/")}
                authorName={comment.user.email}
                textComment={comment.text}
              />
            ))}
          </div>
        </div>
      );
    } else return <div />;
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
    let attachmetsListLocal = "";
    if (
      this.state.attachments != null &&
      typeof this.state.attachments !== "undefined" &&
      this.state.attachments.length > 0
    ) {
      attachmetsListLocal = this.attachmentsList();
    }

    let localOptionalFields = [];
    if (this.state.type != null && this.state.optionalAttributes.length > 0) {
      const globalOptionalFields = [...this.state.optionalAttributes];

      for (let i = 0; i < this.state.optionalAttributes.length; i++) {
        localOptionalFields.push(
          this.renderAdditionalAttribute(
            globalOptionalFields[i].id,
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
            <h2>Edit order</h2>
            <br />
            {this.state.alert}

            <br />
            <h2>Title: {this.state.title}</h2>

            <input
              className="form-control col-md-4"
              onChange={p => this.setState({ title: p.target.value })}
            />
            <h4 className="form-group">
              Status:
              <span className="badge badge-primary m-2">
                {this.state.status}
              </span>
            </h4>
            <h4>
              Type:{" "}
              <span className="badge badge-info m-2">
                {this.state.type.name}
              </span>{" "}
            </h4>
            <div className="form-row">
              <div className="form-group mr-2">
                <label className="">
                  Creation Date:{" "}
                  {this.state.creationDate.substr(0, 16).replace("T", "/")}
                </label>
              </div>
              <div className="form-group mr-2">
                <label className="">
                  {" "}
                  Updated Date:{" "}
                  {this.state.updatedDate.substr(0, 16).replace("T", "/")}
                </label>
              </div>
            </div>
            <div className="form-row">
              <div className="form-group col-md-8">
                <label>Description</label>
                <p>{this.state.description}</p>
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
                <span className="m-2">{this.state.warehouse.adress}</span>
                <Select
                  onChange={this.handleWarehouseChange}
                  options={this.getWarehouseOptions()}
                />
              </div>
            </div>

            <div className="form-row">
              <div
                className={
                  this.state.type.name === "refund"
                    ? "form-group col-md-6"
                    : "form-group col-md-9"
                }
              >
                <h3>Items</h3>
              </div>
              {this.state.type.name === "refund" &&
                this.renderResetItemsButton()}
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

            <h4>All old attachments will be cleaned</h4>
            <h4>Old attachments</h4>
            {attachmetsListLocal}

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

            <Attachments attachments={this.state.newAttachments} />

            {this.state.type != null &&
              this.state.optionalAttributes.length > 0 &&
              localOptionalFields}
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
            <br />
            {this.state.comments != null && this.renderComments()}
            <br />
          </div>
        </div>
      </React.Fragment>
    );
  }
}
export default withRouter(OrderEdit);
