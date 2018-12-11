import React, { Component } from "react";
class ReportsPage extends Component {
    constructor(props) {
        super(props);
        this.state = {
            userRoles: [],
            isLoading: false,
            reportTypeId : 0,
            warehouses : [],
            warehousesPage : 0,
            resultsPerPage : 25,
            reportPage : 0,
            reportRows : [],
            showReport : false,
            fromDate : "1970-01-01",
            toDate : "",
            executors : [],
            executorsPage : 0,
            executorId : "",
            creators : [],
            creatorsPage : 0,
            creatorId : "",
            warehouseId : "",
            equipmentMinQuantity : 1
        }
    }

    componentWillMount() {
         fetch('/userinfo')
            .then(response => response.json())
            .then(data => {
                this.setState({userRoles: data.roles.map(role => role.name), isLoading:false })
                if(!this.state.userRoles.includes('ROLE_ADMIN') && !this.state.userRoles.includes('ROLE_KEEPER')){
                    this.props.history.push('/errorpage');
                }
            })
            .catch(error => console.log(error))


    }

    loadFirstWarehouses = () => {
        fetch('/warehouses-for-report', {
            method : 'POST',
            body : JSON.stringify({
                "pageNumber" : 0,
                "resultsPerPage" : this.state.resultsPerPage
            }),
            headers: {
                "Content-Type": "application/json"
            }
        })
            .then(response => response.json())
            .then(data => {
                this.setState({warehouses : data})
            })
            .catch(error => console.log(error));
        this.setState({warehousesPage : ++this.state.warehousesPage})
    }

    loadFirstExecutors = () => {
        fetch('/executors-for-report', {
            method : 'POST',
            body : JSON.stringify( {
                "pageNumber" : 0,
                "resultsPerPage" : this.state.resultsPerPage
            }),
            headers: {
                "Content-Type": "application/json"
            }
        })
            .then(response => response.json())
            .then(data => {
                this.setState({executors : data })
            })
            .catch(error => console.log(error));
        this.setState({executorsPage : ++this.state.executorsPage})
    }

    loadFirstCreators = () => {
        fetch('/creators-for-report', {
            method : 'POST',
            body : JSON.stringify( {
                "pageNumber" : 0,
                "resultsPerPage" : this.state.resultsPerPage
            }),
            headers: {
                "Content-Type": "application/json"
            }
        })
            .then(response => response.json())
            .then(data => {
                this.setState({creators : data})
            })
            .catch(error => console.log(error));
        this.setState({creatorsPage : ++this.state.creatorsPage})
    }


    handleReportTypeChange = (event) => {
        this.setState({ showReport : false, reportRows : [], reportPage : 0,  warehouseId : "",
            equipmentMinQuantity : 1, executorId : "", creatorId : "", fromDate : "", toDate : ""});
        if(this.state.reportTypeId < 5 && this.state.reportTypeId > 0) {
            this._warehouse.value = "";
        }
        if(this.state.reportTypeId == 8) {
            this._creator.value = "";
        }
        if(this.state.reportTypeId == 7) {
            this._executor.value = "";
        }
        if(this.state.reportTypeId == 3) {
            this._input.value = "";
        }
        if(this.state.reportTypeId > 3) {
            this._fromDate.value = "";
            this._toDate.value = "";
        }
        this.setState({reportTypeId : event.target.value})
        if(event.target.value < 5 && event.target.value > 0) {
            this.loadFirstWarehouses()
        }
        if(event.target.value == 8) {
            this.loadFirstCreators()
        }
        if(event.target.value == 7) {
            this.loadFirstExecutors()
        }

    }
    handleWarehouseChange = (event) => {
        this.setState({warehouseId : event.target.value, showReport : false, reportRows : [], reportPage : 0});
    }
    handleMinChange = (event) => {
        this.setState({equipmentMinQuantity : event.target.value, showReport : false, reportRows : [], reportPage : 0});
    }
    handleExecutorsChange = (event) => {
        this.setState({executorId : event.target.value, showReport : false, reportRows : [], reportPage : 0});
    }
    handleCreatorsChange = (event) => {
        this.setState({creatorId : event.target.value, showReport : false, reportRows : [], reportPage : 0});
    }
    handleFromInputChange = (event) => {
        this.setState({fromDate : event.target.value, showReport : false, reportRows : [], reportPage : 0});
    }
    handleToInputChange = (event) => {
        this.setState({toDate : event.target.value, showReport : false, reportRows : [], reportPage : 0});
    }

