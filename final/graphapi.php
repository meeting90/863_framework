﻿<?php
require_once 'subgraph/Graph.php';
require_once 'subgraph/YenTopKShortestPathsAlg.php';
require_once 'GraphUtil.php';
if (! isset ( $_GET ['apiname'] )) {
	echo 'graphapi.php usage</br>';
	echo 'graphapi.php?apiname=getgraphlist&uid=1</br>获取该用户graph列表</br>';
	echo 'graphapi.php?apiname=addgraph&uid=1</br>增加graph</br>';
	echo 'graphapi.php?apiname=graph&graphid=1&uid=1</br>显示原始graph</br>';
	echo 'graphapi.php?apiname=subgraph&graphid=1&source=37&target=87&topk=20&uid=1</br>获取经过ks抽取的子graph的json数据</br>';
	echo 'graphapi.php?apiname=evograph&graphid=1&source=36&target=87&&topk=20&pathnum=5&uid=1</br>获取经过evo抽取的最终结果的json数据</br>';
	echo 'graphapi.php?apiname=uploadgraph&uid=1&graphname=newGraph</br>上传修改后的数据集</br>';
	// 'api.php?apiname=ksconsole&graph=smallgraph&uid=1</br>获取ks抽取的过程信息</br>';
	// echo 'api.php?apiname=evoconsole&graph=smallgraph&uid=1</br>获取evo计算的过程信息</br>';
} else {
	$apiname = $_GET ['apiname'];
	$uid = $_GET ['uid'];
	
	if ($apiname == 'addgraph') {
		$path = $uid . '/';
		// GraphUtil::uploadfile($path);
		$result = array ();
		
		$error = "";
		$msg = "";
		$fileElementName = 'fileToUpload';
		
		$allowType = array (
				'.json',
				'.dot' 
		); // allow file type
		$num = strrpos ( $_FILES [$fileElementName] ['name'], '.' );
		$fileName = substr ( $_FILES [$fileElementName] ['name'], 0, $num ); // file name
		$fileType = substr ( $_FILES [$fileElementName] ['name'], $num, 8 ); // file type
		
		if (! empty ( $_FILES [$fileElementName] ['error'] )) {
			switch ($_FILES [$fileElementName] ['error']) {
				case '1' :
					$error = '上传的文件大小超出了php.ini 的限制';
					break;
				case '2' :
					$error = '上传的文件大小超出了html 中的表单设置';
					break;
				case '3' :
					$error = '文件上传中断';
					break;
				case '4' :
					$error = '没有上传文件';
					break;
				case '6' :
					$error = '找不到临时文件夹';
					break;
				case '7' :
					$error = '文件写入磁盘错误';
					break;
				case '8' :
					$error = '上传文件扩展名错误';
					break;
				case '999' :
				default :
					$error = '未知上传错误';
			}
		} elseif (empty ( $_FILES ['fileToUpload'] ['tmp_name'] ) || $_FILES ['fileToUpload'] ['tmp_name'] == 'none') {
			$error = 'No file was uploaded..';
		} elseif (! in_array ( $fileType, $allowType )) {
			$error = 'Unsupported upload file type';
		} else {
			$msg .= " File Name: " . $_FILES ['fileToUpload'] ['name'] . ", ";
			$msg .= " File Size: " . @filesize ( $_FILES ['fileToUpload'] ['tmp_name'] );
			
			// save file
			if (! file_exists ( $path )) {
				mkdir ( $path, 0777 );
			}
			$count = 0;
			$name = $fileName;
			while ( file_exists ( $path . $name . $fileType ) ) { // change file name
				$count ++;
				$name = $fileName . ' (' . $count . ')';
			}
			if (! move_uploaded_file ( $_FILES [$fileElementName] ['tmp_name'], $path . $name . $fileType )) {
				$error = '保存文件失败';
			} else {
				
				$result ['fullname'] = $path . $name . $fileType;
				$result ['dir'] = $path;
				$result ['name'] = $name;
				$result ['type'] = $fileType;
				
				if (isset ( $_POST ['graph_name'] )) {
					$name = $_POST ['graph_name'];
				}
				// $filename="1/smallgraph.dot"
				$pathinfo = pathinfo ( $result ['fullname'] );
				$user_graph = new Graph ( $result ['fullname'], $pathinfo ['extension'] );
				$json = json_encode ( $user_graph->tojsonarray () );
				$jsonfile = GraphUtil::savejson ( $path, 'target-' . $name, $json );
				GraphUtil::insertRecord ( $uid, $name, $result ['fullname'], $jsonfile );
				// echo $result;
			}
		}
		$result ['error'] = $error;
		$result ['msg'] = $msg;
		
		echo "{";
		echo "error: '" . $error . "',\n";
		echo "msg: '" . $msg . "'\n";
		echo "}";
	} else if ($apiname == 'getgraphlist') {
		$graphlist = GraphUtil::getgraphlist ( $uid );
		echo json_encode ( $graphlist );
	} else if ($apiname == 'graph') {
		$graph = $_GET ['graphid'];
		$result = GraphUtil::getgraphByid ( $graph );
		echo json_encode ( $result );
	} else if ($apiname == 'subgraph') {
		$graph_id = $_GET ['graphid'];
		$source = $_GET ['source'];
		$target = $_GET ['target'];
		$k = $_GET ['topk'];
		$result = GraphUtil::getsubgraph ( $graph_id, $source, $target, $k );
		echo json_encode ( $result );
	} else if ($apiname == 'evograph') {
		$graph_id = $_GET ['graphid'];
		$source = $_GET ['source'];
		$target = $_GET ['target'];
		$k = $_GET ['topk'];
		$pathnum = $_GET ['pathnum'];
		$result = GraphUtil::getevograph ( $graph_id, $source, $target, $k, $pathnum );
		echo json_encode ( $result );
	}
	else if($apiname=='uploadgraph'){
		$path = $uid . '/';
		$name=$_GET ['graphname'];
		$graph = $_GET ['oldgraphid'];
		while ( file_exists ( $path . $name . '.json' ) ) { // change file name
				$count ++;
				$name = $name . ' (' . $count . ')';
		}
		$deltas=$_POST;
		$result = GraphUtil::getgraphByid ( $graph );
		$filename=$result->json;	
		$jsonarray=json_decode ( file_get_contents ( $filename ), true );
		
		foreach ( $deltas as $command ){
			if($command['type']=='add_node')
				$jsonarray['nodes'][]=$command['object'];
			else if($command['type']=='add_edge'){
				$jsonarray['edges'][]=$command['object'];
			}else if($command['type']=='del_node'){
				array_splice($jsonarray['nodes'],$command['object'],1);
			}else if($command['type']=='del_edge'){
				array_splice($jsonarray['edges'],$command['object'],1);
			}		
		}
		
		
		$user_graph=new Graph($jsonarray,null);
		$json=json_encode($user_graph->tojsonarray());
		$jsonfile = GraphUtil::savejson ( $path,  'target-'.$name, $json);
		GraphUtil::insertRecord ( $uid, $name, $jsonfile, $jsonfile );
		
		
	}
}

