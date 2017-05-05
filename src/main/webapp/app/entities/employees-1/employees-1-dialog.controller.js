(function() {
    'use strict';

    angular
        .module('db2App')
        .controller('Employees1DialogController', Employees1DialogController);

    Employees1DialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Employees1'];

    function Employees1DialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Employees1) {
        var vm = this;

        vm.employees1 = entity;
        vm.clear = clear;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.employees1.id !== null) {
                Employees1.update(vm.employees1, onSaveSuccess, onSaveError);
            } else {
                Employees1.save(vm.employees1, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('db2App:employees1Update', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
