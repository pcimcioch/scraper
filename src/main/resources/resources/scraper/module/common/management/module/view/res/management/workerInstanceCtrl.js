var frontendApp = angular.module('moduleManagerViewApp');

frontendApp.controller('workerInstanceCtrl', ['$scope', function($scope) {
    "use strict";

    $scope.form = {$valid: false};
    $scope.isSchedule = false;
    $scope.schedule = '';
    $scope.settings = {};
}]);