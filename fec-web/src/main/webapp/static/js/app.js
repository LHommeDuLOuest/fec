/**
 * @ author KHERBICHE L
 */

'use strict';
//var App = angular.module('fecApp',[]); 


var app = angular.module('app', ['ngRoute','ngResource']);

	app.config(function($routeProvider){
	    $routeProvider
	        .when('/',{
	            templateUrl: '/fec/WEB-INF/views/index.html',
	            controller: 'fecController'
	        })
	        .when('/home',{
	            templateUrl: '/fec/WEB-INF/views/index.html',
	            controller: 'fecController'
	        })
	        .when('/upload',{
	            templateUrl: '/fec/WEB-INF/views/test.html',
	            controller: 'testController'
	        })
	        .when('/error',{
	            templateUrl: '/fec/WEB-INF/views/error.html',
	            controller: 'testController'
	        })
	        .otherwise(
	            { redirectTo: '/'}
	        );
	});