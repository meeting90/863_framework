
var selectedWorkflow=null;
var selectedBaseInfo=null;
var evaluated=false;
var invokedServices = [];

function showWorkflowList(){
	 $.blockUI({ message: '<h1><img src="./images/spinner.gif" /> 加载中...</h1>' }); 
	
	drawWorkflowListLayout();
	loadWorkflowList();
}

function showEvaluateDialog(){
	if(selectedWorkflow==null){
		alert('请选择一个工作流!');
		return;
	}
	$('#evaluateDialog').dialog({
		title:'评估工作流',
		width:300,
		closed:false,
		cache:false,
		buttons: [
		          {
		        	  text:'评估',
		        	  iconCls:'icon-ok',
		        	  handler:function(){
		        		  evaluateWorkflow($("#trust_requirement").val());
		        		  $('#evaluateDialog').dialog('close');
		        	  }
			
		          },
		          {
		        	text:'关闭',
		        	iconCls:'icon-no',
		        	handler:function(){
		        		$('#evaluateDialog').dialog('close');
		        	}
		          }
		          ]
	});
}
function evaluateWorkflow(requirement){
	console.info("evaluateWorkflow");
	if(selectedWorkflow==null){
		alert("请选择一个工作流");
		return;
	}
	
	calculateAndShowProcessTrust(selectedWorkflow);
	if(selectedWorkflow.trustValue<requirement){
		appendErrorMessage("工作流评估失败!");
		appendErrorMessage(selectedBaseInfo.wfName+" 可信度:"+ selectedWorkflow.trustValue);
		appendErrorMessage("***************");
	}else{
		appendSuccessMessage("工作流评估成功!");
		appendSuccessMessage(selectedBaseInfo.wfName+" 可信度:"+ selectedWorkflow.trustValue);
		appendSuccessMessage("***************");
	}

	openConsole();
	evaluated=true;
	
}

function analysisWorkflow(){
	if(selectedWorkflow==null){
		alert("请选择一个工作流");
		return;
	}
	if(!evaluated){
		alert("请先评估工作流");
		return;
	}	
	
	drawAnalysisView(selectedWorkflow);	
	console.info(selectedWorkflow);

	//模拟点击事件，打开colorbox
	$('a.analysisGallery').click();
	
	

       
	
	
	
}



function showUploadDialog(){
	$('#uploadDialog').dialog({
		title:'上传Web服务',
		width:400,
		closed:false,
		cache:false
	});
	
	$('#uploadsubmit').click(function(){
		$.ajaxFileUpload({
			url:uploadFileWorkflowURL+uid,
			secureuri:false,
			fileElementId:"uploadfileinput",
			dataType:"json",
			success:function(data,status){
				alert("上传成功");
			},
			error:function(data,status,e){
				alert("上传出错");
			}
		});
	});
}


function loadWorkflowList(){
	 $.ajax({
  	type: 'POST',
    url: getFocusedWfURL+uid,
    dataType: 'json',
    success: function(data) {
    	
    	
    	drawFocusedWorkflowListView(data);
    	$.unblockUI();
    	
    },
	error:function(XMLHttpRequest, textStatus, errorThrown){
		
		alert("获取工作流列表失败！");
		window.location = loginURL;
	}
  });

}

function loadWorkflowDetail(wfid){
	evaluated=false;
	selectedWorkflow=null;
	selectedBaseInfo=null;
	invokedServices = [];
	$('#invokedServices').empty();
	drawWorkflowDetailView();
	loadWfBaseInfo(wfid);
	loadWfInfo(wfid);
}
function loadWfBaseInfo(wfid){
	
	
	$.ajax({
		type:'POST',
		dataType:'json',
		url:getWorkflowURL+wfid,
		success:function(data){
			selectedBaseInfo=data;
			drawWfBaseInfoView(data);
		},
		error:function(){
			alert("获取工作流基本信息失败！");
			window.location = loginURL;
		}
	});
}
function loadWfInfo(wfid){
	$.ajax({
		type:'POST',
		dataType:'json',
		url:getWorkflowInfoURL+wfid,
		success:function(data){
			selectedWorkflow=data;
			
			drawWorkflowInterfaceView(data);
		},error:function(){
			alert("获取工作流结构信息失败！");
			window.location = loginURL;
		}
	});
}



