'use strict';

const React = require('react');
const {NavLink} = require('react-router-dom');

class Menu extends React.Component {
    componentDidMount() {
        this.highlightActive();
    }

    componentDidUpdate() {
        this.highlightActive();
    }

    highlightActive() {
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
                                <li><NavLink to="/saved">Show all</NavLink></li>
                                <li><NavLink to="/ignored">Show ignored</NavLink></li>
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

