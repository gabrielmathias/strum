(function() {
    'use strict';

    angular
        .module('strumApp')
        .controller('KanBanController', KanBanController);

    KanBanController.$inject = ['$scope', 'Principal', 'Strum' , 'Sprint','Story','Task', 'TaskSearch', '$state'];

    function KanBanController ($scope, Principal, StrumService , SprintService, StoryService, TaskService, TaskSearch, $state) {
        var vm = this;

        vm.columns = ['Todo','Doing','Done'];
        vm.tasks = [];
        vm.sprint = {};
        vm.strum =  {};

        loadAllTasks();

        function loadAllTasks () {

            TaskService.query({
                page: 0,
                size: 1000
            }, onSuccess, onError);

            function onSuccess(data, headers) {
//                vm.links = ParseLinks.parse(headers('link'));
//                vm.totalItems = headers('X-Total-Count');
//                vm.queryCount = vm.totalItems;
                vm.tasks = data;
//                vm.page = pagingParams.page;
            }
            function onError(error) {
                AlertService.error(error.data.message);
            }
        }


    }
})();
