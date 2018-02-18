'use strict';

const client = require('../client');

const baseActions = (path, idFunc) => {
    return {
        getAll: () => {
            return client({method: 'GET', path})
        },

        remove: (entity) => {
            return client({method: 'DELETE', path: path + '/' + idFunc(entity)})
        },

        update: (entity) => {
            return client({
                method: 'PUT', path: path + '/' + idFunc(entity),
                headers: {'Content-Type': 'application/json'},
                entity
            })
        },

        create: (entity) => {
            return client({
                method: 'POST', path,
                headers: {'Content-Type': 'application/json'},
                entity
            })
        }
    }
};

const apartmentsActions = (path, idFunc) => {
    const actions = {
        getNew: () => {
            return client({method: 'GET', path: path + '/new'})
        },
        getFavorites: () => {
            return actions.search('favorites')
        },
        search: (filterName) => {
            return client({method: 'GET', path: path + `/search/${filterName}`})
        },
        removeAll: () => {
            return client({method: 'DELETE', path: path + '/all'})
        },
        createAll: (apartments) => {
            return client({
                    method: 'POST', path: path + '/all',
                    headers: {'Content-Type': 'application/json'},
                    entity: {items: apartments}
                }
            )
        }
    };
    return Object.assign(baseActions(path, idFunc), actions);
};

const sources = baseActions('/api/sources', source => source.name);
const filters = baseActions('/api/filters', filter => filter.name);
const apartments = apartmentsActions('/api/apartments', apartment => apartment.id);


module.exports = {
    sources, filters, apartments
};