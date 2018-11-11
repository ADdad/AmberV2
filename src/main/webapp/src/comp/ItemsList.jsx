import React, { Component } from "react";
class ItemsList extends Component {
  state = {};
  render() {
    return (
      <table className="table">
        <thead>
          <tr>
            <th scope="col">Model</th>
            <th scope="col">Producer</th>
            <th scope="col">Country</th>
            <th scope="col">Quantity</th>
          </tr>
        </thead>
        <tbody>
          {this.props.equipment.map(p => (
            <tr key={p.id}>
              <td>{p.model}</td>
              <td>{p.producer}</td>
              <td>{p.country}</td>
              <td>{p.quantity}</td>
            </tr>
          ))}
        </tbody>
      </table>
    );
  }
}

export default ItemsList;
