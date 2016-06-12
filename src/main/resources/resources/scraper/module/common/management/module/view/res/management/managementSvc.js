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

    self.addModuleInstance = function(moduleName, instance, settings) {
        return $http.post('./api/module/' + moduleName + '/' + instance + '/', settings, {headers: {'Content-Type': 'application/json'}});
    };

    self.stopWorkerModule = function(workerId) {
        return $http.get('./api/module/' + workerId + '/stop');
    };
}]);