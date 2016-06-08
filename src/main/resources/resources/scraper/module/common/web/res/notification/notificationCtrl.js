var commonApp = angular.module('commonApp');

commonApp.controller('notificationCtrl', ['$scope', 'notificationSvc', function($scope, notificationSvc) {
    $scope.notificationSvc = notificationSvc;
}]);