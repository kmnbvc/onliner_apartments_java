'use strict';

const React = require('react');
const client = require('./client');
const Menu = require('./Menu');
const SourceEdit = require('./SourceEdit');

class Sources extends React.Component {
    render() {
        return [
            <Menu key="menu">
                <Toolbar/>
            </Menu>,
            <SourcesTable key="sources"/>
        ];
    }
}

class Toolbar extends React.Component {
    render() {
        return (
            <SourceEdit onSave={() => location.reload()} className="btn btn-default navbar-btn pull-right">Add</SourceEdit>
        );
    }
}

class SourcesTable extends React.Component {
    constructor(props) {
        super(props);
        this.state = {sources: []};
        this.loadData = this.loadData.bind(this);
        this.deleteSource = this.deleteSource.bind(this);
    }

    componentDidMount() {
        this.loadData();
    }

    loadData() {
        client({method: 'GET', path: '/api/sources'})
            .then(response => this.setState({sources: response.entity._embedded.sources}));
    }

    deleteSource(source) {
        client({method: 'DELETE', path: source._links.self.href}).then(this.loadData);
    }

    render() {
        const rows = this.state.sources.map(source => (
            <tr key={source.name}>
                <td>{source.name}</td>
                <td>
                    <a href={source.url}>{source.url}</a>
                </td>
                <td>
                    <div className="btn-group">
                        <SourceEdit source={source} onSave={this.loadData} className="btn btn-default btn-sm">
                            <span className="glyphicon glyphicon-pencil"></span>
                        </SourceEdit>
                        <DeleteButton source={source} onClick={this.deleteSource}/>
                    </div>
                </td>
            </tr>
        ));

        return (
            <div>
                <h3>Apartments source urls</h3>
                <table className="table sources">
                    <thead>
                    <tr>
                        <th>Name</th>
                        <th>Url</th>
                        <th>Actions</th>
                    </tr>
                    </thead>
                    <tbody>{rows}</tbody>
                </table>
            </div>
        );
    }
}

const DeleteButton = ({source, onClick}) => {
    return (
        <button onClick={() => {
            onClick(source);
        }} title="Delete" className="btn btn-default btn-sm">
            <span className="glyphicon glyphicon-trash"></span>
        </button>
    )
};

module.exports = Sources;