    loadMoreWarehouses = () => {
        fetch('/warehouses-for-report', {
            method : 'POST',
            body : JSON.stringify({
                "pageNumber" : this.state.warehousesPage,
                "resultsPerPage" : this.state.resultsPerPage
            }),
            headers: {
                "Content-Type": "application/json"
            }
        })
            .then(response => response.json())
            .then(data => {
                this.setState({warehouses : this.state.warehouses.concat(data)})
            })
            .catch(error => console.log(error));
        this.setState({warehousesPage : ++this.state.warehousesPage})
    }
    loadMoreExecutors = () => {
        fetch('/executors-for-report', {
            method : 'POST',
            body : JSON.stringify( {
                "pageNumber" : this.state.executorsPage,
                "resultsPerPage" : this.state.resultsPerPage
            }),
            headers: {
                "Content-Type": "application/json"
            }
        })
            .then(response => response.json())
            .then(data => {
                this.setState({executors : this.state.executors.concat(data) })
            })
            .catch(error => console.log(error));
        this.setState({executorsPage : ++this.state.executorsPage})
    }
    loadMoreCreators = () => {
        fetch('/creators-for-report', {
            method : 'POST',
            body : JSON.stringify( {
                "pageNumber" : this.state.creatorsPage,
                "resultsPerPage" : this.state.resultsPerPage
            }),
            headers: {
                "Content-Type": "application/json"
            }
        })
            .then(response => response.json())
            .then(data => {
                this.setState({creators : this.state.creators.concat(data)})
            })
            .catch(error => console.log(error));
        this.setState({creatorsPage : ++this.state.creatorsPage})
    }

