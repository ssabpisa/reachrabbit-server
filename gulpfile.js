/**
 * gulp entry point
 *
 * @author     Poon Wu <poon.wuthi@gmail.com>
 * @since      0.0.1
 */
'use strict';

require('dotenv').config();

var _ = require('lodash'),
  async = require('async'),
  fs = require('fs'),
	gulp = require('gulp-help')(require('gulp')),
	gutil = require('gulp-util'),
	path = require('path'),
	_exec = require('child_process').exec,
  plugins = require('gulp-load-plugins')({
      pattern: ['gulp-*', 'gulp.*'],
      replaceString: /\bgulp[\-.]/
  }),
  args = process.argv.slice(3),
  argv    = require('yargs').argv,
  sequelizeCliPath = './node_modules/sequelize-cli/bin/sequelize';

// exec with piped output
function exec(cmd, obj, done) {
  var task = _exec(cmd, obj, done);
  task.stderr.pipe(process.stderr);
  task.stdout.pipe(process.stdout);
}

// exec with sequelize cli
function seqExec(cmd, done) {
  var cli = args.slice().map(function(e) {
    if(_.indexOf(e, ' ') >= 0) {
      return '"' + e + '"';
    }
    return e;
  }).join(' ');
  exec('node ' + sequelizeCliPath + ' ' + cmd + ' ' + cli, {}, done);
}

// pass gulp task directly to seqExec
var seqExecTask = function(done) {
  seqExec(this.seq.slice(-1)[0], done);
};

// create model migration files task
function createMigrationFiles(done) {
  var db = require('./api/models');

  // check if there's flag for singular model
  var isModel = function(model, key) {
    return argv.name ? key === argv.name || model.tableName === argv.name : true;
  };

  // each model
  _.forIn(db, function(model, key) {
    if(key !== 'Sequelize' && key !== 'sequelize' && isModel(model, key)) {
      // tablename
      var tableName = model.tableName;

      /* jshint quotmark: true */
      var template = "/**\n * Migration for table: " + tableName + "\n * Autogenerated by gulp task\n */\n'use strict';\n\nvar model = require('../models')."+ key +";\n\nmodule.exports = {\n  up: function(queryInterface, Sequelize) {\n    return queryInterface.createTable(model.tableName, model.attributes);\n  },\n  down: function(queryInterface, Sequelize) {\n    return queryInterface.dropTable(model.tableName);\n  }\n};";
      /* jshint quotmark:single */

      if(tableName !== undefined) {
        var filename = _.lowerCase(key);
        fs.writeFileSync('./api/migrations/' + filename + '.js', template);
      }
    }
  });
  done(null);
}

// lint
gulp.task('lint', 'Lint all server side js', function() {
	return gulp.src(['./**/*.js', '!./node_modules/**/*.js'])
		.pipe(plugins.jshint())
		.pipe(plugins.jshint.reporter(require('jshint-stylish')));
});

// test
gulp.task('test', 'Run all test', ['test:api']);
gulp.task('test:api', 'Run API test', function(done) {
  exec('swagger project test', {}, done);
});

// run express-swagger app
gulp.task('start', 'Start express app', function(done) {
  exec('swagger project start --debug', {}, done);
});

// run swagger edit mode
gulp.task('edit', 'Start swagger edit mode', function(done) {
  exec('swagger project edit', {}, done);
});

// mimick sequelize cli
gulp.task('db:migrate', 'Run pending migrations', seqExecTask);
gulp.task('db:migrate:undo', 'Revert last migration ran', seqExecTask);
gulp.task('db:migrate:undo:all', 'Revert all migration ran', seqExecTask);

gulp.task('db:seed', 'Run specified seed', seqExecTask);
gulp.task('db:seed:all', 'Run every seeder', seqExecTask);
gulp.task('db:seed:undo', 'Delete data from the database', seqExecTask);
gulp.task('db:seed:undo:all', 'Delete data from the database', seqExecTask);

// create model using our own migration file
gulp.task('model:create', 'Generate model', function(done) {
  var migrationPath = './api/migrations';
  async.waterfall([
    // call to sequelize model creation
    function(cb) {
      seqExec('model:create', cb);
    },
    // remove sequelize migration file (we don't need theirs)
    function(stdout, stderr, cb) {
      // delete native migration file
      var toBeDeleted = null;
      fs.readdir('./api/migrations', function(err, list) {
        if(err) {
          return cb(err);
        }

        // get latest created file
        _.forEach(list, function(f) {
          if(_.isNil(toBeDeleted)) {
            toBeDeleted = f;
          } else if(fs.statSync(path.resolve(migrationPath, toBeDeleted)).ctime.getTime() > fs.statSync(path.resolve(migrationPath, f)).ctime.getTime()) {
            toBeDeleted = f;
          }
        });

        if(toBeDeleted === null) {
          return cb(null);
        }

        // delete it
        fs.unlink(path.resolve(migrationPath, toBeDeleted), cb);
      });
    },
    // add our own version of migration file
    function(cb) {
      createMigrationFiles(cb);
    }
  ], done);
}, {
  aliases: ['model:generate'],
  options: {
    attributes: 'Model attributes in string form i.e, "username:string, phone:integer"',
    name: 'Model name'
  }
});

gulp.task('migration:create', 'Autogenerate migration files from models', function(done) {
  createMigrationFiles(done);
}, {
  options: {
    name: 'Model name'
  }
});
