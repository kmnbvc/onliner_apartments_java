'use strict';

const React = require('react');
const ApartmentsTable = require('./ApartmentsTable');
const Menu = require('./Menu');
const client = require('./client');

class SavedApartments extends React.Component {
    constructor(props) {
        super(props);
        this.state = {apartments: [], filter: this.props.match.params.filter};
        this.deleteAll = this.deleteAll.bind(this);
        this.loadData = this.loadData.bind(this);
        this.loadDetails = this.loadDetails.bind(this);
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
        const predefinedFiltersPaths = {
            all: '/search/getAll',
            active: '/search/getActive',
            ignored: '/search/getIgnored'
        };
        const filter = this.state.filter;
        const path = predefinedFiltersPaths[filter] || `/search/find?filter=${filter}`;
        client({method: 'GET', path: `/api/apartments/${path}`})
            .then(response => this.setState({apartments: response.entity._embedded.apartments}));
    }

    deleteAll() {
        client({method: 'DELETE', path: '/api/apartments/all'})
            .then(() => this.setState({apartments: []}));
    }

    loadDetails() {
        const source = new EventSource('/api/apartments/details');

        source.addEventListener('message', function (msg) {
            console.log(msg);
        }, false);

        source.addEventListener('total', function (msg) {
            console.log(msg);
        }, false);

        source.addEventListener('finish', function () {
            console.log('closing source');
            source.close();
        }, false);

        source.addEventListener('error', function (msg) {
            console.log(msg);
        }, false);
    }

    render() {
        return [
            <Menu key="menu">
                <Toolbar apartments={this.state.apartments} delete={this.deleteAll} loadDetails={this.loadDetails}/>
            </Menu>,
            <ApartmentsTable key="apartments" apartments={this.state.apartments} showDetails={true} header={this.state.filter}/>
        ];
    }
}

class Toolbar extends React.Component {
    render() {
        return (
            <div className="navbar-form navbar-right btn-toolbar">
                <button onClick={this.props.loadDetails} disabled={this.props.apartments.length === 0} className="btn btn-default">Load details</button>
                <button onClick={this.props.delete} disabled={this.props.apartments.length === 0} className="btn btn-default">Delete all</button>
            </div>
        );
    }
}

module.exports = SavedApartments;
