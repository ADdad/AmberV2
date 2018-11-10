import React, { Component } from "react";
import Comment from "./Comment";
import ItemsList from "./ItemsList";
import ExecutorButtons from "./ExecutorButtons";
class OrderReview extends Component {
  constructor(props) {
    super(props);
    this.state = {
      requestId: 0,
      userId: 1,
      userRoles: ["Admin", "User", "Keeper"],
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
        { name: "Additional", order: 1, value: "Test" },
        { name: "Additional2", order: 2, value: "Test" }
      ],
      warehouse: 0,
      type: "Order",
      status: "In progress",
      creationDate: "01.02.1998",
      updatedDate: "01.03.1998",
      description: "Adjkahskjdhs",
      attachments: [],
      comments: [
        {
          id: 1,
          authorName: "Admin",
          date: "01.05.2012",
          textComment: "Correct that"
        },
        {
          id: 2,
          authorName: "Andrew",
          date: "02.05.2012",
          textComment: "Corrected"
        }
      ],
      executors: [
        { id: 5, firstName: "Andrew", secondName: "Lobinski" },
        { id: 4, firstName: "Nan", secondName: "Kek" }
      ],
      executorId: 0
    };
  }

  //   componentDidMount() {
  //     const { requestId } = this.props.match.params;

  //     fetch("/userinfo")
  //       .then(response => response.json())
  //       .then(data => {
  //         this.setState({
  //           userId: data.id,
  //           userRoles: data.roles
  //         });
  //       })
  //       .catch(error => console.log(error));

  //     fetch(`/r_info/${requestId}`)
  //       .then(response => response.json())
  //       .then(data => {
  //         this.setState({
  //           title: data.title,
  //           status: data.status,
  //           type: data.type,
  //           description: data.description,
  //           creationDate: data.creation_date,
  //           updatedDate: data.modified_date,
  //           warehouse: data.warehouse,
  //           executors: data.executors,
  //           equipment: data.equipment,
  //           attributes: data.attributes,
  //           attachments: data.attachments,
  //           comments: data.comments,
  //           requestId: requestId,
  //           isLoading: false
  //         });
  //       })
  //       .catch(error => console.log(error));
  //   }

  //   handleSubmit = () => {
  //     fetch("/postOrder", {
  //       method: "POST",
  //       body: JSON.stringify(this.state),
  //       headers: {
  //         "Content-Type": "application/json"
  //       }
  //     })
  //       .then(res => res.json())
  //       .then(response => console.log("Success:", JSON.stringify(response)))
  //       .catch(error => console.error("Error:", error));
  //   };
  render() {
    let equipment = [];
    // let attachments = [];
    // for (let i = 0; i < 3; i++) {
    //   attachments.push(<li key={i}>Attachment {i}</li>);
    // }
    // for (let i = 0; i < this.state.num; i++) {
    //   items.push(
    //     <div key={i} className="form-row">
    //       <div className="form-group col-md-8">
    //         <label>Item</label>
    //         <select
    //           className="form-control"
    //           onChange={e => {
    //             let item = this.state.item;
    //             item.itemType = e.target.value;
    //             this.setState({ item });
    //           }}
    //         >
    //           {this.state.items.map(p => (
    //             <option key={p} value={p}>
    //               {p}
    //             </option>
    //           ))}
    //         </select>
    //       </div>
    //       <div className="form-group col-md-4">
    //         <label>Quantity</label>
    //         <input
    //           id="quantity"
    //           className="form-control"
    //           type="number"
    //           min="0"
    //           step="1"
    //           data-bind="value:replyNumber"
    //           onChange={e => {
    //             let item = this.state.item;
    //             item.itemQuantity = e.target.value;
    //             this.setState({ item });
    //             console.log(this.state.item);
    //           }}
    //         />
    //       </div>
    //     </div>
    //   );
    // }
    return (
      <React.Fragment>
        <div className="container">
          <div className="container">
            <br />
            <br />
            <br />
            <br />
            <br />
            <h2>Title: {this.state.status} </h2>
            <input
              className="form-control col-md-4"
              onChange={p => this.setState({ status: p.target.value })}
              hidden
            />
            <h4 className="form-group">
              Status:
              <span className="badge badge-primary m-2">
                {this.state.status}
              </span>
            </h4>
            <h4>
              Type:{" "}
              <span className="badge badge-info m-2">{this.state.type}</span>{" "}
            </h4>
            {/* <form className="md-form"> */}
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
              <div className="form-group">
                <h4>Executor</h4>
                <select
                  className="form-control"
                  onChange={e => this.setState({ executorId: e.target.value })}
                >
                  {this.state.executors.map(p => (
                    <option key={p.id} value={p.id}>
                      {p.firstName + " " + p.secondName}
                    </option>
                  ))}
                </select>
              </div>
            </div>
            <h3>Order items</h3>
            <ItemsList equipment={this.state.equipment} />

            <ExecutorButtons status={this.state.status} />
            {/*attachments*/}
            <br />
            <br />
            <h2>Comments</h2>
            {this.state.comments.map(comment => (
              <Comment
                key={comment.id}
                date={comment.date}
                authorName={comment.authorName}
                textComment={comment.textComment}
              />
            ))}
            {/* </form> */}
          </div>
        </div>
      </React.Fragment>
    );
  }
}
export default OrderReview;