    handleGenerateReport = () => {
        if(this.state.reportTypeId == 1 ) {
            if ((this.state.warehouseId != "")) {
                fetch('/reports/equipment/available', {
                    method: 'POST',
                    body: JSON.stringify({
                        "warehouseId": this.state.warehouseId,
                        "pageNumber": this.state.reportPage,
                        "resultsPerPage": this.state.resultsPerPage
                    }),
                    headers: {
                        "Content-Type": "application/json"
                    }
                })
                    .then(response => response.json())
                    .then(data => {
                        this.setState({reportRows: this.state.reportRows.concat(data)})
                    })
                    .catch(error => console.log(error))
                this.setState({reportPage: ++this.state.reportPage, showReport : true})
            } else {
                alert("Wrong inputs!")
            }
        } else if (this.state.reportTypeId == 2) {
            if ((this.state.warehouseId != "")) {
                fetch('/reports/equipment/nonavailable', {
                    method: 'POST',
                    body: JSON.stringify({
                        "warehouseId": this.state.warehouseId,
                        "pageNumber": this.state.reportPage,
                        "resultsPerPage": this.state.resultsPerPage
                    }),
                    headers: {
                        "Content-Type": "application/json"
                    }
                })
                    .then(response => response.json())
                    .then(data => {
                        this.setState({reportRows: this.state.reportRows.concat(data)})
                    })
                    .catch(error => console.log(error))
                this.setState({reportPage: ++this.state.reportPage, showReport : true})
            } else {
                alert("Wrong inputs!")
            }
        } else if (this.state.reportTypeId == 3) {
            if ((this.state.warehouseId != "") && this.state.equipmentMinQuantity >= 0) {
                fetch('/reports/equipment/ending', {
                    method: 'POST',
                    body: JSON.stringify({
                        "warehouseId": this.state.warehouseId,
                        "pageNumber": this.state.reportPage,
                        "resultsPerPage": this.state.resultsPerPage,
                        "equipmentThreshold" : this.state.equipmentMinQuantity
                    }),
                    headers: {
                        "Content-Type": "application/json"
                    }
                })
                    .then(response => response.json())
                    .then(data => {
                        this.setState({reportRows: this.state.reportRows.concat(data)})
                    })
                    .catch(error => console.log(error))
                this.setState({reportPage: ++this.state.reportPage, showReport : true})
            } else {
                alert("Wrong inputs!")
            }
        } else if (this.state.reportTypeId == 4) {
            if ((this.state.warehouseId != "") && this.state.fromDate != "1970-01-01" && this.state.toDate >= this.state.fromDate) {
                fetch('/reports/equipment/delivered', {
                    method: 'POST',
                    body: JSON.stringify({
                        "warehouseId": this.state.warehouseId,
                        "pageNumber": this.state.reportPage,
                        "resultsPerPage": this.state.resultsPerPage,
                        "fromDate" : this.state.fromDate,
                        "toDate" : this.state.toDate
                    }),
                    headers: {
                        "Content-Type": "application/json"
                    }
                })
                    .then(response => response.json())
                    .then(data => {
                        this.setState({reportRows: this.state.reportRows.concat(data)})
                    })
                    .catch(error => console.log(error))
                this.setState({reportPage: ++this.state.reportPage, showReport : true})
            } else {
                alert("Wrong inputs!")
            }
        } else if (this.state.reportTypeId == 5) {
            if(this.state.fromDate != "1970-01-01" && this.state.toDate >= this.state.fromDate) {
                fetch('/reports/orders/processed', {
                    method: 'POST',
                    body: JSON.stringify({
                        "pageNumber": this.state.reportPage,
                        "resultsPerPage": this.state.resultsPerPage,
                        "fromDate" : this.state.fromDate,
                        "toDate" : this.state.toDate
                    }),
                    headers: {
                        "Content-Type": "application/json"
                    }
                })
                    .then(response => response.json())
                    .then(data => {
                        this.setState({reportRows: this.state.reportRows.concat(data)})
                    })
                    .catch(error => console.log(error))
                this.setState({reportPage: ++this.state.reportPage, showReport : true})
            } else {
                alert("Wrong inputs!")
            }
        } else if (this.state.reportTypeId == 6) {
            if(this.state.fromDate != "1970-01-01" && this.state.toDate >= this.state.fromDate) {
                fetch('/reports/orders/unprocessed', {
                    method: 'POST',
                    body: JSON.stringify({
                        "pageNumber": this.state.reportPage,
                        "resultsPerPage": this.state.resultsPerPage,
                        "fromDate" : this.state.fromDate,
                        "toDate" : this.state.toDate
                    }),
                    headers: {
                        "Content-Type": "application/json"
                    }
                })
                    .then(response => response.json())
                    .then(data => {
                        this.setState({reportRows: this.state.reportRows.concat(data)})
                    })
                    .catch(error => console.log(error))
                this.setState({reportPage: ++this.state.reportPage, showReport : true})
            } else {
                alert("Wrong inputs!")
            }
        } else if (this.state.reportTypeId == 7) {
            if(this.state.fromDate != "1970-01-01" && this.state.toDate >= this.state.fromDate && this.state.executorId != "") {
                fetch('/reports/orders/executed', {
                    method: 'POST',
                    body: JSON.stringify({
                        "pageNumber": this.state.reportPage,
                        "resultsPerPage": this.state.resultsPerPage,
                        "fromDate" : this.state.fromDate,
                        "toDate" : this.state.toDate,
                        "userId" : this.state.executorId
                    }),
                    headers: {
                        "Content-Type": "application/json"
                    }
                })
                    .then(response => response.json())
                    .then(data => {
                        this.setState({reportRows: this.state.reportRows.concat(data)})
                    })
                    .catch(error => console.log(error))
                this.setState({reportPage: ++this.state.reportPage, showReport : true})
            } else {
                alert("Wrong inputs!")
            }
        } else if (this.state.reportTypeId == 8) {
            if(this.state.fromDate != "1970-01-01" && this.state.toDate >= this.state.fromDate && this.state.creatorId != "") {
                fetch('/reports/orders/created', {
                    method: 'POST',
                    body: JSON.stringify({
                        "pageNumber": this.state.reportPage,
                        "resultsPerPage": this.state.resultsPerPage,
                        "fromDate" : this.state.fromDate,
                        "toDate" : this.state.toDate,
                        "userId" : this.state.creatorId
                    }),
                    headers: {
                        "Content-Type": "application/json"
                    }
                })
                    .then(response => response.json())
                    .then(data => {
                        this.setState({reportRows: this.state.reportRows.concat(data)})
                    })
                    .catch(error => console.log(error))
                this.setState({reportPage: ++this.state.reportPage, showReport : true})
            } else {
                alert("Wrong inputs!")
            }
        }
    }

