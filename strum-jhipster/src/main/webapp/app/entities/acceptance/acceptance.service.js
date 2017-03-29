(function() {
    'use strict';
    angular
        .module('strumApp')
        .factory('Acceptance', Acceptance);

    Acceptance.$inject = ['$resource'];

    function Acceptance ($resource) {
        var resourceUrl =  'api/acceptances/:id';

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
