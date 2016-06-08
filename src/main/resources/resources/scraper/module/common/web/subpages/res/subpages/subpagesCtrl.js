var frontendApp = angular.module('subpagesApp');

frontendApp.controller('subpagesCtrl', ['$scope', 'subpagesSvc', 'notificationSvc', function($scope, subpagesSvc, notificationSvc) {
    $scope.subpages = [];

    $scope.refreshSubpages = function() {
        notificationSvc.actionsCount++;
        return subpagesSvc.getSubpages(function(response) {
            $scope.subpages = response.data;
        }, function() {
            notificationSvc.error('Error refeshing sub pages list');
        }).finally(function() {
            notificationSvc.actionsCount--;
        });
    };

    var init = function() {
        $scope.refreshSubpages();
    };

    init();
}]);