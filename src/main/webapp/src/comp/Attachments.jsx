import React, { Component } from "react";
class Attachments extends Component {
  state = {};
  render() {
    const previewStyle = {
      display: "inline",
      width: 100,
      height: 100
    };

    let title = "";
    if (this.props.newAttachments.length > 0) title = <h3>Attachments</h3>;
    else title = "";
    return (
      <React.Fragment>
        {title}
        <div className="form-row">
          {this.props.newAttachments.length > 0 && (
            <React.Fragment>
              {this.props.newAttachments.map(file => (
                <figure className="form-group">
                  <img
                    alt="Preview"
                    key={file.preview}
                    src={URL.createObjectURL(file)}
                    style={previewStyle}
                    onError={e => {
                      e.target.onerror = null;
                      e.target.src = "/img/ico-doc.png";
                    }}
                  />
                  <figcaption>{file.name.substring(0, 10) + "..."}</figcaption>
                </figure>
              ))}
            </React.Fragment>
          )}
        </div>
      </React.Fragment>
    );
  }
}

export default Attachments;