function drawWorkflowListLayout(divId,newId1,newId2){
	
	 
	  var focusedlist=$('<div>').attr('id','list4');
	  focusedlist.appendTo($('#workflowList'));
	
	  
	  var focusedul=$('<ul>').attr('id','focusedWorkflowList');
	  focusedul.appendTo(focusedlist);
}


function drawFocusedWorkflowListView(data){
	$('#focusedWorkflowList').empty();
	drawWorkflowListCore(data,'#focusedWorkflowList',false);
	
	
}
function drawWorkflowListCore(data,div,filter){
	console.info(data);
	  data.forEach(function (d){
		  if(!filter){
			  focusedWorkflow.push(d.wfId);
		  }
		  if(!filter || $.inArray(d.wfId,focusedWorkflow)==-1){
			 var href_tag = $('<a>').attr('href', '#').attr('id',d.wfId).attr('onclick', 'loadWorkflowDetail(this.id);').html(d.wfName);
	    	 var span_tag=$('<span>');
	    	 var  li_tag = $('<li>');
			 span_tag.appendTo($(div));
	    	 li_tag.appendTo(span_tag);
	    	 href_tag.appendTo(li_tag);
		  }
		});
		 
}

function drawWorkflowDetailView(){
	$('#mainView').empty();
	var detailAccordion=$('<div>').attr('id','fwAccordion');
	detailAccordion.appendTo($('#mainView'));
	$('#fwAccordion').accordion({
		fit:false,
		multiple:true
	});
	$('#fwAccordion').accordion('add',{
		title:'基本信息',	
		
		selected:true,
		collapsed:false,
		collapsible:false,
		id:'fwfBaseInfo'
		
	});
	$('#fwAccordion').accordion('add',{
		title:'服务接口',
		
		id:'fwfInterface',
		collapsed:false,
		collapsible:false,
		selected:true,
		height:600
		

	});
}
function showBackupDialog(wsName){
	
}
function drawWfBaseInfoView(data){
	console.info('drawWFBaseInfoVIew');
	var fwInfoContainer=$('<div>').attr('id','fwInfoContainer');
	fwInfoContainer.appendTo($('#fwfBaseInfo'));
	fwfProperty=$('<div>').attr('id','fwfProperty');
	fwfProperty.appendTo(fwInfoContainer);

	
	$('#fwfProperty').propertygrid({
		showGroup:false,
		showHeader:false,
		width:500,
		
	});

	var wfId={name:'工作流ID',value:data.wfId};
	var wfName={name:'工作流名',value:data.wfName};
	var wfPath={name:'工作流连接',value:'<a href=".'+data.wfPath+'">.'+data.wfPath+'</a>'};

	$('#fwfProperty').propertygrid('appendRow',wfId);
	$('#fwfProperty').propertygrid('appendRow',wfName);
	$('#fwfProperty').propertygrid('appendRow',wfPath);
	
	
}



function drawWorkflowInterfaceView(data){
	calculateDimension(data);
	var width=Math.max($('#fwfInterface').width(),data.width+20);
	var height=Math.max($('#fwfInterface').height(),data.height+20);
	var paper=Raphael('fwfInterface',width,height);
	drawBPELActivity(paper,data,width/2,height/2);
}


