var selectedService=null;
function showServiceList(){
	 $.blockUI({ message: '<h1><img src="./images/spinner.gif" /> 加载中...</h1>' }); 
	drawServiceListLayout();
	
	loadFocusService();
	
		
}
function showEvaluateDialog(){
	if(selectedService==null){
		alert('请选择一个服务!');
		return;
	}
	$('#evaluateDialog').dialog({
		title:'评估服务',
		width:300,
		closed:false,
		cache:false,
		buttons: [
		          {
		        	  text:'评估',
		        	  iconCls:'icon-ok',
		        	  handler:function(){
		        		  evaluateService();
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

//************ 评估选中服务 ***********//
function evaluateService(){
	
	$.blockUI({ message: '<h1><img src="./images/spinner.gif" /> 加载中...</h1>' }); 
	$.ajax({
		type:'POST',
		url:getTrustRelationURL+'&uid='+uid+'&threshold=100', //top 100 信任的用户对该服务的评价
		dataType:'json',
		success: function(data){
			loadWsRating(data,selectedService);
		},
		error:function(){
			alert("评估服务失败!");
			window.location = loginURL;
		}
	});
}
//*************** 显示文件上传对话 ***********//、
function showUploadDialog(){
	$('#uploadDialog').dialog({
		title:'上传Web服务',
		width:400,
		closed:false,
		cache:false
	
	});
	$('#uploadsubmit').click(function(){
		$.ajaxFileUpload({
			url:uploadFileServiceURL,
			secureuri:false,
			fileElementId:"uploadfileinput",
			dataType:"json",
			success:function(data,status){
				if(data.err!=1){
					alert(data.msg);
				}
			},
			error:function(data,status,e){
				alert("上传出错");
			}
		});
	});
}
function loadFocusService(){
	 $.ajax({
		  	type: 'POST',
	        url: getFocusWsURL+uid,
	        dataType: 'json',
	        success: function(data) {
	        	
	        	
	        	drawFocusedListView(data);
	        	loadOtherService();
	        	
	        },
			error:function(){
				 alert("获取用户关注的服务失败!");
				 window.location = loginURL;
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
			$.unblockUI();
		},error:function(){
			
			alert("获取其他服务失败！");
			window.location = loginURL;
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
			alert("获取更多服务失败！");
			window.location = loginURL;
		}
	});
}
function loadServiceDetail(wsid){
	$.blockUI({ message: '<h1><img src="./images/spinner.gif" /> 加载中...</h1>' }); 
	$('#serviceDetail').empty();
	drawServiceDetailView();
	
	loadWsBaseInfo(wsid);
	
}
function loadWsBaseInfo(wsid){
	
	$.ajax({
		type:'POST',
		dataType:'json',
		url:getServiceURL+wsid,
		success:function(data){
			selectedService=data;
			drawBaseInfoView(data);
			loadWsInfo(wsid);
		},
		error:function(){
			alert('获取服务信息失败!');
			window.location = loginURL;
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
			$.unblockUI();
		},error:function(){
			alert('获取服务接口信息!');
			window.location = loginURL;
			
		}
	});
}
function loadWsRating(relationData,service){
	console.info("loadWSRating");
	$.ajax({
		type:'POST',
		dataType:'json',
		url:getServiceRatingURL+"&wsid="+service.wsId+"&uid="+uid,
		success:function(ratingData){
			$.unblockUI();
			drawServiceRatingView(relationData,ratingData);
		},error:function(){
			
			alert('评估服务失败!');
			window.location = loginURL;
			
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
		editable:false,
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
		success:function(data){
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
		success:function(data){
			alert('服务关注成功');
			drawFocusedListView(data);
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
		success:function(data){
			alert('服务取消关注成功');
			drawFocusedListView(data);
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
function drawServiceRatingView(relationData,ratingData){

	
	//处理获取的数据
	
	var directTrustUser=[];
	var hiddenTrustUser=[];
	
	
	relationData.relation.forEach(function(trust){
		if(trust.rate==1)
			directTrustUser.push(trust.trustee);
		else 
			hiddenTrustUser.push(trust.trustee);
	});
	console.info(directTrustUser);
	console.info(hiddenTrustUser);
	var histogram=[new Object(),new Object(),new Object(),new Object(),new Object()];
	var map={'0.3':0,'0.5':1,'0.7':2,'0.9':3,'1':4};
	
	var max=0;
	
	ratingData.ratings.forEach(function(rate){
		console.info(map[rate.rateValue]);
		
		if(!histogram[map[rate.rateValue]].all){
			histogram[map[rate.rateValue]].all=0;
			histogram[map[rate.rateValue]].direct=0;
			histogram[map[rate.rateValue]].hidden=0;
			histogram[map[rate.rateValue]].other=0;
		}
		histogram[map[rate.rateValue]].all++;
		
		max=Math.max(max,histogram[map[rate.rateValue]].all);
		if($.inArray(rate.userId,directTrustUser)!=-1||rate.userId==uid)
			histogram[map[rate.rateValue]].direct++;
		else if($.inArray(rate.userId,hiddenTrustUser)!=-1)
			histogram[map[rate.rateValue]].hidden++;
		else
			histogram[map[rate.rateValue]].other++;
		
	});
	
	
	
	$('#mainView').empty();
	var width=$('#mainView').width(),
	height=Math.max(300,$('#mainView').height());

	var paper=Raphael('mainView', width,height);
	
	var elementRefer=(max/10+1).toFixed(0);
	
	var allCount=20;
	
	var histogramCount=12;
	var otherCount=8;
	
	var eLength=width/allCount;
	
	var eHeight=height/allCount;
	
		
	var 
 	histogramH=eHeight*histogramCount,
 	histogramW=eLength*histogramCount,
	histogramY=eHeight*histogramCount;
	histogramX=eLength*otherCount;

	//画横坐标,纵坐标
	 var h = ["M", histogramX, histogramY, "H", histogramX+histogramW].join(","),
	 v=["M", histogramX, histogramY, "V", histogramY-histogramH].join(",");
	 paper.path(h).attr({stroke: '#000',fill: "none", "opacity":1,"arrow-end" :'block-wide-long'});
	 paper.path(v).attr({stroke: '#000',fill: "none", "opacity":1,"arrow-end" :'block-wide-long'});
	 paper.text(histogramX+histogramW-eLength,histogramY+eLength/2,"评价值");
	 paper.text(histogramX-32,histogramY-histogramH+eHeight,"评价用户数");
	//画数据网格
	var label=['0.3','0.5','0.7','0.9','1.0'];
	for(var i=1;i<=10;i++){
		v=["M", histogramX+eLength*i, histogramY, "V", histogramY-histogramH].join(",");
		paper.path(v);
		
		if(i%2==1){
			var textX=histogramX+eLength*i+eLength/2;
			var textY=histogramY+eLength/2;
			paper.text(textX,textY,label[(i-1)/2]);
		}
		h=["M", histogramX, histogramY-eHeight*i, "H", histogramX+histogramW].join(","),
		paper.path(h);	
		paper.text(histogramX-eLength/2, histogramY-eHeight*i, elementRefer*i);
		
	}
	//画说明文文字
	var directColor="#483D8B";
	var hiddenColor="#228B22";
	var otherColor="#20B2AA";
	var directRect=paper.rect(eLength*2.5,eHeight,eLength,eHeight);
    directRect.attr({color:directColor,fill:directColor});
	paper.text(eLength*5,eHeight/2*3,"直接信任用户的评价");
	
	var hiddentRect=paper.rect(eLength*2.5,eHeight*5,eLength,eHeight);
	hiddentRect.attr({color:hiddenColor,fill:hiddenColor});
	paper.text(eLength*5,eHeight/2*11,"前100个信任用户的评价");
	
	var otherRect=paper.rect(eLength*2.5,eHeight*9,eLength,eHeight);
	otherRect.attr({color:otherColor,fill:otherColor});
	paper.text(eLength*5,eHeight/2*19,"其他用户的评价");
	
	
	//画评价直方图
	histogram.forEach(function(data,i){
		var elementX=histogramX+eLength*(2*i+1),
		elementY=histogramY-eHeight*data.all/elementRefer;
		var allRect=paper.rect(elementX,elementY,eLength,eHeight*data.all/elementRefer);
		allRect.attr({color: Raphael.getColor()});
		
		var currentY=elementY;
		var otherRect=paper.rect(elementX,currentY,eLength,eHeight*data.other/elementRefer);
		otherRect.attr({fill:otherColor,color:otherColor});
		
		currentY=currentY+eHeight*data.other/elementRefer;
		var hiddentRect=paper.rect(elementX,currentY,eLength,eHeight*data.hidden/elementRefer);
		hiddentRect.attr({fill:hiddenColor,color:hiddenColor});
		
		currentY=currentY+eHeight*data.hidden/elementRefer;
		var directRect=paper.rect(elementX,currentY,eLength,eHeight*data.direct/elementRefer);
		directRect.attr({fill:directColor,color:directColor});
			
	});

	
	var requirement=$('#trust_requirement').val();
	//画服务可信度
	var trustText=paper.text(eLength*otherCount,eHeight*(histogramCount+3.5),"服务可信度:");
	
	var trustResultRect=paper.rect(eLength*(otherCount+1),eHeight*(histogramCount+3),eLength*otherCount,eHeight);
	trustResultRect.attr({color:"#F00"});
	var rateValue=ratingData.fulRateValue<1?ratingData.fulRateValue:1;
	var fillRect=paper.rect(eLength*(otherCount+1),eHeight*(histogramCount+3),0,eHeight);
	if(rateValue>=requirement)
		fillRect.attr({fill:"#228B22",color:"#228B22"});
	else
		fillRect.attr({fill:"#F00",color:"#F00"});
	
	var bbox=trustResultRect.getBBox();

	var rateText=paper.text(bbox.x+bbox.width/2,bbox.y+bbox.height/2,(rateValue*100).toFixed(3)+"%");
	rateText.hide();
	fillRect.animate({"width": eLength*otherCount*rateValue}, 1000,'linear',function(){
		rateText.show();
	});
	
	
	

	//在console中添加服务评估信息
	
	if(rateValue>=requirement){
		appendSuccessMessage("服务评估成功!");
		appendSuccessMessage(selectedService.wsName+" 可信度:"+ rateValue);
		appendSuccessMessage("***************");
	}else{
		appendErrorMessage("服务评估失败!");
		appendErrorMessage(selectedService.wsName+" 可信度:"+ rateValue);
		appendErrorMessage("***************");
	}
	openConsole();
		
	
	
	
	
	
}

