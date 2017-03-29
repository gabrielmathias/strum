(function() {
    'use strict';

    angular
        .module('strumApp')
        .controller('AcceptanceDialogController', AcceptanceDialogController);

    AcceptanceDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Acceptance', 'User', 'Story'];

    function AcceptanceDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Acceptance, User, Story) {
        var vm = this;

        vm.acceptance = entity;
        vm.clear = clear;
        vm.save = save;
        vm.users = User.query();
        vm.stories = Story.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.acceptance.id !== null) {
                Acceptance.update(vm.acceptance, onSaveSuccess, onSaveError);
            } else {
                Acceptance.save(vm.acceptance, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('strumApp:acceptanceUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
