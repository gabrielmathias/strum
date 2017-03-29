(function() {
    'use strict';

    angular
        .module('strumApp')
        .controller('StrumDialogController', StrumDialogController);

    StrumDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Strum', 'User'];

    function StrumDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Strum, User) {
        var vm = this;

        vm.strum = entity;
        vm.clear = clear;
        vm.save = save;
        vm.users = User.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.strum.id !== null) {
                Strum.update(vm.strum, onSaveSuccess, onSaveError);
            } else {
                Strum.save(vm.strum, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('strumApp:strumUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
