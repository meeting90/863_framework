var selectedServiceId=null;
function showServiceList(){
	
	drawServiceListLayout();
	
	loadFocusService();
	loadOtherService();
		
}
//************ 评估选中工作流 ***********//
function evaluateService(){
	if(selectedServiceId==null){
		alert('请选择一个服务!');
		return;
	}
	
		
	
}
//*************** 显示文件上传对话 ***********//、
function showUploadDialog(){
	$('#uploadDialog').dialog({
		title:'上传Web服务',
		width:300,
		closed:false,
		cache:false,
		buttons: [{
	      
		        text:'关闭',
		        iconCls:'icon-no',
		        handler:function(){
		        	$('#uploadDialog').dialog('close');
		        }
	    }]
	});
	
}
function loadFocusService(){
	 $.ajax({
		  	type: 'POST',
	        url: getFocusWsURL+uid,
	        dataType: 'json',
	        success: function(data) {
	        	
	        	
	        	drawFocusedListView(data);
	        	
	        },
			error:function(XMLHttpRequest, textStatus, errorThrown){
				
				alert("Status: " + textStatus); alert("Error: " + errorThrown); 
			}
	      });
	  
}

function searchService(value,name){
	
	serviceOffset=0;
	console.info(searchServiceURL);
	  $.ajax({
		  	type: 'POST',
	        url: searchServiceURL+value,
	        dataType: 'json',
	        success: function(data) {
	        	drawSearchServiceListView(data);
	        },
			error:function(){
				alert("get search User error");
			}
	      });
}
function loadOtherService(){
	serviceOffset=0;
	$.ajax({
		type:'POST',
		url:getAllWsURL+serviceOffset,
		dataType:'json',
		success:function(data){
			drawOtherServiceView(data);
			serviceOffset+=data.length;
		},error:function(){
			console.info("get all service error");
		}
	});
}
function loadMoreService(){
	$.ajax({
		type:'POST',
		url:getAllWsURL+serviceOffset,
		dataType:'json',
		success:function(data){
			drawMoreServiceView(data);
			serviceOffset+=data.length;
		},error:function(){
			console.info("get all service error");
		}
	});
}
function loadServiceDetail(wsid){
	$('#serviceDetail').empty();
	drawServiceDetailView();
	loadWsBaseInfo(wsid);
	loadWsInfo(wsid);
}
function loadWsBaseInfo(wsid){
	
	$.ajax({
		type:'POST',
		dataType:'json',
		url:getServiceURL+wsid,
		success:function(data){
			drawBaseInfoView(data);
		},
		error:function(){
			alert('get Service error');
		}
	});
}
function loadWsInfo(wsid){
	$.ajax({
		type:'POST',
		dataType:'json',
		url:getServiceInfoURL+wsid,
		success:function(data){
			drawServiceInterfaceView(data);
		},error:function(){
			
		}
	});
}
function loadWsRating(wsid){
	console.info("loadWSRating");
	$.ajax({
		type:'POST',
		dataType:'json',
		url:getServiceRatingURL+wsid,
		success:function(data){
			drawServiceRatingView(data);
		},error:function(){
			alert('load Ws Rating error');
		}
	});
}


function drawServiceListLayout(){
	

	  var focusedlist=$('<div>').attr('id','list4');
	  focusedlist.appendTo($('#serviceList'));
	  
	  var focusedHeader=$('<h3>');
	  
	  var b=$('<b>').html('关注的服务');
	  b.appendTo(focusedHeader);
	  focusedHeader.appendTo(focusedlist);
	  
	  var focusedul=$('<ul>').attr('id','focusedServiceList');
	  focusedul.appendTo(focusedlist);
	  
	  var otherlist=$('<div>').attr('id','list4');
	  otherlist.appendTo($('#serviceList'));
	 
	  var otherHeader=$('<h3>');
	  
	  var b=$('<b>').html('其他服务');
	  b.appendTo(otherHeader);
	 
	  otherHeader.appendTo(otherlist);
	  
	  var searchdiv=$('<div>').attr('id','searchServiceDiv').attr('style','width:200px;padding:10px;margin-left:auto;margin-right:auto');
	  searchdiv.appendTo(otherlist);
	  var input=$('<input>').attr("id","searchServiceInput");
	  
	  input.appendTo(searchdiv);
	  $("#searchServiceInput").searchbox({
		  prompt:"输入要搜索的服务名",
		  searcher:searchService,
		  width:200
		  
		  
	  });
	 

	  var ul=$("<ul>").attr("id","serviceResult");
	  ul.appendTo(otherlist);

		
	

}


