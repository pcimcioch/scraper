var frontendApp = angular.module('moduleManagerViewApp');

frontendApp.controller('managementCtrl', ['$scope', 'managementSvc', 'notificationSvc', 'statusSvc', function($scope, managementSvc, notificationSvc, statusSvc) {
    $scope.GLYPHICON_MAP = {
        'WORKER': 'glyphicon-cog',
        'SERVICE': 'glyphicon-wrench',
        'STANDALONE': 'glyphicon-tasks'
    };

    $scope.ROW_CLASS_MAP = {
        'WORKER': 'module-worker',
        'SERVICE': 'module-service',
        'STANDALONE': 'module-standalone'
    };

    $scope.statusSvc = statusSvc;

    $scope.modules = [];

    $scope.refreshModules = function() {
        notificationSvc.actionsCount++;
        return managementSvc.getModules(function(response) {
            $scope.modules = response.data;
        }, function() {
            notificationSvc.error('Error refeshing modules list');
        }).finally(function() {
            notificationSvc.actionsCount--;
        });
    };

    $scope.stopWorkerModule = function(workerId) {
        notificationSvc.actionsCount++;
        managementSvc.stopWorkerModule(workerId, function(response) {
        }, function() {
            notificationSvc.error('Error stopping module');
        }).finally(function() {
            notificationSvc.actionsCount--;
        });
    };

    var init = function() {
        $scope.refreshModules();
    };

    init();
}]);