//递归计算整个BPELactivity需要的画布大小
function calculateDimension(activity){
	var oWidth=150,
		oHeight=12,
		gap=12,
		padding=12;


	var width=0;
	var height=0;
	
	switch(activity.nodeType){
		case 'sequence':case 'repeatUtil':case 'while':case 'scope':
		//计算其子Activity需要的画布大小，子Activity纵向排列，高度求和，宽度取最大
			
			activity.BPELActivity.forEach(function(a) {
				calculateDimension(a);
				width=Math.max(width,a.width);
				height+=(a.height+gap+oHeight*2);
			});
			if(width==0)
				width=oWidth;
			width+=padding*2;
			height+=(oHeight+gap);
			activity.width=width;
			activity.height=height;
			break;
		case 'flow':case 'forEach':case 'pick':case  'switch':case 'if':
			//计算其子Activity需要的画布大小，子Activity横向排列，高度取最大，宽度求和
			
			activity.BPELActivity.forEach(function(a){
				calculateDimension(a);
				width+=(a.width+gap);
				height=Math.max(height,a.height+gap);
			});
			width+=padding;
			height+=(2*oHeight+gap+padding);
			if(activity.header.length>0){//如果有头部信息，高度在加一个图元基本高度
				height+=(oHeight+gap);
			}
			activity.width=width;
			activity.height=height;
			break;

		case 'wait': case 'throw': case 'terminate': case 'rethrow': case 'empty':case 'assign':
			//基本activity图元的大小
			activity.width=oWidth;
			activity.height=oHeight+gap;
			break;
		case 'receive':case 'reply':case 'invoke':	
			//带操作的activity图元大小	
			activity.width=oWidth;
			activity.height=oHeight*3+gap*2;
			break;

	}
}
//递归画出BPELActivity

function drawBPELActivity(paper,activity,cx,cy){
	var gap=12,
		oHeight=12,
		iconPx=16;
	function addToInvokedList(name){
		if($.inArray(name,invokedServices)==-1){
			invokedServices.push(name);
			 var href_tag = $('<a>').attr('href', '#').attr('id',name).attr('onclick', 'showBackupDialog(this.id);').html(name);
	    	 var span_tag=$('<span>');
	    	 var  li_tag = $('<li>');
	    	 
			 span_tag.appendTo($('#invokedServices'));
	    	 li_tag.appendTo(span_tag);
	    	 href_tag.appendTo(li_tag);
		}
		
	}
	function drawTextNodeWithIcon(text,iconPath,textX,textY,showWapper){
		
			var short=text;
			if(text.length>32)
				short=text.substr(0,13)+"...";
			var textNode=paper.text(textX,textY,short).attr({cursor: "move"});
			var icon=null;
			
		
			textNode.full=text;
			textNode.short=short;
			textNode.drag(
					function(dx,dy){//move
						this.attr({'text':this.full,'x':this.ox+dx,'y':this.oy+dy});
					},function(dx,dy){//drag	
						this.ox = this.attr("x");
						this.oy = this.attr("y") ;
						this.attr({'text':this.full});
					},function(){//up
						this.attr({'text':this.short,'x':this.ox,'y':this.oy});
					});
			var bbox=textNode.getBBox();
			if(iconPath)
			   icon=paper.image(iconPath,bbox.x-iconPx,bbox.y,iconPx,iconPx);

			var wapper=paper.rect(bbox.x-iconPx,bbox.y,bbox.width+iconPx,bbox.height);
			if (!showWapper) {
				wapper.attr({'fill-opacity':0,'stroke-opacity':0});
			}else{
				wapper.attr({'fill-opacity':1,'fill':'#FFF'});
			}
			wapper.insertBefore(textNode);		
			wapper.icon=icon;
			wapper.text=textNode;
			return wapper;
	}
	var containerX=cx - activity.width/2;
	var containerY=cy - activity.height/2;
	var nextY=containerY;
	var nextX=containerX;
	var container=paper.rect(containerX,containerY,activity.width,activity.height).attr({'fill-opacity':0.2,'fill':'#87CEEB'});
	activity.container=container;
	if(activity.name==undefined)
		activity.name=activity.nodeType;
	var nameNode=drawTextNodeWithIcon(activity.name,'./icons/'+activity.nodeType+'.gif',cx,nextY,true);
	nextY+=(gap+oHeight);
	if (activity.extra.length>0) {//basic activity 
			opNode=drawTextNodeWithIcon(activity.extra[0],'./icons/operation.gif',cx,nextY,false);
			nextY+=(gap+oHeight);
			sourceNode=drawTextNodeWithIcon(activity.extra[1],'./icons/service_obj.gif',cx,nextY,false);
			nextY+=(gap+oHeight);
			activity.sourceNode=sourceNode;
			addToInvokedList(activity.extra[1]);
			
	}else{
		var rateNode=paper.rect(containerX+gap,containerY+gap,activity.width-gap*2,oHeight);
		rateNode.attr({'fill-opacity':0,'stroke-opacity':0});
		activity.sourceNode=rateNode;
	}
	nextY+=(gap+oHeight);
	
	switch(activity.nodeType){
		case 'sequence':case 'repeatUtil':case 'while':case 'scope':
			var newCx=cx;
			var lastObj=nameNode;
			activity.BPELActivity.forEach(function(a){
				
				var newCy=nextY+a.height/2;
				var aNode=drawBPELActivity(paper,a,newCx,newCy);
				nextY+=(a.height+gap+oHeight);
				paper.connection(lastObj,aNode,'#000',0.5);
				lastObj=aNode;
			});
			break;
		case 'flow':case 'forEach':case 'pick':case  'switch':case 'if':
			var newCy=cy;
			if(activity.header.length>0) newCy+=(gap+oHeight*2);
			activity.BPELActivity.forEach(function  (a,i) {
				var newCx=nextX+a.width/2;
				if(activity.header.length>0){
					var header=drawTextNodeWithIcon(activity.header[i],null,newCx,nextY,true);
				}
				var newCx=nextX+gap+a.width/2;
				
				var aNode =drawBPELActivity(paper,a,newCx,newCy);
				if(header){
					paper.connection(nameNode,header,'#000',0.5);
					paper.connection(header,aNode,'#000',0.5);
				}else{
					//paper.connection(nameNode,aNode,'#F00',0.5);
				}
				nextX+=a.width+gap;
			});
			break;
		default:
			break;

	}
	return container;
	
}

