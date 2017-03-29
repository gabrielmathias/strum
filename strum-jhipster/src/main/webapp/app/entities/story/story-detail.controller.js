(function() {
    'use strict';

    angular
        .module('strumApp')
        .controller('StoryDetailController', StoryDetailController);

    StoryDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Story', 'User', 'Sprint'];

    function StoryDetailController($scope, $rootScope, $stateParams, previousState, entity, Story, User, Sprint) {
        var vm = this;

        vm.story = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('strumApp:storyUpdate', function(event, result) {
            vm.story = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
