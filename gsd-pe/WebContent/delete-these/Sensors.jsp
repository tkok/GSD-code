<%@page import="dk.itu.policyengine.integration.*"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@page import="java.util.*"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<% Connection connection = new Connection(); %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>List of services and id's</title>
<script type="text/javascript">
	function getSelectedElement(){
		var element = document.getElementById('sensorsList');
		var value = element.options[element.selectedIndex].value;
		return value;
	}
	function update(ish){
		var e = document.getElementById('element').value = ish;
	}
</script>
</head>
<body>
		<div id="content">
			<select id="sensorsList" onchange="update(this.value)">
			<% List<String> sensor = connection.getSensorIds(); 
				for(String s: sensor){
					out.println("<option value="+s+">"+s+"</option>");
				}
			%>					
			</select>
			<form method = "POST" action ="ListProperties" >
				<input id = "element" name="element" value="" type = "hidden" />
				<input type="submit" value="Show properties" title="Show properties">
			</form>
			</div>
			
</body>
</html>