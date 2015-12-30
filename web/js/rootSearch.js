      var apiKey="AIzaSyCptf9xX97FROBMciKq3ChVJO4ZngEF_HQ";

      var directions = new google.maps.DirectionsService();
      var renderer = new google.maps.DirectionsRenderer();
      var map, transitLayer;
	 // var autocomplete;
	 // var autocomplete2;

      var nearBuildLonDep;
      var nearBuildLatDep;

      var nearBuildLonDest;
      var nearBuildLatDest;

      var intervalSearcher;

      function initialize() {
        var mapOptions = {
          zoom: 14,
          center: new google.maps.LatLng(51.538551, -0.016633),
          mapTypeId: google.maps.MapTypeId.ROADMAP
        };

        map = new google.maps.Map(document.getElementById('map'), mapOptions);

        google.maps.event.addDomListener(document.getElementById('go'), 'click',
        route);

        var input = document.getElementById('from');
        //autocomplete = new google.maps.places.Autocomplete(input);
        //autocomplete.bindTo('bounds', map);
        var input2=document.getElementById('to');
        //autocomplete2=new google.maps.places.Autocomplete(input2);
        //autocomplete2.bindTo('bounds',map);

        transitLayer = new google.maps.TransitLayer();

        var control = document.getElementById('transit-wpr');
        map.controls[google.maps.ControlPosition.TOP_RIGHT].push(control);

        google.maps.event.addDomListener(control, 'click', function() {
          transitLayer.setMap(transitLayer.getMap() ? null : map);
        });

        addDepart();
        route();
      }

      function addDepart() {
        var depart = document.getElementById('depart');
        for (var i = 0; i < 24; i++) {
          for (var j = 0; j < 60; j += 15) {
          var x = i < 10 ? '0' + i : i;
          var y = j < 10 ? '0' + j : j;
          depart.innerHTML += '<option>' + x + ':' + y + '</option>';
        }
        }
      }

      function route() {
	      var ori=document.getElementById('from').value;
	      var dest=document.getElementById('to').value;

		  //出発地 まずオートコンプリート
		  var completeService=new google.maps.places.AutocompleteService();
		  completeService.getQueryPredictions({input:ori},function(predictions,status){
		  	if(status!=google.maps.places.PlacesServiceStatus.OK){
		  		alert(status);
		  		return;
		  	}
		  	var PlaceId=predictions[0].place_id;
		  	//次に最寄かさ置き場検索
			  var service=new google.maps.places.PlacesService(
			  			document.getElementById('from').appendChild(document.createElement('div')));
			 // var request= new google.maps.places.PlaceDetailsRequest;
			  //request.placeId=placeid;
			  var request={"placeId":PlaceId};
			  service.getDetails(request,function(results,status){
			  	nearBuildLonDep=results.geometry.location.lng();
				nearBuildLatDep=results.geometry.location.lat();
				$.ajax({
					url: '../api/searchOneBuilding?longitude=' + nearBuildLonDep + '&latitude=' + nearBuildLatDep,
					async:false,
					success: function(xml) {
						nearBuildLonDep=$('longitude',xml).text(); //更新
						nearBuildLatDep=$('latitude',xml).text();
					 },
					 error: function(XMLHttpRequest, textStatus, errorThrown) {
			            		$("#XMLHttpRequest").html("XMLHttpRequest : " + XMLHttpRequest.status);
			            		$("#textStatus").html("textStatus : " + textStatus);
			            		$("#errorThrown").html("errorThrown : " + errorThrown.message);
			         }
			     });
			  });
		  });
	      //var placeid=autocomplete.getPlace().place_id;


		  //目的地 まずオートコンプリート

		//	url: "https://maps.googleapis.com/maps/api/place/autocomplete/json?input="+dest+"&key="+apiKey,
			//var placeid=jsonData.predictions[0].place_id;
			//placeid=autocomplete2.getPlace().place_id;
			var completeService=new google.maps.places.AutocompleteService();
		    completeService.getQueryPredictions({input:dest},function(predictions,status){
			  	if(status!=google.maps.places.PlacesServiceStatus.OK){
			  		alert(status);
			  		return;
			  	}
			  	var PlaceId2=predictions[0].place_id;

				var service2=new google.maps.places.PlacesService(
				document.getElementById('to').appendChild(document.createElement('div')));
				//var request2= new google.maps.places.PlaceDetailsRequest;
				//request2.placeId=placeid;
				var request2={"placeId":PlaceId2};
				service2.getDetails(request2,function(results,status){
					nearBuildLonDest=results.geometry.location.lng();
					nearBuildLatDest=results.geometry.location.lat();
					//次に最寄かさ置き場検索
					$.ajax({
						url: '../api/searchOneBuilding?longitude=' + nearBuildLonDest + '&latitude=' + nearBuildLatDest,
						async:false,
						success: function(xml) {
							nearBuildLonDest=$('longitude',xml).text(); //更新
							nearBuildLatDest=$('latitude',xml).text();
						 },
						 error: function(XMLHttpRequest, textStatus, errorThrown) {
				            		$("#XMLHttpRequest").html("XMLHttpRequest : " + XMLHttpRequest.status);
				            		$("#textStatus").html("textStatus : " + textStatus);
				            		$("#errorThrown").html("errorThrown : " + errorThrown.message);
				         }
					  });
				});
			});
			//そしてそれらの位置に寄るルート検索
			intervalSearcher=setInterval('rootSearch()',1000);
       }
      google.maps.event.addDomListener(window, 'load', initialize);

	function rootSearch(){
		if((nearBuildLonDep!==undefined)&& (nearBuildLonDest!==undefined) ){
			var departure = document.getElementById('depart').value;
	        var bits = departure.split(':');
	        var now = new Date();
	        var tzOffset = (now.getTimezoneOffset() + 60) * 60 * 1000;

	        var time = new Date();
	        time.setHours(bits[0]);
	        time.setMinutes(bits[1]);

	        var ms = time.getTime() - tzOffset;
	        if (ms < now.getTime()) {
	          ms += 24 * 60 * 60 * 1000;
	        }

	        var departureTime = new Date(ms);

	        var request = {
	          origin: document.getElementById('from').value,
	          destination: document.getElementById('to').value,
	          travelMode: google.maps.DirectionsTravelMode.DRIVING,
	          provideRouteAlternatives: true,
	          transitOptions: {
	            departureTime: departureTime
	          },
	          waypoints:[
				{
					location: String(nearBuildLatDep)+","+String(nearBuildLonDep)
				},
				{
					location: String(nearBuildLatDest)+","+String(nearBuildLonDest)
				}
			  ]
	        };

	        var panel = document.getElementById('panel');
	        panel.innerHTML = '';
	        directions.route(request, function(response, status) {
	          if (status == google.maps.DirectionsStatus.OK) {
	            renderer.setDirections(response);
	            renderer.setMap(map);
	            renderer.setPanel(panel);
	          } else {
	            renderer.setMap(null);
	            renderer.setPanel(null);
	          }
	        });

		}

      clearInterval(intervalSearcher);
	}
