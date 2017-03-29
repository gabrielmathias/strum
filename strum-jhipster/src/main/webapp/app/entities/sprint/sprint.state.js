(function() {
    'use strict';

    angular
        .module('strumApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('sprint', {
            parent: 'entity',
            url: '/sprint?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'strumApp.sprint.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/sprint/sprints.html',
                    controller: 'SprintController',
                    controllerAs: 'vm'
                }
            },
            params: {
                page: {
                    value: '1',
                    squash: true
                },
                sort: {
                    value: 'id,asc',
                    squash: true
                },
                search: null
            },
            resolve: {
                pagingParams: ['$stateParams', 'PaginationUtil', function ($stateParams, PaginationUtil) {
                    return {
                        page: PaginationUtil.parsePage($stateParams.page),
                        sort: $stateParams.sort,
                        predicate: PaginationUtil.parsePredicate($stateParams.sort),
                        ascending: PaginationUtil.parseAscending($stateParams.sort),
                        search: $stateParams.search
                    };
                }],
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('sprint');
                    $translatePartialLoader.addPart('sprintStatus');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('sprint-detail', {
            parent: 'sprint',
            url: '/sprint/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'strumApp.sprint.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/sprint/sprint-detail.html',
                    controller: 'SprintDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('sprint');
                    $translatePartialLoader.addPart('sprintStatus');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Sprint', function($stateParams, Sprint) {
                    return Sprint.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'sprint',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('sprint-detail.edit', {
            parent: 'sprint-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/sprint/sprint-dialog.html',
                    controller: 'SprintDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Sprint', function(Sprint) {
                            return Sprint.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('sprint.new', {
            parent: 'sprint',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/sprint/sprint-dialog.html',
                    controller: 'SprintDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                description: null,
                                start_at: null,
                                end_at: null,
                                days_planned: null,
                                days_used: null,
                                status: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('sprint', null, { reload: 'sprint' });
                }, function() {
                    $state.go('sprint');
                });
            }]
        })
        .state('sprint.edit', {
            parent: 'sprint',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/sprint/sprint-dialog.html',
                    controller: 'SprintDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Sprint', function(Sprint) {
                            return Sprint.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('sprint', null, { reload: 'sprint' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('sprint.delete', {
            parent: 'sprint',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/sprint/sprint-delete-dialog.html',
                    controller: 'SprintDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Sprint', function(Sprint) {
                            return Sprint.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('sprint', null, { reload: 'sprint' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
