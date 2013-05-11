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
        <script type="text/javascript" src="/test/js/autocomplete.js"></script>
        <script>
        $(document).ready(function() {
            function operator(operator) {
                // Construct operator selector 
				var operatorselect = "";
                
                if(operator == "LESS_THAN") {  operatorselect += '<option value="LESS_THAN" selected><</option>' } else {  operatorselect += '<option value="LESS_THAN"><</option>'}
                if(operator == "GREATER_THAN") { operatorselect += '<option value="GREATER_THAN" selected>></option>' } else { operatorselect += '<option value="GREATER_THAN">></option>'}
                if(operator == "EQUALS") {  operatorselect += '<option value="EQUALS" selected>==</option>' } else {  operatorselect += '<option value="EQUALS">==</option>'}
                if(operator == "NOT") {  operatorselect += '<option value="NOT" selected>!=</option>' } else {  operatorselect += '<option value="NOT">!=</option>'}
	
                return operatorselect;
            }
            
            function setto(value) {
                // Construct operator selector 
				var setto = "";
                
                if(value == 0) { setto += '<option value="0" selected>0 (Off)</option>' } else {  setto += '<option value="0">0 (Off)</option>'}
                if(value == 1) { setto += '<option value="1" selected>1 (On)</option>' } else { setto += '<option value="1">1 (On)</option>'}
                	
                return setto;
            }
           
            function doPopulation(json)
            {
                // count variables
                var countce = 0;
                var countts = 0;
                var countes = 0;
                var countst = 0;

                // iterate over each policy
                for ( var k in json) {
			
                	var active = "";
                    if(json[k].active == true) { var active = '<span style="font-weight: bold; color: lightgreen;">Active</span>'; } else { var active = '<span style="font-weight: bold; color: #8C1700;">Not active</span>'; }

                    var activecheckbox = "";
                    if(json[k].active == true) { var activecheckbox = '<input type="radio" name="active" id="r_true" value="true" style="float: left;" checked> <label style="float: left;" for="r_true">Yes</label> <input style="float: left;" type="radio" id="r_false" name="active" value="false"> <label style="float: left;" for="r_false">No</label>'; } else { var activecheckbox = '<input type="radio" id="r_true" style="float: left;" name="active" value="true"> <label style="float: left;" for="r_true">Yes</label> <input type="radio" style="float: left;" name="active" id="r_false" value="false" checked> <label style="float: left;" for="r_false">No</label>'; }
                    
                    // Append policy to view
                    $('.policies')
                    .append(
                    '<div class="policy_box">'
                    	+ '<form id="submit" action="PersistPolicy" method="post">'
                    	+ '<div class="inner_section"><span class="headline line">Policy Information</span> (<b>Id: '
                        	+ json[k].id
                        	+ '<input type="hidden" id="id" name="id" value="' + json[k].id + '"></b>, ' + active + ')'
                        + '</div>'
                        + '<div class="inner_section">'
                        	+ '<div style="float: left;">Is active: </div>' + activecheckbox
                        	+ '<br /><br />Name: <input type="text" class="required" name="name" id="name" value="' + json[k].name + '"> '
                        	+ 'From: <input type="text" id="fromTime" name="fromTime" style="width: 50px;" value="' + json[k].interval.fromTime + '"> '
                        	+ 'To: <input type="text" id="toTime" name="toTime" style="width: 50px;" value="' + json[k].interval.toTime + '"><br /><br />'
                        	+ 'Description: <textarea class="field required" style="width: 500px;" id="description" type="text" name="description">'
                        	+ json[k].description
                        	+ '</textarea>'
                        + '</div>'
                        //+ '<textarea type="text" style="width: 400px; height: 400px;" id="policy1" name="policy1">' + JSON.stringify(json[k].policy) + '</textarea>'
                        + '<input type="hidden" id="policy" name="policy" value=\'' + JSON.stringify(json[k].policy) + '\'>'
                        + '<div class="inner_section"><span class="headline line">Policy Statements</span> (<a class="newstatement" id="newst-' + json[k].id + '" href="JavaScript:void(0);">+ New</a>)</div>'
                        + '<div class="statements"></div>'
                        + '<div class="inner_section"><input type="submit" value="Update" name="update" class="button"></div><div style="clear:both"></div></form>'
                        + '<div class="deletebutton deletepolicy" id="deletepolicy-' + json[k].id + '"></div>'
                    + '</div>');

                    // iterate over statements 
                    // clean up array before populating
                    json[k].policy.statements = json[k].policy.statements.filter(function(e){return e});
                        
                    for ( var l in json[k].policy.statements) {
                        // statement type
                        var con_type = json[k].policy.statements[l].type;
                        
                        $('.statements')
                        .append(
                        		'<div class="statement" id="statement_' + l + '">'
                        			+ '<a href="Javascript:void(0);"><div class="statement_headline" id="st' + l +'"><div class="statement_inner_headline">Statement ' + l + '</div><div class="deletebutton deletestatement" id="deletest-' + json[k].id + '-' + l + '"></div></div></a>'
                        			+ '<div class="statement_content" id="st' + l + '_content">'
                        				+ '<div class="inner_section">'
											
                        					// If 
	                                        + '<div class="inner_section"><span class="headline2">IF Values </span>(<a class="newif" id="' + json[k].id + '-' + l +'" href="JavaScript:void(0);">+ New</a>)</b></div>'
	                                        + '<div id="if-' + json[k].id + '-' + l + '" class="inner_section">'
	                                        	+ '<div style="clear: both;"></div>'
	                                        + '</div>'
	                                        
	                                        // Then
	                                        + '<div class="inner_section"><span class="headline2">THEN Values </span>(<a class="newthen" id="' + json[k].id + '-' + l + '" href="JavaScript:void(0);">+ New</a>)</b></div>'
					                        + '<div id="then-' + json[k].id + '-' + l + '" class="inner_section">'
					                        	+ '<div style="clear: both;"></div>'
					                        + '</div>'
					                        
					                        // Else
					                        + '<div class="inner_section"><span class="headline2">ELSE Values </span>(<a class="newelse" id="' + json[k].id + '-' + l +'" href="JavaScript:void(0);">+ New</a>)</b></div>'
					                        + '<div id="else-' + json[k].id + '-' + l + '" class="inner_section">'
					                        	+ '<div style="clear: both;"></div>'
					                        + '</div>'
					                        
                        				+ '</div>'
                        			+ '</div>'
                        		+ '</div');
					
                        // iterate over conditionalExpressions 
                        json[k].policy.statements[l].data.conditionalExpressions = json[k].policy.statements[l].data.conditionalExpressions.filter(function(e){return e});
                        
                        for ( var m in json[k].policy.statements[l].data.conditionalExpressions) {

                            // Append to policy
                            $('#if-'+ json[k].id + '-' + l)
                            .prepend('<div id="if-' + json[k].id + '-' + l + '-' + m +'" class="inner_inner_section if"><div><b>if-' + json[k].id + '-' + l + '-' + m +'</b></div>'
                            //+ json[k].policy.statements[l].data.conditionalExpressions[m].aValue.type
                                + '<input style="width:140px;" class="autocomplete_get required" type="text" id="if-sensorid-' + l + '-' + m + '" name="sensorid" value="' + json[k].policy.statements[l].data.conditionalExpressions[m].sensorId + '">'
                                + ', <select id="if-operator-' + l + '-' + m + '" name="operator">'
                                + operator(json[k].policy.statements[l].data.conditionalExpressions[m].operator)
                                + '</select>, <input type="text" id="if-datafloatvalue-' + l + '-' + m + '" name="datafloatvalue" style="width:30px;" value="'
                                + json[k].policy.statements[l].data.conditionalExpressions[m].aValue.data.floatValue
                                + '">, '
                                + '<select><option value="' + json[k].policy.statements[l].data.conditionalExpressions[m].prefixOperator + '" selected>' + json[k].policy.statements[l].data.conditionalExpressions[m].prefixOperator + '</option></select>'
                                + '<div class="deletebutton deleteif" id="deleteif-' + json[k].id + '-' + l + '-' + m +'"></div>'
                                + '</div>'
                        );
								
                            countce++;
                        }
                        // iterate over thenStatements
                        // clean up array before populating
                        json[k].policy.statements[l].data.thenStatements = json[k].policy.statements[l].data.thenStatements.filter(function(e){return e});
                        
                        for (var m in json[k].policy.statements[l].data.thenStatements) {

                            recursiveThen(json, k, l, m);
                                
                            countts++;
                        }
                        // iterate over elseStatements 
                        // clean up array before populating
                        json[k].policy.statements[l].data.elseStatements = json[k].policy.statements[l].data.elseStatements.filter(function(e){return e});
                        
                        for (var m in json[k].policy.statements[l].data.elseStatements) {
				   			
                            // Append to policy
                            $('#else-'+ json[k].id + '-' + l)
                            .prepend('<div id="else-' + json[k].id + '-' + l + '-' + m +'" class="inner_inner_section else"><div><b>else-' + json[k].id + '-' + l + '-' + m +'</b></div>'
                                //+ json[k].policy.statements[l].data.elseStatements[m].type
                                
                                + 'Set: <input style="width:140px;" type="text" class="autocomplete_set required" id="else-sensorid-' + l + '-' + m + '" name="sensorid" value="' + json[k].policy.statements[l].data.elseStatements[m].data.sensorID + '">, '
                                + 'To: <select id="else-datafloatvalue-' + l + '-' + m + '" name="datafloatvalue">'
                                + setto(json[k].policy.statements[l].data.elseStatements[m].data.aValue.floatValue)
                                + '</select>'
                                + '<div class="deletebutton deleteelse" id="deleteelse-' + json[k].id + '-' + l + '-' + m +'"></div>'
                                + '</div>'
                        );
                            countes++;
                        }
                        
                        countst++;

                    }

                    $(".statements").delegate(".statement_headline", "click", function (){
                        var contentPanelId = jQuery(this).attr("id");
                        var alter = contentPanelId + "_content";
                        $("#" + alter).toggle();
                    });
                    
                    jQuery(".newstatement").click(function() {
                        
                        var st = json[k].policy.statements.length;
                        
                        // Alter POLICY OBJECT
                        json[k].policy.statements.push({type : 'dk.itu.policyengine.domain.IfStatement', data : {conditionalExpressions : [], thenStatements : [], elseStatements : [] }});
                        
                        $('.statements')
                        .append('<div class="statement" id="statement_' + st + '">'
            			+ '<a href="Javascript:void(0);"><div class="statement_headline" id="st' + st + '"><div class="statement_inner_headline">Statement ' + st + '</div><div class="deletebutton deletestatement" id="deletest-' + json[k].id + '-' + st + '"></div></div></a>'
            			+ '<div class="statement_content" id="st' + st + '_content">'
            				+ '<div class="inner_section">'
								
            					// If 
                                + '<div class="inner_section"><span class="headline2">IF Values </span>(<a class="newif" id="' + json[k].id + '-' + st + '" href="JavaScript:void(0);">+ New</a>)</b></div>'
                                + '<div id="if-' + json[k].id + '-' + st + '" class="inner_section">'
                                	+ '<div style="clear: both;"></div>'
                                + '</div>'
                                
                                // Then
                                + '<div class="inner_section"><span class="headline2">THEN Values </span>(<a class="newthen" id="' + json[k].id + '-' + st + '" href="JavaScript:void(0);">+ New</a>)</b></div>'
		                        + '<div id="then-' + json[k].id + '-' + st + '" class="inner_section">'
		                        	+ '<div style="clear: both;"></div>'
		                        + '</div>'
		                        
		                        // Else
		                        + '<div class="inner_section"><span class="headline2">ELSE Values </span>(<a class="newelse" id="' + json[k].id + '-' + st + '" href="JavaScript:void(0);">+ New</a>)</b></div>'
		                        + '<div id="else-' + json[k].id + '-' + st + '" class="inner_section">'
		                        	+ '<div style="clear: both;"></div>'
		                        + '</div>'
		                        
            				+ '</div>'
            			+ '</div>'
            		+ '</div');
						
                    });
					
                    $(".statements").delegate(".newif", "click", function (){
                        var contentPanelId = jQuery(this).attr("id");
                        var alter = "if-" + contentPanelId;

                        var n = contentPanelId.split("-");
                        
                        var ce = json[k].policy.statements[n[1]].data.conditionalExpressions.length;
                        
                        // Alter POLICY OBJECT
                        json[k].policy.statements[n[1]].data.conditionalExpressions.push({prefixOperator : 'AND', aValue : {type: 'dk.itu.policyengine.domain.FloatValue', data: { floatValue : '20' }}, operator:'LESS_THAN', sensorId : 'environment.temp'});
                        
                        $('#' + alter)
                        .prepend('<div id="if-' + json[k].id + '-' + n[1] + '-' + ce +'" class="inner_inner_section if"><div><b>if-' + json[k].id + '-' + n[1] + '-' + ce +'</b></div>'

                            + '<input class="autocomplete_get required" id="if-sensorid-' + n[1] + '-' + ce + '" style="width:140px;" type="text" name="sensorid" value="' + json[k].policy.statements[n[1]].data.conditionalExpressions[ce].sensorId + '">'
                            + ', <select id="if-operator-' + n[1] + '-' + ce + '" name="operator">'
                            + operator(json[k].policy.statements[n[1]].data.conditionalExpressions[ce].operator)
                            + ', </select>, <input id="if-datafloatvalue-' + n[1] + '-' + ce + '" type="text" name="datafloatvalue" style="width:30px;" value="'
                            + json[k].policy.statements[n[1]].data.conditionalExpressions[ce].aValue.data.floatValue
                            + '">, '
                            + '<select><option value="' + json[k].policy.statements[n[1]].data.conditionalExpressions[ce].prefixOperator + '" selected>' + json[k].policy.statements[n[1]].data.conditionalExpressions[ce].prefixOperator + '</option></select>'
                            + '<div class="deletebutton deleteif" id="deleteif-' + json[k].id + '-' + n[1] + '-' + ce +'"></div>'
                            + '</div>'
                    	);
                        
                        // Enable autocomplete
                        $( ".autocomplete_get" ).autocomplete({
                            source: get_sensors
                          });
						
                    });
                    
                    $(".statements").delegate(".newthen", "click", function (){
                        var contentPanelId = jQuery(this).attr("id");
                        var alter = "then-" + contentPanelId;

                        var n = contentPanelId.split("-");
                        
                        var ts = json[k].policy.statements[n[1]].data.thenStatements.length;
                        
                        // Alter POLICY OBJECT 
                        json[k].policy.statements[n[1]].data.thenStatements.push({type: 'dk.itu.policyengine.domain.SetStatement', data : {aValue: { floatValue : '0' }, sensorID : 'room-6-ac-6-gain'}});
                        
                        $('#' + alter)
                        .prepend('<div id="then-' + json[k].id + '-' + n[1] + '-' + ts +'" class="inner_inner_section then"><div><b>then-' + json[k].id + '-' + n[1] + '-' + ts +'</b></div>'
                        		 + 'Set: <input style="width:140px;" type="text" id="then-sensorid-' + n[1] + '-' + ts + '" class="autocomplete_set required" name="sensorid" value="' + json[k].policy.statements[n[1]].data.thenStatements[ts].data.sensorID + '">, '
                        		 + 'To: <select id="then-datafloatvalue-' + n[1] + '-' + ts + '" name="datafloatvalue">'
                                 + setto(json[k].policy.statements[n[1]].data.thenStatements[ts].data.aValue.floatValue)
                                 + '</select>'
                                + '<div class="deletebutton deletethen" id="deletethen-' + json[k].id + '-' + n[1] + '-' + ts +'"></div>'
                                + '</div>'
                    	);
                        
                     	// Enable autocomplete
                        $( ".autocomplete_set" ).autocomplete({
                            source: set_sensors
                          });
						
                    });
                    
                    $(".statements").delegate(".newelse", "click", function (){
                        var contentPanelId = jQuery(this).attr("id");
                        var alter = "else-" + contentPanelId;

                        var n = contentPanelId.split("-");
                        
                        var es = json[k].policy.statements[n[1]].data.elseStatements.length;
                        
                        // Alter POLICY OBJECT 
                        json[k].policy.statements[n[1]].data.elseStatements.push({type: 'dk.itu.policyengine.domain.SetStatement', data : {aValue: { floatValue : '0' }, sensorID : 'room-6-ac-6-gain'}});
                        
                        $('#' + alter)
                        .prepend('<div id="else-' + json[k].id + '-' + n[1] + '-' + es +'" class="inner_inner_section else"><div><b>else-' + json[k].id + '-' + n[1] + '-' + es +'</b></div>'
                        		 + 'Set: <input style="width:140px;" type="text" id="else-sensorid-' + n[1] + '-' + es + '" class="autocomplete_set required" name="sensorid" value="' + json[k].policy.statements[n[1]].data.elseStatements[es].data.sensorID + '">, '
                        		 + 'To: <select id="else-datafloatvalue-' + n[1] + '-' + es + '" name="datafloatvalue">'
                                 + setto(json[k].policy.statements[n[1]].data.elseStatements[es].data.aValue.floatValue)
                                 + '</select>'
                                + '<div class="deletebutton deleteelse" id="deleteelse-' + json[k].id + '-' + n[1] + '-' + es +'"></div>'
                                + '</div>'
                    	);
						
                     	// Enable autocomplete
                        $( ".autocomplete_set" ).autocomplete({
                            source: set_sensors
                          });
                    });
                    
                 // Delete statement function with delegate events
                    $(".statements").delegate(".deletestatement", "click", function (){
                    	
	                    	var contentPanelId = jQuery(this).attr("id");
	
	                        var n = contentPanelId.split("-");
	
	                        // Alter POLICY OBJECT
	                        delete json[k].policy.statements[n[2]];
	                        
	                        // Update GUI
	                        $('#statement_' + n[2]).remove();
							
                    	});
                    
                    // Delete if function with delegate events
                    $(".statements").delegate(".deleteif", "click", function (){
                    	
	                    	var contentPanelId = jQuery(this).attr("id");
	
	                        var n = contentPanelId.split("-");
	
	                        // Alter POLICY OBJECT
	                        delete json[k].policy.statements[n[2]].data.conditionalExpressions[n[3]];
	                        
	                        // Update GUI
	                        $('#if-' + n[1] + '-' + n[2] + '-' + n[3]).remove();

                    	});
                    
                 	// Delete then function with delegate events
                    $(".statements").delegate(".deletethen", "click", function (){
                    	
                    	var contentPanelId = jQuery(this).attr("id");

                        var n = contentPanelId.split("-");

                        // Alter POLICY OBJECT
                        delete json[k].policy.statements[n[2]].data.thenStatements[n[3]];
                        
                        // Update GUI
                        $('#then-' + n[1] + '-' + n[2] + '-' + n[3]).remove();


                	});
                    
                 	// Delete else function with delegate events
                    $(".statements").delegate(".deleteelse", "click", function (){
                    	
                    	var contentPanelId = jQuery(this).attr("id");

                        var n = contentPanelId.split("-");

                        // Alter POLICY OBJECT
                        delete json[k].policy.statements[n[2]].data.elseStatements[n[3]];
                        
                        // Update GUI
                        $('#else-' + n[1] + '-' + n[2] + '-' + n[3]).remove();


                	});
                    
                 	// Delete else function with delegate events
                    $(".policy_box").delegate(".deletepolicy", "click", function (){
                    	
                    	var contentPanelId = jQuery(this).attr("id");

                        var n = contentPanelId.split("-");

                        // Confirm box
                        var retVal = confirm("Are you sure you want to delete this policy ?");
                        if( retVal == true ){
                        	
                        	// delete
                            window.location = "/test/DeletePolicy?id=" + n[1]
                        	
                     	  return true;
                        }else{
                           
                     	  return false;
                        }

                	});
                    
					$("form#submit").submit(function() {
                    	
                    	// for each statement
                    	for ( var l in json[k].policy.statements) {
                    		// for each conditional statement
                        	for ( var m in json[k].policy.statements[l].data.conditionalExpressions) {
                        		// update each value in the object
								
                        		json[k].policy.statements[l].data.conditionalExpressions[m].aValue.data.floatValue = $('#if-datafloatvalue-' + l + '-' + m).val();
                        		json[k].policy.statements[l].data.conditionalExpressions[m].operator = $('#if-operator-' + l + '-' + m).val();
                        		json[k].policy.statements[l].data.conditionalExpressions[m].sensorId = $('#if-sensorid-' + l + '-' + m).val();
                        		
                        	}
                        	for ( var m in json[k].policy.statements[l].data.thenStatements) {
                        		// update each value in the object
                        		if (json[k].policy.statements[l].data.thenStatements[m].type == "dk.itu.policyengine.domain.IfStatement") {
                        			// We dont support nested in GUI
                        		} else {
                        		json[k].policy.statements[l].data.thenStatements[m].data.aValue.floatValue = $('#then-datafloatvalue-' + l + '-' + m).val();
                        		json[k].policy.statements[l].data.thenStatements[m].data.sensorID = $('#then-sensorid-' + l + '-' + m).val();
                        		}
                        	}
                    		for ( var m in json[k].policy.statements[l].data.elseStatements) {
                        		// update each value in the object
								
                        		json[k].policy.statements[l].data.elseStatements[m].data.aValue.floatValue = $('#else-datafloatvalue-' + l + '-' + m).val();
                        		json[k].policy.statements[l].data.elseStatements[m].data.sensorID = $('#else-sensorid-' + l + '-' + m).val();
                        		
                        	}
                    	}
                    	
                    	// Set Policy hidden field value to new updated object
                    	$('#policy').val(JSON.stringify(json[k].policy));
                        
                    	if($("form#submit").valid() == true) {
							
                    		// if valid submit!
                    		
                    		// Test
                    		//console.log(JSON.stringify(json[k].policy));
                    		//return false;
                        } else { return false; }
                        
                    });

                    
                    $('#fromTime').timepicker();
                    $('#toTime').timepicker();
                    $( ".autocomplete_get" ).autocomplete({
                        source: get_sensors
                      });
                    $( ".autocomplete_set" ).autocomplete({
                        source: set_sensors
                      });

                }
                
                
            }
            
            function recursiveThen(json, k, l, m)
            {

                // check for nested statements 
                if (json[k].policy.statements[l].data.thenStatements[m].type == "dk.itu.policyengine.domain.IfStatement") {

                    /*// iterate over nested conditionalExpressions
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
                    }*/
                } else {
		   				
                    // Append to policy (non nested) 
                    $('#then-'+ json[k].id + '-' + l)
                    .prepend('<div id="then-' + json[k].id + '-' + l + '-' + m +'" class="inner_inner_section then"><div><b>then-' + json[k].id + '-' + l + '-' + m +'</b></div>'
                        //+ json[k].policy.statements[l].data.thenStatements[m].type
                        
                        + 'Set: <input style="width:140px;" type="text" class="autocomplete_set required" id="then-sensorid-' + l + '-' + m + '" name="sensorid" value="' + json[k].policy.statements[l].data.thenStatements[m].data.sensorID + '">, '
                        + 'To: <select id="then-datafloatvalue-' + l + '-' + m + '" name="datafloatvalue">'
                        + setto(json[k].policy.statements[l].data.thenStatements[m].data.aValue.floatValue)
                        + '</select>'
                        + '<div class="deletebutton deletethen" id="deletethen-' + json[k].id + '-' + l + '-' + m +'"></div>'
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
        }); 
        </script>   
    </head>
    <body>
        <div class="header">
            <div class="top">Policy Engine Administration</div>
            <div class="menu"><a id="all" href="/test/">All policies</a> | <a id="new" href="/test/newpolicy.jsp">Create new policy</a></div>
        </div>
        <div class="policies">

        </div>
    </body>
</html>