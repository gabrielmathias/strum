(function() {
    'use strict';

    angular
        .module('strumApp')
        .controller('StrumDeleteController',StrumDeleteController);

    StrumDeleteController.$inject = ['$uibModalInstance', 'entity', 'Strum'];

    function StrumDeleteController($uibModalInstance, entity, Strum) {
        var vm = this;

        vm.strum = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Strum.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
