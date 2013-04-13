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
var key;
var key2;
var key3;
var key4;
var myArray = new Array();
counter = 0;

function cutOutFloorID(str) {
    return str.split('-')[1];
}
function cutOutRoomID(str) {
    return str.split('m-')[1];
}

//alert(cutOutFloorID("floor-20-room-61"));
//alert(cutOutRoomID("floor-20-room-61"));

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







</script>

</head>
<body>
  <div id="images">
 
</div>
			
			
<select id="floors"></select>
<select id="rooms"></select>
<!-- 
<div class="theArray">The Array: <br /></div>
<div class="container">Container: <br /></div>
 -->
 
</body>
</html>