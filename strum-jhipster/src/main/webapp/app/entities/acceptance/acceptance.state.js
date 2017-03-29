(function() {
    'use strict';

    angular
        .module('strumApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('acceptance', {
            parent: 'entity',
            url: '/acceptance?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'strumApp.acceptance.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/acceptance/acceptances.html',
                    controller: 'AcceptanceController',
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
                    $translatePartialLoader.addPart('acceptance');
                    $translatePartialLoader.addPart('acceptanceStatus');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('acceptance-detail', {
            parent: 'acceptance',
            url: '/acceptance/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'strumApp.acceptance.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/acceptance/acceptance-detail.html',
                    controller: 'AcceptanceDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('acceptance');
                    $translatePartialLoader.addPart('acceptanceStatus');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Acceptance', function($stateParams, Acceptance) {
                    return Acceptance.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'acceptance',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('acceptance-detail.edit', {
            parent: 'acceptance-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/acceptance/acceptance-dialog.html',
                    controller: 'AcceptanceDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Acceptance', function(Acceptance) {
                            return Acceptance.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('acceptance.new', {
            parent: 'acceptance',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/acceptance/acceptance-dialog.html',
                    controller: 'AcceptanceDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                criteria: null,
                                status: null,
                                message: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('acceptance', null, { reload: 'acceptance' });
                }, function() {
                    $state.go('acceptance');
                });
            }]
        })
        .state('acceptance.edit', {
            parent: 'acceptance',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/acceptance/acceptance-dialog.html',
                    controller: 'AcceptanceDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Acceptance', function(Acceptance) {
                            return Acceptance.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('acceptance', null, { reload: 'acceptance' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('acceptance.delete', {
            parent: 'acceptance',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/acceptance/acceptance-delete-dialog.html',
                    controller: 'AcceptanceDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Acceptance', function(Acceptance) {
                            return Acceptance.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('acceptance', null, { reload: 'acceptance' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
