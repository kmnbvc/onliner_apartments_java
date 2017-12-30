const React = require('react');
const dateFormatter = require('./util/date-format');
const client = require('./client');

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
                        <th width="13%">Updated</th>
                        <th width="7%">Price</th>
                        <th width="6%">Type</th>
                        <th width="15%">Address</th>
                        {this.props.showDetails ? <th width="44%">Text</th> : null}
                        <th width="5%">URL</th>
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
        client({method: 'PUT', path: apartment._links.self.href,
            headers: {'Content-Type': 'application/json'},
            entity: apartment});
    }

    toggleIgnored(event) {
        event.preventDefault();
        const apartment = this.props.apartment;
        apartment.ignored = !apartment.ignored;
        client({method: 'PUT', path: apartment._links.self.href,
            headers: {'Content-Type': 'application/json'},
            entity: apartment});
    }

    render() {
        const ap = this.props.apartment;
        return (
            <tr onClick={this.props.onClick}>
                <td>
                    <a onClick={this.toggleFavorite} className={`glyphicon ${ap.favorite ? 'glyphicon-star' : 'glyphicon-star-empty'}`} href="#"/>
                    <a onClick={this.toggleIgnored} className={`glyphicon ${ap.ignored ? 'glyphicon-ok' : 'glyphicon-ban-circle'}`} href="#"/>
                </td>
                <td>{ap.id}</td>
                <td>{dateFormatter(ap.last_time_up)}</td>
                <td>{ap.price.amount + ' ' + ap.price.currency}</td>
                <td>{ap.rent_type}</td>
                <td>{ap.location.address}</td>
                {this.props.showDetails ? <td>{ap.text}</td> : null}
                <td>
                    <a href={ap.url} target="_blank">Open</a>
                </td>
            </tr>
        )
    }
}

module.exports = ApartmentsTable;
