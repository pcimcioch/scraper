var commonApp = angular.module('commonApp');

commonApp.controller('notificationCtrl', ['$scope', 'notificationSvc', function($scope, notificationSvc) {
    "use strict";
    
    $scope.notificationSvc = notificationSvc;
}]);