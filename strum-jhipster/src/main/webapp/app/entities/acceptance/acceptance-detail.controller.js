(function() {
    'use strict';

    angular
        .module('strumApp')
        .controller('AcceptanceDetailController', AcceptanceDetailController);

    AcceptanceDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Acceptance', 'User', 'Story'];

    function AcceptanceDetailController($scope, $rootScope, $stateParams, previousState, entity, Acceptance, User, Story) {
        var vm = this;

        vm.acceptance = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('strumApp:acceptanceUpdate', function(event, result) {
            vm.acceptance = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
