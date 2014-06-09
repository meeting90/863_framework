<?php
require_once "session.inc";
require_once "Model.class.php";
ob_start();
?>
<html>
  <head>
  <meta charset="UTF-8">
    <title></title>
    <?php
      if(!isset($_POST['user_login']) || ($_POST['user_login'] != 'Sign in')){
    ?>

    <style type="text/css">
    /* Reset */
      body,div,dl,dt,dd,ul,ol,li,h1,h2,h3,h4,h5,h6,pre,form,fieldset,input,textarea,p,blockquote,th,td { margin:0; padding:0; }
      table { border-collapse:collapse; border-spacing:0; }
      fieldset,img { border:0; }
      address,caption,cite,code,dfn,em,strong,th,var { font-style:normal; font-weight:normal; }
      ol,ul { list-style:none; }
      caption,th { text-align:left; }
      h1,h2,h3,h4,h5,h6 { font-size:100%; font-weight:normal; }
      q:before,q:after { content:''; }
      abbr,acronym { border:0; }
      
      /* Font,  Link & Container */
      body { font:12px/1.6 arial,helvetica,sans-serif; }
      a:link { color:#369;text-decoration:none; }
      a:visited { color:#669;text-decoration:none; }
      a:hover { color:#fff;text-decoration:none;background:#039; }
      a:active { color:#fff;text-decoration:none;background:#f93; }
      button { cursor:pointer;line-height:1.2; }
      .mod { width:100%; }
      .hd:after, .bd:after, .ft:after, .mod:after {content:'\0020';display:block;clear:both;height:0; }
      .error-tip { margin-left:10px; }
      .error-tip, .error { color:#fe2617; }
          
      /* Layout */
      .wrapper { float:center; width:1280px;margin:0 auto; }
      #header { padding-top:30px; }
      #content { min-height:400px;*height:400px; }
      #header, #content { margin-bottom:40px; }
      #header, #content, #footer { width:100%;overflow:hidden; }
      #footer { color:#999;padding-top:6px;border-top: 1px dashed #ddd; }
      .article { float:left;width:490px; }
      .aside { float:right;width:310px;color:#666; }
     
      /* header */
      .logo { float:left; width:1280px;  height:180px; overflow:hidden; line-height:15em; }
      a.logo:link,
      a.logo:visited,
      a.logo:hover,
      a.logo:active { background:transparent  no-repeat;
        background-image:url(./logo.jpg); }
      h1 { color:#494949;display:block;font-size:25px;font-weight:bold;line-height:1.1;margin:0;padding:0 0 30px;word-wrap:break-word; }
       
      /* form */
      .item { margin:15px 0;zoom:1; }
      label { display: inline-block; float:left; margin-right: 15px; text-align: right; width: 60px; font-size: 14px; line-height: 30px; vertical-align: baseline }
      .remember { cursor: pointer; font-size: 12px; display: inline; width: auto; text-align: left; float: none; margin: 0; color: #666 }
      
      .basic-input { width: 200px; padding: 5px; height: 30px; font-size: 14px;vertical-align:middle; -moz-border-radius: 3px; -webkit-border-radius: 3px; border-radius: 3px; border: 1px solid #c9c9c9 }
      
      .basic-input:focus { border: 1px solid #a9a9a9 }
      
      .btn-submit { cursor: pointer;color: #ffffff;background: #3fa156; border: 1px solid #528641; font-size: 14px; font-weight: bold; padding:6px 26px; border-radius: 3px; -moz-border-radius: 3px; -webkit-border-radius: 3px; *width: 100px;*height:30px; }
      .btn-submit:hover { background-color:#4fca6c;border-color:#6aad54; } 
      .btn-submit:active { background-color:#3fa156;border-color:#528641; } 
      #item-error { padding-left:75px; }
       
      /* footer */
      .fright { float:right; }
      .icp { float:left; }
      </style> 
      <script> 
      function loadTestImage(action){
        frm = document.getElementById('lzform');
        frm.action=action;
      }
      </script> 
  </head>
  <body>
    <div class="wrapper">
      <div id="header">
        <a href="Login.php" class="logo"></a>
      </div>
      <div id="content">
        <h1>Sign in Health Assessment System</h1>
        <div class="article">
          <form id="lzform" method="post" action="<?php echo $PHP_SELF; ?>" onsubmit="return validateForm(this);">
            
            <div class="item">
              <label>Username</label>
              <input id="username" name="form_username" type="text" class="basic-input" maxlength="60" tabindex="1"/>
            </div>
            <div class="item">
              <label>Password</label>
              <input id="password" name="form_password" type="password" class="basic-input" maxlength="20" tabindex="2"/>
            </div>
            <div>
              <label></label>
              <input type="submit" value="Sign in" name="user_login" class ="btn-submit" tabindex="5"/>
            </div>
          </form>
        </div>
      </div>
      <div id="footer">
        <span id="icp" class="fleft gray-link">
          @2011 神州数码, all right reserved
        </span>
        <span class="fright">
          <a ></a>
          <a href="api.php">API</a>
        </span>
      </div>
    </div>
    <script>
    function validateForm(frm) {
      var error = 0,
      
      username = frm.elements['form_username'],
      passwd = frm.elements['form_password'],
      defaultError = document.getElementById('item-error');
 
      if (defaultError) {
        defaultError.style.display = 'none';
      }
 
      
      if (username && username.value === '') {
        displayError(username, 'need username');
        error = 1;
      } else {
        username && clearError(username);
      }
      if (passwd && passwd.value === '') {
        displayError(passwd, 'need password');
        error = 1;
      } else {
        passwd && clearError(passwd);
      }
      return !error;
    }
 
    function displayError(el, msg) {
      var err = document.getElementById(el.name + '_err');
      if (!err) {
        err = document.createElement('span');
        err.id = el.name + '_err';
        err.className = 'error-tip';
        el.parentNode.appendChild(err);
      }
      err.style.display = 'inline';
      err.innerHTML = msg;
    }
 
    function clearError(el) {
      var err = document.getElementById(el.name + '-err');
      if (err) {
        err.style.display = 'none';
      }
    }
    </script>
  </body>
</html>


<?php
} else {
  //echo "received:".$_POST['user_login'];
  if(trim($_POST['form_username']) != '' && trim($_POST['form_password']) != ''){
    $user = new Model('user_info');
    $userName = trim($_POST['form_username']);
    $tmp = $user->queryByName($userName);
    if($tmp){
      if($tmp[0]->password == trim($_POST['form_password'])){
        $_SESSION['uid'] = $tmp[0]->id;
        $_SESSION['name'] = $tmp[0]->name;
        setcookie("uid", $tmp[0]->id);
        setcookie("name", $tmp[0]->name);
        
        header("Location:./tool1.html");
        return true;
      } else {
        unset($_POST['user_login']);
        if(isset($_SESSION['uid']))
          unset($_SESSION['uid']);
        //header("Location:./tool1.html");
        exit('登录失败！点击此处 <a href="javascript:history.back(-1);">返回</a> 重试');
      }
    } else {
      unset($_POST['user_login']);
      if(isset($_SESSION['uid']))
          unset($_SESSION['uid']);
      //header("Location:./Login.php");
      exit('登录失败！点击此处 <a href="javascript:history.back(-1);">返回</a> 重试');
    }
  }
}
?>
</html>