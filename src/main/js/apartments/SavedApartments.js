'use strict';

const React = require('react');
const ApartmentsTable = require('./ApartmentsTable');
const Menu = require('../Menu');
const client = require('../client');

class SavedApartments extends React.Component {
    constructor(props) {
        super(props);
        this.state = {apartments: [], filter: this.props.match.params.filter};
        this.deleteAll = this.deleteAll.bind(this);
        this.loadData = this.loadData.bind(this);
        this.updateDetails = this.updateDetails.bind(this);
    }

    componentDidMount() {
        this.loadData();
    }

    componentWillReceiveProps(nextProps) {
        const {filter} = nextProps.match.params;
        if (filter !== this.state.filter) {
            this.setState({filter}, this.loadData);
        }
    }

    loadData() {
        const filter = this.state.filter;
        client({method: 'GET', path: `/api/apartments/search/${filter}`})
            .then(response => this.setState({apartments: response.entity}));
    }

    deleteAll() {
        client({method: 'DELETE', path: '/api/apartments/all'})
            .then(() => this.setState({apartments: []}));
    }

    updateDetails(apartment) {
        const apartments = [].concat(...this.state.apartments);
        const id = apartments.findIndex(a => a.id === apartment.id);
        if (id !== -1) {
            this.setState({apartments: apartments.fill(apartment, id, id + 1)});
        }
    }

    render() {
        return [
            <Menu key="menu">
                <Toolbar apartments={this.state.apartments} delete={this.deleteAll} updateDetails={this.updateDetails}/>
            </Menu>,
            <ApartmentsTable key="apartments" apartments={this.state.apartments} showDetails={true} header={this.state.filter}/>
        ];
    }
}

class Toolbar extends React.Component {
    constructor(props) {
        super(props);
        this.state = {detailsLoadingCurrent: 0, detailsLoadingTotal: 0};
        this.loadDetails = this.loadDetails.bind(this);
    }

    loadDetails() {
        const source = new EventSource('/api/apartments/sse/details');

        source.addEventListener('message', (msg) => {
            this.setState({detailsLoadingCurrent: this.state.detailsLoadingCurrent + 1});
            const apartment = JSON.parse(msg.data);
            this.props.updateDetails(apartment);
        }, false);

        source.addEventListener('total', (msg) => {
            this.setState({detailsLoadingTotal: msg.data});
        }, false);

        source.addEventListener('finish', () => {
            this.setState({detailsLoadingCurrent: 0, detailsLoadingTotal: 0});
            source.close();
        }, false);

        source.addEventListener('error', (msg) => {
            console.log(msg);
        }, false);
    }

    render() {
        return (
            <div className="navbar-form navbar-right btn-toolbar">
                <button onClick={this.loadDetails} disabled={this.props.apartments.length === 0} className="btn btn-default">
                    {this.state.detailsLoadingTotal ? 'Loading: ' + this.state.detailsLoadingCurrent + '/' + this.state.detailsLoadingTotal : 'Load details'}
                </button>
                <button onClick={this.props.delete} disabled={this.props.apartments.length === 0} className="btn btn-default">Delete all</button>
            </div>
        );
    }
}

module.exports = SavedApartments;
