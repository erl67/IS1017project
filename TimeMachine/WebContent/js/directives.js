/* global angular: false */
(function() {
  var app = angular.module('weatherTM');

  app.directive('inputPanel', function() {
    return {
      restrict: 'E',
      controller: 'InputController as ic',
      templateUrl: 'directives/input.html'
    };
  });

  app.directive('navpills', function() {
    return {
      restrict: 'E',
      controller: 'PanelController as panelCtrl',
      templateUrl: 'directives/navpills.html'
    };
  });

  app.directive('historicalEvents', function() {
    return {
      restrict: 'E',
      controller: 'HistoryController as hc',
      templateUrl: 'directives/historicalEvents.html'
    };
  });

  app.directive('graph', function() {
    return {
      restrict: 'E',
      controller: 'ChartController',
      templateUrl: 'directives/graph.html'
    };
  });
})();
