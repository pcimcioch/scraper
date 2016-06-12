var frontendApp = angular.module('moduleManagerViewApp');

frontendApp.controller('workerModuleCtrl', ['$scope', function($scope) {
    "use strict";
    
    $scope.settings = {};
    $scope.valid = false;
    $scope.instance = "";
}]);