'use strict';

const React = require('react');
const {NavLink} = require('react-router-dom');
const client = require('./client');

class Menu extends React.Component {
    constructor(props) {
        super(props);
        this.state = {filters: []};
    }

    componentDidMount() {
        this.highlightActive();
        client({method: 'GET', path: '/api/filters'})
            .then(response => this.setState({filters: response.entity}));
    }

    componentDidUpdate() {
        this.highlightActive();
    }

    highlightActive() {
        $('nav li.active').removeClass('active');
        $(`nav li a[href='${decodeURIComponent(window.location.pathname)}']`).parents('li').addClass('active');
    }

    render() {
        return (
            <nav className="navbar navbar-default navbar-fixed-top">
                <div className="container">
                    <ul className="nav navbar-nav">
                        <li><NavLink to="/">Show new</NavLink></li>
                        <li className="dropdown btn-group">
                            <NavLink to="/saved/active" className="btn">Show saved</NavLink>
                            <NavLink to="#" className="btn dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true" aria-expanded="false">
                                <span className="caret"></span>
                            </NavLink>
                            <ul className="dropdown-menu">
                                <li><NavLink to="/saved/all">Show all</NavLink></li>
                                {
                                    this.state.filters.map(filter => <li key={'linkTo' + filter.name}><NavLink to={'/saved/' + filter.name}>Show {filter.name}</NavLink></li>)
                                }
                                <li><NavLink to="/saved/ignored">Show ignored</NavLink></li>
                            </ul>
                        </li>
                        <li><NavLink to="/favorites">Show favorites</NavLink></li>
                        <li><NavLink to="/sources">Sources</NavLink></li>
                        <li><NavLink to="/filters">Filters</NavLink></li>
                    </ul>
                    <div id="toolbar">{this.props.children}</div>
                </div>
            </nav>
        );
    }
}

module.exports = Menu;

