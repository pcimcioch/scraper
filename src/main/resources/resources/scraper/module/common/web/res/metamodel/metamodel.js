var frontendApp = angular.module('commonApp');

frontendApp.directive('dynamicForm', function() {
    return {
        restrict: 'E',
        scope: {
            metamodel: '=',
            settings: '=',
            valid: '='
        },
        templateUrl: '/common.web/res/metamodel/metamodel.html',
        link: function(scope) {
            scope.settings = scope.metamodel.defaultObject ? scope.metamodel.defaultObject : {};
            scope.$watch("metamodelForm.$valid", function(newVal, oldVal) {
                scope.valid = newVal;
            });
        }
    };
});