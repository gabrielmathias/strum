(function() {
    'use strict';

    angular
        .module('strumApp')
        .factory('AcceptanceSearch', AcceptanceSearch);

    AcceptanceSearch.$inject = ['$resource'];

    function AcceptanceSearch($resource) {
        var resourceUrl =  'api/_search/acceptances/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
