<%@page import="java.net.URLEncoder"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="db.*"
	import="org.hibernate.*"
	import="java.sql.Timestamp"
	import="java.util.*"
	import="java.text.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>首页</title>
	<link href="css/Alumni.css" rel="stylesheet" type="text/css">
	<link href="css/paper.css" rel="stylesheet" type="text/css">
	<link href="css/menu_red.css" rel="stylesheet" type="text/css" title="styles1"  media="screen" />
	<link href="css/tablesorter.css" rel="stylesheet" type="text/css">
	<script type="text/javascript" src="inc/jquery-1.4.2.min.js"></script>
	<script type="text/javascript" src="inc/jquery.metadata.js"></script>
	<script type="text/javascript" src="inc/mbMenu.js"></script>
	<script type="text/javascript" src="inc/jquery.hoverIntent.js"></script>
	<script type="text/javascript" src="inc/jquery.tablesorter.min.js"></script>
	<script type="text/javascript" src="inc/internalsite.js"></script>
</head>

<%
	if(request.getSession().getAttribute("username") == null) {
		request.getSession().setAttribute("error", "您尚未登录，请登录！");
		response.sendRedirect("index.jsp");
		return;
	}
	String username = (String)request.getSession().getAttribute("username");
	Long userid=(Long)request.getSession().getAttribute("userid");
	String phpHost=(String)request.getSession().getAttribute("php_host");
	
	Cookie nameCookie=new Cookie("name",username);
	nameCookie.setMaxAge(1800);	// expire 30 minutes later
	Cookie idCookie=new Cookie("uid",String.valueOf(userid));
	idCookie.setMaxAge(1800);		// expire 30 minutes later
	Cookie hostCookie=new Cookie("php_host",URLEncoder.encode(phpHost,"UTF-8"));
	hostCookie.setMaxAge(1800);    // expire 30 minutes later
	response.addCookie(nameCookie);
	response.addCookie(idCookie);
	response.addCookie(hostCookie);
	
%>

<body>


<div class="head">
	<!-- <div class="loginfobar">
		，你好！&nbsp;&nbsp;<a href="logout.action">注销</a>
	</div>
	 -->
	<div class="logos">
		<img src="images/seg_logo2.png"/>
	</div>
	<div align="right" style="padding-top: 70px;">
		<a href="Logout" style="text-decoration: underline;color: #0000FF;">注销</a>
	</div>
	<div class="menu">
		<div class="nav">
			<table width="100%" border="0" cellpadding="0" cellspacing="0"
				bgcolor="#203c53">
				<!--style="position:absolute;bottom:0"-->
				<tr>
					<td valign="bottom">
						<table border="0" cellpadding="0" cellspacing="0"
							bgcolor="#FFFFFF" class="container">
							<tr>
								<td class="myMenu" align="right">
									<!-- start horizontal menu -->
									<center>
									<table class="rootVoices" cellspacing='0' cellpadding='0'
										border='0'>
										<tr>
											<td class="rootVoice {menu: 'empty'}"
												onclick="window.location='./main.jsp';">网站首页</td>
											<td class="rootVoice {menu: 'empty'}"
												onclick="window.location='../internetware/BACH/index.jsp'">BACH</td>
											<td class="rootVoice {menu: 'empty'}"
												onclick="window.location='../internetware/SSCC'">SSCC</td>
											 
											 <td class="rootVoice {menu: 'empty'}"
												onclick="window.location='./workflo/trust.html'">SETI</td>
											<td class="rootVoice {menu: 'empty'}"
												onclick="window.location='./workflow/service.html'">SSA</td>
											<td class="rootVoice {menu: 'empty'}"
												onclick="window.location='./workflow/workflow.html'">ASA</td>
										   
											<!-- <td class="rootVoice {menu: 'empty'}"
												onclick="window.location='about.jsp'">关于</td> -->
										</tr>
									</table> <!-- end horizontal menu -->
									</center>
								</td>
							</tr>
						</table>
					</td>
				</tr>
			</table>
		</div>
	</div>
</div><div class="main" id="wapper">
<div class="title">操作记录</div>
<script type="text/javascript">

