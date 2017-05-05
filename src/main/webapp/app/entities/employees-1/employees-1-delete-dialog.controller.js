(function() {
    'use strict';

    angular
        .module('db2App')
        .controller('Employees1DeleteController',Employees1DeleteController);

    Employees1DeleteController.$inject = ['$uibModalInstance', 'entity', 'Employees1'];

    function Employees1DeleteController($uibModalInstance, entity, Employees1) {
        var vm = this;

        vm.employees1 = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Employees1.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
