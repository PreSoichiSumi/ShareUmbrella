$('#login').click(function() {
	$.ajax({
		type : 'post',
		url : '../api/login',
		data : {
			'userId' : $('#userId').val(),
			'pass' : $('#pass').val()
		},
		timeout : 100000,
		dataType : 'xml'
	}).done(function(data) {
		var result = $('login',data).text();
		if(result == "success") {
			window.location.href = 'index.html';
		}else{
			$('#login_error_msg').text("ログインに失敗しました。ユーザＩＤとパスワードをお確かめください");
			$('#login_error_form').show();
		}
	}).fail(function(data) {
		console.log("fail");
	});
});
