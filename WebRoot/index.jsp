<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<HTML>
<HEAD>
<TITLE>登录</TITLE>
<META HTTP-EQUIV="Content-Type" CONTENT="text/html; charset=utf-8">
<style type="text/css">
<!--
br {
	font-family: Arial, Helvetica, sans-serif;
	font-size: 12px;
}
.input {
	font-family: Arial, Helvetica, sans-serif;
	font-size: 12px;
	color: #192267;
	height: 18px;
	width: 180px;
	border-top-width: 1px;
	border-right-width: 1px;
	border-bottom-width: 1px;
	border-left-width: 1px;
	border-top-style: solid;
	border-right-style: solid;
	border-bottom-style: solid;
	border-left-style: solid;
	border-top-color: 192267;
	border-right-color: 192267;
	border-bottom-color: 192267;
	border-left-color: 192267;
}
.text {
	font-family: Arial, Helvetica, sans-serif;
	font-size: 12px;
	color: #192267;
}

body {
	background-color: #F1FAF7;
}
tr {
	font-family: Arial, Helvetica, sans-serif;
	font-size: 12px;
}
-->
</style>
</HEAD>

<script language="javascript">
function loginsubmit()
{
   if(document.log_form.username.value=="")
   {
     alert("您的用户名忘了填写了！");
	 document.log_form.username.focus();
	 return false;
   }
   else if(document.log_form.password.value=="")
   {
     alert("您的密码忘了填写了！");
	 document.log_form.password.focus();
	 return false;
   }
   else 
   {
     document.log_form.submit();
   }
}
</script>


<BODY LEFTMARGIN=0 TOPMARGIN=0 MARGINWIDTH=0 MARGINHEIGHT=0>
<table width="100%" height="100%"  border="0" cellpadding="0" cellspacing="0">
  <tr>
    <td><div align="center">
        <table width="681" border="0" align="center" cellpadding="0" cellspacing="0">
    
          <tr> 
            <td colspan="2"><img src="images/login_01.gif" width=681 height=74 alt=""></td>
          </tr>
          <tr> 
            <td width="241" rowspan="2"><img src="images/login_02.gif" width="241" height="336"></td>
            <td width="440" height="222" background="images/login_03.gif" >
            <form method = "post" action="Login" name="log_form" onsubmit="return loginsubmit()">
            <table width="80%"  border="0" cellspacing="0" cellpadding="0">
            
            			<%if (session.getAttribute("error") != null) {%>
            			<font size=4 color='red'><%=session.getAttribute("error") %></font>
            			<%} %>
                <tr> 
                  <td width="30%" height="25"><div align="right" class="text">用户名：</div></td>
                  <td width="70%"><input name="username" type="text" class="input"></td>
                </tr>
                <tr> 
                  <td height="25"><p align="right" class="text">密码：</p></td>
                  <td><input name="password" type="password" class="input"></td>
                </tr>
                <tr> 
                  <td height="35"><div align="left"> </div></td>
                  <td height="35">
                  	<input name="button" type="submit" style="width:60px;"  id="button" value="登录">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                  	<a href="register.jsp" tabindex="9">点此注册新用户</a>
                  </td>
                </tr>
            </table></form></td>
          </tr>
          <tr>
            <td ><img src="images/login_04.gif" width="440" height="114"></td>
          </tr>
        </table>
    </div></td>
  </tr>
</table>
</BODY>
</HTML>