var endpoint = '../api';
var timer;

var myVideo;

navigator.getUserMedia  = navigator.getUserMedia ||
                          navigator.webkitGetUserMedia ||
                          navigator.mozGetUserMedia ||
                          navigator.msGetUserMedia;

$(function(){

    // メディアソース取得
    MediaStreamTrack.getSources(
        //カメラソース取得成功時CallBack
        function (media_sources) {
            //カメラ選択用コンボボックスクリア
            $("#cameraid").empty();
            //メディアソース中のVideoのみを抽出
            for (var i=0; i < media_sources.length; i++) {
                if( media_sources[i].kind == "video" ){
                    //コンボボックス選択生成（value=カメラID）
                    var name = media_sources[i].label = "" ? media_suorces[i].label : "camera-" + (i+1);
                    var selstr = (media_sources[i].id == cameraid ) ? "selected" : "";
                    var childstr = '<option value="'+media_sources[i].id+'"';
                    childstr = childstr + selstr + '>';
                    childstr = childstr + name + '</option>';
                    $("#cameraid").append(childstr);
                }
            }
            //初回の描画
            setCamera(media_sources[0].id);
        }
    );
});

//カメラ起動
function setCamera(cameraid){
    //起動オプション
    var hdConstraints;

    //カメラID指定あり
    hdConstraints = {
        video: {
            optional: [{sourceId: cameraid}]
        }
    };

    //カメラ起動開始
    navigator.getUserMedia(hdConstraints,
        //カメラ呼び出し成功時CallBack
        function(localMediaStream) {
            //
            window.stream = localMediaStream;
            // videoタグ取得
            myVideo = document.getElementById('video');
            // videoタグへのソース設定
            myVideo.src= window.URL.createObjectURL(localMediaStream);
            shot();
        },
        //カメラ呼び出し失敗時CallBack
        function(err) {
            // 失敗時の処理
            alert('カメラから映像を取得することができませんでした。'+err);
        }
    );
};

//カメラID切り替え
function changeCamera(id) {

    //一度停止させる
    if (!!window.stream) {
    	stopTimer();
        myVideo.src = null;
        window.stream.stop();
    }

    //ID指定により再起動
    setCamera(id);
};

//コンボボックス選択時イベント処理
$('#cameraid').change(function() {
    //カメラID切り替え
    changeCamera($(this).val());
});

function stopTimer(){

	clearInterval(timer);
}

function shot() {

	var v;
	var canv;
	var ctx;

    timer = setInterval(function(){

        // <video>に書き込んだ画像をテキスト化（シリアライズ）するための処理
		// （参考）<canvas>に<video>の画像を投入してから toDataURL() でシリアライズ
		v = $('#video').get(0);
		canv = document.createElement('canvas');
		ctx = canv.getContext('2d');
		canv.width = 256;
		canv.height = 256;
		ctx.drawImage(v, 0, 0, 256, 256);
		src = canv.toDataURL();

		// QRコード取得開始
		qrcode.decode(src);

		// QRコード取得時のコールバックを設定
		qrcode.callback = function(result) {

			//resultが全て数字で構成されていればサーバ側へ問い合わせ
			if(result.match(/[^0-9]+/)){
 				$('div#result>p').text("QRコードの読み取りに失敗しました．再度お試しください．");
			}else{
    			// API呼び出し
				$.ajax({
					url: endpoint + '/rentUmbrella?umbId=' + result,
					success: function(xml) {
						// 結果を表示
						var message = $('rentUmbrealla',xml).text();
						$('div#result>p').text(message);
						console.log("rental->"+result);
						stopTimer();
					}
				});
			}
		};

    },500);
}