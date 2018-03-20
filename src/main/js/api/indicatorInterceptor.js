
    const React = require('react');
    const ReactDOM = require('react-dom');
    const interceptor = require('rest/interceptor');

    class SpinnerComponent extends React.Component {
        constructor(props) {
            super(props);
            this.state = {counter: 0};
            this.show = this.show.bind(this);
            this.hide = this.hide.bind(this);
            this.props.handlers.show = this.show;
            this.props.handlers.hide = this.hide;
        }

        show() {
            setTimeout(() => this.setState({counter: this.state.counter + 1}), 300);
        }

        hide() {
            this.setState({counter: this.state.counter - 1});
        }

        render() {
            return (
                <div className='spinner-container'>
                    {this.state.counter > 0 ? <Spinner/> : null}
                </div>
            );
        }
    }

    const Spinner = () => {
        return (
            <span className='spinner'>
                <span className="glyphicon glyphicon-refresh glyphicon-refresh-animate"></span> Loading...
            </span>
        );
    };

    const spinnerHandlers = {};

    ReactDOM.render((
        <SpinnerComponent handlers={spinnerHandlers}/>
    ), document.getElementById('loading-spinner'));



    module.exports = interceptor({
        request: function (request) {
            spinnerHandlers.show();
            return request;
        },
        response: function (response) {
            spinnerHandlers.hide();
            return response;
        }
    });
