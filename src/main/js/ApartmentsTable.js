const React = require('react');

const dateFormatter = require('./util/date-format');

class ApartmentsTable extends React.Component {

    constructor(props) {
        super(props);
    }

    render() {
        const rows = this.props.apartments.map(apartment =>
            <ApartmentRow key={apartment.url}
                          apartment={apartment}
                          showDetails={this.props.showDetails}/>);
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
    render() {
        const ap = this.props.apartment;
        return (
            <tr>
                <td>
                    <a className="glyphicon glyphicon-star-empty" href="#"/>
                    <a className="glyphicon glyphicon-ban-circle" href="#"/>
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
