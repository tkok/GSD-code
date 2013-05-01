<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
    <head>
        <link href="style.css" rel="stylesheet" type='text/css'>
        <link href='http://fonts.googleapis.com/css?family=Droid+Serif:400,700|Droid+Sans:400,700' rel='stylesheet' type='text/css'>
        <script type="text/javascript" src="http://code.jquery.com/jquery-1.9.1.js"></script>
        <script type="text/javascript" src="jquery.validate.js"></script>
        <script>

            function doPopulation(json)
            {
                // iterate over each policy 
                for ( var k in json) {
                    // Append policy to view
                    $('.policies')
                    .append(
                    '<a href="/test/details.jsp?id='+ json[k].id +'"><div class="policy_box"><div class="inner_section"><b>Id: '
                        + json[k].id
                        + '</b>, <span style="color: lightgreen;">Active</span>, From: <b>' 
                        + json[k].interval.fromTime 
                        + '</b>, To: <b>'
                        + json[k].interval.toTime
                        + '</b><br /><br /><span class="headline">' 
                        + json[k].name
                        + '</span><br /><br /><span class="description">' 
                        + json[k].description
                        + '</span></div>'
                        + '<div style="clear:both"></div></div></a>');

                }

            }

            // Default behavior
            $.getJSON( "http://localhost:8080/test/GetAllPolicies", function( json ) {
	   
                doPopulation(json);
	
            });
            
        </script>   
    </head>
    <body>
        <div class="header">
            <div class="top">Policy Engine Administration</div>
            <div class="menu"><a id="all" href="/test/">All policies</a> | <a id="new" href="javascript:void(0);">Create new policy</a></div>
        </div>
        <%
		    if (request.getParameter("updated") == "true") {
		        out.println("Updated successfully");
		    } 
		%>
        <div class="policies">

        </div>
    </body>
</html>