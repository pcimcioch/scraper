var frontendApp = angular.module('loggerViewApp');

frontendApp.controller('logsCtrl', ['$scope', 'logsSvc', 'notificationSvc', function($scope, logsSvc, notificationSvc) {
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
        notificationSvc.actionsCount++;
        return logsSvc.getLogs(function(response) {
            setLogs(response.data);
        }, function() {
            notificationSvc.error('Error refeshing logs list');
        }).finally(function() {
            notificationSvc.actionsCount--;
        });
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
        notificationSvc.actionsCount++;
        return logsSvc.removeModuleLogs(moduleName, function(response) {
        }, function() {
            notificationSvc.error('Error removing logs for module: ' + moduleName);
        }).finally(function() {
            $scope.refreshLogs();
            notificationSvc.actionsCount--;
        });
    };

    $scope.removeAllLogs = function() {
        notificationSvc.actionsCount++;
        return logsSvc.removeAllLogs(function(response) {
        }, function() {
            notificationSvc.error('Error removing all logs');
        }).finally(function() {
            $scope.refreshLogs();
            notificationSvc.actionsCount--;
        });
    };

    var init = function() {
        $scope.refreshLogs();
    };

    init();
}]);