import React, { Component } from "react";
import Comment from "./Comment";
import ItemsList from "./ItemsList";
import ExecutorButtons from "./ExecutorButtons";
import CreatorButtons from "./CreatorButtons";
import Select from "react-select";
import ExecutorRefundButtons from "./ExecutorRefundButtons";
import ExecutorReplenishmentButtons from "./ExecutorReplenishmentButtons";
import CircularProgress from "@material-ui/core/CircularProgress";
import Button from "@material-ui/core/Button";
class OrderReview extends Component {
  constructor(props) {
    super(props);
    this.state = {
      requestId: 0,
      userId: 1,
      userRoles: [],
      creator: {},
      title: "",
      equipment: [],
      attributes: [],
      warehouse: 0,
      alert: "",
      executorAlert: "",
      type: "",
      status: "",
      creationDate: "01.02.1998",
      updatedDate: "01.03.1998",
      description: "",
      attachments: [],
      comments: [],
      executors: null,
      executorId: null,
      unavalibleEquipment: null
    };
    this.executorAlert = this.executorAlert.bind(this);
  }

  getExecutorsOptions = () => {
    let res = [];
    console.log(this.state.executors);
    this.state.executors.map(e =>
      res.push({
        value: e.id,
        label: e.firstName + " " + e.secondName + ", " + e.email
      })
    );
    return res;
  };

  executorAlert = value => {
    this.setState({ executorAlert: value });
    window.scrollTo(0, 0);
  };

  handleExecutorChange = selectedExecutor => {
    this.setState({ executorId: selectedExecutor.value });
  };

  handleBack = () => {
    this.props.history.push("/dashboard");
  };

