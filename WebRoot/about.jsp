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
	//System.out.println("username:" + username);
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
												onclick="window.location='BACH/index.jsp'">BACH</td>
											<td class="rootVoice {menu: 'empty'}"
												onclick="window.location='SSCC'">SSCC</td>
											<td class="rootVoice {menu: 'empty'}"
												onclick="window.location='about.jsp'">关于</td>
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
<div class="title">关于</div>
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
  
  <div style="display: block; text-align: left;" id="posts" class="show">
    	<p style="font-size:16px;">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;网构化软件可信评估工具服务集是一个可扩展的工具和服务平台，可以通过以下方式改造已有的工具。
    	工具构造好之后，请将工具的war包发给管理员，通过审核后即可将工具部署到平台中。
    	</p>
    	<!-- <p style="font-size:16px;">
    	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;一、数据库表结构
    	</p>
    	<p style="font-size:16px;"></p> -->
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