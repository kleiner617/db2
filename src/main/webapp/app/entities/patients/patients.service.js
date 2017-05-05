(function() {
    'use strict';
    angular
        .module('db2App')
        .factory('Patients', Patients);

    Patients.$inject = ['$resource', 'DateUtils'];

    function Patients ($resource, DateUtils) {
        var resourceUrl =  'api/patients/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.birthdate = DateUtils.convertLocalDateFromServer(data.birthdate);
                    }
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    data.birthdate = DateUtils.convertLocalDateToServer(data.birthdate);
                    return angular.toJson(data);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    data.birthdate = DateUtils.convertLocalDateToServer(data.birthdate);
                    return angular.toJson(data);
                }
            }
        });
    }
})();
