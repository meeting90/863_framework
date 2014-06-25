<?php
require_once "Model.class.php";
require_once "BpelNode.class.php";
require_once "AttributeBpel.class.php";
require_once "TrustForcast.class.php";
require_once "BpelXmlDes.class.php";
//require_once "functions.php";
// echo updateEvaParams(1, 'BalanceVerification1', 90, 0.5)."<br/>";
/*
Header("Content-type: text/xml");
showWsdlFile('Deduction.wsdl');*/

// showBpelJson('BankTwo.bpel');
function updateEvaParams($uid, $sid, $threshold, $lambda){
	//echo "$uid:$sid:$threshold:$lambda<br/>";
  $eps = new Model('evaluate_parameter');
  $data = array('threshold' => $threshold,
		'lambda' => $lambda);
  $options = array('where' => array('uid' => $uid,
				    'sid' => $sid));
  return $eps->save($data, $options);
}

/**return an object 
  usage like: 
  $t = (getEvaParams(1, 'BalanceVerification1'));
  echo $t->lambda;*/
function getEvaParams($uid, $sid){
  $eps = new Model('evaluate_parameter');
  $res = $eps->queryByUid_Sid($uid, $sid);
  
  if($res){
    return $res[0];
  }
}

function showAllWorkflow(){
	$m = new Model('workflow');
	$res = $m->queryByAll();
	
	if($res){
		$wfs = array();
		foreach($res as $r){
			$wfs[] = $r->workflowname;
		}
		return $wfs;
	}
	return false;
}
function showAllServices(){
	$m = new Model('service');
	$services = $m->queryByAll();
	$ret = array();
	if($services){
		foreach($services as $service){
			$ret[] = $service->name;
		}
		return json_encode($ret);
	}
	return false;
}
function setBackupService($workflowname, $main_id, $backup_ids){
	$m = new Model('service');
	//clear the old primary id information
	$results = $m->queryByPrimaryId($main_id);
	if($results){
		$clear = array();
		foreach($results as $result){
			if(!in_array($result->sid)){
				$clear[] = $result->sid;
			}
		}
		$data = array('primaryid' => 0);
		
		foreach($clear as $c){
			$options['where'] = array('sid' => $c);
			$m->save($data, $options);
		}
	}
	
	$data = array('primaryid' => $main_id);
	$options = array('where' => array());
	if($backup_ids){
		foreach($backup_ids as $backup_id){
			$options['where'] = array('sid' => $backup_id);
			$m->save($data, $options);
		}
	}
	$m = new Model('service');
	$tmp = $m->queryBySid($main_id);
	$main_name = $tmp[0]->name;
	
	$datas = array();
	foreach($backup_ids as $bid){
		$m = new Model('service');
		$tmp = $m->queryBySid($bid);
		
		$data = array('main_name' => $main_name,
								'back_address' => $tmp[0]->address);
		
		$datas[] = $data;
	}
	
	return updateBSListXML($workflowname, $datas);
}
function getBackupServiceList($workflowname){
	$m = new Model('bslist');
	$tmp = $m->queryByName($workflowname);
	
	return stripslashes($tmp[0]->list);
}
function getBackupService($main_id){
	
	$m = new Model('service');
	$results = $m->queryByPrimaryId($main_id);
	
	return $results;
}

function showServForcastInfo($sid, $uid){
  $info = array();
  $info[] = 0.8;
  $params = getEvaParams($uid, $sid);
  $info[] = $params->threshold;
  $info[] = $params->lambda;
}

function showServData($sid){
  $sts = new Model('service_data');
  $tasks = $sts->queryBySid($sid);
//  var_dump($tasks);
  if($tasks){
    $trusts = array();
    foreach($tasks as $task){
      $trusts[] = $task->data;
    }
    return json_encode($trusts);
  }else{
  	echo "not found sid=$sid<br/>";
  }
  return false;
}

function showServForcast($uid, $sid, $back_include=true){
	if(!$back_include)
		return json_encode( calculate_trust($uid, $sid) );
	else
		return json_encode( calculate_family_trust($uid, $sid) );
}

function showWsdlFile($filename){
  $wfq = new Model("wsdl_files");
  $tmp = $wfq->queryByFilename($filename);
  
  $file = $tmp[0]->contents;
  if($file){
    echo stripslashes($file);
  } else {
    echo "not find $filename";
  }
}
function showBpelFile($workflow_name){
	$m = new Model('workflow');
	$tmp = $m->queryByWorkflowname($workflow_name);
	if($tmp){
		$filename = $tmp[0]->bpelname;
	} else {
		die("not find bpel of workflow: $workflow_name");
	}
  $bfs = new Model('bpel_files');
  $tmp = $bfs->queryByFilename($filename);
  //echo $filename;
  $file = $tmp[0]->contents;
  //var_dump($file);
  if($file){
    return stripslashes($file);
  } else {
    echo "not find $filename";
    return false;
  }
}
function showAllBpelFiles(){
  $bfs = new Model('bpel_files');
  $files = $bfs->queryByAll();
  $filenames = array();
  if($files){
    foreach($files as $file)
      $filenames[] = $file->filename;
    return json_encode($filenames);
  }
  return false;
}

function showWorkflowEvaluation($uid, $workflow_name){
  $rootNode = searchBpel($uid,$workflow_name);
  //var_dump($rootNode);
  if($rootNode){
    $eva = $rootNode->evaluate($workflow_name, $uid);
    return $eva;
  }
  return false;
}

