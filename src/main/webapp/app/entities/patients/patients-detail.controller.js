(function() {
    'use strict';

    angular
        .module('db2App')
        .controller('PatientsDetailController', PatientsDetailController);

    PatientsDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Patients'];

    function PatientsDetailController($scope, $rootScope, $stateParams, previousState, entity, Patients) {
        var vm = this;

        vm.patients = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('db2App:patientsUpdate', function(event, result) {
            vm.patients = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
