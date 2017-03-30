(function() {
    'use strict';

    angular
        .module('strumApp')
        .controller('KanBanController', KanBanController);

    KanBanController.$inject = ['$scope', 'Principal', 'Strum' , 'Sprint','Story','Task', 'TaskSearch', '$state'];

    function KanBanController ($scope, Principal, StrumService , SprintService, StoryService, TaskService, TaskSearch, $state) {
        var vm = this;

        vm.columns = {};
        vm.tasks = [];
        vm.sprint = {};
        vm.strum =  {};

        loadAllTasks();


        function drop(data,event) {
            console.log("drop do controller");
        }

        $scope.onDragComplete=function(data,evt){
               console.log("drag success, data:", data);
            }

            $scope.onDropComplete=function(data,evt){
                console.log("drop success, data:", data);
            }

        function loadAllTasks () {

            TaskService.query({
                page: 0,
                size: 1000
            }, onSuccess, onError);



            function onSuccess(data, headers) {
                vm.columns.todo = {};
                vm.tasks["todo"] = data;
                vm.columns.todo.name = "todo";
                vm.columns.done = {};
                vm.tasks["done"] = [];
                vm.columns.done.name = "done";
            }
            function onError(error) {
                AlertService.error(error.data.message);
            }
        }


    }
})();