function drawFocusedListView(data){
	$('#focusedServiceList').empty();
	focusedService=[];
	drawServiceListCore(data,'#focusedServiceList',false);
	
	
}

function drawSearchServiceListView(data){
	$('#serviceResult').empty();
	drawServiceListCore(data,'#serviceResult',true);
}
function drawOtherServiceView(data){
	$('#serviceResult').empty();
	drawServiceListCore(data,'#serviceResult',true);
	drawMoreServiceLink();
}
function drawMoreServiceView(data){
	$('#moreService').remove();
	drawServiceListCore(data,'#serviceResult',true);
	drawMoreServiceLink();
}
function drawMoreServiceLink(){
	  var href_tag=$('<a style="color:green;">').attr('href','#').attr('onclick','loadMoreService()').html('more  ...');
	  var span_tag=$('<span>');
	  var li_tag=$('<li>').attr('id','moreService');
	  span_tag.appendTo('#serviceResult');
	  li_tag.appendTo(span_tag);
	  href_tag.appendTo(li_tag);	
}
function drawServiceListCore(data,div,filter){
	
	  data.forEach(function (d){
		  if(!filter){
			  focusedService.push(d.wsId);
		  }
		  if(!filter || $.inArray(d.wsId,focusedService)==-1){
			 var href_tag = $('<a>').attr('href', '#').attr('id',d.wsId).attr('onclick', 'loadServiceDetail(this.id);').html(d.wsName);
	    	 var span_tag=$('<span>');
	    	 var  li_tag = $('<li>');
			 span_tag.appendTo($(div));
	    	 li_tag.appendTo(span_tag);
	    	 href_tag.appendTo(li_tag);
		  }
		});
		 
}

function drawServiceDetailView(){
   
	$('#mainView').empty();
	var detailAccordion=$('<div>').attr('class','easyui-accordion').attr('id','fdAccordion');
	detailAccordion.appendTo($('#mainView'));

	$('#fdAccordion').accordion({
		fit:false,
		multiple:true
	});
	$('#fdAccordion').accordion('add',{
		title:'基本信息',	
		collapsed:false,
		collapsible:false,
		fit:false,
		id:'fsBaseInfo'
		
	});

	$('#fdAccordion').accordion('add',{
		title:'服务接口',
		id:'fsInterface',
		collapsed:false,
		collapsible:false,
		selected:true,
		height:500
		
		

	});

	
}
function drawBaseInfoView(data){
	var fsInfoContainer=$('<div>').attr('id','fsInfoContainer');
	fsInfoContainer.appendTo($('#fsBaseInfo'));
	fsProperty=$('<div>').attr('id','fsProperty');
	fsProperty.appendTo(fsInfoContainer);

	
	$('#fsProperty').propertygrid({
		showGroup:false,
		showHeader:false,
		width:500,
		
		
	});

	var wsId={name:'服务ID',value:data.wsId};
	var wsName={name:'服务名',value:data.wsName};
	var wsPath={name:'服务连接',value:'<a href=".'+data.wsPath+'">.'+data.wsPath+'</a>'};

	$('#fsProperty').propertygrid('appendRow',wsId);
	$('#fsProperty').propertygrid('appendRow',wsName);
	$('#fsProperty').propertygrid('appendRow',wsPath);
	
	var rateInput=$('<input>').attr('id','rateInput');
	rateInput.appendTo($('#fsInfoContainer'));
	
	$('#rateInput').combobox({
		valueField:'label',
		textField:'value',
		data:[{label:'1.0',value:'1.0',selected:true},
		      {label:'0.9',value:'0.9'},
		      {label:'0.7',value:'0.7'},
		      {label:'0.5',value:'0.5'},
		      {label:'0.3',value:'0.3'}],
		
	});
	
	var rateButton=$('<a>').attr('id','rateButton').attr('style','margin-right:10px');
	rateButton.appendTo($('#fsInfoContainer'));
	$('#rateButton').linkbutton({
		text:'评价该服务',
		onClick:function(){
			var value=$('#rateInput').combobox('getValue');
			updateRating(uid,data.wsId,value);
		}
	});
	
	if($.inArray(data.wsId,focusedService)!=-1){
		buttontext='取消关注';
		buttonIcon='icon-remove';
	}else{
		buttontext='关注该服务';
		buttonIcon='icon-add';
	}

	var followButton=$('<a>').attr('id','followButton');
	followButton.appendTo($('#fsInfoContainer'));
	$('#followButton').linkbutton({
		iconCls:buttonIcon,
		text:buttontext,
		onClick:function(){
			if(buttonIcon=='icon-add')
        		addfollowService(uid,data.wsId);
        	else
        		removefollowService(uid,data.wsId);
		}
	});
	
}

