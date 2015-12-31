var endpoint = '../api';


// ヘッダ部分書く用
var rntl= 	'<ul class="nav navbar-nav">'+
				'<li><a href="index.html">Home</a></li>'+
				'<li class="dropdown"><a class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true" aria-expanded="false">Search<span class="caret"></span></a>'+
				'<ul class="dropdown-menu">'+
					'<li><a href="search.html">Surrounding</a></li>'+
					'<li><a href="rootSearch.html">RootNavigation</a></li>'+
				'</ul></li>'+
			'</ul>';
var loggedIn	='<ul class="nav navbar-nav">'+
				 '<li><a href="rental.html">Rental</a></li>'+
				 '<li><a href="return.html">Return</a></li>'+
				 '</ul>'+
				 '<ul class="nav navbar-nav navbar-right">'+
				 '<li><a href="mypage.html">MyPage</a></li>'+
				 '<li><a href="index.html" onClick="logout();">Logout</a></li>'+
				 '</ul>';

// ヘッダ初期化処理
function initHeader() {
	$.ajax({
		url: endpoint + '/confirmSession',
		success: function(xml) {
			console.log(xml);
			var message = $('userId',xml).text();
			console.log(message);

			if (message == "") {

			} else {
				$('#rental').html(rntl);
				$('#aboutUser').html(loggedIn);
			}
		}
	});
}

function logout() {

	$.ajax({
		url: endpoint + '/logout',
		success: function(xml) {
			console.log(xml);
		}
	});
}


