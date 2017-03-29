(function() {
    'use strict';

    angular
        .module('strumApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('strum', {
            parent: 'entity',
            url: '/strum?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'strumApp.strum.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/strum/strums.html',
                    controller: 'StrumController',
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
                    $translatePartialLoader.addPart('strum');
                    $translatePartialLoader.addPart('strumStatus');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('strum-detail', {
            parent: 'strum',
            url: '/strum/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'strumApp.strum.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/strum/strum-detail.html',
                    controller: 'StrumDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('strum');
                    $translatePartialLoader.addPart('strumStatus');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Strum', function($stateParams, Strum) {
                    return Strum.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'strum',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('strum-detail.edit', {
            parent: 'strum-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/strum/strum-dialog.html',
                    controller: 'StrumDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Strum', function(Strum) {
                            return Strum.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('strum.new', {
            parent: 'strum',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/strum/strum-dialog.html',
                    controller: 'StrumDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                description: null,
                                status: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('strum', null, { reload: 'strum' });
                }, function() {
                    $state.go('strum');
                });
            }]
        })
        .state('strum.edit', {
            parent: 'strum',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/strum/strum-dialog.html',
                    controller: 'StrumDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Strum', function(Strum) {
                            return Strum.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('strum', null, { reload: 'strum' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('strum.delete', {
            parent: 'strum',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/strum/strum-delete-dialog.html',
                    controller: 'StrumDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Strum', function(Strum) {
                            return Strum.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('strum', null, { reload: 'strum' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