  choseExecutor = () => {
    if (
      this.state.status === "On reviewing" &&
      this.state.userRoles.filter(role => role === "ROLE_ADMIN").length > 0
    ) {
      if (this.state.executors.length < 1) {
        return <h4>Executors: that warehouse haven`t executors</h4>;
      } else {
        if (this.state.executorId == null)
          this.setState({ executorId: this.state.executors[0].id });
        return (
          <div className="form-row">
            <div className="form-group col-md-4">
              <h4>Executor</h4>
              <Select
                onChange={this.handleExecutorChange}
                options={this.getExecutorsOptions()}
                value={{
                  value: this.state.executors[0].id,
                  label:
                    this.state.executors[0].firstName +
                    " " +
                    this.state.executors[0].secondName +
                    ", " +
                    this.state.executors[0].email
                }}
              />
            </div>
          </div>
        );
      }
    } else {
      if (
        this.state.executor != null &&
        typeof this.state.executor !== "undefined"
      ) {
        return (
          <div className="form-row">
            <div className="form-group">
              <h4>
                Executor:{" "}
                {this.state.executor.firstName +
                  " " +
                  this.state.executor.secondName}
              </h4>
            </div>
          </div>
        );
      } else {
        return (
          <div className="form-row">
            <div className="form-group">
              <h4>Executor: not chosen</h4>
            </div>
          </div>
        );
      }
    }
  };

  componentWillMount() {
    const { requestId } = this.props.match.params;
    this.setState({ requestId: requestId });

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
    this.loadRequestData(requestId);
  }

  loadRequestData = requestId => {
    fetch(`/request/${requestId}`, {
      method: "GET"
    })
      .then(response => response.json())
      .then(data => {
        console.log(data);
        this.setState({
          title: data.title,
          status: data.status,
          type: data.type,
          description: data.description,
          creationDate: data.creationDate,
          updatedDate: data.modifiedDate,
          warehouse: data.warehouse,
          creator: data.creator,
          executor: data.executor,
          attributes: data.attributes,
          comments: data.comments,
          equipment: data.equipment,
          isLoading: false
        });
      })
      .catch(error => console.log(error));
    this.loadUnavalibleEquipment(requestId);
  };

  additionalAttributes = () => {
    let localAttributes = this.state.attributes;
    localAttributes.sort(function(a, b) {
      return parseInt(a.order) - parseInt(b.order);
    });

    let renderAttributes = [];
    localAttributes.map(a => {
      renderAttributes.push({
        name: a.name,
        value: a.value.replace(/\|/g, ", ")
      });
    });

    return (
      <React.Fragment>
        {renderAttributes.map(a => (
          <h5 key={a.name}>
            {a.name}: {a.value}
          </h5>
        ))}
      </React.Fragment>
    );
  };

  loadUnavalibleEquipment = requestId => {
    console.log(requestId);
    fetch(`/equipment/unavalible/${requestId}`)
      .then(response => response.json())
      .then(data => {
        console.log(data);
        this.setState({
          unavalibleEquipment: data.list
        });
      })
      .catch(error => console.log(error));
  };

  buttonsSpace = () => {
    if (this.state.type.name === "order") {
      return (
        <React.Fragment>
          <ExecutorButtons
            userId={this.state.userId}
            userRoles={this.state.userRoles}
            status={this.state.status}
            requestId={this.state.requestId}
            executorId={this.state.executorId}
            validateComment={this.executorAlert}
          />
          {this.state.creator.id == this.state.userId && (
            <CreatorButtons
              requestId={this.state.requestId}
              status={this.state.status}
            />
          )}
        </React.Fragment>
      );
    } else if (this.state.type.name === "refund")
      return (
        <React.Fragment>
          <ExecutorRefundButtons
            userRoles={this.state.userRoles}
            status={this.state.status}
            requestId={this.state.requestId}
            executorId={this.state.executorId}
            validateComment={this.executorAlert}
          />
          {this.state.creator.id == this.state.userId && (
            <CreatorButtons
              requestId={this.state.requestId}
              status={this.state.status}
            />
          )}
        </React.Fragment>
      );
    else if (this.state.type.name === "replenishment") {
      let resultItems = [];
      this.state.equipment.map(equip =>
        resultItems.push({ id: equip.id, quantity: equip.quantity })
      );
      return (
        <React.Fragment>
          <ExecutorReplenishmentButtons
            userRoles={this.state.userRoles}
            status={this.state.status}
            requestId={this.state.requestId}
            executorId={this.state.executorId}
            validateComment={this.executorAlert}
            warehouseId={this.state.warehouse.id}
            resultItems={resultItems}
          />
          {this.state.creator.id == this.state.userId && (
            <CreatorButtons
              requestId={this.state.requestId}
              status={this.state.status}
            />
          )}
        </React.Fragment>
      );
    }
  };

  componentDidMount() {
    fetch(`/attachments/list/${this.props.match.params.requestId}`)
      .then(response => response.json())
      .then(data => {
        this.setState({ attachments: data.listFiles });
      })
      .catch(error => console.log(error));
  }

  renderLoader = () => {
    return (
      <React.Fragment>
        <br />
        <br />
        <br />
        <br />
        <div style={{ display: "flex", justifyContent: "center" }}>
          <CircularProgress />
        </div>
      </React.Fragment>
    );
  };

  loadExecutors = () => {
    if (
      this.state.status === "On reviewing" &&
      this.state.userRoles.filter(role => role === "ROLE_ADMIN").length > 0
    ) {
      fetch(`/request/executors/${this.state.warehouse.id}`)
        .then(response => response.json())
        .then(data => {
          this.setState({ executors: data.list });
          console.log("Executors", data);
        })
        .catch(error => console.log(error));
    }
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

  render() {
    if (
      this.state.userRoles.length == 0 ||
      this.state.status == "" ||
      this.state.unavalibleEquipment == null
    ) {
      return this.renderLoader();
    } else {
      if (this.state.executors == null) {
        this.loadExecutors();
      }
    }

    let attachmetsListLocal = "";
    if (
      this.state.attachments != null &&
      typeof this.state.attachments !== "undefined" &&
      this.state.attachments.length > 0
    ) {
      attachmetsListLocal = this.attachmentsList();
    }

    return (
      <React.Fragment>
        <div className="container">
          <div className="container">
            <h4 className="text-danger">{this.state.executorAlert}</h4>
            <Button
              variant="contained"
              color="secondary"
              onClick={this.handleBack}
            >
              Back
            </Button>
            <h2>Title: {this.state.title} </h2>
            <h4>
              Creator:{" "}
              {this.state.creator.firstName +
                " " +
                this.state.creator.secondName +
                ", " +
                this.state.creator.email}
            </h4>
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
              <div className="form-group">
                <h4>Description</h4>
                <p>{this.state.description}</p>
              </div>
            </div>

            <div className="form-row">
              <h4 className="form-group">
                Warehouse:
                <span className="m-2">{this.state.warehouse.adress}</span>
              </h4>
            </div>

            {this.state.executors != null &&
              this.state.type.name != "replenishment" &&
              this.choseExecutor()}
            <h3>Order items</h3>
            {this.state.unavalibleEquipment.length > 0 && (
              <h4 className="text-danger">
                This order has unavalibe equipment
              </h4>
            )}
            <ItemsList
              equipment={this.state.equipment}
              unavalibleEquipment={this.state.unavalibleEquipment}
            />
            <div className="form-row">
              <div className="form-group">{this.additionalAttributes()}</div>
            </div>
            {attachmetsListLocal}
            {this.state.status !== "" && this.buttonsSpace()}
            <br />
            {this.state.comments != null && this.renderComments()}
            <br />
          </div>
        </div>
      </React.Fragment>
    );
  }
}
export default OrderReview;
