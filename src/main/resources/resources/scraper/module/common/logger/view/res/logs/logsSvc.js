var frontendApp = angular.module('loggerViewApp');

frontendApp.service('logsSvc', ['$http', function($http) {
    var self = this;

    self.getLogs = function(successCallback, errorCallback) {
        return $http.get('./api/log').then(successCallback, errorCallback);
    };

    self.removeModuleLogs = function(moduleName, successCallback, errorCallback) {
        return $http.delete('./api/log/' + moduleName).then(successCallback, errorCallback);
    };

    self.removeAllLogs = function(successCallback, errorCallback) {
        return $http.delete('./api/log').then(successCallback, errorCallback);
    };
}]);