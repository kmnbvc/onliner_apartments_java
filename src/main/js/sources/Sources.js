'use strict';

const React = require('react');
const {sources: client} = require('../api/client_helper');
const Menu = require('../Menu');
const SourceEdit = require('./SourceEdit');

class Sources extends React.Component {
    render() {
        return [
            <Menu key="menu">
                <SourceEdit onSave={() => location.reload()} className="btn btn-default navbar-btn pull-right">Add</SourceEdit>
            </Menu>,
            <SourcesTable key="sources"/>
        ];
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
        client.getAll().then(response => this.setState({sources: response.entity}));
    }

    deleteSource(source) {
        client.remove(source).then(this.loadData);
    }

    render() {
        const rows = this.state.sources.map(source => (
            <tr key={source.name} className={source.active ? '' : 'not-active'}>
                <td>{source.name}</td>
                <td>
                    <a href={source.url}>{source.url}</a>
                </td>
                <td>
                    <div className="btn-group">
                        <SourceEdit source={source} onSave={this.loadData} className="btn btn-default btn-sm" title="Edit">
                            <span className="glyphicon glyphicon-pencil"></span>
                        </SourceEdit>
                        <ToggleStateButton source={source} onClick={this.loadData}/>
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

const ToggleStateButton = ({source, onClick}) => {
    const toggleState = function () {
        source.active = !source.active;
        client.update(source).then(onClick);
    };

    return (
        <button onClick={toggleState} className="btn btn-default btn-sm" title={source.active ? 'Disable' : 'Enable'}>
            <span className={"glyphicon " + (source.active ? "glyphicon-remove" : "glyphicon-ok")}></span>
        </button>
    )
};

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
