(function() {
    'use strict';

    angular
        .module('strumApp')
        .controller('StoryDialogController', StoryDialogController);

    StoryDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Story', 'User', 'Sprint'];

    function StoryDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Story, User, Sprint) {
        var vm = this;

        vm.story = entity;
        vm.clear = clear;
        vm.save = save;
        vm.users = User.query();
        vm.sprints = Sprint.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.story.id !== null) {
                Story.update(vm.story, onSaveSuccess, onSaveError);
            } else {
                Story.save(vm.story, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('strumApp:storyUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
