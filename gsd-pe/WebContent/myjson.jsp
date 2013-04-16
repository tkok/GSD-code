<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
  <style>img{ height: 100px; float: left; }</style>
  <script src="http://code.jquery.com/jquery-1.9.1.js"></script>
  <title>My JSON</title>
  
  
  <script>
  
// Variable used ////////////////////////////////////////////////////////////////////////////////////////////////////
//{"statements":[{"type":"dk.itu.kben.gsd.domain.IfStatement","data":{"conditionalExpressions":[{"prefixOperator":"AND","aValue":{"type":"dk.itu.kben.gsd.domain.IntValue","data":{"theValue":10}},"operator":"EQUALS","sensorId":"ROOM1.TEMPERATURE"}],"thenStatements":[{"type":"dk.itu.kben.gsd.domain.SetStatement","data":{"aValue":{"type":"dk.itu.kben.gsd.domain.BooleanValue","data":{"theValue":true}},"sensorID":"ROOM1.HEATER"}}],"elseStatements":[{"type":"dk.itu.kben.gsd.domain.SetStatement","data":{"aValue":{"type":"dk.itu.kben.gsd.domain.BooleanValue","data":{"theValue":true}},"sensorID":"ROOM1.BLINDS"}}]}}]}

var jsonType ="dk.itu.kben.gsd.domain.IfStatement"
var jsonData = "";

var jsonFloor = "";
var jsonRoom = "";


var key;
var key2;
var key3;
var key4;
var myArray = new Array();

/////////////////////////////////////////////////////////////////////////////////////////////////////////


// These funtion break floor-1-room2 and return floor/room id. Eg. 1 for floor-1 and 2 for room-2.
function cutOutFloorID(str) {
    return str.split('-')[1];
}
function cutOutRoomID(str) {
    return str.split('m-')[1];
}

//alert(cutOutFloorID("floor-20-room-61"));
//alert(cutOutRoomID("floor-20-room-61"));

// This funtion appends the rooms and floor ids used in the selectors in the <body> element /////////////////////
function buildFloorAndRoomSelectors() {
	  var sim = "http://127.0.0.1:8080/test/ListSensors";
	  document.write("Fetching from: <b>"+sim+"</b><br />");
	  
	  
	  $.getJSON('http://127.0.0.1:8080/test/ListSensors', function(data) {
		  
		  for (key in data)
			{
			   //alert(key);
			   if (key == 'value')
			    //alert(data.value); 
				   for (key2 in data.value)
					{
					   //alert(key2); //alerts "rooms"
					   if (key2 == 'rooms')
						    //alert(data.value); 
							   for (key3 in data.value.rooms)
								{
									//counter++;
									//$('.container').append(counter + ": <br />");
								  	//$('.container').append(key3 + "<br />");
								  	myArray.push(key3);
								  	$('#floors').append("<option value='floor-"+cutOutFloorID(key3)+"'>Floor "+cutOutFloorID(key3)+"</option>");
								  	$('#rooms').append("<option value='"+cutOutRoomID(key3)+"'>Room "+cutOutRoomID(key3)+"</option>");
								  	
								}
					   $('.theArray').append(myArray);
					}
			}
		  
		 
		  
		});
	  
	};
	
	
	
buildFloorAndRoomSelectors();


// These funtion update jsonVariable onchange from the selectors //////////////////////////////////////////
function updateFloor(f){
	jsonFloor = f;
}
function updateRoom(r){
	jsonRoom = r;
}



 // End of SCRIPT /////////////////////////////////////////////////////////////////////////////////////////////////////////




</script>

</head>
<body>

	<hr />
  	<form action="/test/PersistPolicy" method="post">
  		<input type="hidden" name="id" value="policyID">
		<input name="policyEntity" value="jsonString">
		<input type="hidden" name="fromTime" value="08:00">
		<input type="hidden" name="toTime" value="16:00">
		<input type="hidden" name="active" value="true">
		<input type="submit" value="Submit">
	</form>
  
	<hr />			
			
<select id="floors" onchange="updateFloor(this.value)"></select>
<select id="rooms" onchange="updateRoom(this.value)"></select>

<!-- 
<div class="theArray">The Array: <br /></div>
<div class="container">Container: <br /></div>
 -->
 
</body>
</html>