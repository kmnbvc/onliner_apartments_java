'use strict';

const React = require('react');
const Modal = require('react-modal');
const {sources: client} = require('../api/client_helper');
const {Map, TileLayer, FeatureGroup, Marker} = require('react-leaflet');
const {EditControl} = require('react-leaflet-draw');
const update = require('immutability-helper');

class SourceEdit extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            source: this.props.source || {priceRange: {}, bounds: {}},
            isOpen: false
        };
        this.edit = this.edit.bind(this);
        this.close = this.close.bind(this);
        this.save = this.save.bind(this);
        this.handleSubmit = this.handleSubmit.bind(this);
        this.onAreaSelected = this.onAreaSelected.bind(this);
        this.sourceChangeHandler = this.sourceChangeHandler.bind(this);
    }

    edit() {
        this.setState({isOpen: true});
    }

    close() {
        this.setState({isOpen: false});
    }

    save(source) {
        client.create(source).then(this.close).then(this.props.onSave);
    }

    handleSubmit(event) {
        event.preventDefault();
        this.save(this.state.source);
    }

    sourceChangeHandler(path) {
        return (event) => {
            const toUpdate = this.setValue({}, path, {$set: event.target.value});
            this.setState({source: update(this.state.source, toUpdate)});
        }
    }

    setValue(obj, path, value) {
        const fields = path.split('.');
        const [subPath, field] = [fields.slice(0, fields.length - 1), fields[fields.length - 1]];
        let target = obj;
        subPath.forEach(element => {
            target[element] = {};
            target = target[element];
        });
        target[field] = value;

        return obj;
    }

    onAreaSelected(bounds) {
        const updated = {
            northWestLatitude: bounds.getNorthWest().lat,
            northWestLongitude: bounds.getNorthWest().lng,
            southEastLatitude: bounds.getSouthEast().lat,
            southEastLongitude: bounds.getSouthEast().lng
        };
        this.setState({source: update(this.state.source, {bounds: {$set: updated}})});
    }

    render() {
        const source = this.state.source;
        return (
            <React.Fragment>
                <button onClick={this.edit} title={this.props.title} className={this.props.className}>
                    {this.props.children}
                </button>
                <Modal isOpen={this.state.isOpen} onRequestClose={this.close}>
                    <form onSubmit={this.handleSubmit} className="form-horizontal">
                        <Input name="name" value={source.name} onChange={this.sourceChangeHandler('name')}>Name</Input>
                        <Input name="price_min" value={source.priceRange.min} type="number" min="1"
                               onChange={this.sourceChangeHandler('priceRange.min')}>Price min</Input>
                        <Input name="price_max" value={source.priceRange.max}
                               onChange={this.sourceChangeHandler('priceRange.max')}>Price max</Input>
                        <Select name="price_currency" value={source.priceRange.currency} items={['USD', 'BYN']}
                               onChange={this.sourceChangeHandler('priceRange.currency')}>Currency</Select>
                        <Input name="lb_lat" value={source.bounds.northWestLatitude}
                                onChange={this.sourceChangeHandler('bounds.northWestLatitude')}>North-West latitude</Input>
                        <Input name="lb_long" value={source.bounds.northWestLongitude} 
                                onChange={this.sourceChangeHandler('bounds.northWestLongitude')}>North-West longitude</Input>
                        <Input name="rt_lat" value={source.bounds.southEastLatitude}
                                onChange={this.sourceChangeHandler('bounds.southEastLatitude')}>South-East latitude</Input>
                        <Input name="rt_long" value={source.bounds.southEastLongitude}
                                onChange={this.sourceChangeHandler('bounds.southEastLongitude')}>South-East longitude</Input>
                        <div className="btn-toolbar">
                            <button className="btn btn-default">Сохранить</button>
                            <button type="button" onClick={this.close} className="btn btn-default">Отмена</button>
                        </div>
                    </form>
                    <br/>
                    <MapAreaSelector onSelect={this.onAreaSelected} source={this.state.source}/>
                </Modal>
            </React.Fragment>
        )
    }
}

class MapAreaSelector extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            zoom: 13,
            layer: null
        };

        this._onCreated = this._onCreated.bind(this);
        this._onDrawStart = this._onDrawStart.bind(this);
        this.drawSelectedBounds = this.drawSelectedBounds.bind(this);
        this.clearSelection = this.clearSelection.bind(this);
    }

    componentDidMount() {
        this.drawSelectedBounds(this.props.source.bounds);
    }

    componentWillReceiveProps(nextProps) {
        this.drawSelectedBounds(nextProps.source.bounds);
    }

    _onCreated(e) {
        const layer = e.layer;
        this.setState({layer}, () => this.props.onSelect(layer.getBounds()));
    }

    _onDrawStart(e) {
        this.clearSelection();
    }

    drawSelectedBounds(bounds) {
        this.clearSelection();
        const latLng = [[bounds.northWestLatitude || 0, bounds.northWestLongitude || 0],
                [bounds.southEastLatitude || 0, bounds.southEastLongitude || 0]];
        const layer = L.rectangle(latLng).addTo(this.map.leafletElement);
        this.setState({layer});
    }

    clearSelection() {
        if (this.state.layer)
            this.state.layer.remove();
    }

    render() {
        const center = [53.90513435188643, 27.553710937500004];
        return (
            <Map center={center} zoom={this.state.zoom} ref={map => {this.map = map}}>
                <TileLayer
                    attribution="&amp;copy <a href=&quot;http://osm.org/copyright&quot;>OpenStreetMap</a> contributors"
                    url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"/>
                <FeatureGroup>
                    <EditControl
                        position='topright'
                        draw={{polygon: false, circle: false, polyline: false, marker: false, circlemarker: false}}
                        edit={{edit: false, remove: false}}
                        onCreated={this._onCreated}
                        onDrawStart={this._onDrawStart}/>
                </FeatureGroup>
            </Map>
        )
    }
}

const Label = (props) => <label htmlFor={props.htmlFor} className="col-sm-2 control-label">{props.children}</label>;
const Input = (props) => (
    <div className="form-group">
        <Label htmlFor={props.name}>{props.children}</Label>
        <div className="col-sm-10">
            <input id={props.name} name={props.name} value={props.value || ''} readOnly={!props.onChange} required={true}
                   type={props.type || "text"} className="form-control" onChange={props.onChange} min={props.min}/>
        </div>
    </div>
);
const Select = (props) => (
    <div className="form-group">
        <Label htmlFor={props.name}>{props.children}</Label>
        <div className="col-sm-10">
            <select id={props.name} name={props.name} value={props.value || ''} required={true}
                    className="form-control" onChange={props.onChange}>
                <option></option>
                {props.items.map(item => <option key={item + '_option'}>{item}</option>)}
            </select>
        </div>
    </div>
);


module.exports = SourceEdit;
