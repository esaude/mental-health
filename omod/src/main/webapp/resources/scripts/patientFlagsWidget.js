angular.module('flags.fragment.dashboardWidget').controller('DashboardWidgetCtrl',
        [ '$scope', '$window', function($scope, $window) {
            $scope.patient = {
                patient : $window.config.patient.uuid
            // http://stackoverflow.com/a/202247/321797
            };
} ]);