var graph=null;
var graphJson=null;
var lastNodeId=0;
var delta=new Object();
var deltaIndex=0;
function executeCmd(command, var1, var2, var3){
	var cmd;
	if(command=='add_node'){
		var node={'id':lastNodeId++,'name':var1,'weight':0};
		graphJson.nodes.push(node);
		var add_node={'type':'add_node','object':node};
		delta[deltaIndex++]=add_node;
		cmd='add node:('+node.id+":"+node.name+")";
		$('<li>').html(cmd).appendTo($('#sortable'));
	}	
	else if(command=='add_edge'){
		var source_id=var1.split(":")[0];
		var target_id=var2.split(":")[0];
		var edge={'source':source_id,'target':target_id,'weight':var3};
		graphJson.edges.push(edge);
		var add_edge={'type':'add_edge','object':edge};
		delta[deltaIndex++]=add_edge;
		cmd='update edge:('+var1+"->"+var2+"#"+var3+")";
		$('<li>').html(cmd).appendTo($('#sortable'));
	}
	else if(command=='delete_node'){
		var id=var1.split(":")[0];
		var index=binarySearch(parseInt(id));
		if(index===false)
			cmd='excpetion:node:('+var1+') does not exsit';
		else{
			graphJson.nodes.splice(index,1);
			var del_node={'type':'del_node','object':index};
			delta[deltaIndex++]=del_node;
			cmd='delete_node:('+var1+')';
		}
		$('<li>').html(cmd).appendTo($('#sortable'));
	}
	else if(command=='delete_edge'){
		var id=var1.split(":")[0];
		var edge=var1.split(":")[1];
		graphJson.edges.splice(parseInt(id),1);
		var del_edge={'type':'del_edge','object':id};
		delta[deltaIndex++]=del_edge;
		cmd='delete_edge:('+edge+')';
		$('<li>').html(cmd).appendTo($('#sortable'));
	}
	autoComplete();
	
}
function binarySearch(id){
	var high=(graphJson.nodes.length-1)>id?id:(graphJson.nodes.length-1);
	var low=0;
	while(low<=high){
		var mid=parseInt((low+high)/2);
		if(graphJson.nodes[mid].id==id)
			return mid;
		else if(graphJson.nodes[mid].id>id){
			high=mid-1;
		}else{
			low=mid+1;
		}
	}
	return false;
	
	
}
function getAllNodes(){
	var result=[];
	var i;
	for(i=0;i<graphJson.nodes.length;i++){
		
		result.push(graphJson.nodes[i].id+":"+graphJson.nodes[i].name);
	}
	return result;
}
function getAllEdges(){
	var result=[];
	var i;
	for(i=0;i<graphJson.edges.length;i++){
		
		result.push(i+":"+graphJson.edges[i].source+"->"+graphJson.edges[i].target+"#"+graphJson.edges[i].weight);
	}
	return result;
}
function autoComplete(){
	$('#delete_edges_input').autocomplete({
		source: getAllEdges()
	});
	$('#delete_nodes_input').autocomplete({
		source: getAllNodes()
	});
	
	$('#add_edges_input_s').autocomplete({
		source: getAllNodes()
	});
	$('#add_edges_input_t').autocomplete({
	  source: getAllNodes()
	});
	$('#add_edges_input_w').autocomplete({
		source: ['0.1','0.4','0.7','0.9']
	});
}
function editFormDialog() {
	if (!gCurGraphId) {
		alert("请选择社会网络.");
		return;
	}
	graph=JSON.stringify(gCurGraphData);
	graphJson=JSON.parse(graph);
	lastNodeId=graphJson.nodes.length;
	deltaIndex=0;
	$('#sortable').empty();
	$('#add_nodes_input').val("");
	$('#add_edges_input_s').val("");
	$('#add_edges_input_t').val("");
	$('#add_edges_input_w').val("");
	$('#delete_nodes_input').val("");
	$('#delete_edges_input').val("");
	
	$('#edit-form').dialog(
			{
				width: 420,
				height:500,
				autoOpen : false,
				buttons : {
					 上传: function() {
						 
							if($('#new_graph_name').val()==''){
								alert('请输入新数据集的名称');
								return;
							}
							uploadNewJson();
							$('#edit-form').dialog("close");
						
					},
					取消: function() {
						$('#edit-form').dialog("close");
					}

				}
			});
	$("#edit-form").dialog("open");
}
function uploadNewJson(){
	var graphName='default';
	
	graphName=$('#new_graph_name').val();
	console.info(delta);
	$.blockUI({
		message : '<h1><img src="images/spinner.gif" /> 加载中...</h1>'
	});
	
	$.ajax({
		url : SERVER_URL+"/graphapi.php?apiname=uploadgraph&uid="+gUID+"&graphname="+graphName+"&oldgraphid="+gCurGraphId,
		type : 'POST',
		dataType : "json",
		data : delta,
		success : function() {
			appendSuccMessage("成功上传新的数据集");
			getGraphList();
			$.unblockUI();
		},
		error: function(XMLHttpRequest, textStatus, errorThrown){
			appendErrorMessage("数据集上传失败");
			$.unblockUI();
			console.info(XMLHttpRequest.responseText);
			console.info(textStatus);
			console.info(errorThrown);
		}
	});
}

$(function() {
    $( "#update_graph" ).tabs();
 });
function editorClick(){
	$("#add_nodes_button").button().click( function() { 
		executeCmd('add_node',$('#add_nodes_input').val(),null,null);
	} );
	$("#add_edges_button").button().click( function() { 
		if($('#add_edges_input_s').val()==''){
			alert("请输入起点");
			return;
		}if($('#add_edges_input_t').val()==''){
			alert("请输入终点");
			return;
		}
		if($('#add_edges_input_w').val()==''){
			alert("请输入信任值");
			return;
		}
		var allNodes=getAllNodes();
		if(!$.inArray($('#add_edges_input_s').val(), allNodes)){
			alert("无效的起点!");
			return;
		}
		if(!$.inArray($('#add_edges_input_t').val(), allNodes)){
			alert("无效的终点!");
			return;
		}
		if(!$.inArray($('#add_edges_input_w').val(), [0.1,0.4,0.7,0.9])){
			alert("无效的 信任值!");
			return;
		}
	
		executeCmd('add_edge',$('#add_edges_input_s').val(),$('#add_edges_input_t').val(),$('#add_edges_input_w').val());
	} );
	$("#delete_edges_button").button().click( function() { 
		if($('#delete_edges_input').val()==''){
			alert("请输入边");
			return;
		}
		var allEdges=getAllEdges();
		if(!$.inArray($('#delete_edges_input').val(), allEdges)){
			alert("无效的边!");
			return;
		}
		executeCmd('delete_edge',$('#delete_edges_input').val(),null,null);
	} );
	$("#delete_nodes_button").button().click( function() { 
		if($('#delete_nodes_input').val()==''){
			alert("请输入起点");
			return;
		}
		var allNodes=getAllNodes();
		if(!$.inArray($('#delete_nodes_input').val(), allNodes)){
			alert("无效的起点!");
			return;
		}
		executeCmd('delete_node',$('#delete_nodes_input').val(),null,null);
	} );
}