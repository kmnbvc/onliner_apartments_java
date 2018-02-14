'use strict';

const React = require('react');
const Modal = require('react-modal');
const client = require('../client');

class SourceEdit extends React.Component {
    constructor(props) {
        super(props);
        this.state = {source: this.props.source || {}, isOpen: false};
        this.edit = this.edit.bind(this);
        this.close = this.close.bind(this);
        this.save = this.save.bind(this);
        this.handleSubmit = this.handleSubmit.bind(this);
    }

    componentWillReceiveProps(nextProps) {
        this.setState({source: nextProps.source || {}});
    }

    edit() {
        this.setState({isOpen: true});
    }

    close() {
        this.setState({isOpen: false});
    }

    save(source) {
        client({method: 'POST', path: '/api/sources',
            headers: {'Content-Type': 'application/json'},
            entity: source
        }).then(this.close).then(this.props.onSave);
    }

    handleSubmit(event) {
        event.preventDefault();
        const fields = [].concat(...event.target);
        const props = fields.filter(field => !!field.name).map(field => {return {[field.name]: field.value || null}});
        const source = Object.assign({}, ...props);
        this.save(source);
    }

    render() {
        const source = this.state.source;
        return [
            <button key={source.name + '_edit'} onClick={this.edit} title={this.props.title} className={this.props.className}>
                {this.props.children}
            </button>,
            <Modal key={source.name + '_popup'} isOpen={this.state.isOpen} style={{overlay: {zIndex: 9999}}}>
                <form onSubmit={this.handleSubmit}>
                    <div className="form-group">
                        <label htmlFor="name">Название</label>
                        <input defaultValue={source.name} type="text" className="form-control" id="name" name="name"/>
                    </div>
                    <div className="form-group">
                        <label htmlFor="url">Ссылка</label>
                        <textarea defaultValue={source.url} name="url" id="url" cols="30" rows="10" className="form-control"></textarea>
                    </div>
                    <div className="btn-toolbar">
                        <button className="btn btn-default">Сохранить</button>
                        <button type="button" onClick={this.close} className="btn btn-default">Отмена</button>
                    </div>
                </form>
            </Modal>
        ]
    }
}

module.exports = SourceEdit;