    renderAdditionalInputs = () => {
        if(this.state.reportTypeId == 1 || this.state.reportTypeId == 2){
            return (
                <React.Fragment>
                <div className="col-3">
                    <label>Select warehouse:</label>
                    <div className="input-group">
                        {this.renderLoadMoreInputRows()}
                        <select onChange={this.handleWarehouseChange} ref={(node) => this._warehouse = node} className="custom-select form-control" id="inputGroupSelect02">
                            <option value="" disabled selected>Warehouse</option>
                            {this.state.warehouses.map(w => (
                                <option value={w.id}>{w.address}</option>
                            ))}
                        </select>
                    </div>
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
                    <div className="input-group">
                        {this.renderLoadMoreInputRows()}
                        <select onChange={this.handleWarehouseChange} ref={(node) => this._warehouse = node} className="custom-select form-control" id="inputGroupSelect03">
                            <option value="" disabled selected>Warehouse</option>
                            {this.state.warehouses.map(w => (
                                <option value={w.id}>{w.address}</option>
                            ))}
                        </select>
                    </div>
                    <br/>


                </div>
                    <div className="col-3">
                        <label>Minimum number of equipment:</label>
                        <input onChange={this.handleMinChange} ref={(node) => this._input = node} className="form-control" type="number" min="0" defaultValue="1" />
                    </div>
                    <div className="col-3">
                        <button onClick={this.handleGenerateReport} className="btn btn-outline-primary height-center">Generate report</button>
                    </div>
                </React.Fragment>
            );
        } else if (this.state.reportTypeId == 4) {
            return(
                <React.Fragment>
                    <div className="col-2">
                        <label>Select warehouse:</label>
                        <div className="input-group">
                            {this.renderLoadMoreInputRows()}
                            <select onChange={this.handleWarehouseChange} ref={(node) => this._warehouse = node} className="custom-select form-control" id="inputGroupSelect04">
                                <option value="" disabled selected>Warehouse</option>
                                {this.state.warehouses.map(w => (
                                    <option value={w.id}>{w.address}</option>
                                ))}
                            </select>
                        </div>
                        <br/>
                    </div>
                    <div className="col-2">
                        <label>From date:</label>
                        <input onChange={this.handleFromInputChange} ref={(node) => this._fromDate = node} id="from-date4" className="form-control" type="date"/>
                    </div>
                    <div className="col-2">
                        <label>To date:</label>
                        <input onChange={this.handleToInputChange} ref={(node) => this._toDate = node} id="to-date4" min={this.state.fromDate} className="form-control" type="date"/>
                    </div>
                    <div className="col-2">
                        <button onClick={this.handleGenerateReport} className="btn btn-outline-primary height-center">Generate report</button>
                    </div>
                </React.Fragment>
            )
        } else if (this.state.reportTypeId == 5) {
            return(
                <React.Fragment>
                    <div className="col-2">
                        <label>From date:</label>
                        <input onChange={this.handleFromInputChange} ref={(node) => this._fromDate = node} id="from-date5" className="form-control" type="date"/>
                    <br/>
                    </div>
                    <div className="col-2">
                        <label>To date:</label>
                        <input onChange={this.handleToInputChange} ref={(node) => this._toDate = node} id="to-date5" min={this.state.fromDate} className="form-control" type="date"/>
                    </div>
                    <div className="col-2">
                        <button onClick={this.handleGenerateReport} className="btn btn-outline-primary height-center">Generate report</button>
                    </div>
                </React.Fragment>
            )
        } else if (this.state.reportTypeId == 6) {
            return (
                <React.Fragment>
                    <div className="col-2">
                        <label>From date:</label>
                        <input onChange={this.handleFromInputChange} ref={(node) => this._fromDate = node} id="from-date6" className="form-control" type="date"/>
                        <br/>
                    </div>
                    <div className="col-2">
                        <label>To date:</label>
                        <input onChange={this.handleToInputChange} ref={(node) => this._toDate = node} id="to-date6" min={this.state.fromDate} className="form-control" type="date"/>
                    </div>
                    <div className="col-2">
                        <button onClick={this.handleGenerateReport} className="btn btn-outline-primary height-center">Generate report</button>
                    </div>
                </React.Fragment>
            )
        } else if (this.state.reportTypeId == 7) {
            return (
            <React.Fragment>
                <div className="col-2">
                    <label>Select executor:</label>
                    <div className="input-group">
                        {this.renderLoadMoreInputRows()}
                        <select onChange={this.handleExecutorsChange} ref={(node) => this._executor = node} className="custom-select form-control" id="inputGroupSelect07">
                            <option value="" disabled selected>Executor</option>
                            {this.state.executors.map(e => (
                                <option value={e.id}>{e.pib}</option>
                            ))}
                        </select>
                    </div>
                </div>
                <div className="col-2">
                    <label>From date:</label>
                    <input onChange={this.handleFromInputChange} ref={(node) => this._fromDate = node} id="from-date7" className="form-control" type="date"/>
                    <br/>
                </div>
                <div className="col-2">
                    <label>To date:</label>
                    <input onChange={this.handleToInputChange} ref={(node) => this._toDate = node} id="to-date7" min={this.state.fromDate} className="form-control" type="date"/>
                </div>
                <div className="col-2">
                    <button onClick={this.handleGenerateReport} className="btn btn-outline-primary height-center">Generate report</button>
                </div>
            </React.Fragment>
            )
        } else if (this.state.reportTypeId == 8) {
            return (
                <React.Fragment>
                    <div className="col-2">
                        <label>Select creator:</label>
                        <div className="input-group">
                            {this.renderLoadMoreInputRows()}
                            <select onChange={this.handleCreatorsChange} ref={(node) => this._creator = node} className="custom-select form-control" id="inputGroupSelect08">
                                <option value="" disabled selected>Creator</option>
                                {this.state.creators.map(c => (
                                    <option value={c.id}>{c.pib}</option>
                                ))}
                            </select>
                        </div>
                    </div>
                    <div className="col-2">
                        <label>From date:</label>
                        <input onChange={this.handleFromInputChange} ref={(node) => this._fromDate = node} id="from-date8" className="form-control" type="date"/>
                        <br/>
                    </div>
                    <div className="col-2">
                        <label>To date:</label>
                        <input onChange={this.handleToInputChange} ref={(node) => this._toDate = node} id="to-date8" min={this.state.fromDate} className="form-control" type="date"/>
                    </div>
                    <div className="col-2">
                        <button onClick={this.handleGenerateReport} className="btn btn-outline-primary height-center">Generate report</button>
                    </div>
                </React.Fragment>
            )
        }
    }

