var endpoint = '../api';
var map;
var manager;

//検索ボタンクリック時の動作
$('#searchSubmit').click(function() {

	//地図表示機能を呼び出す
	if (navigator.geolocation) {
		navigator.geolocation.getCurrentPosition(showMap, showError);

	} else {
		alert('not supported');
	};

	//施設一覧をページ左部に配置予定
	var message = '施設リスト';
	$('div#list').text(message);

	$('#list').append('<hr><img src="./image/umbIcon_blue.png" alt="十分">多い');
	$('#list').append('<img src="./image/umbIcon_yellow.png" alt="ふつう">ふつう');
	$('#list').append('<img src="./image/umbIcon_red.png" alt="少ない">少ない<hr><br>');
});


//地図を表示
//BuildingList版
function showMap(position) {
	var longitude;
	var latitude;
	var searchText=document.getElementById('search').value;

	longitude = position.coords.longitude;//経度
	latitude = position.coords.latitude;//緯度

	//現在地の経緯度，または，入力された文字列から予測された位置の緯度経度を取得
	if(searchText==""){
		longitude = position.coords.longitude;//経度
		latitude = position.coords.latitude;//緯度
		// API呼び出し.lngとlatが決まらないと検索できないので入れ子に
		$.ajax({
			url: endpoint + '/searchBuilding?longitude=' + longitude + '&latitude=' + latitude,
			success: function(xml) {
				console.log(xml);
				console.log('mypos' + longitude + ',' + latitude);

				var len = $('buildings',xml).size();

				var lons = $('buildings',xml).find('longitude');
				var lats = $('buildings',xml).find('latitude');
				var counts = $('buildings',xml).find('count');
				var names = $('buildings',xml).find('name');

				// 地図のセンターポジション
				var latlng = new google.maps.LatLng(latitude,longitude);
				// 地図のオプションを決める
				var opts = {zoom : 16, center : latlng};
				// 地図を描画 （<hoge id="map">のHTML要素に地図を書込む）
				map = new google.maps.Map($('div#map').get(0), opts);

				var longitudeResult;
				var latitudeResult;
				var count;
				var name;

				for(var i = 0; i<len; i++){

					longitudeResult = lons.eq(i).text();
					latitudeResult = lats.eq(i).text();
					count = counts.eq(i).text();
					name = names.eq(i).text();

					var umbImagePath;

					if (count <= 5) {
						umbImagePath = './image/umbIcon_red.png';
					} else if (count >= 20 ) {
						umbImagePath = './image/umbIcon_blue.png';
					} else {
						umbImagePath = './image/umbIcon_yellow.png';
					}

					//情報ウィンドウ
		            var infoHtml = "<p>施設名：" + name + "<br>傘の本数：" + count + "</p>";
		            var infowindow = new google.maps.InfoWindow({
		                content: infoHtml
					});

					//地図左のリストに追加
					if((Math.abs(longitude-longitudeResult) <= 0.02) && (Math.abs(latitude - latitudeResult) <= 0.02)){
						$('#list').append(infoHtml);

						//場所用ボタン追加
						var bt = '<p><input type="button" value="場所" onclick="pan(';
						bt += longitudeResult + ',' + latitudeResult;
						bt += ')"></p>';
						$('#list').append(bt + '<hr>');
						//console.log(bt);
					}

					//地図上にマーカーを設置
					var m_latlng = new google.maps.LatLng(latitudeResult,longitudeResult);
		  			var marker = new google.maps.Marker({
		    			position: m_latlng,
		    			map: map,
		    			title:"施設情報",
		    			//icon: "./image/umbIcon.png"
		    			icon: new google.maps.MarkerImage(
		    				//'./image/umbIcon2.png'			// url
		    				umbImagePath
							//new google.maps.Size(39,156)
		    			)
		  			});

					dispInfo(marker, infowindow);
				}  //for文ここまで

			}
		});
	}else{

		//出発地 まずオートコンプリート
		  var completeService=new google.maps.places.AutocompleteService();
		  completeService.getQueryPredictions({input:searchText},function(predictions,status){
		  	if(status!=google.maps.places.PlacesServiceStatus.OK){
		  		alert(status);
		  		return;
		  	}
		  	var PlaceId=predictions[0].place_id;
		  	//次に最寄かさ置き場検索
			  var service=new google.maps.places.PlacesService(
			  			document.getElementById('search').appendChild(document.createElement('div')));
			 // var request= new google.maps.places.PlaceDetailsRequest;
			  //request.placeId=placeid;
			  var request={"placeId":PlaceId};
			  service.getDetails(request,function(results,status){
			  	longitude=results.geometry.location.lng();
				latitude=results.geometry.location.lat();
				// API呼び出し.lngとlatが決まらないと検索できないので入れ子に
				$.ajax({
					url: endpoint + '/searchBuilding?longitude=' + longitude + '&latitude=' + latitude,
					success: function(xml) {
						console.log(xml);
						console.log('mypos' + longitude + ',' + latitude);

						var len = $('buildings',xml).size();

						var lons = $('buildings',xml).find('longitude');
						var lats = $('buildings',xml).find('latitude');
						var counts = $('buildings',xml).find('count');
						var names = $('buildings',xml).find('name');

						// 地図のセンターポジション
						var latlng = new google.maps.LatLng(latitude,longitude);
						// 地図のオプションを決める
						var opts = {zoom : 16, center : latlng};
						// 地図を描画 （<hoge id="map">のHTML要素に地図を書込む）
						map = new google.maps.Map($('div#map').get(0), opts);

						var longitudeResult;
						var latitudeResult;
						var count;
						var name;

						for(var i = 0; i<len; i++){

							longitudeResult = lons.eq(i).text();
							latitudeResult = lats.eq(i).text();
							count = counts.eq(i).text();
							name = names.eq(i).text();

							var umbImagePath;

							if (count <= 5) {
								umbImagePath = './image/umbIcon_red.png';
							} else if (count >= 20 ) {
								umbImagePath = './image/umbIcon_blue.png';
							} else {
								umbImagePath = './image/umbIcon_yellow.png';
							}

							//情報ウィンドウ
				            var infoHtml = "<p>施設名：" + name + "<br>傘の本数：" + count + "</p>";
				            var infowindow = new google.maps.InfoWindow({
				                content: infoHtml
							});

							//地図左のリストに追加
							if((Math.abs(longitude-longitudeResult) <= 0.02) && (Math.abs(latitude - latitudeResult) <= 0.02)){
								$('#list').append(infoHtml);

								//場所用ボタン追加
								var bt = '<p><input type="button" value="場所" onclick="pan(';
								bt += longitudeResult + ',' + latitudeResult;
								bt += ')"></p>';
								$('#list').append(bt + '<hr>');
								//console.log(bt);
							}

							//地図上にマーカーを設置
							var m_latlng = new google.maps.LatLng(latitudeResult,longitudeResult);
				  			var marker = new google.maps.Marker({
				    			position: m_latlng,
				    			map: map,
				    			title:"施設情報",
				    			//icon: "./image/umbIcon.png"
				    			icon: new google.maps.MarkerImage(
				    				//'./image/umbIcon2.png'			// url
				    				umbImagePath
									//new google.maps.Size(39,156)
				    			)
				  			});

							dispInfo(marker, infowindow);
						}  //for文ここまで

					}
				});
			  });
		  });

	}

}

