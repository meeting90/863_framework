var paper;

function showAllRelation(){
	 
	 $.blockUI({ message: '<h1><img src="./images/spinner.gif" /> 加载中...</h1>' }); 
	 $.ajax({
		  	type: 'POST',
	        url: getAllRelationURL,
	        dataType: 'json',
	        success: function(data) {
	        	console.info("getAllRelation success");	
	        	openConsole();
	        	drawRelationGraph(data);
	        	$.unblockUI();
	        },
			error:function(){
				console.info("getAllRelation error");
				
				alert("获取社会网络信息失败！");
				//window.location = loginURL;
				
			}
	      });
}

function showExtraDialog(){
	
	
	
	$('#extraDialog').dialog({
		title:'用户信任子网抽取',
		width:300,
		closed:false,
		cache:false,
		buttons: [{
	        text:'抽取',
	        iconCls:'icon-ok',
	        handler:function(){
	        	$('#extraDialog').dialog('close');
	        	extraSubRelation();
	        }
	        	
	        },{
		        text:'关闭',
		        iconCls:'icon-no',
		        handler:function(){
		        	$('#extraDialog').dialog('close');
		        }
	    }]
	});
}


function drawRelationGraph(data){
	
	var width = $("#mainView").width();
	var height = $("#mainView").height();
	drawRelationView("mainView",width,height,data);
	
}

function extraSubRelation(){
	
	var threshold=$('#threshold_input').val();
	console.info(threshold);
	$.blockUI({ message: '<h1><img src="./images/spinner.gif" /> 加载中...</h1>' }); 
	$.ajax({
		type:'POST',
		url:getTrustRelationURL+'&uid='+uid+'&threshold='+threshold,
		dataType:'json',
		success: function(data){
			var width=$('#mainView').width();
			var height=$('#mainView').height();
			drawUserRelationView('mainView',width,height,uid,data);
			$.unblockUI();
		},
		error:function(){
			console.info('extraSubRelation error');
			$.unblockUI();
		}
	});
}

function loadUserData(){
	
	drawUserListLayout();
	loadFocusedUser();
	
	
}

function showUserList(){
	$.blockUI({ message: '<h1><img src="./images/spinner.gif" /> 加载中...</h1>' }); 
	$(document.body).layout('collapse','east');
	$(document.body).layout('expand','east');
	
	loadUserData();
	
}





function loadFocusedUser(){
	console.info(uid);
	  $.ajax({
		  	type: 'POST',
	        url: getFocusedUserURL+uid,
	        dataType: 'json',
	        success: function(data) {
	        	console.info("get Focused User success");	
	        	drawFocusedUserListView(data);
	        	loadUser();
	        	
	        	
	        },
			error:function(){
				alert("获取关注用户列表失败！");
				//window.location = loginURL;
			}
	      });
}
function loadUser(){
	 userOffset=0;
	  $.ajax({
		  	type: 'POST',
	        url: getAllUserURL+userOffset,
	        dataType: 'json',
	        success: function(data) {
	        	console.info("get All User success");	
	        	
	        	drawUserListView(data);
	        	userOffset+=data.length;
	        	$.unblockUI();
	        },
			error:function(){
				console.info("get All User error");
				alert("获取其他用户列表失败！");
				//window.location = loginURL;
			}
	      });
}

function loadMoreUser(){
	  $.ajax({
		  	type: 'POST',
	        url: getAllUserURL+userOffset,
	        dataType: 'json',
	        success: function(data) {
	        	console.info("get All User success");	
	        	
	        	drawMoreUserList(data);
	        	userOffset+=data.length;
	        },
			error:function(){
				alert("获取更多用户列表失败！");
				//window.location = loginURL;
			}
	      });
}

function searchUser(value,name){
	
	userOffset=0;
	  $.ajax({
		  	type: 'POST',
	        url: searchUserURL+value,
	        dataType: 'json',
	        success: function(data) {
	        	drawSearchUserListView(data);
	        },
			error:function(){
				alert("搜索用户失败！");
				window.location = loginURL;
			}
	      });
}

function loadUserDetail(userid){
	console.info('userid:'+userid);
	$.ajax({
		type:'POST',
		url:getUserDetailURL+userid,
		dataType:'json',
		success:function(data){
			drawUserDatailView(data);
			
		},
		eror:function(){
			alert("获取用户详细信息失败！");
			window.location = loginURL;
			
		}
	});
	
}

function addRelation(trustorId,trusteeId){
	
	$.ajax({
		type:'POST',
		dataType:'json',
		url:addRelationURL+'&trustorId='+trustorId+'&trusteeId='+trusteeId,
		success:function(data){
			alert('信任用户成功');
			drawFocusedUserListView(data);
        	loadUser();
		},error:function(){
			alert('取消信任用户失败');
			window.location = loginURL;
		}
		
	});
	
}
function removeRelation(trustorId,trusteeId){
	
	$.ajax({
		type:'POST',
		dataType:'json',
		url:removeRelationURL+'&trustorId='+trustorId+'&trusteeId='+trusteeId,
		success:function(data){
			alert('取消信任用户成功');
			
			drawFocusedUserListView(data);
        	loadUser();
		},error:function(){
			alert("取消信任用户失败");
		}
	});
}

