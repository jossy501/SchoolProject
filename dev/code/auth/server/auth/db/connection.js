const monk = require('monk');
const db = monk('localhost/authdb');

module.exports = db;
