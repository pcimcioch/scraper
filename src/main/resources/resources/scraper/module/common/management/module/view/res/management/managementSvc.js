var frontendApp = angular.module('moduleManagerViewApp');

frontendApp.service('managementSvc', ['$http', function($http) {
    var self = this;

    self.getModules = function(successCallback, errorCallback) {
        return $http.get('./api/module').then(successCallback, errorCallback);
    };

    self.runWorkerModule = function(moduleName, instance, settings, successCallback, errorCallback) {
        return $http.post('./api/module/' + moduleName + '/' + instance + '/run', settings, {headers: {'Content-Type': 'application/json'}}).then(successCallback, errorCallback);
    };

    self.stopWorkerModule = function(workerId, successCallback, errorCallback) {
        return $http.get('./api/module/' + workerId + '/stop').then(successCallback, errorCallback);
    };
}]);