<?php
require_once "session.inc";
if(check_login()){
  //echo "login:".$_SESSION['uid'];
  Header("Location: sss.html");
}else
 Header("Location:login.php");
?>