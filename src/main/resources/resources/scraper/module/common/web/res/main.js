var frontendApp = angular.module('commonApp', ['ui.bootstrap']);

frontendApp.directive('spinner', function($timeout) {
    "use strict";
    
    return {
        restrict: 'E',
        template: '<span class="glyphicon glyphicon-hourglass spinning"></span>',
        scope: {
            show: '=',
            delay: '@'
        },
        link: function(scope, elem, attrs) {
            var showTimer;

            scope.$watch('show', function(newVal) {
                if (newVal) {
                    showSpinner();
                } else {
                    hideSpinner();
                }
            });

            function showSpinner() {
                if (showTimer) {
                    return;
                }

                showTimer = $timeout(showElement.bind(this, true), getDelay());
            }

            function hideSpinner() {
                if (showTimer) {
                    $timeout.cancel(showTimer);
                }

                showTimer = null;
                showElement(false);
            }

            function showElement(show) {
                if (show) {
                    elem.css({display: ''});
                } else {
                    elem.css({display: 'none'});
                }
            }

            function getDelay() {
                var delay = parseInt(scope.delay);
                return delay && angular.isNumber(delay) ? delay : 500;
            }
        }
    };
});