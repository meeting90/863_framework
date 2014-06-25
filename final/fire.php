<?php
require_once "Model.class.php";

	$ans1= array();
	$ans2= array();
	
	for($sid=1;$sid<11;$sid++){
		$sm = new Model('service');
		$tm = $sm->queryBySid($sid);
		$sts = new Model('service_data');
	  $tasks = $sts->queryBySid($sid);
	  $eps = new Model('evaluate_parameter');
		$tm2 = $eps->queryByUid_Sid(55, $sid);
		$res = $tm2[0]->threshold;
	  if($tasks != false){
	      $trusts = 0;
	      $times = 0;
	      foreach($tasks as $task){
	      	if ($task->data < $res)
	        	$trusts++;
	        	$times += $task->data;
	      }
		    $ans1[$tm[0]->name] = $trusts / count($tasks);
		   
		    $ans2[$tm[0]->name] = $times / count($tasks);
		  }
	  }
	  foreach($ans1 as $key => $value){
			echo $key." ".$value."<br/>";
		}
		foreach($ans2 as $key => $value){
			echo $key." ".$value."<br/>";
		}
?>