//根据体系结构计算并展示工作流的可信度，需要在函数drawBPELActivity后执行
function calculateAndShowProcessTrust(activity){
	
	function drawRateValue(activity){
		//首先清除之前的评估信息
		if(activity.rateNode){
			activity.rateNode.fillRect.remove();
			activity.rateNode.textNode.remove();
			activity.rateNode.remove();
			activity.rateNode=null;
		}
		var bbox=activity.sourceNode.getBBox(),
		paper=activity.sourceNode.paper;
		
		activity.sourceNode.hide();
		if(activity.sourceNode.icon)
			activity.sourceNode.icon.hide();
		if(activity.sourceNode.text)
			activity.sourceNode.text.hide();
		var rect=paper.rect(bbox.x,bbox.y,bbox.width,bbox.height);
		
		var fillRect=paper.rect(bbox.x,bbox.y,0,bbox.height);
		rect.fillRect=fillRect;
		fillRect.attr({fill:"#228B22",color:"#228B22"});
		fillRect.animate({width: activity.trustValue*bbox.width},1000,'linear',function(){
			var text=paper.text(bbox.x+bbox.width/2,bbox.y+bbox.height/2,(activity.trustValue*100).toFixed(3)+"%");
			rect.textNode=text;
		});
		activity.rateNode=rect;
		
	}
	
	switch(activity.nodeType){
	case 'sequence':case 'repeatUtil':case 'while':case 'scope':case 'flow': case 'forEach':
		var multiply=1;
		activity.BPELActivity.forEach(function  (a) {
			calculateAndShowProcessTrust(a);
			multiply*=a.trustValue;
		});
		activity.trustValue=multiply;
	
		break;
	case 'pick':case  'switch':case 'if':
		var sum=0;
		activity.BPELActivity.forEach(function  (a) {
			calculateAndShowProcessTrust(a);
			sum+=a.trustValue;
		});
		activity.trustValue=sum/activity.BPELActivity.length;
		
		break;

	case 'wait': case 'throw': case 'terminate': case 'rethrow': case 'empty': case 'receive':case 'reply':case 'assign':
		//内部原子操作，trustValue 为1
		activity.trustValue=1;
		break;
	case 'invoke':	
		//调用其他的web 服务获取其 trust值.
		var service = $.ajax({
			async:false,
			url:getWsIdURL+activity.extra[1],
			dataType:'json',
			type:'POST'
		});
		console.info(service.responseJSON);
		if(service.responseJSON.length==0) {
			appendErrorMessage("服务评估失败");
			appendErrorMessage(activity.extra[1]+'服务不在后台数据库中');
			appendErrorMessage("***********");
			activity.rateValue=0;
			return;
		}
		var rateful=$.ajax({
							url:getServiceFullRatingURL+"&wsid="+service.responseJSON[0].wsId+"&uid="+uid,
							async:false,
							dataType:'json',
							type:'POST'
		});
		console.info(rateful.responseJSON);
		
		if(!rateful.responseJSON){
			activity.trustValue=0;
		}else{
			activity.trustValue=rateful.responseJSON[0].rateValue<0.999?rateful.responseJSON[0].rateValue:0.999;
		}
		break;

	}
	drawRateValue(activity);
	
}


