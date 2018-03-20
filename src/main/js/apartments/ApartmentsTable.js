const React = require('react');
const dateFormatter = require('../util/date-format');
const {apartments: client} = require('../api/client_helper');
const Modal = require('react-modal');
const {Map, TileLayer, Marker} = require('react-leaflet');
const Lightbox = require('react-images').default;

class ApartmentsTable extends React.Component {

    constructor(props) {
        super(props);
        this.state = {selectedRow: null};
        this.selectRow = this.selectRow.bind(this);
    }

    selectRow(event) {
        const selectedRow = event.target.closest('tr');
        this.toggleClass([this.state.selectedRow, selectedRow], 'selected');
        this.setState({selectedRow});
    }

    toggleClass(elements, className) {
        elements.filter(element => !!element).forEach(element => element.classList.toggle(className));
    }

    render() {
        const rows = this.props.apartments.map(apartment =>
            <ApartmentRow key={apartment.url}
                          apartment={apartment}
                          showDetails={this.props.showDetails}
                          onClick={this.selectRow}/>);
        return (
            <div>
                <Header items={rows.length} prefix={this.props.header}/>
                <table className="table apartments">
                    <thead>
                    <tr>
                        <th width="4%"></th>
                        <th width="6%">ID</th>
                        <th width="14%">Updated</th>
                        <th width="7%">Price</th>
                        <th width="6%">Type</th>
                        <th width="16%">Address</th>
                        {this.props.showDetails ? <th width="40%">Text</th> : null}
                        {this.props.showDetails ? <th width="5%">Images</th> : null}
                    </tr>
                    </thead>
                    <tbody>{rows}</tbody>
                </table>
            </div>
        )
    }
}

const Header = ({items, prefix = ''}) => {
    const text = `${prefix} apartments (${items} items)`.trim().toLowerCase();
    const capitalizedText = text[0].toUpperCase() + text.substring(1);
    return <h3>{capitalizedText}</h3>;
};

class ApartmentRow extends React.Component {
    constructor(props) {
        super(props);
        this.toggleFavorite = this.toggleFavorite.bind(this);
        this.toggleIgnored = this.toggleIgnored.bind(this);
    }

    toggleFavorite(event) {
        event.preventDefault();
        const apartment = this.props.apartment;
        apartment.favorite = !apartment.favorite;
        client.update(apartment);
    }

    toggleIgnored(event) {
        event.preventDefault();
        const apartment = this.props.apartment;
        apartment.ignored = !apartment.ignored;
        client.update(apartment);
    }

    render() {
        const ap = this.props.apartment;
        return (
            <tr onClick={this.props.onClick} className={ap.active ? '' : 'not-active'}>
                <td>
                    <a onClick={this.toggleFavorite} className={`glyphicon ${ap.favorite ? 'glyphicon-star' : 'glyphicon-star-empty'}`} href="#"/>
                    <a onClick={this.toggleIgnored} className={`glyphicon ${ap.ignored ? 'glyphicon-ok' : 'glyphicon-ban-circle'}`} href="#"/>
                </td>
                <td><a href={ap.url} target="_blank">{ap.id}</a></td>
                <td>{dateFormatter(ap.last_time_up)}</td>
                <td>{ap.price.amount + ' ' + ap.price.currency}</td>
                <td>{ap.rent_type}</td>
                <td><LocationLink apartment={ap}/></td>
                {this.props.showDetails ? <td>{ap.text}</td> : null}
                {this.props.showDetails ? <td><PhotosViewer apartment={ap}/></td> : null}
            </tr>
        )
    }
}

class PhotosViewer extends React.Component {
    constructor(props) {
        super(props);
        this.state = {isOpen: false, currentImage: 0, apartment: this.props.apartment};
        this.open = this.open.bind(this);
        this.close = this.close.bind(this);
        this.showNext = this.showNext.bind(this);
        this.showPrev = this.showPrev.bind(this);
        this.gotoImage = this.gotoImage.bind(this);
    }

    open(e) {
        e.preventDefault();
        this.setState({isOpen: true})
    }

    close() {
        this.setState({isOpen: false, currentImage: 0});
    }

    showNext() {
        this.setState({currentImage: this.state.currentImage + 1});
    }

    showPrev() {
        this.setState({currentImage: this.state.currentImage - 1});
    }

    gotoImage(index) {
        this.setState({currentImage: index});
    }

    render() {
        const ap = this.state.apartment;
        const images = ap.images.map(img => {return {src: img}});
        return [
            <a key={ap.id + '_photos_gallery_link'} onClick={this.open} href='#'>Show</a>,
            <Lightbox key={ap.id + '_photos_gallery'} isOpen={this.state.isOpen}
                      images={images}
                      showThumbnails={true}
                      onClose={this.close}
                      currentImage={this.state.currentImage}
                      onClickNext={this.showNext}
                      onClickPrev={this.showPrev}
                      onClickThumbnail={this.gotoImage}
                      preventScroll={false}/>
        ]
    }
}

class LocationLink extends React.Component {
    constructor(props) {
        super(props);
        this.state = {isOpen: false, apartment: this.props.apartment};
        this.open = this.open.bind(this);
        this.close = this.close.bind(this);
    }

    open(event) {
        event.preventDefault();
        this.setState({isOpen: true})
    }

    close() {
        this.setState({isOpen: false});
    }

    render() {
        const ap = this.state.apartment;
        const position = [ap.location.latitude, ap.location.longitude];
        return [
            <a key={ap.id + '_location_link'} onClick={this.open} href='#'>{ap.location.address}</a>,
            <Modal key={ap.id + '_location_popup'} isOpen={this.state.isOpen} onRequestClose={this.close}>
                <Map center={position} zoom={13}>
                    <TileLayer attribution="&amp;copy <a href=&quot;http://osm.org/copyright&quot;>OpenStreetMap</a> contributors"
                        url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"/>
                    <Marker position={position}/>
                </Map>
            </Modal>
        ]
    }
}

module.exports = ApartmentsTable;