function showBpelServices($uid,$workflow_name){
  $rootNode = searchBpel($uid,$workflow_name);
//  var_dump($rootNode);
  $services = array();
  getServices($rootNode, $services);
//  var_dump($services);
//  echo "<br/>";
  $res = json_encode(array_values(array_flip(array_flip($services))));
//  var_dump($res);
  return $res;
  
}

function getServices($rootNode, &$services){
  foreach($rootNode->getChildNodes() as $node){
    if($node->getName() == 'invoke'){
      $attrs = $node->getAttributesList();
      //var_dump($attrs);echo "<br/>";
      foreach($attrs as $attr){
				if($attr->getAttrName() == 'servicename')
					if((string)$attr->getAttrValue() != "")
	  				$services[] = (string)$attr->getAttrValue();
      }
    }
    getServices($node, $services);
  }
}
function showBpelJson($uid,$workflow_name){
  $rootNode = searchBpel($uid,$workflow_name);
  
  if($rootNode){
    return trans2JSon($rootNode);
  }
  return false;
}
function trans2JSon($rootNode){
	
  return urldecode(json_encode($rootNode->copy2JsonNode()));
}

function searchBpel($uid,$workflow_name) {
	$bfs = new Model('bpel_files');
  $tmp = $bfs->queryByFilename($workflow_name.".bpel");
  $file = stripcslashes($tmp[0]->contents);
  if($file){
    $bpel = simplexml_load_string($file);
    //var_dump( $bpel );
    $ns = $bpel->getNamespaces(true);
    
    $rootNode = new BpelNode();
    $rootNode->setName("sequence");
  //  var_dump( $bpel->children($ns['bpel']) );
    search($uid,$workflow_name, $ns['bpel'], $bpel->children($ns['bpel'])->sequence, $rootNode);
    return $rootNode;
  }
  return $false;
}
function search($uid,$workflow_name, $namespace, $rootElement, $rootNode) {
	//var_dump( $rootElement->children($namespace) );
  foreach( $rootElement->children($namespace) as $child) {	
  	//echo $child->getName();
    if( $child->getName() == "sequence" ||
	$child->getName() == "invoke" ||
	$child->getName() == "reply" ||
	$child->getName() == "switch" ||
	$child->getName() == "case" ||
	$child->getName() == "otherwise" ||
	$child->getName() == "if" ||
	$child->getName() == "else" ||
	$child->getName() == "elseif" ||
  $child->getName() == "while" ||
  $child->getName() == "condition") {
  	//echo $child->getName()."::<br/>";
	  $node = new BpelNode();
	  $node->setName($child->getName());
	  if($child->getName() == "condition"){
	  	$node->setValue(addslashes((string)$child));
	  }
	  
	  foreach($child->attributes() as $attribute => $value){
	    $attr = new AttributeBpel();
	    $attr->setAttrName($attribute);
	    $attr->setAttrValue((string)$value);
	    $node->appendAttribute($attr);
	  }
	  if($child->getName() == "invoke"){
	    $attrs = $node->getAttributesList();
	    $partnerLink = '';
	    $operation = '';
	    foreach($attrs as $attr){
	      if($attr->getAttrName() == "partnerLink"){
					$partnerLink = $attr->getAttrValue();
	      }
	      if($attr->getAttrName() == "operation"){
					$operation = $attr->getAttrValue();
	      }
	    }
// 						echo $porttype ."  ". $operation."<br/>";
			
			$bxd = new BpelXmlDes($workflow_name);
			$partnerLinkType = $bxd->getPartnerLinkType($partnerLink);
			//echo "$workflow_name, $partnerLink, $partnerLinkType<br/>";
	    $servname = showServiceName($workflow_name, strip($partnerLinkType), strip($operation));
	    
// 	    echo $servname."<br/>";
 	    $eps = getEvaParams($uid, $servname);
// 	    $eps = getEvaParams(1, $servname);
                                          //echo $_SESSION['uid']." and ".$servname." threashold:".$eps['threashold']."</br>";
	    $params = array('alpha' => 0.8,
			    'start' => 0,
			    'end' => $eps->threshold);
	    $servtrust = showServForcast($servname, $params);
	    $node->setTrust($servtrust);
	    $attrnode = new AttributeBpel();
	    $attrnode->setAttrName("servicename");
	    $attrnode->setAttrValue($servname);
	    $node->appendAttribute($attrnode);
	  }
	  if($child->getName() != "sequence")
	  	$rootNode->appendChildNode($node);
//					echo $child->getName() . $child->count() . "<br />";
	  if($child->count() > 0){
	    if($child->getName() == "sequence")
	      search($uid,$workflow_name, $namespace, $child, $rootNode);
	    else 
	      search($uid,$workflow_name, $namespace, $child, $node);
	  }
	}
  }
}

/*$options = array('method' => xxx,
									 'parameter' => array('' => ''))*/
function webservice_run($options){
	
	
	$soapClient = new SoapClient("http://localhost/Filt/services/Filter?wsdl");
	//var_dump($soapClient->__getFunctions());
	
	$method = $options['method'];
	
	//var_dump($options['parameter']);
	$result = $soapClient->$method($options['parameter']);
	
	echo $result->doFiltReturn;
	
	
}
?>