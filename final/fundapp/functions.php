<?php
	require_once "Model.class.php";
	require_once "WsdlAnalyze.class.php";
	
	function getServiceById($id){
		$m = new Model("service");
		$results = $m->queryById($id);
		if($results){
				return $results[0];
		}
		return false;
	}
	function getServiceByName($name){
		$m = new Model("service");
		$results = $m->queryByName((string)$name);
		
		if($results){
				return $results[0];
		}
		return false;
	}
	/*return not involved root service*/
	function getFamilyServices($rootId){
		$m = new Model('service');
		$results = $m->queryByPrimaryId($rootId);
		
		if($results){
			return $results;
		}
		return false;
	}
	function showServiceName($workflow_name, $partnerLinkType){
	  $file = $workflow_name."Artifacts.wsdl";
	  $wa = new WsdlAnalyze($file);
	  $servname = $wa->getServiceName($partnerLinkType, $operation);
	  if($servname){
			return $servname;
	  }
	  return false;
	}
	function strip($str){
	  $index = strrpos($str, ":");
	  if(!$index)
	    return $str;
	  return substr($str, $index+1);
	}
	
	function calculate_trust($uid, $sid){
		
    	$eps = new Model('evaluate_parameter');
			$res = $eps->queryByUid_Sid($uid, $sid);
		//var_dump($res);
	    $params = array('alpha' => 0.8,
	            				'start' => 0,
	            				'end' => $res[0]->threshold);

	    $sts = new Model('service_data');
	    $tasks = $sts->queryBySid($sid);
	    
	    if($tasks != false){
	      $trusts = array();
	      foreach($tasks as $task){
	        $trusts[] = $task->data;
	      }
//	      print_r($trusts);echo "<br/>";print_r($params);echo "<br/>";
	      $st = new TrustForcast($trusts, $params['alpha']);
	      $res = $st->forcast($params['start'], $params['end']);
	      return $res;
	    } else {
	    	return false;
	    }
    }
  function calculate_family_trust($uid, $sid){
  	$res = calculate_trust($uid, $sid);
		if($res){
			$backs = getFamilyServices($sid);
    	if($backs){
    		$back_trusts = array();
    		foreach($backs as $back){
					$back_trusts[] = calculate_trust($uid, $sid);
    		}
    	}
    	$main_trust = calculate_trust($uid, $sid);
			if(isset($back_trusts)){
				$tmp = 1;
				foreach($back_trusts as $back_trust){
					$tmp = $tmp * (1 -$back_trust);
				}
				$tmp = $tmp * (1 - $main_trust);
				return 1 - $tmp;
			} else{
				return $main_trust;
			}	
		}
  }
  
  /*$datas=array(array('main_name' => 'xxx', 'back_address' => 'yyy'),
  							array('main_name' => 'xxxx', 'back_address' => 'yyyy'))*/
  function updateBSListXML($workflow_name, $datas){
  	$m = new Model('bslist');
		$tmp = $m->queryByName($workflow_name);
	
		$xml = simplexml_load_string(stripslashes($tmp[0]->list));
  	if(! $xml){
  		$services = simplexml_load_string("<?xml version=\"1.0\" encoding=\"UTF-8\"?><Services></Services>");
  	}else {
  		$services = $xml;
  	}
  	/*$services = simplexml_load_string("<?xml version=\"1.0\" encoding=\"UTF-8\"?><Services></Services>");*/
  	//$services->addChild('Services');
  	
  	foreach($datas as $data){
  		//$services = $xml->Services;
  		//print_r($data);echo "<br/>";
  		//var_dump($services);
  		$service = $services->addChild('Service');
  		$service->addAttribute('name', $data['main_name']);
  		$address = $service->addChild('address', urlencode($data['back_address']));
  		
  		
  	}
  	
  	$m = new Model('bslist');
  	$data = array('list' => addslashes($services->asXML()));
  	
  	$options = array('where' => array('name' => $workflow_name) );
  	
  	return $m->save($data, $options);
  }
?>