//*****

function drawRelationView(holderid,width,height,data){
	 var
	 relations=[],
	 shapes=[],
	 cx=width/2,
	 cy=height/2,
	 radius=Math.min(cx,cy);
	 if(paper==undefined)
		 paper = Raphael(holderid, width, height);
	 else
		 paper.clear();
	 paper.setStart();
	 var userCount=0;
	 var serviceCount=0;
	 data.nodes.forEach(function(d,i) {
		 var color=Raphael.getColor(),
		 r=Math.random()*radius/5*2 ,
		 alpha=Math.random()*2*Math.PI,
		 cos = Math.cos(alpha),
         sin = Math.sin(alpha);
		 var node;
		 if(d.user){
			 node=paper.circle(cx+r*cos,cy+r*sin,5);
			 userCount++;
		 }
		 else{
			 node=paper.rect(cx+(r+radius/5*3)*cos,cy+(r+radius/5*3)*sin,8,8,2);
			 serviceCount++;
		 }
		 node.attr({fill:color,stroke:color, "fill-opacity":0.2, "stroke-width": 2, cursor: "move",id: d.id});
		 node.data("name",d.name);
		 node.drag(move,dragger,up);
		 shapes.push(node);
		
		});
	
	 var u2uCount=0;
	 u2sCount=0;
	 data.edges.forEach(function(d,i){
		 var color=Raphael.getColor();
		 
		 relations.push(paper.connection(shapes[d.source],shapes[d.target],color));
		 if(shapes[d.target].type=='rect')
			 u2sCount++;
		 else
			 u2uCount++;
		 
	 });
	 paper.relations=relations;
	 paper.setFinish();
	 
	 appendSuccessMessage("服务评价总数:"+u2sCount);
	 appendSuccessMessage("用户显示信任关系总数:"+u2uCount);
	 appendSuccessMessage("服务节点:"+serviceCount);
	 appendSuccessMessage("用户节点:"+userCount);
	 appendSuccessMessage("整体视图加载成功");
	 appendSuccessMessage("********************");

 }

function drawUserRelationView(holderid,width,height,uid,data){
	var 
	 shapes={},
	 cx=width/2,
	 cy=height/2,
	 radius=Math.min(cx,cy);
	
	 
	 if(paper==undefined)
		 paper = Raphael(holderid, width, height);
	 else
		 paper.clear();
	 
	 
	 paper.relations=[];
	 
	 var trustorNode=paper.circle(cx,cy,20);
	 trustorNode.attr({fill:"#f00",stroke: "#f00","fill-opacity":0.7,"stroke-width": 2,cursor:"move",id:uid});
	 trustorNode.data("name",uid);
	 trustorNode.drag(move,dragger,up);
	 shapes[uid]=trustorNode;
	
	 
	 
	 data.relation.forEach(function(d,i) {
		 var color=Raphael.getColor(),
		 r=Math.random()*radius*2/5+radius*3/5 ,
		 alpha=Math.random()*2*Math.PI,
		 cos = Math.cos(alpha),
         sin = Math.sin(alpha);
		 var node=paper.circle(cx+r*cos,cy+r*sin,10);
        
		 
		 node.attr({fill:color,stroke:color, "fill-opacity":0.2, "stroke-width": 2, cursor: "move",id: d.trustee});
		 node.data("name",d.trustee);
		 node.data("rate",d.rate);
		 node.data("fullrate",d.fullrate);
		 node.drag(move,dragger,up);
		
		 shapes[d.trustee]=node;
		 var color=Raphael.getColor();
		 paper.relations.push(paper.connection(trustorNode,node,color,node.data("rate")));
		});


	 data.other.forEach(function(d,i){
		 var color=Raphael.getColor();
		 paper.relations.push(paper.connection(shapes[d.trustorId],shapes[d.trusteeId],color,d.trustValue));
		 
	 });
	 

	 
	 
	

}


//**********
function drawUserListLayout(){
	
	 $('#userList').empty();
	
	  var focusedlist=$('<div>').attr('id','list4');
	  focusedlist.appendTo($('#userList'));
	  
	  var focusedHeader=$('<p>').html('信任的用户');
	  focusedHeader.appendTo(focusedlist);
	  
	  var focusedul=$('<ul>').attr('id','focusedUserList');
	  focusedul.appendTo(focusedlist);
	  
	  var otherlist=$('<div>').attr('id','list4');
	  otherlist.appendTo($('#userList'));
	  
	  var otherHeader=$('<p>').html('其他用户');
	  otherHeader.appendTo(otherlist);
	  
	  var searchdiv=$('<div>').attr('id','searchDiv').attr('style','width:200px;padding:10px;margin-left:auto;margin-right:auto');
	  searchdiv.appendTo(otherlist);
	  var input=$('<input>').attr("id","searchUserInput");
	  
	  input.appendTo(searchdiv);
	  $("#searchUserInput").searchbox({
		  prompt:"输入要搜索的用户名",
		  searcher:searchUser,
		  width:200
		  
		  
	  });
	  var ul=$("<ul>").attr("id","userResult");
	  ul.appendTo(otherlist);

		
}

	
	
