<?php
	echo $_SERVER['PHP_SELF']."<br/>";
	echo $_SERVER['SERVER_PROTOCOL']."<br/>";
	echo $_SERVER['REQUEST_TIME']."<br/>";
	echo $_SERVER['argv']."<br/>";
	echo $_SERVER['SERVER_NAME']."<br/>";
	echo $_SERVER['SERVER_SOFTWARE']."<br/>";
	echo $_SERVER['REQUEST_METHOD']."<br/>";
	echo $_SERVER['REQUEST_STRING']."<br/>";
	echo $_SERVER['DOCUMENT_ROOT']."<br/>";
	echo $_SERVER['HTTP_ACCEPT']."<br/>";
	echo $_SERVER['HTTP_HOST']."<br/>";
	echo $_SERVER['HTTP_REFERER']."<br/>";
	echo $_SERVER['HTTP_USER_AGENT']."<br/>";
	echo $_SERVER['REMOTE_ADDR']."<br/>";
	echo $_SERVER['HTTP_HOST'].$_SERVER['PHP_SELF']."<br/>";
	
	$a = simplexml_load_string("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?><a/>");
	$a->addChild('c', 'hello');
	
	print( $a->asXML() );
?>