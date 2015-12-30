/**
 *
 */
var endpoint = '../api';

function initDB() {

	console.log("initDB in.");

	$.ajax({
		url: endpoint + '/initDB',
		success: function(xml) {
			console.log("initDB done.");
		}
	});
}

function addAccount() {

	console.log("addAccount in.");

	var userId = $('#userId').val();
	var pass = $('#pass').val();
	var point = $('#point').val();

	$.ajax({
		url: endpoint + '/addAccount?userId=' + userId + '&pass=' + pass + '&point=' + point,
		success: function(xml) {
			console.log("addAccount done.");
		}
	});
}

function addUmbrella() {

	console.log("addUmbrella in.");

	var umbId = $('#umbId').val();
	var status = $('#status').val();
	var owner = $('#owner').val();
	var type = $('#type').val();
	var buildId = $('#buildId').val();

	$.ajax({
		url: endpoint + '/addUmbrella?umbId=' + umbId + '&status=' + status + '&owner=' + owner + '&type=' + type + '&buildId=' + buildId,
		success: function(xml) {
			console.log("addUmbrella done.");
		}
	});
}

function addBuilding() {

	console.log("addBuilding in.");

	var buildId_building = $('#buildId_building').val();
	var name = $('#name').val();
	var longitude = $('#longitude').val();
	var latitude = $('#latitude').val();
	var count = $('#count').val();

	$.ajax({
		url: endpoint + '/addBuilding?buildId_building=' + buildId_building + '&name=' + name + '&longitude=' + longitude + '&latitude=' + latitude + '&count=' + count,
		success: function(xml) {
			console.log("addBuilding done.");
		}
	});
}