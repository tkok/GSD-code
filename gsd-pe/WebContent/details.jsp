<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
    <head>
        <link href="/test/css/style.css" rel="stylesheet" type='text/css'>
        <link href="/test/js/jquery-ui-1.10.2.custom/css/jquery-ui.min.css" rel="stylesheet" type='text/css'>
        <link href="/test/js/jquery-ui-1.10.2.custom/css/jquery.ui.theme.css" rel="stylesheet" type='text/css'>
        <link href='http://fonts.googleapis.com/css?family=Droid+Serif:400,700|Droid+Sans:400,700' rel='stylesheet' type='text/css'>
        <script type="text/javascript" src="http://code.jquery.com/jquery-1.9.1.js"></script>
        <script type="text/javascript" src="/test/js/jquery.validate.js"></script>
        <script type="text/javascript" src="/test/js/jquery-ui-1.10.2.custom/js/jquery-ui-1.10.2.custom.min.js"></script>
        <script type="text/javascript" src="/test/js/jquery-ui-timepicker-addon.js"></script>
        <script>

            function operator(operator) {
                // Construct operator selector 
                var operatorselect = '<select name="operator">';
	
                if(operator == "LESS_THAN") {  operatorselect += '<option value="LESS_THAN" selected><</option>' } else {  operatorselect += '<option value="LESS_THAN"><</option>'}
                if(operator == "GREATER_THAN") { operatorselect += '<option value="GREATER_THAN" selected>></option>' } else { operatorselect += '<option value="GREATER_THAN">></option>'}
                if(operator == "EQUALS") {  operatorselect += '<option value="EQUALS" selected>==</option>' } else {  operatorselect += '<option value="EQUALS">==</option>'}
                if(operator == "NOT") {  operatorselect += '<option value="NOT" selected>!=</option>' } else {  operatorselect += '<option value="NOT">!=</option>'}
	
                operatorselect += '</select>';
	
                return operatorselect;
            }
           
            function doPopulation(json)
            {
                // count variables
                var countce = 0;
                var countts = 0;
                var countes = 0;
                // iterate over each policy
                for ( var k in json) {
			
                	var active = "";
                    if(json[k].active == true) { var active = '<span style="font-weight: bold; color: lightgreen;">Active</span>'; } else { var active = '<span style="font-weight: bold; color: #8C1700;">Not active</span>'; }

                    var activecheckbox = "";
                    if(json[k].active == true) { var activecheckbox = '<input type="radio" name="active" id="r_true" value="true" style="float: left;" checked> <label style="float: left;" for="r_true">Yes</label> <input style="float: left;" type="radio" id="r_false" name="active" value="false"> <label style="float: left;" for="r_false">No</label>'; } else { var activecheckbox = '<input type="radio" id="r_true" style="float: left;" name="active" value="true"> <label style="float: left;" for="r_true">Yes</label> <input type="radio" style="float: left;" name="active" id="r_false" value="false" checked> <label style="float: left;" for="r_false">No</label>'; }
                    
                    // Append policy to view
                    $('.policies')
                    .append(
                    '<div class="policy_box"><form id="submit" action="PersistPolicy" method="post"><div class="inner_section"><span class="headline line">Policy Information</span> (<b>Id: '
                        + json[k].id
                        + '<input type="hidden" id="id" name="id" value="' + json[k].id + '"></b>, '
                        + active
                        + ')</div>'
                        + '<div class="inner_section">'
                        + '<div style="float: left;">Is active: </div>' + activecheckbox
                        + '<br /><br />Name: <input type="text" class="required" name="name" id="name" value="'
                        + json[k].name
                        + '"> '
                        + 'From: <input type="text" id="fromTime" name="fromTime" style="width: 50px;" value="' + json[k].interval.fromTime + '"> '
                        + 'To: <input type="text" id="toTime" name="toTime" style="width: 50px;" value="' + json[k].interval.toTime + '"><br /><br />'
                        + 'Description: <textarea class="field required" style="width: 500px;" id="description" type="text" name="description">'
                        + json[k].description
                        + '</textarea></div>'
                        + '<input type="hidden" id="policy" name="policy" value=\'' + JSON.stringify(json[k].policy) + '\'>'
                        + '<div class="inner_section"><span class="headline line">Policy Rules</span></div>'
                        + '<div class="inner_section"><span class="headline2">IF Values </span>(<a id="newif-' + json[k].id + '" href="JavaScript:void(0);">+ New</a>)</b></div>'
                        + '<div id="if-' + json[k].id + '" class="inner_section">'
                        + '<div style="clear: both;"></div>'
                        + '</div>'
                        + '<div class="inner_section"><span class="headline2">THEN Values </span>(+ New)</b></div>'
                        + '<div id="then-' + json[k].id + '" class="inner_section">'
                        + '<div style="clear: both;"></div>'
                        + '</div>'
                        + '<div class="inner_section"><b>THEN (1st level nested) VALUES </b></div>'
                        + '<div id="then-nested-' + json[k].id + '" class="inner_section">'
                        + '<div class="inner_section float"><i><b>IF VALUES </b></i></div>'
                        + '<div id="then-nested-if-' + json[k].id + '" class="inner_section">'
                        + '<div style="clear: both;"></div>'
                        + '</div>'
                        + '<div class="inner_section"><i><b>THEN VALUES </b></i></div>'
                        + '<div id="then-nested-then-' + json[k].id + '" class="inner_section">'
                        + '<div style="clear: both;"></div>'
                        + '</div>'
                        + '<div class="inner_section"><i><b>ELSE VALUES </b></i></div>'
                        + '<div id="then-nested-else-' + json[k].id + '" class="inner_section">'
                        + '<div style="clear: both;"></div>'
                        + '</div>'
                        + '</div>'
                        + '<div class="inner_section"><span class="headline2">ELSE Values </span>(+ New)</b></div>'
                        + '<div id="else-' + json[k].id + '" class="inner_section">'
                        + '<div style="clear: both;"></div>'
                        + '</div>'
                        + '<div class="inner_section"><input type="submit" value="Update" name="update" class="button"></div><div style="clear:both"></div></form></div>');

                    // iterate over statements 
                    for ( var l in json[k].policy.statements) {
                        // statement type
                        var con_type = json[k].policy.statements[l].type;
					
                            
                        // iterate over conditionalExpressions 
                        for ( var m in json[k].policy.statements[l].data.conditionalExpressions) {
                            // Append to policy
                            $('#if-'+ json[k].id)
                            .prepend('<div id="if-' + json[k].id + '-' + l + '-' + m +'" class="inner_inner_section if"><div><b>if-' + json[k].id + '-' + l + '-' + m +'</b></div>'
                            //+ json[k].policy.statements[l].data.conditionalExpressions[m].aValue.type
                                + '<input style="width:140px;" type="text" name="sesorid" value="' + json[k].policy.statements[l].data.conditionalExpressions[m].sensorId + '">'
                                + ', '
                                + operator(json[k].policy.statements[l].data.conditionalExpressions[m].operator)
                                + ', <input type="text" name="datafloatvalue" style="width:30px;" value="'
                                + json[k].policy.statements[l].data.conditionalExpressions[m].aValue.data.floatValue
                                + '">, '
                                + '<select><option value="' + json[k].policy.statements[l].data.conditionalExpressions[m].prefixOperator + '" selected>' + json[k].policy.statements[l].data.conditionalExpressions[m].prefixOperator + '</option></select>'
                                + '</div>'
                        );
								
                            countce++;
                        }
                        // iterate over thenStatements
                        for (var m in json[k].policy.statements[l].data.thenStatements) {

                            recursiveThen(json, k, l, m);
                                
                            countts++;
                        }
                        // iterate over elseStatements
                        for (var m in json[k].policy.statements[l].data.elseStatements) {
				   			
                            // Append to policy
                            $('#else-'+ json[k].id)
                            .prepend('<div id="else-' + json[k].id + '-' + l + '-' + m +'" class="inner_inner_section else"><div><b>else-' + json[k].id + '-' + l + '-' + m +'</b></div>'
                                + json[k].policy.statements[l].data.elseStatements[m].type
                                + ', '
                                + json[k].policy.statements[l].data.elseStatements[m].data.aValue.floatValue
                                + ', '
                                + json[k].policy.statements[l].data.elseStatements[m].data.sensorID
                                + '</div>'
                        );
                            countes++;
                        }

                    }
                    
                    $("form#submit").submit(function() {
                        
                        if($("form#submit").valid() == true) {
            				
                        	// Submit

                        } else { return false; }
                        
                    });
	
                    // buttons defined
                    $('#newif-' + json[k].id).click(function() {
						
                        console.log(countce);
                        $('#if-' + json[k].id)
                        .prepend('<div id="if-' + json[k].id + '-' + countce + '-' + m +'" class="inner_inner_section if"><div><b>if-' + json[k].id + '-' + countce + '-' + m +'</b></div>'
                        //+ json[k].policy.statements[l].data.conditionalExpressions[m].aValue.type
                            + '<input style="width:140px;" type="text" name="sesorid" value="' + json[k].policy.statements[l].data.conditionalExpressions[m].sensorId + '">'
                            + ', '
                            + operator(json[k].policy.statements[l].data.conditionalExpressions[m].operator)
                            + ', <input type="text" name="datafloatvalue" style="width:30px;" value="'
                            + json[k].policy.statements[l].data.conditionalExpressions[m].aValue.data.floatValue
                            + '">, '
                            + '<select><option value="' + json[k].policy.statements[l].data.conditionalExpressions[m].prefixOperator + '" selected>' + json[k].policy.statements[l].data.conditionalExpressions[m].prefixOperator + '</option></select>'
                            + '</div>'
                    );
                    });
                    
                    $('#fromTime').timepicker();
                    $('#toTime').timepicker();

                }
                
                
            }
            
            function recursiveThen(json, k, l, m)
            {

                // check for nested statements 
                if (json[k].policy.statements[l].data.thenStatements[m].type == "dk.itu.policyengine.domain.IfStatement") {

                    // iterate over nested conditionalExpressions
                    for (var n in json[k].policy.statements[l].data.thenStatements[m].data.conditionalExpressions) {
				   			
                        // Append to policy
                        $('#then-nested-if-'+ json[k].id)
                        .prepend('<div id="then-nested-if-' + json[k].id + '-' + l + '-' + m +'-' + n +'" class="inner_inner_section if"><div><b>then-nested-if-' + json[k].id + '-' + l + '-' + m +'-' + n +'</b></div>'
                            + json[k].policy.statements[l].data.thenStatements[m].data.conditionalExpressions[n].prefixOperator
                            + ', '
                            + json[k].policy.statements[l].data.thenStatements[m].data.conditionalExpressions[n].aValue.type
                            + ', '
                            + json[k].policy.statements[l].data.thenStatements[m].data.conditionalExpressions[n].aValue.data.floatValue
                            + ', '
                            + json[k].policy.statements[l].data.thenStatements[m].data.conditionalExpressions[n].operator
                            + ', '
                            + json[k].policy.statements[l].data.thenStatements[m].data.conditionalExpressions[n].sensorId
                            + '</div>'
                    );

                    }
                    // iterate over nested thenStatements
                    for (var n in json[k].policy.statements[l].data.thenStatements[m].data.thenStatements) {

                        //recursiveThen(json, k, l, m)
				   				
                        // Problem is how to handle the paths in each level of a run
				   			
				   			
                        // check for 2nd level nested statements
                        if (json[k].policy.statements[l].data.thenStatements[m].data.thenStatements[n].type == "dk.itu.policyengine.domain.IfStatement") {
                            // IF we want to support 2nd level nested statements - do the same as for 1st level......
                            console.log("-- NESTED 2nd lvl thenStatement: " + n + " ---");
				   				
                            // NO SUPPORT FOR 2nd level nested so just take type Append to policy
                            $('#then-nested-then-'+ json[k].id)
                            .prepend('<div id="then-nested-then-' + json[k].id + '-' + l + '-' + m +'-' + n +'" class="inner_inner_section then"><div><b>then-nested-then-' + json[k].id + '-' + l + '-' + m +'-' + n +'</b></div>'
                                + '2nd level nested NOT SUPPORTED, '
                                + json[k].policy.statements[l].data.thenStatements[m].data.thenStatements[n].type
                                + '</div>'
                        );

								
                        } else {
				   			
                            // Append to policy
                            $('#then-nested-then-'+ json[k].id)
                            .prepend('<div id="then-nested-then-' + json[k].id + '-' + l + '-' + m +'-' + n +'" class="inner_inner_section then"><div><b>then-nested-then-' + json[k].id + '-' + l + '-' + m +'-' + n +'</b></div>'
                                + json[k].policy.statements[l].data.thenStatements[m].data.thenStatements[n].type
                                + ', '
                                + json[k].policy.statements[l].data.thenStatements[m].data.thenStatements[n].data.aValue.floatValue
                                + ', '
                                + json[k].policy.statements[l].data.thenStatements[m].data.thenStatements[n].data.sensorID
                                + '</div>'
                        );	
	   			
                        }
   			
                    }
                    // iterate over nested elseStatements
                    for (var n in json[k].policy.statements[l].data.thenStatements[m].data.elseStatements) {
				   			
                        // Append to policy
                        $('#then-nested-else-'+ json[k].id)
                        .prepend('<div id="then-nested-else-' + json[k].id + '-' + l + '-' + m +'-' + n +'" class="inner_inner_section else"><div><b>then-nested-else-' + json[k].id + '-' + l + '-' + m +'-' + n +'</b></div>'
                            + json[k].policy.statements[l].data.thenStatements[m].data.elseStatements[n].type
                            + ', '
                            + json[k].policy.statements[l].data.thenStatements[m].data.elseStatements[n].data.aValue.floatValue
                            + ', '
                            + json[k].policy.statements[l].data.thenStatements[m].data.elseStatements[n].data.sensorID
                            + '</div>'
                    );
                    }
                } else {
		   				
                    // Append to policy (non nested) 
                    $('#then-'+ json[k].id)
                    .prepend('<div id="then-' + json[k].id + '-' + l + '-' + m +'" class="inner_inner_section then"><div><b>then-' + json[k].id + '-' + l + '-' + m +'</b></div>'
                        + json[k].policy.statements[l].data.thenStatements[m].type
                        + ', '
                        + json[k].policy.statements[l].data.thenStatements[m].data.aValue.floatValue
                        + ', '
                        + json[k].policy.statements[l].data.thenStatements[m].data.sensorID
                        + '</div>'
                );
                }

            }
            
            // JSP power
	          <%
	          String id = request.getParameter("id").toString();
	          %>
          
            // Default behavior
            $.getJSON( "http://localhost:8080/test/GetPolicy?id=<% out.print(id); %>", function( json ) {
	   
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