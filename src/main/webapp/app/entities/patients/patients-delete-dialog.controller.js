(function() {
    'use strict';

    angular
        .module('db2App')
        .controller('PatientsDeleteController',PatientsDeleteController);

    PatientsDeleteController.$inject = ['$uibModalInstance', 'entity', 'Patients'];

    function PatientsDeleteController($uibModalInstance, entity, Patients) {
        var vm = this;

        vm.patients = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Patients.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
