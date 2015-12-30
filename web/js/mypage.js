var endpoint = '../api';
var webEndpoint='../web';
var myPageUserId="else";
var myPageUmbPoint=0;
var myPageUmbList=[];
var js;

// ページロード時のデータ取得
$(window).load(function() {
	console.log("loaded");
	// API呼び出し
	$.ajax({
		type:'GET',
		url: endpoint + '/getUserInfo',
		dataType:'json',
		success: function(data) {
			var anyUmbrella=false;
			console.log(data);

			myPageUserId=data.userId;
			myPageUmbPoint=data.point;
			myPageUmbList=data.umbIdList;
			$('#USERID').text(myPageUserId);
			$('#UMBPOINT').text(myPageUmbPoint);
			var umbListStr="<span id=\"UMBLIST\"><ul>";
			for(var i in myPageUmbList){
				umbListStr+=("<li>Id= "+myPageUmbList[i]+" </li>");
				anyUmbrella=true;
			}
			umbListStr+="</ul></span>";
			if(anyUmbrella){
				$('#UMBLIST').html(umbListStr);
			}
		},
		error: function(XMLHttpRequest, textStatus, errorThrown){
			console.log('erroraaaaa');
			confirm('XMLHttpRequest: '+XMLHttpRequest.status
				+'\ntextStatus '+textStatus
				+'\nerrorThrown '+errorThrown.message);
		}
	});
});

//UPチャージ
$("#charge").click(function(){
	var money=$("#money").val();
	console.log("start charging");
	$.ajax({
		type:'POST',
		url: endpoint+'/charge?money='+money,
		dataType:'text',
		success: function(res){
			console.log("チャージしました");
		},
		error: function(XMLHttpRequest, textStatus, errorThrown){
			confirm('XMLHttpRequest: '+XMLHttpRequest.status
					+'\ntextStatus '+textStatus
					+'\nerrorThrown '+errorThrown.message);
					console.log("えらった");
		}
	});
});

//退会処理
$("#leave").click(function(){
	confirm("退会しました．");
	//window.location.href="index.html";
	//document.location="index.html";
});