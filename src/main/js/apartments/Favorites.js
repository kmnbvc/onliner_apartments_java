'use strict';

const React = require('react');
const ApartmentsTable = require('./ApartmentsTable');
const Menu = require('../Menu');
const client = require('../client');

class Favorites extends React.Component {
    constructor(props) {
        super(props);
        this.state = {apartments: []};
        this.clearAll = this.clearAll.bind(this);
        this.clearInactive = this.clearInactive.bind(this);
    }

    clearAll() {

    }

    clearInactive() {

    }

    componentDidMount() {
        client({method: 'GET', path: '/api/apartments/search/favorites'})
            .then(response => this.setState({apartments: response.entity}));
    }

    render() {
        return [
            <Menu key="menu">
                <Toolbar/>
            </Menu>,
            <ApartmentsTable key="favorites" apartments={this.state.apartments} showDetails={true} header="Favorites"/>
        ];
    }
}

class Toolbar extends React.Component {
    render() {
        return (
            <div className="navbar-form navbar-right btn-toolbar">
                <button className="btn btn-default">Clear inactive</button>
                <button className="btn btn-default">Clear all</button>
            </div>
        );
    }
}

module.exports = Favorites;