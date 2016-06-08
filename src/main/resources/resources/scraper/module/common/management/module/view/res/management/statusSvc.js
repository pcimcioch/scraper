var frontendApp = angular.module('moduleManagerViewApp');

frontendApp.service('statusSvc', ['$http', '$rootScope', 'notificationSvc', function($http, $rootScope, notificationSvc) {
    var self = this;

    var connected = false;
    var stompClient = null;

    self.statuses = {};

    self.getModuleStatuses = function(successCallback, errorCallback) {
        return $http.get('./api/module/status').then(successCallback, errorCallback);
    };

    self.refreshModuleStatuses = function() {
        notificationSvc.actionsCount++;
        return self.getModuleStatuses(function(response) {
            self.statuses = {};
            var statuses = response.data;
            for (var i = 0; i < statuses.length; ++i) {
                updateStatus(statuses[i]);
            }
        }, function() {
            notificationSvc.error('Error refeshing module states');
        }).finally(function() {
            notificationSvc.actionsCount--;
        });
    };
    
    var connect = function() {
        var socket = new SockJS('/module.view/endpoint');
        stompClient = Stomp.over(socket);
        stompClient.connect({}, function() {
            connected = true;
            stompClient.subscribe('module.runner/topic/status', function(statusMsg) {
                $rootScope.$apply(function() {
                    updateStatus(JSON.parse(statusMsg.body));
                });
            });
        });
    };

    var updateStatus = function(status) {
        if (status.executionFlow.running) {
            if (!self.statuses[status.moduleDetails.module]) {
                self.statuses[status.moduleDetails.module] = {};
            }
            self.statuses[status.moduleDetails.module][status.id] = status;
        } else if (self.statuses[status.moduleDetails.module] && self.statuses[status.moduleDetails.module][status.id]) {
            delete self.statuses[status.moduleDetails.module][status.id];
        }
    };

    var init = function() {
        self.refreshModuleStatuses().then(connect);
    };

    init();
}]);