import React, { Component } from "react";
import "../css/Comments.css";
class Comment extends Component {
  state = {};
  render() {
    return (
      <div className="media-body u-shadow-v18 g-bg-secondary g-pa-30 col-md-12 border border-secondary rounded m-2">
        <div className="g-mb-15">
          <h5 className="h5 g-color-gray-dark-v1 mb-0">
            {this.props.authorName}
          </h5>
          <span className="g-color-gray-dark-v4 g-font-size-12">
            {this.props.date}
          </span>
        </div>

        <p>{this.props.textComment}</p>
      </div>
    );
  }
}

export default Comment;
