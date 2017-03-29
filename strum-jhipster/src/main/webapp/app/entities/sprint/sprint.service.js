(function() {
    'use strict';
    angular
        .module('strumApp')
        .factory('Sprint', Sprint);

    Sprint.$inject = ['$resource', 'DateUtils'];

    function Sprint ($resource, DateUtils) {
        var resourceUrl =  'api/sprints/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.start_at = DateUtils.convertDateTimeFromServer(data.start_at);
                        data.end_at = DateUtils.convertDateTimeFromServer(data.end_at);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
