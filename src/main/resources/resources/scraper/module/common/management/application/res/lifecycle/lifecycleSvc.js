var frontendApp = angular.module('applicationLifecycleApp');

frontendApp.service('lifecycleSvc', ['$http', function($http) {
    "use strict";
    
    var self = this;

    self.stopApplication = function() {
        return $http.get('./api/lifecycle/stop');
    };
}]);