import React, { Component } from "react";
class ErrorPage extends Component {
    constructor(props) {
        super(props);
        this.state = {

        }
    }
    handleReturnToDashboard = () => {
        this.props.history.push('/dashboard')
    }
    render() {
        return (

            <React.Fragment>
                <br/>
                <br/>
                <br/>
                <br/>

                <div className="container">
                    <h1>Page not found</h1>
                    <p>Please return to your Dashboard</p>
                    <button className="btn btn-primary m-2"
                            onClick={this.handleReturnToDashboard}>
                        Go to Dashboard
                    </button>
                </div>
            </React.Fragment>
        );
    }
}
export default ErrorPage;
