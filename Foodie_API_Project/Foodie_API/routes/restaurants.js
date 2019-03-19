var express = require('express');
var router = express.Router();
var fetch = require('node-fetch');
var keys = require('../keys');

function getGIO(address, cb) {
    fetch(`https://api.geocod.io/v1.3/geocode?api_key=${keys.geocodio}&q=${address}`)
        .then(res => new Promise((resolve) => {
            if(res.status !== 200)
                resolve(false);
            else
                resolve(res.json());
        }))
        .then(res => {
            if(res)
                cb(false, res);
            else
                cb("Error retrieving data from GeoCode API");
        })
        .catch(err => cb(err));
}

function getRestaurants(location, cb) {
    fetch(`https://developers.zomato.com/api/v2.1/geocode?user-key=${keys.zomato}&lat=${location.lat}&lon=${location.lng}`, {
        headers: {
            'user-key': keys.zomato
        }
    })
        .then(res => new Promise((resolve) => {
          if(res.status !== 200)
            resolve(false);
          else
            resolve(res.json());
        }))
        .then(res => {
          if(res)
            cb(false, res);
          else
            cb("Error retrieving data from Zomato API");
        })
        .catch(err => cb(err));
}

router.get('/', function(req, res, next) {
  var { address } = req.query;

  if(!address) return res.status(400).send("No address provided");

  getGIO(address, (err, data) => {
      if(err) return res.status(500).send("Error getting geocode: " + err);
      getRestaurants(data.results[0].location, (err, data) => {
          if(err) {
            res.status(500);
            return res.status(500).send("Error retrieving restaurants " + err);
          }

          var restaurants = [];
          data.nearby_restaurants.forEach((rest) => {
              var restaurant = rest.restaurant;
              restaurants.push({
                  name: restaurant.name,
                  address: restaurant.location.address,
                  cuisines: restaurant.cuisines,
                  rating: restaurant.user_rating.aggregate_rating
              })
          });
          res.json({restaurants: restaurants});
      });
  });
});

module.exports = router;
