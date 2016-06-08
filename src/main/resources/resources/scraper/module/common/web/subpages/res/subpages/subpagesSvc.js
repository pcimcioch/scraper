var frontendApp = angular.module('subpagesApp');

frontendApp.service('subpagesSvc', ['$http', function($http) {
    var self = this;

    self.getSubpages = function(successCallback, errorCallback) {
        return $http.get('./api/subpage').then(successCallback, errorCallback);
    };
}]);