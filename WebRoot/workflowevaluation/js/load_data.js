var gUserName = null;
var gUID = null;
var gServiceListName = [];
var gServiceListUserData = new Object();
var gServiceListEvaluateData = new Object();
var gUserDataFlag;
var gEvaluateDataFlag;
var loginUrl;

function checkLogin() {
  gUserName = $.cookie('name');
  gUID = $.cookie('uid');
  if (gUserName==null || gUID==null) {
	  return false;
  } else {
	  return true;
  }
}

function refreshLoginStatus() {
  if (checkLogin()) {
	  $('#login').html(gUserName);
	  logout_tag =  $("<a href='#' onclick='logout();'>Logout</a>").html('[Logout]');
	  logout_tag.appendTo($('#login'));
  } else {
	  $('#login').html('<a href="'+loginUrl+'">Login</a>');
  } 
}

function logout() {
  gUserName = null;
  gUID = null;
  $.cookie('name', null);
  $.cookie('uid', null);
  window.location.replace(loginUrl);
}

function getServiceList() {
  gServiceListName = [];
  gIsFinishGetServiceListName = false;
  var serviceListUrl = 'http://workflowevaluation.sinaapp.com/api.php?apiname=listservice';
  $.ajax({
    url: serviceListUrl,
    dataType: 'json',
    success: function(data) {
      $.each(data, function(key, val){
        gServiceListName.push(val);
      });
      getAllServiceUserData();
    }
  });
}

function getAllServiceEvaluateData() {
  gServiceListEvaluateData = new Object();
  var length = gServiceListName.length;
  for (var i=0; i<=length-1; i++) {
	  getServiceEvaluateData(gServiceListName[i]);
  }
}

function getServiceEvaluateData(serviceName) {
  initServiceEvaluateDataFlagArray();
  var serviceListDataUrlBase = 'http://workflowevaluation.sinaapp.com/api.php?apiname=servforcast&uid=1&servname=';
  var serviceListDataUrl = serviceListDataUrlBase + serviceName;
  $.ajax({
    url: serviceListDataUrl,
	  dataType: 'json',
    success: function(evaluateData) {
	    gEvaluateDataFlag[serviceName] = true;
	    gServiceListEvaluateData[serviceName] = evaluateData;
	    if (checkEvaluateDataFinish()) {
//		    $.unblockUI();
		    refreshServiceList();
      }
    }
  });
}

function getAllServiceUserData() {
  gServiceListUserData = new Object();
  var length = gServiceListName.length;
  for (var i=0; i<=length-1; i++) {
	  getServiceUserData(gServiceListName[i]);
  }
}

function getServiceUserData(serviceName) {
  initServiceUserDataFlagArray();
  var serviceListUserDataBase = 'http://workflowevaluation.sinaapp.com/api.php?apiname=evaparaminfo&uid=1&sname=';
  var serviceListUserDataUrl = serviceListUserDataBase+serviceName;
  $.ajax({
    url: serviceListUserDataUrl,
	  dataType: 'json',
    success: function(userData) {
	    gServiceListUserData[serviceName] = userData;
		  gUserDataFlag[serviceName] = true;
		  if (checkUserDataFinish()) {
			  getAllServiceEvaluateData();
		  }
	  }
  });
}

function initServiceUserDataFlagArray() {
  gUserDataFlag = new Object();
  for (var i=0; i<gServiceListName.length; i++) {
	  gUserDataFlag[gServiceListName[i]] = false;
  }
}

function checkUserDataFinish() {
  for (var i=0; i<gServiceListName.length; i++) {
	  if (!gUserDataFlag[gServiceListName[i]]) {
		  return false;
	  }
  }
  return true;
}

function initServiceEvaluateDataFlagArray() {
  gEvaluateDataFlag = new Object();
  for (var i=0; i<gServiceListName.length; i++) {
    gEvaluateDataFlag[gServiceListName[i]] = false;
  }
}

function checkEvaluateDataFinish() {
  for (var i=0; i<gServiceListName.length; i++) {
    if (!gEvaluateDataFlag[gServiceListName[i]]) {
      return false;
    }
  }
  return true;
}

function refreshServiceList() {
  for (var i=0; i<gServiceListName.lengthlogo-nju1.jpg; i++) {
    href_tag = $('<a>').attr('href', '#').attr('onclick', "editService('"+gServiceListName[i]+"')").html(""+gServiceListName[i]+"");
	  li_tag = $('<li>');
	  li_tag.appendTo($('#service_list'));
	  href_tag.appendTo(li_tag);
  }
}