$(document).ready(function() {	


  //Get all the LI from the #tabMenu UL
  $('#tabMenu > li').click(function(){
    
    //perform the actions when it's not selected
    if (!$(this).hasClass('selected')) {    
           
    //remove the selected class from all LI    
    $('#tabMenu > li').removeClass('selected');
    
    //Reassign the LI
    $(this).addClass('selected');
    
    //Hide all the DIV in .boxBody
    $('.boxBody div').slideUp('1500');
    
    //Look for the right DIV in boxBody according to the Navigation UL index, therefore, the arrangement is very important.
    $('.boxBody div:eq(' + $('#tabMenu > li').index(this) + ')').slideDown('1500');
    
  }
    
  }).mouseover(function() {

    //Add and remove class, Personally I dont think this is the right way to do it, anyone please suggest    
    $(this).addClass('mouseover');
    $(this).removeClass('mouseout');   
    
  }).mouseout(function() {
    
    //Add and remove class
    $(this).addClass('mouseout');
    $(this).removeClass('mouseover');    
    
  });

	//Mouseover with animate Effect for Category menu list
  $('.boxBody #category li').click(function(){

    //Get the Anchor tag href under the LI
    window.location = $(this).children().attr('href');
  }).mouseover(function() {

    //Change background color and animate the padding
    $(this).css('backgroundColor','#888');
    $(this).children().animate({paddingLeft:"20px"}, {queue:false, duration:300});
  }).mouseout(function() {
    
    //Change background color and animate the padding
    $(this).css('backgroundColor','');
    $(this).children().animate({paddingLeft:"0"}, {queue:false, duration:300});
  });  
	
	//Mouseover effect for Posts, Comments, Famous Posts and Random Posts menu list.
  $('#.boxBody li').click(function(){
    window.location = $(this).children().attr('href');
  }).mouseover(function() {
    $(this).css('backgroundColor','#888');
  }).mouseout(function() {
    $(this).css('backgroundColor','');
  });  	
	
  // call the tablesorter plugin 
  $("table").tablesorter({ 
    // sort on the first column , order asc 
    sortList: [[2,0]] 
  }); 
});

</script>

<div class="boxBody">
  
  <div style="display: block;" id="posts" class="show">
    <center>
    <table width="830" border="0" cellpadding="0" cellspacing="1" class="tablesorter">
	  <thead>
		<tr>
			<th width="450" scope="col">说明</th>
			<th width="120" scope="col">用户</th>
			<th width="180" scope="col">时间</th>
			<th width="120" scope="col">操作</th>
		</tr>
      </thead>
	  <tbody>
	  	<%
	  		HistoryDAO historyDAO = new HistoryDAO();
	  		UserDAO userDAO = new UserDAO();
			List<User> userList = userDAO.findByUsername(username);
	  		List<History> historyList = historyDAO.findByUserId(userList.get(0).getId());
	  	%>
		<%
			Iterator it = historyList.iterator();
			Format format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			while(it.hasNext()){
				History history = (History)it.next();
		%>
				<tr>
					<td align="left"><%=history.getDescription() %></td>
					<td align="center"><%=userDAO.findById(history.getUserId()).getUsername() %></td>
					<td align="center"><%=format.format(history.getAccessTime()) %></td>
					<%
					  String url = null;
					  if(history.getServiceId()==1) {
						  url = "<a href='SSCC/display.jsp?dirid=" + history.getParam() + "'>查看</a>";
					  } else if(history.getServiceId()==2) {
						  url = "<a href='BACH/index.jsp?link=true&" + history.getParam() + "'>查看</a>";
					  }
					 else if(history.getServiceId()==5) {
						  url = "<a href='workflowevaluation/seti_display.html?link=true&document="+history.getDescription()+"&param=" + history.getParam() + "'>查看</a>";
					  } 
					  else if(history.getServiceId()==3) {
						  url = "<a href='workflowevaluation/ssa_display.html?link=true&param=" + history.getParam() + "'>查看</a>";
					  } else if(history.getServiceId()==4) {
						  url = "<a href='workflowevaluation/asa_display.html?link=true&param=" + history.getParam() + "'>查看</a>";
					  } 
					%>
					<td align="center"><%=url %></td>
				</tr>
		  <%} //while循环结束%> 
	  </tbody>
	</table> 
	</center>
  </div>        

</div>



</div>
</div>

<div class="bottom">

<div>
<span style="padding:0 10px;">&copy; 2013 NJU Software Engineering Group Nanjing, 210046, P.R.CHINA</span><br/>
<span style="padding:0 10px;">Tel: (86-25)8359-3433</span>
<span style="padding:0 10px;">Email: <a href="mailto:bbs@seg.nju.edu.cn">bbs@seg.nju.edu.cn</a></span>
</div>
</div>

</body>
</html>