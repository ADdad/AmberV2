import React, { Component } from "react";
import Select from "react-select";
class ReportsPage extends Component {
    constructor(props) {
        super(props);
        this.state = {
            userRoles: ['ROLE_ADMIN'],
            isLoading: false,
            reportTypeId : 0,
            warehouses : [{"id": 1, "address" : 'Kyiv'},{"id" : 2, "address": 'Lvov'}],
            reportRows : [],
            showReport : false
        }
    }

    componentDidMount() {
        fetch('/userinfo')
            .then(response => response.json())
            .then(data => {
                this.setState({userRoles: data.roles, isLoading:false })
            })
            .catch(error => console.log(error))
        if((!this.state.userRoles.includes('ROLE_ADMIN') && !this.state.userRoles.includes('ROLE_KEEPER'))){
            this.props.history.push('/errorpage');
        }
    }


    handleReportTypeChange = (event) => {
        //let e = document.getElementById("reportType");
        this.setState({reportTypeId : event.target.value});
    }
    getWarehouseOptions = () => {
        let res = [];
        this.state.warehouses.map(w => res.push({ value: w.id, label: w.address }));
        return res;
    };

    loadMoreWarehouses = () => {

    }

    handleGenerateReport = () => {
        this.setState({reportRows : [{"equipmentModel":"model1","equipmentProducer":"producer1","equipmentCountry":"Ukraine","quantity":15},
                {"equipmentModel":"model4","equipmentProducer":"producer1","equipmentCountry":"Poland","quantity":10},
                {"equipmentModel":"model3","equipmentProducer":"producer3","equipmentCountry":"United Kingdom","quantity":20}],
            showReport : true})
    }

    renderAdditionalInputs = () => {
        if(this.state.reportTypeId == 1 || this.state.reportTypeId == 2){
            return (
                <React.Fragment>
                <div className="col-3">
                    <label>Select warehouse:</label>
                    <select className="form-control" >
                        {this.state.warehouses.map(w => (
                            <option value={w.id}>{w.address}</option>
                        ))}
                        <option onClick={this.loadMoreWarehouses()}>Load more</option>
                    </select>
                    <br/>

                </div>
                <div className="col-3 ">
                    <button onClick={this.handleGenerateReport} className="btn btn-outline-primary height-center">Generate report</button>
                </div>
                </React.Fragment>
            );
        } else if (this.state.reportTypeId == 3) {
            return (
                <React.Fragment>
                <div className="col-3">
                    <label>Select warehouse:</label>
                    <select className="form-control" >
                        {this.state.warehouses.map(w => (
                            <option value={w.id}>{w.address}</option>
                        ))}
                        <option onClick={this.loadMoreWarehouses()}>Load more</option>
                    </select>



                </div>
                    <div className="col-3">
                        <label>Minimum number of equipment:</label>
                        <input className="form-control" type="number" defaultValue="1" />
                    </div>
                    <div className="col-3">
                        <button onClick={this.handleGenerateReport} className="btn btn-outline-primary height-center">Generate report</button>
                    </div>
                </React.Fragment>
            );
        }
    }

    renderReport = () => {
        if((this.state.reportTypeId == 1 || this.state.reportTypeId == 2) && this.state.showReport) {
            return (
                <table className="table">
                    <thead>
                    <tr>
                        <th>Model</th>
                        <th>Quantity</th>
                        <th>Producer</th>
                        <th>Country</th>
                    </tr>
                    </thead>
                    <tbody>
                    {this.state.reportRows.map(r => (
                        <tr>
                            <td>{r.equipmentModel}</td>
                            <td>{r.quantity}</td>
                            <td>{r.equipmentProducer}</td>
                            <td>{r.equipmentCountry}</td>
                        </tr>
                    ))}
                    </tbody>
                </table>
            );
        } else {
            return (<div></div>)
        }
    }


    render() {

        if (this.state.isLoading) {
            return <p>Loading ...</p>;
        }
        return (
            <React.Fragment>
                <br/>
                <br/>
                <br/>
                <br/>

                <div className="container">
                    <div className="row">
                    <div className="col-3">
                        <label>Select order type:</label>
                        <select className="form-control" onChange={this.handleReportTypeChange} id="reportType">
                            <option value="1">Available equipment</option>
                            <option value="2">Nonavailable equipment</option>
                            <option value="3">Ending equipment</option>
                            <option value="4">Delivered equipment</option>
                            <option value="5">Processed orders</option>
                            <option value="6">Unprocessed orders</option>
                            <option value="7">Orders by executor</option>
                            <option value="8">Orders by creator</option>
                        </select>
                    </div>
                    {this.renderAdditionalInputs()}

                    </div>
                    {this.renderReport()}
                </div>

            </React.Fragment>
        );
    }

}

export default ReportsPage;