//画出置顶向上的工作流评估过程
function drawAnalysisView(activity){
	
	
	var width=$('#analysisGallery').width();
	var height=$('#analysisGallery').height()
	var paper=Raphael('analysisGallery',width,height);
	
	console.info(height);
	calculateDepth(activity);
	var depth=activity.depth;
	var breadth=invokedServices.length;
	console.info(breadth);
	console.info(depth);
	var oWidth=width/breadth;
	
	var oHeight=height/depth;
	var block=1;
	var radius=oWidth/4;
	var timmer=1;
	var delayUnit=500;
	drawAnalysisNode(paper,activity);
	function calculateDepth(activity){
		var depth=1;
		if(activity.BPELActivity.length==0){
			activity.depth=depth;
			
		}else{
			activity.BPELActivity.forEach(function(a){
				calculateDepth(a);
				depth=Math.max(a.depth+1,depth);
			});
			activity.depth=depth;
		
		}
			
	}
	function showNodeAfterDelay(cx,cy,name,trustValue){
		var node=paper.circle(cx,cy,0).attr({stroke:'#F00'});
		var text=paper.text(cx,cy,name).attr({fill:'#F00'});
		text.hide();
		
		var trust=paper.text(cx,cy+oHeight/5*2,trustValue).attr({fill:'#228B22'});
		
		text.hide();
		trust.hide();
		
		var anim=Raphael.animation({r:radius},delayUnit,'linear',function(){
			
			trust.show();
			text.show();
			
		});
		node.animate(anim.delay(timmer*delayUnit));
		timmer++;
		return node;
		
	}
	
	function showPathAfterDelay(fromNode,toNode,trustValue){
		
		
		var bbox1=fromNode.getBBox();
		var bbox2=toNode.getBBox();
		
		var path1=['M',bbox1.x+bbox1.width/2,bbox1.y+radius].join(',');
		var path2=['M',bbox1.x+bbox1.width/2,bbox1.y+radius,'L',bbox2.x+bbox2.width/2,bbox2.y-radius].join(',');
		var path=paper.path(path1).attr({stroke:'#F00'});
		
		var anim=Raphael.animation({path:path2},delayUnit);
		path.animate(anim.delay(timmer*delayUnit));
		timmer++;
		return path;
		
	}
	function drawAnalysisNode(paper,activity){
		
		if(activity.BPELActivity.length>0){
			var cy=height-activity.depth*oHeight+oHeight/2;
			var cx=0;
			activity.BPELActivity.forEach(function(a){
				drawAnalysisNode(paper,a);
				var bbox=a.node.getBBox();
				cx+=(bbox.x+bbox.width/2);
			});
			cx=cx/activity.BPELActivity.length;
		
			activity.node=showNodeAfterDelay(cx,cy,activity.name,activity.trustValue);
			activity.BPELActivity.forEach(function(a){
				showPathAfterDelay(activity.node,a.node);
			});

		}else{//basic node
			var cy=height-activity.depth*oHeight+oHeight/2;
			
			var cx=oWidth*block-oWidth/2;

			activity.node=showNodeAfterDelay(cx,cy,activity.extra[1],activity.trustValue);
			block++;
			
			
		}
	}
	
	

	
	
	
}