'use strict';

const React = require('react');
const {withRouter} = require('react-router-dom');
const ApartmentsTable = require('./ApartmentsTable');
const Menu = require('./Menu');
const client = require('./client');

class NewApartments extends React.Component {
    constructor(props) {
        super(props);
        this.state = {apartments: []};
    }

    componentDidMount() {
        client({method: 'GET', path: '/api/apartments/new'})
            .then(response => this.setState({apartments: response.entity._embedded.apartments}));
    }

    render() {
        return [
            <Menu key="menu">
                <ToolbarWithRouter apartments={this.state.apartments}/>
            </Menu>,
            <ApartmentsTable key="apartments" apartments={this.state.apartments} showDetails={false}/>
        ];
    }
}

class Toolbar extends React.Component {
    constructor(props) {
        super(props);
        this.saveNew = this.saveNew.bind(this);
    }

    saveNew(event) {
        const {history} = this.props;
        client({
                method: 'POST', path: '/api/apartments/all',
                headers: {'Content-Type': 'application/json'},
                entity: {items: this.props.apartments}
            }
        ).then(() => history.push('/saved'));
    }

    render() {
        return (
            <div className="navbar-form navbar-right">
                <button type="button" onClick={this.saveNew} disabled={this.props.apartments.length === 0} className="btn btn-default">
                    Save new apartments
                </button>
            </div>
        );
    }
}

const ToolbarWithRouter = withRouter(Toolbar);

module.exports = NewApartments;

