const path = require('path');
const webpack = require('webpack');

module.exports = {
    entry: {
        main: ["es6-promise", "babel-polyfill", "react-hot-loader/patch", "./src/main/js/App.js"]
    },
    devtool: 'source-map',
    cache: true,
    devServer: {
        hot: true,
        port: 8081,
        proxy: {
            "*": 'http://localhost:8080'
        }
    },
    output: {
        path: path.join(__dirname, "src/main/resources/static/built/"),
        publicPath: 'http://localhost:8081/built/',
        filename: 'bundle.js'
    },
    module: {
        loaders: [
            {
                test: path.join(__dirname, '.'),
                exclude: /(node_modules)/,
                loader: 'babel-loader',
                query: {
                    cacheDirectory: true,
                    presets: ['es2015', 'react']
                }
            }
        ]
    },
    plugins: [
        new webpack.LoaderOptionsPlugin({
            debug: true
        })
    ]
};