import React, { Component } from "react";
import { withRouter } from "react-router-dom";
class CreateOrder extends Component {
  constructor(props) {
    super(props);
    this.state = {
      title: "",
      fields: [],
      myItems: [
        { id: "2das", name: "model1, producer1" },
        { id: "5das", name: "model2, producer1" }
      ],
      resultItems: [{ itemId: 0, quantity: 0 }],
      item: {},
      warehouses: [1, 2, 3, 4, 5],
      items: [1, 2, 3, 4, 5],
      num: 1,
      roles: ["User", "Keeper", "Admin"],
      Warehouse: 0,
      type: "",
      creationDate: "",
      updatedDate: "",
      description: "",
      attachments: [],
      comments: []
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

  renderAdditionalAttribute = (type, name, multiple, values, immutable) => {
    // switch(type) {
    //   case "select": {
    //       return (
    //         <div className="form-row">
    //         <div className="form-group">
    //         </div>
    //         </div>
    //       );
    //   }
    //   case "textarea": {
    //     <div className="form-row">
    //     <div className="form-group">
    //     </div>
    //     </div>
    //   }
    //   default: {
    //     <div className="form-row">
    //     <div className="form-group">
    //     </div>
    //     </div>
    //   }
    // }
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
                  onChange={e => this.setState({ Warehouse: e.target.value })}
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
              <label className="form-group col-md-3 btn btn-lg btn-outline-default">
                Add file{" "}
                <input
                  type="file"
                  multiple
                  hidden
                  onChange={e => this.handleAddAddAttachment(e)}
                />
              </label>
            </div>
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
