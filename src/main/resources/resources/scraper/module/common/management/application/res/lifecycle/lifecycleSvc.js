var frontendApp = angular.module('applicationLifecycleApp');

frontendApp.service('lifecycleSvc', ['$http', function($http) {
    var self = this;

    self.stopApplication = function(successCallback, errorCallback) {
        return $http.get('./api/lifecycle/stop').then(successCallback, errorCallback);
    };
}]);