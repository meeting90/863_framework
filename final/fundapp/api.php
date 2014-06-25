<?php
	header("Content-Type: text/html; charset=utf-8");
	require_once "api_function.php";
	require_once "functions.php";
	require_once "session.inc";
	//check_login();
		
	if(isset($_GET['apiname']) && $_GET['apiname'] != ''){
	$apiname = $_GET['apiname'];
	
	
	if($apiname == "showworkflow"){
		Header("Content-type: text/xml");
		if(isset($_GET['workflowname']) && $_GET['workflowname'] != ''){
			echo showBpelFile($_GET['workflowname']);
		} else {
			echo "no file specified to show" . "<br/>";
		}

	} else if($apiname == "showwsdl"){
 		Header("Content-type: text/xml");
		if(isset($_GET['wsdlname']) && $_GET['wsdlname'] != ''){
			echo showWsdlFile($_GET['wsdlname']);
		} else {
			echo "no file specified to show <br/>";
		}

	} else if($apiname == "workflow2json"){
		if(isset($_GET['workflowname']) && $_GET['workflowname'] != ''){
			echo showBpelJson($_GET['workflowname']);
		} else {
			echo "no file specified to show" . "<br/>";
		}

	} else if($apiname == "servforcast"){
		if(isset($_GET['uid']) && $_GET['uid'] != '' &&
			isset($_GET['servname']) && $_GET['servname'] != ''){
				$s = getServiceByName($_GET['servname']);
				echo showServForcast($_GET['uid'], $s->sid);
		} else {
			echo "no user or service specified to forcast";
		}

	} else if($apiname == "evaluate"){
		if(isset($_GET['workflowname']) && $_GET['workflowname'] != ''){
			echo showWorkflowEvaluation($_SESSION['uid'], $_GET['workflowname']);
		} else {
			echo "no file specified to show" . "<br/>";
		}

	} else if($apiname == "evaparamupdate"){	//update evaluation parameter
		echo updateEvaParams($_GET['uid'], getServiceByName($_GET['sname'])->sid, $_GET['threshold'], $_GET['lambda']);

	} else if($apiname == "evaparaminfo"){		//get evaluation parameter
		$s = getServiceByName($_GET['sname']);
		$p = getEvaParams($_GET['uid'], $s->sid);
		$p->sname = $_GET['sname'];
		echo json_encode($p);

	} else if($apiname == "listservice"){
		echo showAllServices();

//	} else if($apiname == "listbpel"){
//		echo showAllBpelFiles();

	} else if($apiname == "listworkflow"){
		echo json_encode( showAllWorkflow() );
		
	} else if($apiname == "getservdata"){
		$s = getServiceByName($_GET['servname']);
		
		echo showServData($s->sid);

	} else if($apiname == "listworkflowservice"){
    echo showBpelServices($_GET['workflowname']);

  } else if($apiname == "servforcastinfo"){
   	echo showServForcastInfo(getServiceByName($_GET['servname'])->sid, $_SESSION['uid']);

  } else if($apiname == "setbackupservice"){
  	$main_sname = $_GET['mainservice'];
  	if(!isset($_GET['backupservice'])){
  		$backup_snames = false;
  	}
  	$backup_snames = explode(":", $_GET['backupservice']);
  	$s = getServiceByName($main_sname);
  	$main_id = $s->sid;
  	
  	if($backup_snames){
  		$backup_ids = array();
	  	foreach($backup_snames as $backup_sname){
	  		$backup_ids[] = getServiceByName($backup_sname)->sid;
	  	}
  	} else{
  		$backup_ids = false;
  	}
  	echo setBackupService($_GET['workflowname'], $main_id, $backup_ids);

  } else if($apiname == "getbackupservicelist"){
  	echo getBackupServiceList($_GET['workflowname']);
  	
  } else if($apiname == "getbackupservice"){
  	$main_sname = $_GET['mainservice'];
  	$s = getServiceByName($main_sname);
  	
  	$backup_services = getBackupService($s->sid);
  	
  	$res = array();
  	if($backup_services){
	  	foreach($backup_services as $backup_service){
	  		$service = new Model();
	  		$service->service_name = $backup_service->name;
	  		$service->service_address = $backup_service->address;
	  		$res[] = $service;
	  	}
  	}
  	echo json_encode($res);

  } else if($apiname == "addservice" ){
  	if(isset($_POST['service_name'])){
	  	$service_name = $_POST['service_name'];
	  	//upload wsdl file to server
	  	if($_FILES['wsdl']['error'] != 4){
	  		if($_FILES['wsdl']['type']=='image/gif'
	  				|| $_FILES['wsdl']['type']=='image/jpeg'
	  				|| $_FILES['wsdl']['type']=='image/pjpeg'){
	  					echo "Invalid file";
	  		} else if($_FILES['wsdl']['error'] > 0){
	  			echo "Error".$_FILES['wsdl']['error']."<br/>";
	  		} else {
	  			$m = new Model('wsdl_files');
	  			$data = addslashes(fread(fopen($_FILES['wsdl']['tmp_name'], "r"), filesize($_FILES['wsdl']['tmp_name'])));
	  			$datas = array((array('filename' => $_FILES['wsdl']['name'], 'contents' => $data)));
	  			$m->add($datas)."<br/>";
	  			
	  			
	  			$m = new Model('service');
	  			$data = array(array('name' => $service_name,
	  													'primaryid' => 0,
	  													'address' => "http://".$_SERVER['HTTP_HOST'].$_SERVER['PHP_SELF'].'?apiname=showwsdl&wsdlname='.$_FILES['wsdl']['name']
	  													));
	  			$m->add($data)."<br/>";
	  			echo "Done";
	  		}
	  	} else {	//use an exist wsdl
	  		$m = new Model('service');
	  		$data = array(array('name' => $service_name,
	  													'primaryid' => 0,
	  													'address' => ""
	  													));
	  		$m->add($data)."<br/>";
	  		echo "Done";
	  	}
  	}

  } else if($apiname == "updateserviceaddress"){
  	$s = new Model('service');
  	$data = array('address' => urldecode($_GET['address']));
  	$options = array('where' => array('sid' => getServiceByName($_GET['servicename'])->sid));
  	echo $s->save($data, $options);
 
  } else if($apiname == "addworkflow"){
  	if(isset($_POST['workflowname'])){
	  	$wf_name = $_POST['workflowname'];
	  	if($_FILES['bpel']['error'] > 0){
	  		echo "Error".$_FILES['bpel']['error']."<br/>";
	  	} else {
	  		$m = new Model('bpel_files');
	  		$data = addslashes(fread(fopen($_FILES['bpel']['tmp_name'], "r"), filesize($_FILES['bpel']['tmp_name'])));
	  		$datas = array((array('filename' => $_FILES['bpel']['name'], 'contents' => $data)));
	  		echo $m->add($datas)."<br/>";
	  		
	  		if(isset($_POST['referedwsdl']) && $_POST['referedwsdl'] != ''){
	  			$m = new Model('workflow');
	  			$datas = array(array('bpelname' => $_FILES['bpel']['name'],
	  													 'wsdllist' => $_POST['referedwsdl'],
	  													 'workflowname' => $wf_name));
					$m->add($datas);
	  		} else {
	  			$m = new Model('workflow');
	  			$datas = array(array('bpelname' => $_FILES['bpel']['name'],
	  													 'workflowname' => $wf_name));
					$m->add($datas);
					
					$bxd = new BpelXmlDes($_POST['workflowname']);
	  			$wsdllist = $bxd->getImportWsdls();
	  			$data = array('wsdllist' => $wsdllist);
	  			$options = array('where' => array('workflowname' => $wf_name));
	  			$m = new Model('workflow');
	  			$m->save($data, $options);
	  		}
	  	}
	  } 
  } else if($apiname == "addvisitlog"){
  	$m = new Model('visitlog');
  	$data = array(array('workflowname' => ($_GET['name']),
  											 'time'=> ($_GET['time']),
  											 'inf' => urldecode($_GET['log'])));
		$m->add($data);									 
  	
  } else if($apiname == "getvisitlog"){
  	$m = new Model("visitlog");
  	$res = $m->queryByWorkflowname($_GET['name']);
  	
  	if($res){
  		echo json_encode($res);
  	}
  } else if($apiname == "run"){
  	$options = array();
  	$options['method'] = 'doFilt';
  	
  	$m = new Model('workflow');
  	$tmp = $m->queryByWorkflowname($_GET['workflowname']);
  	$bpelname = $tmp[0]->bpelname;
  	$wsdllist = $tmp[0]->wsdllist;
  	$wsdlnames = explode(":", $wsdllist);
  	$wsdladdress = array();
  	//$wsdladdress[] = 'http://localhost:9001/api.php?apiname=showwsdl&wsdlname=xxoo.wsdl';
  	foreach($wsdlnames as $wsdlname){
  		$wsdladdress[] = urlencode("http://localhost:9001/api.php?apiname=showwsdl&wsdlname=".$wsdlname);
  	}
  	
  	$bpel = urlencode("http://localhost:9001/api.php?apiname=showworkflow&workflowname=".$_GET['workflowname']);
  	$wsdls = implode(",", $wsdladdress);
  	$xml = urlencode("http://localhost:9001/api.php?apiname=getbackupservicelist&workflowname=".$_GET['workflowname']);
  	
  	$options['parameter'] = array('bpel' => $bpel, 'wsdls' => $wsdls, 'xml' => $xml);
  	webservice_run($options);
  }	else if($apiname == "logout"){
  	unset($_SESSION['uid']);
  } else {
?>
<html>
<head>
	<title>Welcome to workflowevaluation's api center</title>
</head>
<body>
<?php
	
	if($apiname == "help"){
		echo "<ul>";
		echo "<li><a href=\"api.php?apiname=listbpel\">list all bpel files</a></li>";
		echo "<li><a href=\"api.php?apiname=listservice\">list all services</a></li>";
		echo "</ul>";
	} 
?>
</body>
</html>
<?php 
}} else {
//	echo "please specify api name, try /api.php?apiname=help"; 
		echo "a!!!!!!!!!!!!!!!!!所有bpelname全部替换成workflowname,如bpelname=Travel.bpel改为workflowname=Travel!!!!!!!!!!!!!!!!!!<br/>";
	  echo "ATTENTION: better login first to use apis; username:zychen;password:123</br></br>";
	  echo "supply api as followed: listbpel, showbpel, showwsdl, bpel2json, listservice, listbpelservice, 
		getservname, servforcast, evaluate, evaparamupdate</br>";
	  echo "usage:<br/>";
	  echo "examples:<br/><br/>";
		echo "/api.php?apiname=listworkflow<br/>列出所有的工作流<br/><br/>";
//	  echo "/api.php?apiname=listbpel<br/>列出所有的bpel文件名<br/><br/>";
	  echo "/api.php?apiname=showworkflow&workflowname=Travel<br/>获取一个工作流的内容<br/><br/>";
	  echo "/api.php?apiname=showwsdl&wsdlname=xxx.wsdl<br/>获取一个wsdl文件的内容<br/><br/>";
	  echo "/api.php?apiname=workflow2json&workflowname=Travel<br/>一个工作流的信息以json格式返回<br/><br/>";
	  echo "/api.php?apiname=listservice<br/>列出数据库里所有的服务<br/><br/>";
    echo "/api.php?apiname=listworkflowservice&workflowname=Travel<br/>列出一个工作流里所有的服务<br/><br/>";
	  echo "/api.php?apiname=getservdata&servname=WeatherForecastServiceService<br/>服务的观测数据<br/><br/>";
	  echo "/api.php?apiname=servforcast&servname=WeatherForecastServiceService&uid=1<br/>计算一个用户对某个服务的单点服务评估值<br/><br/>";
	  echo "/api.php?apiname=evaluate&workflowname=Travel<br/>工作流的评估值<br/><br/>";

	  echo "/api.php?apiname=evaparamupdate&uid=1&sname=WeatherForecastServiceService&threshold=50&lambda=0.8<br/>更新用户的评估参数<br/><br/>";
	  echo "/api.php?apiname=evaparaminfo&uid=1&sname=WeatherForecastServiceService<br/>获取用户的评估参数<br/><br/>";
    echo "/api.php?apiname=setbackupservice&workflowname=Travel&mainservice=WeatherForecastServiceService&backupservice=WeatherForecastService2Service<br/>更新服务的备选服务<br/><br/>";    
    echo "/api.php?apiname=getbackupservice&workflowname=Travel&mainservice=WeatherForecastServiceService<br/>获取服务的备选服务列表<br/><br/>";
    echo "/api.php?apiname=addservice<br/>用于处理添加服务的表单的页面,表单提交如下内容:POST['service_name'],FILE['wsdl']<br/><br/>";
    echo "/api.php?apiname=updateserviceaddress&servicename=xxx&address=yyy<br/>更新服务的地址信息,yyy是url编码的串<br/><br/>";
    echo "/api.php?apiname=addworkflow<br/>用于处理添加工作流的表单的页面,表单提交如下内容:POST['workflowname'], 
    			POST['referedwsdl'], FILE['bpel'];其中POST['referedwsdl']的格式如:wsdlname1:wsdlname2:wsdlname3<br/><br/>";
    echo "/api.php?apiname=addvisitlog&name=xxx&time=yyy&log=lll<br/>添加工作流访问记录信息,xxx为url格式的数据<br/><br/>";
    echo "/api.php?apiname=getvisitlog&name=xxx<br/>获取工作流访问记录信息<br/><br/>";
    echo "/api.php?apiname=getbackupservicelist&workflowname=Travel<br/>获取备选服务LIST<br/>";
    echo "/api.php?apiname=run&workflowname=Travel<br/>";
    echo "tips:</br>";
    echo "单点评估的结果跟用户输入的参数threashold有关,评估是否通过根据评估结果同用户输入的lambda比较决定,如果比lambda大则通过,反之则没通过。</br>";
    echo "如果单点评估没有通过,则打叉,并且不需要进行整体评估,在原来显示整体评估结果的地方显示提示信息,如: workflow evaluation quit!!!";
}
?>