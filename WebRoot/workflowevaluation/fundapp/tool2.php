<?php
if(isset($_GET['UserID']) && $_GET['UserID']!='' && isset($_GET['UserName']) && $_GET['UserName']!=''){
	session_start();
  ob_start();
  $_SESSION['uid'] = $_GET['UserID'];
  $_SESSION['name'] = $_GET['UserName'];
  setcookie('uid', $_GET['UserID']);
  setcookie('name', $_GET['UserName']);
  header("Location:./tool2.html");
}
?>