<?php
ini_set('session.use_cookies', 1);
ini_set('session.use_only_cookies', 1);
        session_start();
        ob_start();
        $code = mt_rand(0, 100000);
        $_SESSION['code'] = $code;

function check_login(){
  if(!isset($_SESSION['uid']) || $_SESSION['uid']==''){
	
		return false;
	}
	
  
  return true;
}
?>