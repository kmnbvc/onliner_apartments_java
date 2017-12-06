'use strict';

const React = require('react');
const Modal = require('react-modal');
const client = require('./client');

class FilterEdit extends React.Component {
    constructor(props) {
        super(props);
        this.state = {filter: this.props.filter || {}, isOpen: false, sources: []};
        this.edit = this.edit.bind(this);
        this.close = this.close.bind(this);
        this.save = this.save.bind(this);
        this.handleSubmit = this.handleSubmit.bind(this);
    }

    componentDidMount() {
        client({method: 'GET', path: '/api/sources'})
            .then(response => this.setState({sources: response.entity._embedded.sources}));
    }

    componentWillReceiveProps(nextProps) {
        this.setState({filter: nextProps.filter || {}});
    }

    edit() {
        this.setState({isOpen: true});
    }

    close() {
        this.setState({isOpen: false});
    }

    save(filter) {
        client({method: 'POST', path: '/api/filters',
            headers: {'Content-Type': 'application/json'},
            entity: filter
        }).then(this.close).then(this.props.onSave);
    }

    handleSubmit(event) {
        event.preventDefault();
        const fields = [].concat(...event.target);
        const props = fields.filter(field => !!field.name).map(field => {return {[field.name]: field.value || null}});
        const filter = Object.assign({}, ...props);
        this.save(filter);
    }

    render() {
        const filter = this.state.filter;
        return [
            <button key={filter.name + '_edit'} onClick={this.edit} title={this.props.title} className={this.props.className}>
                {this.props.children}
            </button>,
            <Modal key={filter.name + '_popup'} isOpen={this.state.isOpen} style={{overlay: {zIndex: 9999}}}>
                <form onSubmit={this.handleSubmit}>
                    <div className="form-group">
                        <label htmlFor="name">Name</label>
                        <input type="text" className="form-control" id="name" name="name" defaultValue={filter.name}/>
                    </div>
                    <div className="form-group">
                        <label htmlFor="from_date">From date</label>
                        <input type="date" className="form-control" id="from" name="from" defaultValue={filter.from}/>
                    </div>
                    <div className="form-group">
                        <label htmlFor="active">Active</label>
                        <select defaultValue={filter.active} name="active" id="active" className="form-control">
                            <option value="All">All</option>
                            <option value="Active only">Active only</option>
                            <option value="Inactive only">Inactive only</option>
                        </select>
                    </div>
                    <div className="form-group">
                        <label htmlFor="source">Source</label>
                        <select defaultValue={filter.source ? filter.source._links.self.href : ''} name="source" id="source" className="form-control">
                            <option value="">None</option>
                            {
                                this.state.sources.map(source => <option key={source.name} value={source._links.self.href}>{source.name}</option>)
                            }
                        </select>
                    </div>
                    <div className="form-group">
                        <label htmlFor="owner">Owner</label>
                        <select defaultValue={filter.owner} name="owner" id="owner" className="form-control">
                            <option value="Any">Any</option>
                            <option value="Owner">Owner</option>
                            <option value="Not owner">Not owner</option>
                        </select>
                    </div>
                    <div className="btn-toolbar">
                        <button className="btn btn-default">Save</button>
                        <button type="button" className="btn btn-default" onClick={this.close}>Cancel</button>
                    </div>
                </form>
            </Modal>
        ];
    }
}

module.exports = FilterEdit;
