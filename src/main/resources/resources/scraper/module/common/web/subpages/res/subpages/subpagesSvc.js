var frontendApp = angular.module('subpagesApp');

frontendApp.service('subpagesSvc', ['$http', function($http) {
    "use strict";
    
    var self = this;

    self.getSubpages = function() {
        return $http.get('./api/subpage');
    };
}]);