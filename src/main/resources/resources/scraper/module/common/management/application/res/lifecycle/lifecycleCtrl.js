var frontendApp = angular.module('applicationLifecycleApp');

frontendApp.controller('lifecycleCtrl', ['$scope', 'lifecycleSvc', 'notificationSvc', function($scope, lifecycleSvc, notificationSvc) {
    $scope.stopApplication = function() {
        notificationSvc.actionsCount++;
        lifecycleSvc.stopApplication(function(response) {
            notificationSvc.success('Application stopped');
        }, function() {
            notificationSvc.error('Error stopping application');
        }).finally(function() {
            notificationSvc.actionsCount--;
        });
    };
}]);