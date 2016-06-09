var frontendApp = angular.module('moduleManagerViewApp');

frontendApp.controller('workerModuleCtrl', ['$scope', function($scope) {
    $scope.settings = {};
    $scope.valid = false;
    $scope.instance = "";
}]);