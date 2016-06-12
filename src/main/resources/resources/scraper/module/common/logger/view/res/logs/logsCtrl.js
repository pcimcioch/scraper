var frontendApp = angular.module('loggerViewApp');

frontendApp.controller('logsCtrl', ['$scope', 'logsSvc', 'notificationSvc', function($scope, logsSvc, notificationSvc) {
    "use strict";
    
    $scope.GLYPHICON_MAP = {
        'ERROR': 'glyphicon-exclamation-sign',
        'WARNING': 'glyphicon-warning-sign',
        'INFO': 'glyphicon-info-sign',
        'TRACE': 'glyphicon-menu-right'
    };
    $scope.ALERT_MAP = {
        'ERROR': 'alert-danger',
        'WARNING': 'alert-warning',
        'INFO': 'alert-info',
        'TRACE': 'alert-success'
    };

    $scope.logs = {};

    $scope.refreshLogs = function() {
        return notificationSvc.wrap(logsSvc.getLogs(), function(response) {
            setLogs(response.data);
        }, 'Error refeshing logs list');
    };

    var setLogs = function(logs) {
        $scope.logs = {};
        for (var i = 0; i < logs.length; ++i) {
            var log = logs[i];
            if ($scope.logs[log.module]) {
                $scope.logs[log.module].push(log);
            } else {
                $scope.logs[log.module] = [log];
            }
        }
    };

    $scope.removeModuleLogs = function(moduleName) {
        notificationSvc.wrap(logsSvc.removeModuleLogs(moduleName), null, 'Error removing logs for module: ' + moduleName, function() {
            $scope.refreshLogs();
        });
    };

    $scope.removeAllLogs = function() {
        notificationSvc.wrap(logsSvc.removeAllLogs(), null, 'Error removing all logs', function() {
            $scope.refreshLogs();
        });
    };

    var init = function() {
        $scope.refreshLogs();
    };

    init();
}]);