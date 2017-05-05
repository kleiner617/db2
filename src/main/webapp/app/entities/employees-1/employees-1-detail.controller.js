(function() {
    'use strict';

    angular
        .module('db2App')
        .controller('Employees1DetailController', Employees1DetailController);

    Employees1DetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Employees1'];

    function Employees1DetailController($scope, $rootScope, $stateParams, previousState, entity, Employees1) {
        var vm = this;

        vm.employees1 = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('db2App:employees1Update', function(event, result) {
            vm.employees1 = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
