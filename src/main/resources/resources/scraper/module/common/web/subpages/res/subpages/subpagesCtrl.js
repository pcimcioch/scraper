var frontendApp = angular.module('subpagesApp');

frontendApp.controller('subpagesCtrl', ['$scope', 'subpagesSvc', 'notificationSvc', function($scope, subpagesSvc, notificationSvc) {
    "use strict";
    
    $scope.subpages = [];

    $scope.refreshSubpages = function() {
        return notificationSvc.wrap(subpagesSvc.getSubpages(), function(response) {
            $scope.subpages = response.data;
        }, 'Error refeshing sub pages list');
    };

    var init = function() {
        $scope.refreshSubpages();
    };

    init();
}]);