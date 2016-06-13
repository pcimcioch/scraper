var frontendApp = angular.module('applicationLifecycleApp');

frontendApp.controller('lifecycleCtrl', ['$scope', 'lifecycleSvc', 'notificationSvc', function($scope, lifecycleSvc, notificationSvc) {
    "use strict";
    
    $scope.stopApplication = function() {
        notificationSvc.wrap(lifecycleSvc.stopApplication(), 'Application stopped', 'Error stopping application');
    };
}]);