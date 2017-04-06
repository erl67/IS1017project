/* global angular: false */
(function() {
  var app = angular.module('weatherTM', ['chart.js']);

  app.controller('ErrorController', ['$rootScope', function($rootScope) {
    var error = this;
    error.message = '';
    error.showError = false;
    $rootScope.displayError = function(message) {
      error.message = message;
      error.showError = true;
    };
    error.hideError = function() {
      error.showError = false;
    };
  }]);

  app.controller('LocationController', ['$http', '$rootScope', function($http, $rootScope) {
    var lc = this;
    lc.queryInfo = {
      date: new Date()
    };
    lc.submit = function() {
      lc.queryInfo.date = new Date(lc.queryInfo.date);
      $http.get(`https://maps.googleapis.com/maps/api/geocode/json?address=${lc.location.replace(' ', '+')}`)
        .then(function success(response) {
          if(response.data.status === 'ZERO_RESULTS') {
            $rootScope.displayError('No results found. Please try again.');
          } else if(response.data.status !== 'OK') {
            $rootScope.displayError('An error occurred getting location data. Please try again');
          } else {
            lc.data = response.data.results[0];
            lc.queryInfo.latitude = lc.data.geometry.location.lat;
            lc.queryInfo.longitude = lc.data.geometry.location.lng;
            $rootScope.location = {
              longAddress: lc.data.formatted_address,
              date: lc.queryInfo.date
            };
            $rootScope.getData(lc.queryInfo);
            $rootScope.getHistory(lc.queryInfo);
          }
        });
    };
    lc.geolocate = function() {
      if('geolocation' in navigator) {
        navigator.geolocation.getCurrentPosition((position) => {
          lc.location = `${position.coords.latitude}, ${position.coords.longitude}`;
          lc.submit();
        });
      } else {
        $rootScope.displayError('Geolocation is not supported by your browser. Please type in a location.');
      }
    };
  }]);

  app.controller('darkSky', ['$http', '$rootScope', function($http, $rootScope) {
    var ds = this;
    $rootScope.getData = function(queryInfo) {
      if(!queryInfo.date || !queryInfo.longitude) {
        return;
      }
      var url = `https://crossorigin.me/https://api.darksky.net/forecast/472f1ba38a5f3d13407fdb589d975c8c/${queryInfo.latitude},${queryInfo.longitude},${queryInfo.date.toJSON().split('.')[0]}?exclude=minutely,hourly,flags`;
      $http.get(url)
        .then(function success(response) {
          ds.data = response.data;
        }, function failure(response) {
          $rootScope.displayError(response.data.error);
        });
    };
  }]);

  app.controller('DisplayController', function() {
    var dc = this;
    dc.dataPoints = [];
  });

  app.controller('HistoryController', function($rootScope, $http) {
    var hc = this;
    $rootScope.getHistory = function(queryInfo) {
      var month = queryInfo.date.getMonth() + 1;
      var day = queryInfo.date.getDate();
      var url = `https://crossorigin.me/http://history.muffinlabs.com/date/${month}/${day}`;
      $http.get(url)
        .then(function success(response) {
          hc.historyData = response.data.data;
          console.log(hc.historyData);
        }, function failure() {
          $rootScope.displayError('Cannot fetch historyData');
        });
    };
  });

  app.controller('ChartController', function($scope) {
    $scope.labels = ['January', 'February', 'March', 'April', 'May', 'June', 'July'];
    $scope.series = ['Series A', 'Series B'];
    $scope.data = [
       [65, 59, 80, 81, 56, 55, 40],
       [28, 48, 40, 19, 86, 27, 90]
    ];
    $scope.onClick = function (points, evt) {
      console.log(points, evt);
    };
  });

})();
