var commonApp = angular.module('commonApp');

commonApp.service('notificationSvc', [function() {
    "use strict";
    
    var self = this;

    self.ALERT_MAP = {
        'error': 'alert-danger',
        'warning': 'alert-warning',
        'info': 'alert-info',
        'success': 'alert-success'
    };

    self.notifications = [];
    self.actionsCount = 0;

    self.error = function(message) {
        self.notifications.unshift({
            type: 'error',
            msg: message
        });
    };

    self.warning = function(message) {
        self.notifications.unshift({
            type: 'warning',
            msg: message
        });
    };

    self.info = function(message) {
        self.notifications.unshift({
            type: 'info',
            msg: message
        });
    };

    self.success = function(message) {
        self.notifications.unshift({
            type: 'success',
            msg: message
        });
    };

    self.wrap = function(promise, successAction, errorAction, finallyAction) {
        var callbackError = function(response) {
            if(errorAction) {
                if (typeof errorAction === "function") {
                    errorAction(response);
                } else {
                    if(response.data && response.data.message) {
                        self.error(errorAction + ': ' + response.data.message);
                    } else {
                        self.error(errorAction);
                    }
                }
            }
        };

        var callbackSuccess = function(response) {
            if(successAction) {
                if (typeof successAction === "function") {
                    successAction(response);
                } else {
                    self.success(successAction);
                }
            }
        };
        
        var callbackFinally = function() {
            self.actionsCount--;
            if(finallyAction) {
                finallyAction();
            }
        };

        self.actionsCount++;
        return promise.then(callbackSuccess, callbackError).finally(callbackFinally);
    };

    self.removeLast = function() {
        self.notifications.shift();
    };

    self.removeByIndex = function(index) {
        self.notifications.splice(index, 1);
    };
}]);