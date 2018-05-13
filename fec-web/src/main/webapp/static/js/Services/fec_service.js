/**
 * @author KHERBICHE L
 */

'use strict';
angular.module('fecApp').factory('FecService',['$http', '$q', function($http,$q) {
	
	var REST_SERVICE_URI='http://localhost:8081/fec/api/';
	var factory = {
			getUploadForm:getUploadForm
	};
	return factory;
	
	function getUploadForm(uri) {
		var deferred = $q.defer();
		$http.get(REST_SERVICE_URI+uri)
		     .then(
		    		 function(response){
		    			deferred.resolve(response.data); 
		    		 },
		    		 function(errResponse){
		    			 console.error('Error when ... ');
		    			 deferred.reject(errResponse);
		    		 }
		    		 );
		return deferred.promise;
	}
	
	
}]);