(function() {
    'use strict';

    angular
        .module('strumApp')
        .controller('StrumDetailController', StrumDetailController);

    StrumDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Strum', 'User'];

    function StrumDetailController($scope, $rootScope, $stateParams, previousState, entity, Strum, User) {
        var vm = this;

        vm.strum = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('strumApp:strumUpdate', function(event, result) {
            vm.strum = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
