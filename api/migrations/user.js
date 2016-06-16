/**
 * Migration for table: Users
 * Autogenerated by gulp task
 */
'use strict';

var model = require('../models').User;

module.exports = {
  up: function(queryInterface, Sequelize) {
    return queryInterface.createTable(model.tableName, model.attributes);
  },
  down: function(queryInterface, Sequelize) {
    return queryInterface.dropTable(model.tableName);
  }
};
