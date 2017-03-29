(function() {
    'use strict';

    angular
        .module('strumApp')
        .controller('SprintDialogController', SprintDialogController);

    SprintDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Sprint', 'Strum', 'User'];

    function SprintDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Sprint, Strum, User) {
        var vm = this;

        vm.sprint = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.strums = Strum.query();
        vm.users = User.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.sprint.id !== null) {
                Sprint.update(vm.sprint, onSaveSuccess, onSaveError);
            } else {
                Sprint.save(vm.sprint, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('strumApp:sprintUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.start_at = false;
        vm.datePickerOpenStatus.end_at = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
