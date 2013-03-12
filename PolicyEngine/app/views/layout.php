<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<base href="<?php echo myUrl('',true)?>" />
<title><?php echo $GLOBALS['sitename']?></title>
<LINK REL=StyleSheet HREF="/style.css" TYPE="text/css" MEDIA=screen>
<link href='http://fonts.googleapis.com/css?family=Open+Sans' rel='stylesheet' type='text/css'>

</head>
<body>
<div id="wrapper">





<h1>Policy Engine</h1>
<div id="nav" role="navigation">
<ul class="menu">
<li><a href="<?php echo myUrl(); ?>">Main</a></li>
<li><a href="<?php echo myUrl('policy/list'); ?>">Policies</a></li>
<li><a href="google.com">Google</a></li>
</ul>
</div><!-- #nav -->





<div id="content">
<?php
if (isset($body) && is_array($body))
  foreach ($body as $html)
    echo "$html\n";
if (isset($datetime))
  echo '<p>The time now is '.$datetime.'</p>';
?>
</div>
<div id="footer">
<p>Policy Engine | A Global Software Developement Project</p>
<p>Copyright &copy; 2013 | IT University of Copenhagen | Rued Langgaards Vej 7, DK-2300 København S.</p>
</div><!-- end of #footer -->

</div><!-- end of #wrapper -->
</body>
</html>