/* global angular: false */
(function() {
  var app = angular.module('weatherTM', ['chart.js', 'ngCookies', 'ngSanitize']);

  app.config(['ChartJsProvider', function(ChartJsProvider) {
    // Configure all charts
    ChartJsProvider.setOptions({
      chartColors: ['#ffff00', '#00e2ff', '#7e44ff'],
    });
  }]);

  var dateLabels = [];
  var maxTempData = [];
  var currentTempData = [];
  var currentSummaryData = [];
  var minTempData = [];
  var dateSummary = [];
  var searchTime;

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
      $cookies.remove('TimeMachine_cookie', {path: '/'});
      $cookies.remove('TimeMachine_uid', {path: '/'});
      nc.checkLogin();
    };
  }]);

  app.controller('PanelController', ['$rootScope', function($rootScope) {
    $rootScope.panel = 1;
    this.currentPanel = function(input) {
      return $rootScope.panel === input;
    };
    $rootScope.setPanel = function(input) {
      $rootScope.panel = input;
    };
  }]);

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
        $rootScope.setPanel(2);
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
            ic.queryInfo.title = ic.title || ic.mapsData.formatted_address;
            $rootScope.location = {
              title: ic.queryInfo.title,
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

    $rootScope.runWeatherQuery = function(latitude, longitude, date, title) {
      ic.location = `${latitude}, ${longitude}`;
      ic.queryInfo.date = date;
      ic.title = title;
      ic.submit();
    };

    $rootScope.getDarkSkyData = function(queryInfo) {
      if($rootScope.user.loggedIn) {
        queryInfo.username = $rootScope.user.username || null;
        $http.post('/TimeMachine/HistoryServlet', queryInfo)
          .then(function success(response) {
            response.data.forEach(item => item.Date = new Date(item.Date));
            $rootScope.userHistory = response.data;
          }, function error(response) {
            console.log(response.data);
          });
      }

      var yearsArray = getYears(queryInfo.date);
      searchTime = queryInfo.date.toLocaleTimeString([], {hour: '2-digit', minute: '2-digit'});

      ic.weatherData = [];
      yearsArray.forEach(function(date, index) {
        var url = `https://crossorigin.me/https://api.darksky.net/forecast/472f1ba38a5f3d13407fdb589d975c8c/${queryInfo.latitude},${queryInfo.longitude},${JSON.stringify(Math.round(date.getTime()/1000))}?exclude=minutely,hourly,flags`;
        $http.get(url)
          .then(function success(response) {
            ic.weatherData[index] = response.data;
            // Get dates, minimum temperature, maximum temperature, and summary of weather for each date and store in arrays.
            dateLabels[index] = date.toLocaleDateString();
            maxTempData[index] = ic.weatherData[index].daily.data[0].temperatureMax;
            currentTempData[index] = ic.weatherData[index].currently.temperature;
            currentSummaryData[index] = ic.weatherData[index].currently.summary;
            minTempData[index] = ic.weatherData[index].daily.data[0].temperatureMin;
            dateSummary[index] = ic.weatherData[index].daily.data[0].summary;

            $rootScope.setPanel(3);
          }, function failure(response) {
            $rootScope.displayError(response.data.error);
          });
      });
    };
  }]);

  app.controller('MyHistoryController', ['$rootScope', '$http', function($rootScope, $http) {
    var myc = this;
    myc.getUserHistory = function() {
      $http.get('./HistoryServlet')
        .then(function success(response) {
          response.data.forEach(item => item.Date = new Date(item.Date));
          $rootScope.userHistory = response.data;
        }, function error(response) {
          $rootScope.displayError('Error getting your past searches');
          console.log(response);
        });
    };
    myc.getUserHistory();
    myc.showPerviousQuery = function(item) {
      $rootScope.runWeatherQuery(item.Latitude, item.Longitude, item.Date, item.Title);
    };
  }]);

  app.controller('HistoryController', ['$rootScope', '$http', function($rootScope, $http) {
    var hc = this;
    hc.date = new Date();
    $rootScope.getHistory = function(date) {
      if(!date) {
        return;
      }
      hc.date = date;
      var month = date.getMonth() + 1;
      var day = date.getDate();
      var url = `https://crossorigin.me/http://history.muffinlabs.com/date/${month}/${day}`;
      $http.get(url)
        .then(function success(response) {
          hc.historyData = response.data.data.Events.filter((item) => !(item.year.includes('BC') || parseInt(item.year) < 1940));
        }, function failure() {
          $rootScope.displayError('Cannot fetch historical events');
        });
    };
  }]);

  app.controller('ChartController', ['$scope', function($scope) {
    $scope.labels = dateLabels;
    $scope.series = ['Max. Temperature', 'Search Time Temperature', 'Min. Temperature', 'Summary'];
    $scope.data = [
      maxTempData,
      currentTempData,
      minTempData,
      dateSummary
    ];
    $scope.onClick = function(points, evt) {
      console.log(points, evt);
    };
    $scope.options = {
      animation: {
        onComplete: function() {
          var ctx = this.chart.ctx;
          ctx.textAlign = 'center';
          ctx.textBaseline = 'bottom';

          this.chart.config.data.datasets.forEach(function(dataset) {
            for(var key in dataset._meta) {
              var pointXBuffer = 0;
              var pointYBuffer = 0;
              if(dataset.label == 'Max. Temperature') {
                // For Max. Temperature Data
                dataset._meta[key].data.forEach(function(point) {
                  if(point._index == 0) {
                    pointXBuffer = 35;
                  } else if(point._index > 0 && point._index < 4) {
                    pointXBuffer = 35;
                  } else if(point._index == 4) {
                    pointXBuffer = -35;
                  }
                  if(dataset.data[point._index] == Math.max.apply(Math, maxTempData)) {
                    ctx.font = '2vmin Monaco';
                    ctx.fillStyle = '#FF0000';
                    ctx.fillText(dataset.data[point._index] + ' ℉', point._view.x + pointXBuffer, point._view.y);
                  } else {
                    //  ctx.font = '15px Monaco';
                    //  ctx.fillStyle='#848484';
                    //  ctx.fillText(dataset.data[point._index] + ' ℉', point._view.x + pointXBuffer, point._view.y);
                  }
                  pointXBuffer = 0;
                });
              } else if(dataset.label == 'Min. Temperature') {
                // For Min. Temperature Data
                dataset._meta[key].data.forEach(function(point) {
                  if(point._index == 0) {
                    pointXBuffer = 35;
                  } else if(point._index > 0 && point._index < 4) {
                    pointXBuffer = 35;
                  } else if(point._index == 4) {
                    pointXBuffer = -35;
                  }
                  if(dataset.data[point._index] == Math.min.apply(Math, minTempData)) {
                    ctx.font = '2vmin Monaco';
                    ctx.fillStyle = 'blue';
                    ctx.fillText(dataset.data[point._index] + ' ℉', point._view.x + pointXBuffer, point._view.y);
                  } else {
                    //  ctx.font = '15px Monaco';
                    //  ctx.fillStyle='#848484';
                    //  ctx.fillText(dataset.data[point._index] + ' ℉', point._view.x + pointXBuffer, point._view.y);
                  }
                  pointXBuffer = 0;
                });
              } else if(dataset.label == 'Search Time Temperature') {
                // For Search Time Temperature Data
                dataset._meta[key].data.forEach(function(point) {
                  if(point._index == 0) {
                    var a = dataset.data[point._index];
                    var b = dataset.data[point._index + 1];
                    var difference = Math.abs(a - b);
                    if(difference < 5) {
                      pointYBuffer = 20;
                    }
                    console.log(difference);
                    pointXBuffer = 35;
                  } else if(point._index > 0 && point._index < 4) {
                    pointXBuffer = -35;
                  } else if(point._index == 4) {
                    pointXBuffer = -35;
                    ctx.font = '8px Monaco';
                    ctx.fillStyle = '#848484';
                    ctx.fillText(searchTime, point._view.x + pointXBuffer + 65, point._view.y);
                  }
                  ctx.font = '2vmin Monaco';
                  ctx.fillStyle = '#3c3c3c';
                  ctx.fillText(dataset.data[point._index] + ' ℉', point._view.x + pointXBuffer, point._view.y + pointYBuffer);
                  pointXBuffer = 0;
                  pointYBuffer = 0;
                });
              }
              break;
            }
          });
        }
      }
    };
  }]);

  function getYears(input) {
    var current = new Date();
    var dates = [];

    for(var i = 0; i < 5; i++) {
      dates.push(new Date(input));
    }

    var difference = current.getFullYear() - input.getFullYear();
    var mid = current.getFullYear() - difference / 2 | 0;
    var q1 = mid - difference / 4 | 0;
    var q3 = mid + difference / 4 | 0;

    dates[1].setYear(q1);
    dates[2].setYear(mid);
    dates[3].setYear(q3);
    dates[4].setYear(current.getFullYear());

    return dates;
  }

})();
