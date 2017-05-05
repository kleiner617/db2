(function() {
    'use strict';

    angular
        .module('db2App')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('employees-1', {
            parent: 'entity',
            url: '/employees-1?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Employees1S'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/employees-1/employees-1-s.html',
                    controller: 'Employees1Controller',
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
                }]
            }
        })
        .state('employees-1-detail', {
            parent: 'entity',
            url: '/employees-1/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Employees1'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/employees-1/employees-1-detail.html',
                    controller: 'Employees1DetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'Employees1', function($stateParams, Employees1) {
                    return Employees1.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'employees-1',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('employees-1-detail.edit', {
            parent: 'employees-1-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/employees-1/employees-1-dialog.html',
                    controller: 'Employees1DialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Employees1', function(Employees1) {
                            return Employees1.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('employees-1.new', {
            parent: 'employees-1',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/employees-1/employees-1-dialog.html',
                    controller: 'Employees1DialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                ssn: null,
                                contact_number: null,
                                first_name: null,
                                last_name: null,
                                specialty: null,
                                address: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('employees-1', null, { reload: 'employees-1' });
                }, function() {
                    $state.go('employees-1');
                });
            }]
        })
        .state('employees-1.edit', {
            parent: 'employees-1',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/employees-1/employees-1-dialog.html',
                    controller: 'Employees1DialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Employees1', function(Employees1) {
                            return Employees1.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('employees-1', null, { reload: 'employees-1' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('employees-1.delete', {
            parent: 'employees-1',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/employees-1/employees-1-delete-dialog.html',
                    controller: 'Employees1DeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Employees1', function(Employees1) {
                            return Employees1.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('employees-1', null, { reload: 'employees-1' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
