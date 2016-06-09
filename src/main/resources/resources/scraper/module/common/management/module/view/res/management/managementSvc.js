var frontendApp = angular.module('moduleManagerViewApp');

frontendApp.service('managementSvc', ['$http', function($http) {
    var self = this;

    self.getModules = function(successCallback, errorCallback) {
        return $http.get('./api/module').then(successCallback, errorCallback);
    };

    self.getModuleInstances = function(successCallback, errorCallback) {
        return $http.get('./api/module/instance').then(successCallback, errorCallback);
    };

    self.runModuleInstance = function(id, successCallback, errorCallback) {
        return $http.get('./api/module/instance/' + id + '/run').then(successCallback, errorCallback);
    };

    self.removeModuleInstance = function(id, successCallback, errorCallback) {
        return $http.delete('./api/module/instance/' + id).then(successCallback, errorCallback);
    };

    self.addModuleInstance = function(moduleName, instance, settings, successCallback, errorCallback) {
        return $http.post('./api/module/' + moduleName + '/' + instance + '/', settings, {headers: {'Content-Type': 'application/json'}}).then(successCallback, errorCallback);
    };

    self.stopWorkerModule = function(workerId, successCallback, errorCallback) {
        return $http.get('./api/module/' + workerId + '/stop').then(successCallback, errorCallback);
    };
}]);