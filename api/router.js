/**
 * Handle global API routing
 *
 * @author     Poon Wu <poon.wuthi@gmail.com>
 * @author     Pat Sabpisal <ecegrid@gmail.com>
 * @since      0.0.1
 */
'use strict';

var express   = require('express'),
    expressValidate  = require('express-jsonschema').validate,
    auth      = require('./middlewares/auth'),
    paginate  = require('./middlewares/pagination'),
    $         = require('./controllers'),
    S         = require('./schemas');

var multer = require('multer');
var upload = multer({ dest: '.tmp' });

// validators function
function validate(body, query) {
  return expressValidate({body: body, query: query});
}
function validatePost(body) {
  return validate(body);
}
function validateQuery(query) {
  return validate(undefined, query);
}


module.exports = function() {
  // initiate express router object
  var router = express.Router({
    caseSensitive: true
  });

  // test-only
  if(process.env.NODE_ENV === 'test') {
    router.post('/tests/influencerLogin', $.testController.influencerBypassLogin);
  }

  /*********************************
   * Authentication
   *********************************/
  router.post('/auth/facebook', $.authController.facebook);
  router.post('/auth/google', $.authController.google);
  router.post('/auth/instagram', $.authController.instagram);

  router.post('/login', $.authController.brandLogin);

  /********************************
   * User
   ********************************/
  router.post('/users/influencer', $.userController.createInfluencer);
  router.get('/users/influencer', $.userController.listInfluencer);
  router.get('/users/influencer/:userId', $.userController.getInfluencer);

  router.post('/users/brand', $.userController.createBrand);
  router.get('/users/brand', $.userController.listBrand);
  router.get('/users/brand/:userId', $.userController.getBrand);

  router.get('/profiles', auth(), $.userController.getProfile);
  router.put('/profiles', auth(), $.userController.updateProfile);

  /*********************************
   * Campaign
   *********************************/
  router.get('/campaigns', auth(), paginate(), $.campaignController.list);
  router.get('/campaigns/:campaignId', auth(), $.campaignController.get);
  router.post('/campaigns', auth('brand'), $.campaignController.create);
  router.put('/campaigns/:campaignId', auth('brand'), $.campaignController.update);

  router.get('/mycampaigns', auth(), paginate(), $.campaignController.listByRole);

  // create proposal
  router.post('/campaigns/:campaignId/proposals', auth('influencer'), $.campaignController.createProposal);
  router.post('/campaigns/:campaignId/submissions', auth('influencer'), _.noop);
  router.put('/campaigns/:campaignId/proposals/:proposalId', auth('influencer', 'brand'), $.campaignController.updateProposal);
  router.put('/campaigns/:campaignId/submissions/:submissionId', auth('influencer', 'brand'), _.noop);

  router.post('/campaigns/:campaignId/payments', auth('brand'), $.campaignController.createPayment);

  /*********************************
   * Payment
   *********************************/
  router.get('/payments', auth('admin', 'brand'), _.noop);

  /*********************************
   * Data
   *********************************/
  router.get('/data/categories', $.dataController.getActiveCategories);
  router.get('/data/medium', $.dataController.getActiveMedium);
  router.get('/data/banks', $.dataController.getBanks);

  /*********************************
   * File
   *********************************/

  router.get ('/file', $.fileController.listAll);
  router.post('/file', upload.single('file'), $.fileController.uploadSingle);
  router.post('/file/remote', $.fileController.fromRemote);


  router.get('/default/create', $.defaultController.testCreate);
  router.get('/default/delete', $.defaultController.testDelete);
  router.get('/default/findAll', $.defaultController.testFind);
  router.get('/default/update', $.defaultController.testUpdate);

  return router;
};
