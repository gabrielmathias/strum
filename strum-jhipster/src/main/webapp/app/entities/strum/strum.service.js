(function() {
    'use strict';
    angular
        .module('strumApp')
        .factory('Strum', Strum);

    Strum.$inject = ['$resource'];

    function Strum ($resource) {
        var resourceUrl =  'api/strums/:id';

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
