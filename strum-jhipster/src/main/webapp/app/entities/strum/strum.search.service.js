(function() {
    'use strict';

    angular
        .module('strumApp')
        .factory('StrumSearch', StrumSearch);

    StrumSearch.$inject = ['$resource'];

    function StrumSearch($resource) {
        var resourceUrl =  'api/_search/strums/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
