<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
  <style>img{ height: 100px; float: left; }</style>
  <script src="http://code.jquery.com/jquery-1.9.1.js"></script>
</head>
<body>
  <div id="images">
 
</div>
<script>
var items = [];
(function() {
  var sim = "http://127.0.0.1:8080/test/ListSensors";
  document.write("Fetching from: <b>"+sim+"</b><br />");
  
  $.getJSON('http://127.0.0.1:8080/test/ListSensors', function(data) {
	  
	 
	  $.each(data, function(key, val) {
	    items.push('<li id="' + key + '">' + val + '</li>');
	    
	    
	  });
	  
	  
	 
	  $('<ul/>', {
	    'class': 'my-new-list',
	    html: items.join('')
	  }).appendTo('body');
	});
  
  
  document.write(items);
})();



</script>
 
</body>
</html>