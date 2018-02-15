'use strict';

const React = require('react');
const client = require('../client');
const FilterEdit = require('./FilterEdit');
const Menu = require('../Menu');

class Filters extends React.Component {
    render() {
        return [
            <Menu key="menu">
                <Toolbar/>
            </Menu>,
            <FiltersTable key="filters"/>
        ];
    }
}

class Toolbar extends React.Component {
    render() {
        return (
            <FilterEdit onSave={() => location.reload()} className="btn btn-default navbar-btn pull-right">Add</FilterEdit>
        );
    }
}

class FiltersTable extends React.Component {
    constructor(props) {
        super(props);
        this.state = {filters: []};
        this.loadData = this.loadData.bind(this);
        this.deleteFilter = this.deleteFilter.bind(this);
    }

    componentDidMount() {
        this.loadData();
    }

    loadData() {
        client({method: 'GET', path: '/api/filters'})
            .then(response => this.setState({filters: response.entity}));
    }

    deleteFilter(filter) {
        client({method: 'DELETE', path: `/api/filters/${filter.name}`}).then(this.loadData);
    }

    render() {
        const rows = this.state.filters.map(filter => (
            <tr key={filter.name}>
                <td>{filter.name}</td>
                <td>{filter.active}</td>
                <td>{filter.from}</td>
                <td>{filter.source ? filter.source.name : ''}</td>
                <td>{filter.owner}</td>
                <td>
                    <div className="btn-group">
                        <FilterEdit filter={filter} onSave={this.loadData} className="btn btn-default btn-sm">
                            <span className="glyphicon glyphicon-pencil"></span>
                        </FilterEdit>

                        <DeleteButton filter={filter} onClick={this.deleteFilter}/>
                    </div>
                </td>
            </tr>
        ));

        return (
            <div>
                <h3>Apartments filters</h3>
                <table className="table filters">
                    <thead>
                    <tr>
                        <th>Name</th>
                        <th>Active</th>
                        <th>From date</th>
                        <th>Source</th>
                        <th>Owner</th>
                        <th>Actions</th>
                    </tr>
                    </thead>
                    <tbody>{rows}</tbody>
                </table>
            </div>
        );
    }
}

const DeleteButton = ({filter, onClick}) => {
    return (
        <button onClick={() => { onClick(filter); }}
                title="Delete" className="btn btn-default btn-sm">
            <span className="glyphicon glyphicon-trash"></span>
        </button>
    )
};

module.exports = Filters;