    renderReport = () => {
        if((this.state.reportTypeId < 5) && this.state.showReport) {
            return (
                <React.Fragment>
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
                    {this.renderLoadMoreRows()}
                </React.Fragment>
            );
        } else if((this.state.reportTypeId > 4) && this.state.showReport) {
            return (
                <React.Fragment>
                <table className="table">
                <thead>
                <tr>
                    <th>Order type</th>
                    <th>Status</th>
                    <th>Creator</th>
                    <th>Executor</th>
                    <th>Creation date</th>
                    <th>Last modified date</th>
                    <th>Warehouse</th>
                    <th>Title</th>
                    <th>Description</th>
                </tr>
                </thead>
                <tbody>
                {this.state.reportRows.map(r => (
                    <tr>
                        <td>{r.orderType}</td>
                        <td>{r.orderStatus}</td>
                        <td>{r.creatorPib+" "+r.creatorEmail}</td>
                        <td>{r.executorPib+" "+r.executorEmail}</td>
                        <td>{r.creationDate}</td>
                        <td>{r.modifiedDate}</td>
                        <td>{r.warehouseAddress+" "+r.warehousePhone}</td>
                        <td>{r.title}</td>
                        <td>{r.orderDescription}</td>
                    </tr>
                ))}
                </tbody>
            </table>
                    {this.renderLoadMoreRows()}
                </React.Fragment>)
        } else {
            return (<div></div>)
        }
    }

