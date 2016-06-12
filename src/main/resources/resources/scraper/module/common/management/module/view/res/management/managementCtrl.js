var frontendApp = angular.module('moduleManagerViewApp');

frontendApp.controller('managementCtrl', ['$scope', 'managementSvc', 'notificationSvc', 'statusSvc', function($scope, managementSvc, notificationSvc, statusSvc) {
    "use strict";
    
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
        return notificationSvc.wrap(managementSvc.getModules(), function(response) {
            $scope.modules = response.data;
        }, 'Error refeshing modules list');
    };

    $scope.refreshModuleInstances = function() {
        return notificationSvc.wrap(managementSvc.getModuleInstances(), function(response) {
            setModuleInstances(response.data);
        }, 'Error refeshing module instances list');
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
        notificationSvc.wrap(managementSvc.stopWorkerModule(workerId), null, 'Error stopping worker module');
    };

    $scope.runModuleInstance = function(id) {
        notificationSvc.wrap(managementSvc.runModuleInstance(id), null, 'Error running module instance');
    };

    $scope.removeModuleInstance = function(id) {
        notificationSvc.wrap(managementSvc.removeModuleInstance(id), null, 'Error removing module instance', function() {
            $scope.refreshModuleInstances();
        });
    };

    $scope.addModuleInstance = function(moduleName, instance, settings) {
        notificationSvc.wrap(managementSvc.addModuleInstance(moduleName, instance, settings), null, 'Error adding module instance', function() {
            $scope.refreshModuleInstances();
        });
    };

    var init = function() {
        $scope.refreshModules();
        $scope.refreshModuleInstances();
    };

    init();
}]);