<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
  <style>img{ height: 100px; float: left; }</style>
  <script src="http://code.jquery.com/jquery-1.9.1.js"></script>
  <title>My JSON</title>
</head>
<body>
  <div id="images">
 
</div>
<script>
var key;
var key2;
var key3;
var key4;
(function() {
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
					   alert(key2);
					   if (key2 == 'rooms')
						    //alert(data.value); 
							   for (key3 in data.value.rooms)
								{
								   //alert(key3);
								   $("")
								}
					   
					}
			}
		  
		 
		  
		});
	  
	  
	  document.write(items);
	})();




var d = "http://127.0.0.1:8080/test/ListSensors";

for (var key in d.description)
{
   alert(key);
   //if (key == 'comment')
    //alert(myJSONObject.topicos[0].comment.commentable_type); 
}





</script>

 
</body>
</html>