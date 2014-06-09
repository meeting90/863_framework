<?php 
$link=mysqli_connect(); 
if(!$link) echo "fail!"; 
else echo "success!"; 
mysqli_close($link); 
?>