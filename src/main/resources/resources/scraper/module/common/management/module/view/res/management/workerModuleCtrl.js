var frontendApp = angular.module('moduleManagerViewApp');

frontendApp.controller('workerModuleCtrl', ['$scope', 'managementSvc', 'notificationSvc', function($scope, managementSvc, notificationSvc) {
    $scope.settings = {};
    $scope.valid = false;
    $scope.instance = "";

    $scope.runWorkerModule = function(moduleName, settings) {
        notificationSvc.actionsCount++;
        managementSvc.runWorkerModule(moduleName, $scope.instance, settings, function(response) {
        }, function() {
            notificationSvc.error('Error running module');
        }).finally(function() {
            notificationSvc.actionsCount--;
        });
    };
}]);