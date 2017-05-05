(function() {
    'use strict';

    angular
        .module('db2App')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('patients', {
            parent: 'entity',
            url: '/patients?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Patients'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/patients/patients.html',
                    controller: 'PatientsController',
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
        .state('patients-detail', {
            parent: 'entity',
            url: '/patients/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Patients'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/patients/patients-detail.html',
                    controller: 'PatientsDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'Patients', function($stateParams, Patients) {
                    return Patients.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'patients',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('patients-detail.edit', {
            parent: 'patients-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/patients/patients-dialog.html',
                    controller: 'PatientsDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Patients', function(Patients) {
                            return Patients.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('patients.new', {
            parent: 'patients',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/patients/patients-dialog.html',
                    controller: 'PatientsDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                ssn: null,
                                first_name: null,
                                last_name: null,
                                birthdate: null,
                                gender: null,
                                address: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('patients', null, { reload: 'patients' });
                }, function() {
                    $state.go('patients');
                });
            }]
        })
        .state('patients.edit', {
            parent: 'patients',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/patients/patients-dialog.html',
                    controller: 'PatientsDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Patients', function(Patients) {
                            return Patients.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('patients', null, { reload: 'patients' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('patients.delete', {
            parent: 'patients',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/patients/patients-delete-dialog.html',
                    controller: 'PatientsDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Patients', function(Patients) {
                            return Patients.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('patients', null, { reload: 'patients' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
