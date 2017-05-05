(function() {
    'use strict';

    angular
        .module('db2App')
        .controller('PatientsDialogController', PatientsDialogController);

    PatientsDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Patients'];

    function PatientsDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Patients) {
        var vm = this;

        vm.patients = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.patients.id !== null) {
                Patients.update(vm.patients, onSaveSuccess, onSaveError);
            } else {
                Patients.save(vm.patients, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('db2App:patientsUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.birthdate = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
