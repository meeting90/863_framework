<?php
require_once "session.inc";
if(check_login()){
  //echo "login:".$_SESSION['uid'];
  Header("Location: api.php");
}
?>