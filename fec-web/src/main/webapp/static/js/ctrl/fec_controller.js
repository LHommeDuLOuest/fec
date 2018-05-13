/**
 * @author KHERBICHE L 
 *
'use strict';
angular.module('fecApp').controller('FecController', ['$scope', '$FecService', function($scope, $FecService) {
	
	var self=this;
	self.message={id:null, msg:''};
	
	function getUploadForm(uri) {
		FecService.getUploadForm(uri)
		     .then(
		    		 function(d){
		    			 self.message=d;
		    		 },
		    		 function(errResponse){
		    			 console.error('error when ?');
		    		 });
	}
	
}]);
*/
//
app.controller('fecController', function($scope) {
		    $scope.headingTitle = "Action List";
		});
app.controller('testController', function($scope) {
			$scope.headingTitle = "Test List";
		});