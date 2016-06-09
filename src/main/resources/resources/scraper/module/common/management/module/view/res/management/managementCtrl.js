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
    $scope.moduleInstances = {};

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

    $scope.refreshModuleInstances = function() {
        notificationSvc.actionsCount++;
        return managementSvc.getModuleInstances(function(response) {
            setModuleInstances(response.data);
        }, function() {
            notificationSvc.error('Error refeshing module instances list');
        }).finally(function() {
            notificationSvc.actionsCount--;
        });
    };

    var setModuleInstances = function(instances) {
        $scope.moduleInstances = {};
        for(var i = 0; i< instances.length; ++i) {
            if($scope.moduleInstances[instances[i].module]) {
                $scope.moduleInstances[instances[i].module].push(instances[i]);
            } else {
                $scope.moduleInstances[instances[i].module] = [instances[i]];
            }
        }
    };

    $scope.stopWorkerModule = function(workerId) {
        notificationSvc.actionsCount++;
        managementSvc.stopWorkerModule(workerId, function(response) {
        }, function() {
            notificationSvc.error('Error stopping worker module');
        }).finally(function() {
            notificationSvc.actionsCount--;
        });
    };

    $scope.runModuleInstance = function(id) {
        notificationSvc.actionsCount++;
        managementSvc.runModuleInstance(id, function(response) {
        }, function() {
            notificationSvc.error('Error running module instance');
        }).finally(function() {
            notificationSvc.actionsCount--;
        });
    };

    $scope.removeModuleInstance = function(id) {
        notificationSvc.actionsCount++;
        managementSvc.removeModuleInstance(id, function() {
            $scope.refreshModuleInstances();
        }, function() {
            notificationSvc.error('Error removing module instance');
        }).finally(function() {
            notificationSvc.actionsCount--;
        });
    };

    $scope.addModuleInstance = function(moduleName, instance, settings) {
        notificationSvc.actionsCount++;
        managementSvc.addModuleInstance(moduleName, instance, settings, function() {
            $scope.refreshModuleInstances();
        }, function() {
            notificationSvc.error('Error adding module instance');
        }).finally(function() {
            notificationSvc.actionsCount--;
        });
    };

    var init = function() {
        $scope.refreshModules();
        $scope.refreshModuleInstances();
    };

    init();
}]);