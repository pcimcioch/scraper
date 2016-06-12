var frontendApp = angular.module('loggerViewApp');

frontendApp.service('logsSvc', ['$http', function($http) {
    "use strict";
    
    var self = this;

    self.getLogs = function() {
        return $http.get('./api/log');
    };

    self.removeModuleLogs = function(moduleName) {
        return $http.delete('./api/log/' + moduleName);
    };

    self.removeAllLogs = function() {
        return $http.delete('./api/log');
    };
}]);