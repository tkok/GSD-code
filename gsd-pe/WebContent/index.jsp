<!DOCTYPE html>
<html>
    <head>
        <link href="css/style.css" rel="stylesheet" type='text/css'>
        <link href='http://fonts.googleapis.com/css?family=Droid+Serif:400,700|Droid+Sans:400,700' rel='stylesheet' type='text/css'>
        <script type="text/javascript" src="http://code.jquery.com/jquery-1.9.1.js"></script>
        <script type="text/javascript" src="js/jquery.validate.js"></script>
        <script>
        $(document).ready(function() {
        	$.ajaxSetup({ cache: false });
       	
            function doPopulation(json)
            {
                // iterate over each policy 
                for ( var k in json) {
                    // Append policy to view
                    var active = "";
                    if(json[k].active == true) { var active = '<span style="font-weight: bold; color: lightgreen;">Active</span>'; } else { var active = '<span style="font-weight: bold; color: #8C1700;">Not active</span>'; }
                    
                    $('.policies')
                    .append(
                    '<a href="details.jsp?id='+ json[k].id +'"><div class="policy_box"><div class="inner_section"><b>Id: '
                        + json[k].id
                        + '</b>, '
                        + active
                        + ', From: <b>' 
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
            $.getJSON( "GetAllPolicies", function( json ) {
	   
                doPopulation(json);
	
            });
        });   
        </script>   
    </head>
    <body>
        <div class="header">
            <div class="top">Policy Engine Administration</div>
            <div class="menu"><a id="all" href="index.jsp">All policies</a> | <a id="new" href="newpolicy.jsp">Create new policy</a></div>
        </div>
        <div class="policies">

        </div>
    </body>
</html>