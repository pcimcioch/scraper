var commonApp = angular.module('commonApp');

commonApp.service('notificationSvc', [function() {
    var self = this;

    this.ALERT_MAP = {
        'error': 'alert-danger',
        'warning': 'alert-warning',
        'info': 'alert-info',
        'success': 'alert-success'
    };

    this.notifications = [];
    this.actionsCount = 0;

    this.error = function(message) {
        self.notifications.unshift({
            type: 'error',
            msg: message
        });
    };

    this.warning = function(message) {
        self.notifications.unshift({
            type: 'warning',
            msg: message
        });
    };

    this.info = function(message) {
        self.notifications.unshift({
            type: 'info',
            msg: message
        });
    };

    this.success = function(message) {
        self.notifications.unshift({
            type: 'success',
            msg: message
        });
    };

    this.removeLast = function() {
        self.notifications.shift();
    };

    this.removeByIndex = function(index) {
        self.notifications.splice(index, 1);
    };
}]);