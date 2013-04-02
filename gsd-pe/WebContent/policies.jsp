<html>
    <head>
        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
        <title>GETTING STARTED WITH BRACKETS</title>
        <meta name="description" content="An interactive getting started guide for Brackets.">
        <link rel="stylesheet" href="style.css">
        <script src="http://code.jquery.com/jquery-1.9.1.js"></script>
        <script src="http://code.jquery.com/ui/1.10.2/jquery-ui.js"></script>
        <script>
            jQuery(function(){
                jQuery('a.add-link').click(function(event){
                    event.preventDefault();
                    var $new_tr = jQuery('<tr style="display: none;"><td><input type="checkbox" name="check"></td><td><button type="delete-button">Delete</button></td><td>FOR</td><td><select><option value="all-floors">* (all)</option><option value="floor-0">Floor 0</option><option value="floor-1">Floor 1</option><option value="floor-2">floor 2</option><option value="floor-3">Floor 3</option></select></td><td><select><option value="all-rooms">* (all)</option><option value="room-0">Room 0</option><option value="room-1">Room 1</option><option value="room-2">Room 2</option><option value="room-3">Room 3</option></select></td><td>IF</td><td><select><option value="temp">Temp</option><option value="weather">Whether</option></select> IS <select><option value="greater-than">&gt;</option><option value="less-than">&lt;</option><option value="equal-to">&#61;</option><option value="not-equal-to">!&#61;</option></select> <input type="text" class="if-is-input" name="if-is-value"></td><td>THEN SET</td><td><select><option value="ac">Aircondition</option><option value="heator">Blind</option><option value="ac">Heator</option><option value="heator">Light</option></select></td><td>&#61;</td><td><select><option value="on">ON</option><option value="off">OFF</option></select></td><td><button type="change-button">Change</button></td></tr>'); 
                    //jQuery('table.policy-list').append($newRow);
                    jQuery('#policies').append($new_tr);
                    jQuery($new_tr.show('fast'));
                    
                });
            });
            
            jQuery(function(){
                jQuery('a.add-more-services-link').click(function(event){
                    event.preventDefault();
                    var $new_service = jQuery('<div class="service" style="display: none;"><select><option value="temp">Temp</option><option value="weather">Whether</option></select> IS <select><option value="greater-than">&gt;</option><option value="less-than">&lt;</option><option value="equal-to">&#61;</option><option value="not-equal-to">!&#61;</option></select> <input type="text" class="if-is-input" name="if-is-value"></div>'); 
                    //jQuery('table.policy-list').append($newRow);
                    jQuery('.services').append($new_service);
                    jQuery($new_service.show('fast'));
                    
                });
            });
        </script>
    </head>
    <body>
        <div class="boxed">
            <h1>Policies</h1>
            <p>Add, delete or modify policies</p>
            <hr />
            <table class="policy-list" id="policies" cellpadding="5px" border="1" style="border-collapse:collapse;">
                <tr>
                    <td colspan="2"><button type="delete-selected-button">Delete selected</button></td>
                </tr>
                <tr id="new">
                    <td><input type="checkbox" class="check-input" name="check"></td>
                    <td><button type="delete-button">Delete</button></td>
                    <td>FOR</td>
                    <td>
                        <select>
                            <option value="all-floors">* (all)</option>
                            <option value="floor-0">Floor 0</option>
                            <option value="floor-1">Floor 1</option>
                            <option value="floor-2">floor 2</option>
                            <option value="floor-3">Floor 3</option>
                        </select>
                    </td>
                    <td>
                        <select>
                            <option value="all-rooms">* (all)</option>
                            <option value="room-0">Room 0</option>
                            <option value="room-1">Room 1</option>
                            <option value="room-2">Room 2</option>
                            <option value="room-3">Room 3</option>
                        </select>
                    </td>
                    <td>IF<br /><a href="#" class="add-more-services-link">+</a></td>
                    <td class="services">
                        <div class="service">
                            <select>
                                <option value="temp">Temp</option>
                                <option value="weather">Whether</option>
                            </select>
                            IS
                            <select>
                                <option value="greater-than">&gt;</option>
                                <option value="less-than">&lt;</option>
                                <option value="equal-to">&#61;</option>
                                <option value="not-equal-to">!&#61;</option>
                            </select>
                            <input type="text" class="if-is-input" name="if-is-value">
                        </div>
                    </td>
                    <td>THEN SET</td>
                    <td>
                        <select>
                            <option value="ac">Aircondition</option>
                            <option value="heator">Blind</option>
                            <option value="ac">Heator</option>
                            <option value="heator">Light</option>
                        </select>        
                    </td>
                    <td>&#61;</td>
                    <td>
                        <select>
                            <option value="on">ON</option>
                            <option value="off">OFF</option>
                        </select>        
                    </td>
                    <td><a href="" class="add-link"><button type="go-button">Add</button></a></td>
                    <!-- end -->
                </tr>


                
            </table>
        </div><!-- end of boxed -->
    </body>
</html>