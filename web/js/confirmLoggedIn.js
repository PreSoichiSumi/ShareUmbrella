/**
 *
 */
var endpoint = '../api';

$.ajax({
	url: endpoint + '/confirmSession',
	success: function(xml) {
		var message = $('userId',xml).text();
		if (message == "") {
		document.location='index.html';
		confirm(document.location+"\nこのページにはログインした状態でアクセスしてください．");
		}
	}
});