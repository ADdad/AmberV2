import React, { Component } from "react";
import Comment from "./Comment";
import ItemsList from "./ItemsList";
import ExecutorButtons from "./ExecutorButtons";
import CreatorButtons from "./CreatorButtons";
import Select from "react-select";
class OrderReview extends Component {
  constructor(props) {
    super(props);
    this.state = {
      requestId: 0,
      userId: 2,
      userRoles: ["ROLE_ADMIN", "User", "ROLE_KEEPER"],
      creator: { id: 2, firstName: "Den", secondName: "Star" },
      title: "Example1",
      equipment: [
        {
          id: 1,
          country: "Ukraine",
          producer: "Roshen",
          model: "Candy",
          quantity: 20
        },
        {
          id: 2,
          country: "Ukraine",
          producer: "Roshen",
          model: "Marshmello",
          quantity: 26
        }
      ],
      attributes: [
        { name: "Additional", order: 3, values: ["Test"] },
        { name: "Additional2", order: 2, values: ["Test", "Test2"] }
      ],
      warehouse: 0,
      alert: "",
      type: "Order",
      status: "On reviewing",
      creationDate: "01.02.1998",
      updatedDate: "01.03.1998",
      description: "Adjkahskjdhs",
      attachments: [],
      comments: null,
      executors: [
        {
          id: "djlkas",
          firstName: "jdlkasd",
          secondName: "djklsa",
          email: "Jjlk"
        }
      ],
      executorId: null,
      mode: ""
    };
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

  handleExecutorChange = selectedExecutor => {
    this.setState({ executorId: selectedExecutor.value });
  };

  choseExecutor = () => {
    if (
      this.state.status === "On reviewing" &&
      this.state.userRoles.includes("ROLE_ADMIN") &&
      this.state.mode === "view"
    ) {
      if (this.state.executors.length < 1) {
        return <h4>Executors: that warehouse haven`t executors</h4>;
      } else {
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
    const { mode } = this.props.match.params;
    this.setState({ requestId: requestId, mode: mode });

    fetch("/userinfo")
      .then(response => response.json())
      .then(data => {
        this.setState({
          userId: data.id,
          userRoles: data.roles,
          requestId: requestId
        });
      })
      .catch(error => console.log(error));

    fetch(`/request/info/${requestId}`)
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
        this.loadExecutors();
      })
      .catch(error => console.log(error));
  }

  additionalAttributes = () => {
    let localAttributes = this.state.attributes;
    localAttributes.sort(function(a, b) {
      return parseInt(a.order) - parseInt(b.order);
    });

    let renderAttributes = [];
    localAttributes.map(a => {
      renderAttributes.push({ name: a.name, value: a.value });
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

  buttonsSpace = () => {
    switch (this.state.mode) {
      case "view": {
        return (
          <ExecutorButtons
            userRoles={this.state.userRoles}
            status={this.state.status}
            requestId={this.state.requestId}
            executorId={this.state.executorId}
          />
        );
      }
      case "created": {
        if (this.state.creator.id == this.state.userId)
          return <CreatorButtons status={this.state.status} />;
        else return <h3>Sorry, you can just view that</h3>;
      }
      default: {
        return <h3>Sorry, you can just view that</h3>;
      }
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

  loadExecutors = () => {
    if (
      this.state.status === "On reviewing" &&
      this.state.userRoles.includes("ROLE_ADMIN") &&
      this.state.mode === "view"
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
    if (
      this.state.attachments != null &&
      typeof this.state.attachments !== "undefined" &&
      this.state.comments.length > 0
    ) {
      return (
        <div className="form-row">
          <div className="form-group">
            <h2>Comments</h2>
            {this.state.comments.map(comment => (
              <Comment
                key={comment.id}
                date={comment.creationDate}
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
            <br />
            <br />
            <br />
            <br />
            <br />
            <h2>Title: {this.state.title} </h2>
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
                  Creation Date: {this.state.creationDate}
                </label>
              </div>
              <div className="form-group mr-2">
                <label className="">
                  {" "}
                  Updated Date: {this.state.updatedDate}
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

            {this.state.executors != null && this.choseExecutor()}
            <h3>Order items</h3>
            <ItemsList equipment={this.state.equipment} />
            <div className="form-row">
              <div className="form-group">{this.additionalAttributes()}</div>
            </div>
            {attachmetsListLocal}
            {this.state.status !== "" && this.buttonsSpace()}
            <br />
            {this.comments != null && this.renderComments()}
            <br />
          </div>
        </div>
      </React.Fragment>
    );
  }
}
export default OrderReview;