function updateRating(uid,wsid,rateValue){
	
	$.ajax({
		type:'POST',
		dataType:'json',
		url:updateRatingURL+"&uid="+uid+"&wsid="+wsid+"&ratevalue="+rateValue,
		success:function(){
			alert("评价服务成功");
		
		},error:function(){
			alert("评价服务失败");
		}
	});
	
}


function addfollowService(uid,wsid){
	$.ajax({
		type:'POST',
		dataType: 'json',
		url:addFocusServiceURL+"&uid="+uid+"&wsid="+wsid,
		success:function(){
			alert('服务关注成功');
			refreshServiceView();
			loadServiceDetail(wsid);
		},error:function(){
			alert('服务关注失败');
		}
	});
}
function removefollowService(uid,wsid){
	$.ajax({
		type:'POST',
		dataType:'json',
		url:removeFocusServiceURL+"&uid="+uid+"&wsid="+wsid,
		success:function(){
			alert('服务取消关注成功');
			refreshServiceView();
			loadServiceDetail(wsid);
		},error:function(){
			alert('服务取消关注失败');
		}
	});
}
function drawServiceInterfaceView(data){

	function drawElementView(container,text,image,cy,alignment){
	    var cBBox=container.getBBox(),
	        paper=container.paper,
	        textX=cBBox.x+cBBox.width/2;//default center
	    
	    
	    var textView=paper.text(textX,cy,text);
	    if(alignment==1){//left
	        var bbox=textView.getBBox();
	        textView.attr({'x':cBBox.x+16+bbox.width/2});
	    }
	    bbox=textView.getBBox();
	    paper.image(image,bbox.x-16,bbox.y,16,16);
	    var rect  =paper.rect(cBBox.x,bbox.y,cBBox.width,bbox.height).attr({'fill-opacity':0,'stroke-opacity':0});
	    return rect;
	}
	// 算法简述：
	// 将画布分为3列，第一列为Service，只有一个图元
	// 第二列是Bindings，从上到下排列，起始坐标为10，每个Binding间隔10个像素
	// 第三列为PortTypes,从上到下排列，起始坐标为10,每个Type间隔10个像素
	// 定义文本的高度
	var oHeight=20,
	// 定义Service图元的宽度
		oWidth=200,
	// 定义缩进距离
		padding=10,
	// 定义圆形图元的半径
		radius=20,
	// 定义圆形图元的圆角弧度
		round=5,
	// 所有图元间的默认空隙
	  gap =10;

	var width=$("#fsInterface").width()-gap*2;
	var height=oHeight+gap;
	
	data.portType.forEach(function(d){
		height+=oHeight*(d.operationName.length+1)+gap;
	});
	height=Math.max(height,$("#fsInterface").height());
	console.info(width);
	console.info(height);
	var paper=Raphael('fsInterface',width,height);
	
	
				
	var portWidth=oWidth+padding*2,
		portHeight=(data.port.length+1)*oHeight;
	
	var serviceX = Math.max(width/6-portWidth/2,gap);
	var serviceY =	gap;
    
	var portRect=paper.rect(serviceX,serviceY,portWidth,portHeight,round); //port 
	var color='#87CEEB';
	var lineColor="#000000";
	portRect.attr({fill:color,stroke:lineColor, "fill-opacity":0.2, "stroke-width": 1});
	
	// Service文本：文本的X、Y坐标是文本的中心位置
	var textY = serviceY + oHeight/2;
	portRect.name=drawElementView(portRect, data.service, 'icons/service_obj.gif',textY);
	// 绘制Service中的所有Port，一个Port一个文本展示，下面辅以分割线
	// 说明：分割线的X坐标必然和Service图元的坐标一致（从边界画到右边界的情况下）
	// 分割线的Y坐标为上一个元素的下边界
	portRect.portText=[];
	portRect.splitline=[];

	data.port.forEach(function(tmp_port,index){

		// 没找到一个就先绘制一条分割线：线的Y坐标为上一个Port的下边界
		var lineY = serviceY + oHeight + index*oHeight;
	    var line = ["M",serviceX, lineY, "L",serviceX+portWidth,lineY].join(",");
	    portRect.splitline.push(paper.path(line).attr({stroke: lineColor,fill: "none", "opacity":1}));

	    // Port的X坐标和Service文本的一致
	    var portY = lineY + oHeight/2;
		portRect.portText.push(drawElementView(portRect, tmp_port, 'icons/port_obj.gif', portY,1));
		
	});
	
	var portTypesRect=[];
	// PortType的X坐标在整个画布的第三段中:至少得保证和PortType之间有10个像素的距离
	var portTypeWidth =oWidth+padding*2;
	var portTypeX = Math.min(5*width/6-portTypeWidth/2,width-(gap+portTypeWidth));


	data.portType.forEach(function(p,index){
		// 先绘制PortType Name;然后以跟Service同样的方式绘制OperationName
		var portTypeHeight=(p.operationName.length+1)*oHeight;
		var pre = 0==index? undefined : portTypesRect[portTypesRect.length-1];
		var portTypeY = undefined==pre? gap : pre.attr("y") + pre.attr("height")+gap;
		var portTypeRect=paper.rect(portTypeX,portTypeY,portTypeWidth,portTypeHeight,round).attr({fill:color,stroke:lineColor, "fill-opacity":0.2, "stroke-width": 2});
		var portTypeTextY = portTypeY + oHeight/2;
		portTypeRect.ptText=drawElementView(portTypeRect,p.portTypeName,'icons/porttype_obj.gif',portTypeTextY);
	    portTypeRect.opText=[];
	    portTypeRect.splitline=[];
		p.operationName.forEach(function(op,indexOpe){

			var lineY = portTypeY + oHeight + indexOpe*oHeight;
	    	var line = ["M",portTypeX, lineY, "L",portTypeX+portTypeWidth,lineY].join(",");
			portTypeRect.splitline.push(paper.path(line).attr({stroke:lineColor,fill:'none','opacity':1}));

			var operationY = lineY + oHeight/2;
			
			portTypeRect.opText.push(drawElementView(portTypeRect, op, 'icons/operation_obj.gif', operationY,1));

		});
		portTypesRect.push(portTypeRect);
		
	});
	
	var  bindingRect=[];

	// Binding的X坐标在整个画布的中间:至少得保证和Service之间有10个像素的距离
	var bindingX = Math.max(width/2-radius/2,portRect.attr("x")+portRect.attr("width")+gap);
	data.binding.forEach(function(binding,index){
		var bindingY = index*(gap+radius)+gap;
		var bindingNode=paper.rect(bindingX,bindingY,radius,radius,round).attr({ cursor: "move"});
		bindingNode.name=binding;
		bindingNode.drag(move,dragger,up);
		bindingRect.push(bindingNode);
	});

	var relations=[];
	for(var port in data.p2b){
		var portIndex=$.inArray(port, data.port);
		var bindingIndex=$.inArray(data.p2b[port],data.binding);
		relations.push(paper.connection(portRect.portText[portIndex],bindingRect[bindingIndex],lineColor,1));
	};
	for (var tmpBinding in data.b2pt)
	{
		var bindingIndex = $.inArray(tmpBinding,data.binding);
		var portTypeIndex = -1;

		for (var i = 0; i <data.portType.length; i++) {

			if (data.portType[i].portTypeName==data.b2pt[tmpBinding])
			{
				portTypeIndex = i;
				break;
			}
		};
		relations.push(paper.connection(bindingRect[bindingIndex],portTypesRect[portTypeIndex].ptText,lineColor,1));
		
	};
	paper.relations=relations;
	
}
function drawServiceRatingView(data){

	$('#mainView').empty();
	var width=$('#mainView').width(),
	height=Math.max(300,$('#mainView').height()),
	cx=width/2,
	cy=height/2,
	r=Math.min(cx,cy)/2;
	
	var length=data.length+4;
	var labels=[1,0.9,0.7,0.5,0.3];
	var values=[1.0,1.0,1.0,1.0,1.0];
	
	data.forEach(function(rating){
		if(rating.rateValue==1)
			values[0]++;
		else if(rating.rateValue==0.9)
			values[1]++;
		else if(rating.rateValue==0.7)
			values[2]++;
		else if(rating.rateValue==0.5)
			values[3]++;
		else if(rating.rateValue==0.3)
			values[4]++;
	});
	values.forEach(function (v,i){
		labels[i]='评价值为'+labels[i]+'的用户有:'+(v-1)+'个';
		values[i]=v/length;
	});

	Raphael("fsRatingInfo", width, height).pieChart(cx, cy, r,values ,labels , "#ff0");
	
}

