const moment = require('moment');

const format_date = (timezone) => (date) => {
    if (date) {
        //todo timezone
        // return moment.tz(date, 'UTC').tz(timezone).format('YYYY-MM-DD HH:mm:ss')
        return moment(date).format('YYYY-MM-DD HH:mm:ss');
    }
    return '';
};

module.exports = format_date('Europe/Minsk');
