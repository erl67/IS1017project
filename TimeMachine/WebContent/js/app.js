/* global angular: false */
(function() {
  var app = angular.module('weatherTM', ['chart.js', 'ngCookies', 'ngSanitize']);

  app.controller('NavbarController', ['$rootScope', '$cookies', function($rootScope, $cookies) {
    var nc = this;
    nc.checkLogin = function() {
      var username = $cookies.get('TimeMachine_cookie');
      if(username) {
        $rootScope.user = {
          loggedIn: true,
          username: username
        };
      } else {
        $rootScope.user = {
          loggedIn: false
        };
      }
    };
    nc.checkLogin();
    nc.logout = function() {
      $cookies.remove('TimeMachine_cookie');
      nc.checkLogin();
    };
  }]);

  app.controller('PanelController', function($scope) {
    $scope.panel = 1;
    this.showRawDSData = false;
    this.currentPanel = function(input) {
      return $scope.panel === input;
    };
    $scope.setPanel = function(input) {
      $scope.panel = input;
    };
  });

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

  app.controller('InputController', ['$http', '$rootScope', function($http, $rootScope) {
    var ic = this;
    ic.queryInfo = {
      date: new Date()
    };
    ic.submit = function() {
      ic.queryInfo.date = new Date(ic.queryInfo.date);
      $rootScope.getHistory(ic.queryInfo.date);
      if(!ic.location) {
        return;
      }
      $http.get(`https://maps.googleapis.com/maps/api/geocode/json?address=${ic.location.replace(' ', '+')}`)
        .then(function success(response) {
          if(response.data.status === 'ZERO_RESULTS') {
            $rootScope.displayError('No results found. Please try again.');
          } else if(response.data.status !== 'OK') {
            $rootScope.displayError('An error occurred getting location data. Please try again');
          } else {
            ic.mapsData = response.data.results[0];
            ic.queryInfo.latitude = ic.mapsData.geometry.location.lat;
            ic.queryInfo.longitude = ic.mapsData.geometry.location.lng;
            ic.queryInfo.title = ic.mapsData.formatted_address;
            $rootScope.location = {
              longAddress: ic.mapsData.formatted_address,
              date: ic.queryInfo.date
            };
            $rootScope.getDarkSkyData(ic.queryInfo);
          }
        });
    };
    ic.geolocate = function() {
      if('geolocation' in navigator) {
        navigator.geolocation.getCurrentPosition((position) => {
          ic.location = `${position.coords.latitude}, ${position.coords.longitude}`;
          ic.submit();
        });
      } else {
        $rootScope.displayError('Geolocation is not supported by your browser. Please type in a location.');
      }
    };
  }]);

  app.controller('darkSky', ['$http', '$rootScope', function($http, $rootScope) {
    var ds = this;
    $rootScope.getDarkSkyData = function(queryInfo) {
      //if($rootScope.user.loggedIn) {
      queryInfo.username = $rootScope.user.username || null;
      console.log(queryInfo);
      $http.post('/TimeMachine/HistoryServlet', queryInfo)
        .then(function success(response) {
          console.log(response);
        }, function error(response) {
          console.log(response);
        });
      //}

      var yearsArray = getYears(queryInfo.date);
      yearsArray.forEach(date => console.log(date));
      ds.weatherData = [];
      yearsArray.forEach(function(date, index) {
        console.log(date);
        var url = `https://crossorigin.me/https://api.darksky.net/forecast/472f1ba38a5f3d13407fdb589d975c8c/${queryInfo.latitude},${queryInfo.longitude},${date.toJSON().split('.')[0]}?exclude=minutely,hourly,flags`;
        $http.get(url)
          .then(function success(response) {
            ds.weatherData[index] = response.data;
          }, function failure(response) {
            $rootScope.displayError(response.data.error);
          });
      });
    };
  }]);

  app.controller('DisplayController', function() {
    var dc = this;
    dc.dataPoints = [];
  });

  app.controller('HistoryController', function($rootScope, $http) {
    var hc = this;
    hc.random = function() {
      return 0.5 - Math.random();
    };
    $rootScope.getHistory = function(date) {
      if(!date) {
        return;
      }
      var month = date.getMonth() + 1;
      var day = date.getDate();
      var url = `https://crossorigin.me/http://history.muffinlabs.com/date/${month}/${day}`;
      $http.get(url)
        .then(function success(response) {
          hc.historyData = response.data.data;
        }, function failure() {
          $rootScope.displayError('Cannot fetch historical events');
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

  function getYears(input) {
    var current = new Date();
    var dates = [];

    for(var i = 0; i < 5; i++) {
      dates.push(new Date(input));
    }

    var difference = current.getFullYear() - input.getFullYear();
    var mid = current.getFullYear() - difference/2 |0;
    var q1 = mid + difference/4 |0;
    var q3 = mid - difference/4 |0;

    dates[1].setYear(q3);
    dates[2].setYear(mid);
    dates[3].setYear(q1);
    dates[4].setYear(current.getFullYear());

    return dates;
  }

})();
