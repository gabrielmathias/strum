(function() {
    'use strict';

    angular
        .module('strumApp')
        .controller('AcceptanceDeleteController',AcceptanceDeleteController);

    AcceptanceDeleteController.$inject = ['$uibModalInstance', 'entity', 'Acceptance'];

    function AcceptanceDeleteController($uibModalInstance, entity, Acceptance) {
        var vm = this;

        vm.acceptance = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Acceptance.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