function dispInfo(marker,infowindow){
	// マーカーがクリックされた時に情報ウィンドウ表示
    google.maps.event.addListener(marker,'click', function(event) {
        infowindow.open(marker.getMap(), marker);
    });
}

function pan(lon,lat){
	//場所ボタンクリック時にその場所を中央にして表示させる
	console.log("panTo"+lon+","+lat);
	//map = new google.maps.Map($('div#map').get(0));
	var ltlg = new google.maps.LatLng(lat,lon);
	map.panTo(ltlg);
}

/*
最寄施設のみ表示のバージョン
function showMap(position) {

	//現在地の経緯度
	var longitude = position.coords.longitude;//経度
	var latitude = position.coords.latitude;//緯度

	// API呼び出し
	$.ajax({
		url: endpoint + '/searchBuilding?longitude=' + longitude + '&latitude=' + latitude,
		success: function(xml) {

			//成功時，最寄りの施設の経緯度を取得
			var longitudeResult = $('longitude',xml).text();
			var latitudeResult = $('latitude',xml).text();

			console.log(xml);

			var latlng = new google.maps.LatLng(latitude,longitude);
			// 地図のオプションを決める
			var opts = {zoom : 16, center : latlng};
			// 地図を描画 （<hoge id="map">のHTML要素に地図を書込む）
			map = new google.maps.Map($('div#map').get(0), opts);

			//情報ウィンドウ
            var infoHtml = "<p>施設名：" + $('name',xml).text() + "<br>傘の本数：" + $('count',xml).text() + "<br></p>";
            var infowindow = new google.maps.InfoWindow({
                content: infoHtml
            });

			//地図上にマーカーを設置
			var m_latlng = new google.maps.LatLng(latitudeResult,longitudeResult);
  			var marker = new google.maps.Marker({
    			position: m_latlng,
    			map: map,
    			title:"施設情報"
  			});

            // マーカーがクリックされた時に情報ウィンドウ表示
            google.maps.event.addListener(marker,'click', function() {
                infowindow.open(map,marker);
            });
		}
	});
}
*/

//地図表示に失敗した場合の動作
function showError(error) {

}
