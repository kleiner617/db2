(function() {
    'use strict';
    angular
        .module('db2App')
        .factory('Employees1', Employees1);

    Employees1.$inject = ['$resource'];

    function Employees1 ($resource) {
        var resourceUrl =  'api/employees-1-s/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
