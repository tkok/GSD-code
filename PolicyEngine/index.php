<?php

//===============================================
// Debug
//===============================================
ini_set('display_errors', 'On');
error_reporting(E_ALL);

//===============================================
// mod_rewrite
//===============================================
//Please configure via .htaccess or httpd.conf
//===============================================
// KISSMVC Settings (please configure)
//===============================================
define('APP_PATH', 'app/'); //with trailing slash pls
define('WEB_DOMAIN', 'http://policy.engine'); //with http:// and NO trailing slash pls
define('WEB_FOLDER', '/'); //with trailing slash pls
//define('WEB_FOLDER','/kissmvc_simple/index.php/'); //use this if you do not have mod_rewrite enabled
define('VIEW_PATH', 'app/views/'); //with trailing slash pls
//===============================================
// Includes
//===============================================
require('kissmvc.php');

//===============================================
// Session
//===============================================
session_start();

//===============================================
// Globals
//===============================================
$GLOBALS['sitename'] = 'GSD Policy Engine';

//===============================================
// Functions
//===============================================
function myUrl($url = '', $fullurl = false) {
    $s = $fullurl ? WEB_DOMAIN : '';
    $s.=WEB_FOLDER . $url;
    return $s;
}

//===============================================
// Autoloading for Business Classes
//===============================================
// Assumes Model Classes start with capital letters and Helpers start with lower case letters
function __autoload($classname) {
    $a = $classname[0];
    if ($a >= 'A' && $a <= 'Z')
        require_once(APP_PATH . 'models/' . $classname . '.php');
    else
        require_once(APP_PATH . 'helpers/' . $classname . '.php');
}

//===============================================
// Database
//===============================================
//To release the database connection simply do:
//$GLOBALS['dbh']=null;
function getdbh() {
    if (!isset($GLOBALS['dbh']))
        try {
            //$GLOBALS['dbh'] = new PDO('sqlite:'.APP_PATH.'db/dbname.sqlite');
            $GLOBALS['dbh'] = new PDO('mysql:host=mysql2.gigahost.dk;dbname=webaholic_gsd', 'webaholic', 'Gh2kZuCwlpU5ZfpHQN4i');
        } catch (PDOException $e) {
            die('Connection failed: ' . $e->getMessage());
        }
    return $GLOBALS['dbh'];
}

//===============================================
// Start the controller
//===============================================
$controller = new Controller(APP_PATH . 'controllers/', WEB_FOLDER, 'main', 'index');