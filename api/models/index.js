/**
 * Handle all sequelize models
 *
 * @author     Poon Wu <poon.wuthi@gmail.com>
 * @since      0.0.1
 */
'use strict';

var fs        = require('fs');
var path      = require('path');
var Sequelize = require('sequelize');
var basename  = path.basename(module.filename);
var env       = process.env.NODE_ENV || 'development';
var db        = {};
var sequelize = new Sequelize(process.env.POSTGRES_CONNECTION_URI);

// parse all models in this dir
fs
  .readdirSync(__dirname)
  .filter(function(file) {
    return (file.indexOf('.') !== 0) && (file !== basename) && (file.slice(-3) === '.js');
  })
  .forEach(function(file) {
    // import models
    var model = sequelize['import'](path.join(__dirname, file));
    db[model.name] = model;
  });

// associate models (one-to-one, one-to-many, etc.)
Object.keys(db).forEach(function(modelName) {
  if (db[modelName].associate) {
    db[modelName].associate(db);
  }
});

db.sequelize = sequelize;
db.Sequelize = Sequelize;

// export db
module.exports = db;