    renderLoadMoreRows = () => {
        if(this.state.reportRows.length === this.state.reportPage*this.state.resultsPerPage) {
            return (
                <button className="btn btn-outline-primary" onClick={this.handleGenerateReport}>Load more rows</button>
            )
        } else {
            return (<div></div>)
        }
    }

    renderLoadMoreInputRows = () => {
        if(this.state.reportTypeId < 5 && this.state.warehouses.length == this.state.warehousesPage*this.state.resultsPerPage){
            return (
                <div className="input-group-prepend">
                    <button onClick={this.loadMoreWarehouses} className="btn btn-outline-secondary" type="button">Load More</button>
                </div>
            )
        } else if (this.state.reportTypeId == 7 && this.state.executors.length == this.state.executorsPage*this.state.resultsPerPage) {
            return (
                <div className="input-group-prepend">
                    <button onClick={this.loadMoreExecutors} className="btn btn-outline-secondary" type="button">Load More</button>
                </div>
            )
        } else if (this.state.reportTypeId == 8 && this.state.creators.length == this.state.creatorsPage*this.state.resultsPerPage) {
            return (
                <div className="input-group-prepend">
                    <button onClick={this.loadMoreCreators} className="btn btn-outline-secondary" type="button">Load More</button>
                </div>
            )
        } else {
            return (<div></div>)
        }
    }

    renderLoadExcel = () => {
        if(this.state.reportRows.length > 0) {
            return (
                <button className="btn btn-outline-primary col-3" onClick={this.handleLoadExcel}>Load Excel version</button>
            )
        } else {
            return(<div></div>)
        }
    }

