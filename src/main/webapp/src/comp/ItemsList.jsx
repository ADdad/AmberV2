import React, { Component } from "react";
class ItemsList extends Component {
  state = {};

  isUnavalible = equipmentId => {
    return (
      this.props.unavalibleEquipment.filter(item => item.id == equipmentId)
        .length > 0
    );
  };

  getUnavalibleQuantity = equipmentId => {
    let index = this.props.unavalibleEquipment.findIndex(
      e => e.id == equipmentId
    );
    return this.props.unavalibleEquipment[index].quantity;
  };

  renderUnavalibleColumn = equipmentId => {
    return (
      <td>
        {this.isUnavalible(equipmentId)
          ? this.getUnavalibleQuantity(equipmentId)
          : 0}
      </td>
    );
  };

  render() {
    return (
      <table className="table">
        <thead>
          <tr>
            <th scope="col">Model</th>
            <th scope="col">Producer</th>
            <th scope="col">Country</th>
            <th scope="col">Quantity</th>
            {this.props.unavalibleEquipment.length > 0 && (
              <th scope="col">Unavalible</th>
            )}
          </tr>
        </thead>
        <tbody>
          {this.props.equipment.map(p => (
            <tr key={p.id}>
              <td>{p.model}</td>
              <td>{p.producer}</td>
              <td>{p.country}</td>
              <td>{p.quantity}</td>
              {this.props.unavalibleEquipment.length > 0 &&
                this.renderUnavalibleColumn(p.id)}
            </tr>
          ))}
        </tbody>
      </table>
    );
  }
}

export default ItemsList;
