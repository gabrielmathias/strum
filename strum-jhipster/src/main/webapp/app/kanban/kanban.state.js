(function() {
    'use strict';

    angular
        .module('strumApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider.state('kanban', {
            parent: 'home',
            url: '/kanban',
            data: {
                authorities: []
            },
            views: {
                'content@': {
                    templateUrl: 'app/kanban/kanban.html',
                    controller: 'KanBanController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate,$translatePartialLoader) {
                    $translatePartialLoader.addPart('kanban');
                    return $translate.refresh();
                }]
            }
        });
    }
})();
