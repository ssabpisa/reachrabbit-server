/**
 * Provide influencer user service
 *
 * @author     Poon Wu <poon.wuthi@gmail.com>
 * @since      0.0.3
 */
'use strict';

var config = require('config'),
    User        = require('../models').User,
    Influencer  = require('../models').Influencer,
    InfluencerMedia  = require('../models').InfluencerMedia,
    Media  = require('../models').Media,
    Resource = require('../models').Resource,
    authHelper = require('../helpers/authHelper'),
    cacheHelper = require('../helpers/cacheHelper');

// build => ?
// create => ?
module.exports = {
  build: function(user, t) {

  },
  create: function(user, t) {
    var influencerSchema = ['about', 'bankAccount'];
    var mediaSchema = ['socialAccounts'];

    // split objects
    var medias = user[mediaSchema];
    var mediaKeys = _.keys(medias);
    
    return Media.findAll({
      where: {
        mediaName: mediaKeys
      },
      transaction: t
    })
    .then(function(results) {
      if(results.length <= 0) {
        throw new Error('no media found');
      }

      // add influencerMedia entry to each Media
      _.forEach(results, function(media) {
          media = _.extend(media, {
            InfluencerMedia: {
              socialId: medias[media.mediaName].id,
              pageId: medias[media.mediaName].pageId,
              token: medias[media.mediaName].token
            }
          });
      });

      // create user
      return User.create(newUser, { include: [Influencer], transaction: t })
        .then(function(createdUser) {
          // get created influencer
          return createdUser.Influencer.addMedia(results, { transaction: t })
            .then(function() {
              return createdUser;
            });
        });
    });
  },
  findById: function(id) {
    return User.findById(id, {
      include: [{
        model: Influencer,
        include: [{
          model: Media,
          through: {
            model: InfluencerMedia
          }
        }],
        required: true
      }]
    })
    .then(function(user) {
      if(!user) {
        return user;
      }
      return Resource.findById(user.get('profilePicture'))
        .then(function(result) {
          // assign profile pic
          user.dataValues.profilePicture = result;
          return user;
        });
    })
    .then(function(user) {
      if(user) {
        // flatten user
        _.extend(user.dataValues, user.dataValues.Influencer);

        // media
        user.dataValues.socialAccounts = {};
        _.forEach(user.Influencer.Media, function(media) {
          user.dataValues.socialAccounts[media.mediaName] = {
            id: media.InfluencerMedia.socialId,
            pageId: media.InfluencerMedia.pageId
          };
        });
      }
      return user;
    });
  },
  findByMedia: function(providerName, socialId) {
    return User.findOne({
      include: [{
          //Influencer
          model: Influencer,
          include: [{
            model: Media,
            where: {
              mediaName: providerName,
              isActive: true
            },
            through: {
              where: {
                socialId: socialId
              }
            },
            required: true
          }],
          required: true
      }]
    });
  },
  createToken: function(user, cache) {
    // cache user
    if(cache) {
      cacheHelper.set(user.userId, {
        user: user,
        role: config.ROLE.INFLUENCER
      });
    }
    // encode userid
    return authHelper.encode({
      userId: user.userId
    });
  },
  login: function(providerName, socialId) {
    return this.findByMedia(providerName, socialId)
      .then(function(user) {
        if(!user) {
          throw new Error('no user found');
        }
      });
  }
};
