'use strict';

const React = require('react');
const ReactDOM = require('react-dom');
const {BrowserRouter, Route, Switch, Redirect} = require('react-router-dom');
const NewApartments = require('./apartments/NewApartments');
const SavedApartments = require('./apartments/SavedApartments');
const Favorites = require('./apartments/Favorites');
const Sources = require('./sources/Sources');
const Filters = require('./filters/Filters');

class App extends React.Component {
    render() {
        return (
            <main>
                <Route exact path='/' component={NewApartments}/>
                <Switch>
                    <Route exact path='/saved/:filter' component={SavedApartments}/>
                    <Redirect from='/saved' to='/saved/all'/>
                </Switch>
                <Route path='/favorites' component={Favorites}/>
                <Route path='/sources' component={Sources}/>
                <Route path='/filters' component={Filters}/>
            </main>
        );
    }
}

ReactDOM.render((
    <BrowserRouter>
        <App />
    </BrowserRouter>
), document.getElementById('root'));
