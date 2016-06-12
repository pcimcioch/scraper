var frontendApp = angular.module('moduleManagerViewApp');

frontendApp.service('statusSvc', ['$http', '$rootScope', 'notificationSvc', function($http, $rootScope, notificationSvc) {
    "use strict";
    
    var self = this;

    var connected = false;
    var stompClient = null;

    self.statuses = {};

    self.getModuleStatuses = function() {
        return $http.get('./api/module/status');
    };

    self.refreshModuleStatuses = function() {
        return notificationSvc.wrap(self.getModuleStatuses(), function(response) {
            self.statuses = {};
            var statuses = response.data;
            for (var i = 0; i < statuses.length; ++i) {
                updateStatus(statuses[i]);
            }
        }, 'Error refeshing module states');
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