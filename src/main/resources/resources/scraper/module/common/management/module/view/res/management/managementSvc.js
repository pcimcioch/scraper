var frontendApp = angular.module('moduleManagerViewApp');

frontendApp.service('managementSvc', ['$http', function($http) {
    "use strict";

    var self = this;

    self.getModules = function() {
        return $http.get('./api/module');
    };

    self.getModuleInstances = function() {
        return $http.get('./api/module/instance');
    };

    self.runModuleInstance = function(id) {
        return $http.get('./api/module/instance/' + id + '/run');
    };

    self.removeModuleInstance = function(id) {
        return $http.delete('./api/module/instance/' + id);
    };

    self.addModuleInstance = function(moduleName, instanceName, settings, schedule) {
        return $http.post('./api/module/instance/', {
            moduleName: moduleName,
            instanceName: instanceName,
            settings: settings,
            schedule: schedule
        }, {headers: {'Content-Type': 'application/json'}});
    };

    self.updateModuleInstanceSettings = function(id, settings) {
        return $http.put('./api/module/instance/' + id + '/settings', settings, {headers: {'Content-Type': 'application/json'}});
    };

    self.updateModuleInstanceSchedule = function(id, schedule) {
        return $http.put('./api/module/instance/' + id + '/schedule', schedule, {headers: {'Content-Type': 'text/plain'}});
    };

    self.stopWorkerModule = function(workerId) {
        return $http.get('./api/module/' + workerId + '/stop');
    };
}]);