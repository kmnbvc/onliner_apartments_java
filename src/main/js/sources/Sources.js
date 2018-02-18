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
            <tr key={source.name}>
                <td>{source.name}</td>
                <td>
                    <a href={source.url}>{source.url}</a>
                </td>
                <td>
                    <div className="btn-group">
                        <SourceEdit source={source} onSave={this.loadData} className="btn btn-default btn-sm" title="Edit">
                            <span className="glyphicon glyphicon-pencil"></span>
                        </SourceEdit>
                        <ToggleStateButton source={source}/>
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

class ToggleStateButton extends React.Component {
    constructor(props) {
        super(props);
        this.state = {source: props.source};
        this.toggleState = this.toggleState.bind(this);
    }

    toggleState() {
        const source = Object.assign({}, this.state.source, {active: !this.state.source.active});
        client.update(source).then(() => this.setState({source}));
    }

    render() {
        const source = this.state.source;
        return (
            <button onClick={this.toggleState} className="btn btn-default btn-sm" title={source.active ? 'Disable' : 'Enable'}>
                <span className={"glyphicon " + (source.active ? "glyphicon-remove" : "glyphicon-ok")}></span>
            </button>
        )
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
