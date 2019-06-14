(function() {
  'use strict';

  angular.module('hub').factory('ReferenceAPIService', ReferenceAPIService);

  ReferenceAPIService.$inject = ['$resource', 'SERVER'];

  function ReferenceAPIService($resource, SERVER) {
    return $resource(
      SERVER.API + '/reference/:action',
      {},
      {
        get: {},
        languages: {
          url: SERVER.API + '/reference/languages',
          isArray: true
        }
      }
    );
  }
})();
