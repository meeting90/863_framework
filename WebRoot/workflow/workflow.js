


function showWorkflowList(){
	
	drawWorkflowListLayout();
	loadWorkflowList();
	
		
}

function evaluateService(){
	
}
function analysisService(){
	
}
function showUploadDialog(){
	
}

function loadWorkflowList(){
	 $.ajax({
  	type: 'POST',
    url: getFocusedWfURL+uid,
    dataType: 'json',
    success: function(data) {
    	
    	
    	drawFocusedWorkflowListView(data);
    	
    },
	error:function(XMLHttpRequest, textStatus, errorThrown){
		
		alert("Status: " + textStatus); alert("Error: " + errorThrown); 
	}
  });

}

function loadWorkflowDetail(wfid){
	
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
			drawWfBaseInfoView(data);
		},
		error:function(){
			alert('get Service error');
		}
	});
}
function loadWfInfo(wfid){
	$.ajax({
		type:'POST',
		dataType:'json',
		url:getWorkflowInfoURL+wfid,
		success:function(data){
			drawWorkflowInterfaceView(data);
		},error:function(){
			
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
		oHeight=16,
		gap=16,
		padding=16;


	var width=0;
	var height=0;

	switch(activity.nodeType){
		case 'sequence':case 'repeatUtil':case 'while':case 'Scope':
		//计算其子Activity需要的画布大小，子Activity纵向排列，高度求和，宽度取最大
			
			activity.BPELActivity.forEach(function(a) {
				calculateDimension(a);
				width=Math.max(width,a.width);
				height+=(a.height+gap+oHeight);
			});
			width+=padding;
			height+=(oHeight+gap+padding);
			activity.width=width;
			activity.height=height;
			break;
		case 'flow':case 'forEach':case 'pick':case  'switch':case 'if':
			//计算其子Activity需要的画布大小，子Activity横向排列，高度取最大，宽度求和
			
			activity.BPELActivity.forEach(function(a){
				calculateDimension(a);
				width+=(a.width+gap);
				height=Math.max(height,a.height);
			});
			width+=padding;
			height+=(oHeight+gap+padding);
			if(activity.header.length>0){//如果有头部信息，高度在加一个图元基本高度
				height+=(oHeight+gap);
			}
			activity.width=width;
			activity.height=height;
			break;

		case 'wait': case 'throw': case 'terminate': case 'rethrow': case 'empty':
			//基本activity图元的大小
			activity.width=oWidth;
			activity.height=oHeight;
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
	var gap=16,
		oHeight=16,
		iconPx=16;

	function drawTextNodeWithIcon(text,iconPath,textX,textY,showWapper){
			var short=text;
			if(text.length>32)
				short=text.substr(0,13)+"...";
			var textNode=paper.text(textX,textY,short).attr({cursor: "move"});
			
		
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
			return wapper;
	}
	var containerX=cx - activity.width/2;
	var containerY=cy - activity.height/2;
	var nextY=containerY;
	var nextX=containerX;
	var container=paper.rect(containerX,containerY,activity.width,activity.height).attr({'fill-opacity':0.2,'fill':'#87CEEB'});
	if(activity.name==undefined)
		activity.name=activity.nodeType;
	var nameNode=drawTextNodeWithIcon(activity.name,'./icons/'+activity.nodeType+'.gif',cx,nextY,true);
	nextY+=(gap+oHeight);
	if (activity.extra.length>0) {//basic activity 
			opNode=drawTextNodeWithIcon(activity.extra[0],'./icons/operation.gif',cx,nextY,false);
			nextY+=(gap+oHeight);
			sourceNode=drawTextNodeWithIcon(activity.extra[1],'./icons/service_obj.gif',cx,nextY,false);
			nextY+=(gap+oHeight);
	};
	switch(activity.nodeType){
		case 'sequence':case 'repeatUtil':case 'while':case 'Scope':
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
			if(activity.header.length>0) newCy+=(gap+oHeight);
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
					paper.connection(nameNode,aNode,'#000',0.5);
				}
				nextX+=a.width+gap;
			});
			break;
		default:
			break;

	}
	return container;
	
}


//根据体系结构计算工作流的可信度
function calculateProcessTrust(activity){

}
