$('#register').click(function() {
	console.log("register try");
	$.ajax({
		type : 'post',
		url : '../api/signup',
		data : {
			'userId' : $('#userId').val(),
			'pass' : $('#pass').val()
		},
		dataType : 'text',
		success: function(){
			console.log("signup.js 成功？");
		}
	}).done(function(data) {
		//alert(data);
		console.log("data->" + data);
		var t = "";
		if(data == "OK"){
			t = "登録に成功しました";
			$('#userId').val('');
			$('#pass').val('');
		}else if(data == "WID"){
			t = "既に登録されているIDです";
		}else if(data == "TYPENG"){
			t = "禁止文字が含まれています";
		}else if(data == "LENGTHNG"){
			t = "ID,パスワードは4文字から12文字までです";
		}
		$('#register_error_form').show();
		$('#register_error_msg').text(t);

	});
});