    handleLoadExcel = () => {
        if(this.state.reportTypeId == 1 ) {
            if ((this.state.warehouseId != "")) {
                fetch('/reports/equipment/available/excel', {
                    method: 'POST',
                    body: JSON.stringify({
                        "warehouseId": this.state.warehouseId,
                    }),
                    headers: {
                        "Content-Type": "application/json"
                    }
                })
                    .then(response => {
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
                    })
                    .then(data => {})
                    .catch(error => console.log(error))
            } else {
                alert("Wrong inputs!")
            }
        } else if (this.state.reportTypeId == 2) {
            if ((this.state.warehouseId != "")) {
                fetch('/reports/equipment/nonavailable/excel', {
                    method: 'POST',
                    body: JSON.stringify({
                        "warehouseId": this.state.warehouseId,
                    }),
                    headers: {
                        "Content-Type": "application/json"
                    }
                })
                    .then(response => {
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
                    })
                    .then(data => {})
                    .catch(error => console.log(error))
            } else {
                alert("Wrong inputs!")
            }
        } else if (this.state.reportTypeId == 3) {
            if ((this.state.warehouseId != "") && this.state.equipmentMinQuantity >= 0) {
                fetch('/reports/equipment/ending/excel', {
                    method: 'POST',
                    body: JSON.stringify({
                        "warehouseId": this.state.warehouseId,
                        "equipmentThreshold" : this.state.equipmentMinQuantity
                    }),
                    headers: {
                        "Content-Type": "application/json"
                    }
                })
                    .then(response => {
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
                    })
                    .then(data => {})
                    .catch(error => console.log(error))
            } else {
                alert("Wrong inputs!")
            }
        } else if (this.state.reportTypeId == 4) {
            if ((this.state.warehouseId != "") && this.state.fromDate != "1970-01-01" && this.state.toDate >= this.state.fromDate) {
                fetch('/reports/equipment/delivered/excel', {
                    method: 'POST',
                    body: JSON.stringify({
                        "warehouseId": this.state.warehouseId,
                        "fromDate" : this.state.fromDate,
                        "toDate" : this.state.toDate
                    }),
                    headers: {
                        "Content-Type": "application/json"
                    }
                })
                    .then(response => {
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
                    })
                    .then(data => {})
                    .catch(error => console.log(error))
            } else {
                alert("Wrong inputs!")
            }
        } else if (this.state.reportTypeId == 5) {
            if(this.state.fromDate != "1970-01-01" && this.state.toDate >= this.state.fromDate) {
                fetch('/reports/orders/processed/excel', {
                    method: 'POST',
                    body: JSON.stringify({
                        "fromDate" : this.state.fromDate,
                        "toDate" : this.state.toDate
                    }),
                    headers: {
                        "Content-Type": "application/json"
                    }
                })
                    .then(response => {
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
                    })
                    .then(data => {})
                    .catch(error => console.log(error))
            } else {
                alert("Wrong inputs!")
            }
        } else if (this.state.reportTypeId == 6) {
            if(this.state.fromDate != "1970-01-01" && this.state.toDate >= this.state.fromDate) {
                fetch('/reports/orders/unprocessed/excel', {
                    method: 'POST',
                    body: JSON.stringify({
                        "fromDate" : this.state.fromDate,
                        "toDate" : this.state.toDate
                    }),
                    headers: {
                        "Content-Type": "application/json"
                    }
                })
                    .then(response => {
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
                    })
                    .then(data => {})
                    .catch(error => console.log(error))
            } else {
                alert("Wrong inputs!")
            }
        } else if (this.state.reportTypeId == 7) {
            if(this.state.fromDate != "1970-01-01" && this.state.toDate >= this.state.fromDate && this.state.executorId != "") {
                fetch('/reports/orders/executed/excel', {
                    method: 'POST',
                    body: JSON.stringify({
                        "fromDate" : this.state.fromDate,
                        "toDate" : this.state.toDate,
                        "userId" : this.state.executorId
                    }),
                    headers: {
                        "Content-Type": "application/json"
                    }
                })
                    .then(response => {
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
                    })
                    .then(data => {})
                    .catch(error => console.log(error))
            } else {
                alert("Wrong inputs!")
            }
        } else if (this.state.reportTypeId == 8) {
            if(this.state.fromDate != "1970-01-01" && this.state.toDate >= this.state.fromDate && this.state.creatorId != "") {
                fetch('/reports/orders/created/excel', {
                    method: 'POST',
                    body: JSON.stringify({
                        "fromDate" : this.state.fromDate,
                        "toDate" : this.state.toDate,
                        "userId" : this.state.creatorId
                    }),
                    headers: {
                        "Content-Type": "application/json"
                    }
                })
                    .then(response => {
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
                    })
                    .then(data => {})
                    .catch(error => console.log(error))
            } else {
                alert("Wrong inputs!")
            }
        }
    }

    renderGoToDashboard = () => {

        return(<div></div>)
    }

    goToDashboard = () => {
        this.props.history.push('/dashboard')
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
                    {this.renderGoToDashboard()}
                    </div>
                    <div className="row">
                    <div className="col-3">
                        <label>Select order type:</label>
                        <select className="form-control" onChange={this.handleReportTypeChange} id="reportType">
                            <option value="" disabled selected>Order type</option>
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
                    {this.renderLoadExcel()}
                </div>

            </React.Fragment>
        );
    }

}

export default ReportsPage;