//************
function drawFocusedUserListView(data){
	console.info('drawFocusedUserListView');
	focusedUser=[];
	$('#focusedUserList').empty();
	drawUserCore(data,'#focusedUserList',false);
}
function drawUserListView(data){
	console.info('drawUserListView');
	$('#userResult').empty();
	drawUserCore(data,'#userResult',true);
	drawMoreLink('#userResult');
}
function drawSearchUserListView(data){
	$('#userResult').empty();
	drawUserCore(data,'#userResult',true);
}
function drawMoreUserList(data){
	 $('#moreUser').remove();
	 drawUserCore(data,'#userResult',true);
	 drawMoreLink('#userResult');
	
}
function drawMoreLink(div){
	  var href_tag=$('<a style="color:green;">').attr('href','#').attr('onclick','loadMoreUser()').html('more  ...');
	  var span_tag=$('<span>');
	  var li_tag=$('<li>').attr('id','moreUser');
	  span_tag.appendTo($(div));
	  li_tag.appendTo(span_tag);
	  href_tag.appendTo(li_tag);	
}
function drawUserCore(data, div,filter){
	 
	  data.forEach(function (d){
		     if(!filter){
		    	 focusedUser.push(d[0]);
		     }
		    
		  	 if(!filter || $.inArray(d[0],focusedUser)==-1){
				 var href_tag = $('<a>').attr('href', '#').attr('id',d[0]).attr('onclick', "loadUserDetail(this.id);").html(d[1]);
		    	 var span_tag=$('<span>');
		    	 var  li_tag = $('<li>');
				 span_tag.appendTo($(div));
		    	 li_tag.appendTo(span_tag);
		    	 href_tag.appendTo(li_tag);
	  		}
		});
		 
}
function drawUserDatailView(data){
	

	var buttontext,buttonIcon;
	if($.inArray(username,data.fans)!=-1){
		console.info(username);
		console.info($.inArray(username,data.fans));
		buttontext='取消信任';
		buttonIcon='icon-remove';
	}else{
		buttontext='信任';
		buttonIcon='icon-add';
	}
	
	
	var table=$('<table>').attr('id','userDetailpg');
	table.appendTo($('#userDetail'));
	$('#userDetailpg').propertygrid({
		showGroup:true,
		showHeader:false,
		groupFormatter:function(fvalue, rows){
		    return fvalue + '<span style="color:red"> (' + rows.length + ')</span>';
		}
		
	});
	var rows=[];
	console.info(data.userId);
	var userId={name:'用户ID',value:data.userId,group:'基本信息'};
	var userName={name:'用户名',value:data.username,group:'基本信息'};
	var email={name:'邮箱',value:data.email,group:'基本信息'};

	rows.push(userId);
	rows.push(userName);
	rows.push(email);
	
	data.friends.forEach(function(d,i){
		var friend={name:'用户名',value:d,group:'TA信任的用户'};
		rows.push(friend);
	});
	data.fans.forEach(function(d,i){
		var fan={name:'用户名',value:d,group:'信任TA的用户'};
		rows.push(fan);
	});
	data.focusedWebService.forEach(function(d,i){
		var ws={name:d.wsId,value:d.wsName,group:'信任的服务'};
		rows.push(ws);
	});
	$('#userDetailpg').propertygrid('loadData',rows);
	if(uid==data.userId){
		$('#userDetail').dialog({
			title:'当前登录用户',
			width:500,
			closed:false,
			cache:false,
			buttons: [{
		        text:'取消',
		        iconCls:'icon-no',
		        handler:function(){
		        	$('#userDetail').dialog('close');
		        }
		    }]
		});
	}else{
		$('#userDetail').dialog({
			title:'用户详细信息',
			width:500,
			
			closed:false,
			cache:false,
			
			
			buttons: [{
		        text: buttontext,
		        iconCls:buttonIcon,
		        handler:function(){
		        
		        	if(buttontext=='信任')
		        		addRelation(uid,data.userId);
		        	else
		        		removeRelation(uid,data.userId);
		        	$('#userDetail').dialog('close');
		        }
		    },{
		        text:'关闭',
		        iconCls:'icon-no',
		        handler:function(){
		        	$('#userDetail').dialog('close');
		        }
		    }]
			});
		
	}
	
	
	
	$('#userDetail').dialog('open');
	
}

