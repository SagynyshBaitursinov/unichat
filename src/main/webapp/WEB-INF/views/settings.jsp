<%@ page errorPage="error.jsp" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page session="false" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<c:set var="req" value="${pageContext.request}" />
<c:set var="url">${req.requestURL}</c:set>
<c:set var="uri" value="${req.requestURI}" />
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
  	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"> 
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
	<title>Unichat</title>
    <link rel="icon" href="${req.contextPath}/resources/img/logo.png">	    
	<link rel="stylesheet" href="${req.contextPath}/resources/css/jquery-ui.css">
    <link type="text/css" href="${req.contextPath}/resources/css/toastr.css" rel="stylesheet"/>  
    <link rel="stylesheet" type="text/css" href="${req.contextPath}/resources/css/bootstrap.min.css">
    <link rel="stylesheet" type="text/css" href="${req.contextPath}/resources/css/bootstrap-theme.min.css">
    <link rel="stylesheet" type="text/css" href="${req.contextPath}/resources/css/font-awesome.css">
    <link rel="stylesheet" type="text/css" href="${req.contextPath}/resources/css/settings.css">    
</head>
<body onload="connect()">
<!-- navbar -->
  <nav class="navbar  navbar-custom noshadow nbar">
      <div class="container">
          <div class="navbar-header">
              <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#navbar" aria-expanded="false" aria-controls="navbar">
                  <span class="sr-only">Toggle navigation</span>
                  <span class="icon-bar"></span>
                  <span class="icon-bar"></span>
                  <span class="icon-bar"></span>
              </button>
          <a class="navbar-brand" href="https://vk.com/unichatkz">Unichat</a>
          </div>
              <div id="navbar" class="navbar-collapse collapse">
                  <ul class="nav navbar-nav">
                      <li class="visible-xs">
                          <img src="${req.contextPath}/getAvatar?name=${principal}" alt="User Avatar" style="width: 36px; height: 36px; margin-left: 15px" /><b class="navbar-text nickname" style="margin-left: 14px;">${principal}</b>
                      </li>
                      <li class="activeuserslink"><a href="#" data-toggle="modal" data-target="#myModal"><span class="glyphicon glyphicon-globe"></span><b class="hidden-sm"> ACTIVE USERS</b></a></li>
                      <li id="chatbutton"><a href="${req.contextPath}/chat"><span class="glyphicon glyphicon-comment"></span><b class="hidden-sm"> CHAT</b></a></li>
                      <li><a href="${req.contextPath}/roulette"><span class="glyphicon glyphicon-random"></span><b class="hidden-sm"> ROULETTE</b></a></li>
                  </ul>
                  <ul class="nav navbar-nav navbar-right">
                      <li class="hidden-xs" style="transform: translateX(-5%); ">
                          <img src="${req.contextPath}/getAvatar?name=${principal}" alt="${principal}" style="width: 36px; height: 36px; margin-top: 7px; padding: 5px; margin-right: 10px" />
                      </li>
                      <li id="activeli" class="active"><a href="#"><span class="glyphicon glyphicon-cog active-font"></span><b class="hidden-sm active-font"> SETTINGS</b></a></li>
                      <li><a href="${req.contextPath}/logout"><span class="glyphicon glyphicon-log-out"></span><b class="hidden-sm"> LOG OUT</b></a></li>
                  </ul>
              </div><!--/.nav-collapse -->
          </div>
      </nav>

      <!-- chatbox itself -->
    <div class="container-fluid">
        <div class="row ">
            <form action="${req.contextPath}/settings" method="POST" accept-charset="UTF-8" id="form" autocomplete="off">
              <div class="col-md-4 col-md-offset-4 contentcolumn">
                  <div class="form-group">
                    <label for="old">Type your current password </label>
                    <input id="old" class="form-control" maxlength="50" type="password" autocomplete="off" name="password" placeholder="Your current password">
                  </div>
                  <div class="form-group">
                    <label for="new">Type your new password</label>
                    <input class="form-control" type="password" id="new" maxlength="25" autocomplete="off" name="newpassword" placeholder="Your new password (Don't want to change? Keep it empty)" title="Don't want to change? Keep it empty!">
                  </div>
                  <div class="form-group">
                    <label for="new2">Type password once more</label>
                    <input id="new2" class="form-control" type="password" maxlength="25" autocomplete="off" id="confirm" placeholder="Your new password once more">
                  </div>
                  <div class="form-group">
                    <label for="text">Your first message in the roulette</label>
                    <textarea class="form-control" id="text" name="message" maxlength="255" placeholder="Your message to the world" rows="4">${message}</textarea>
                  </div>
                  <!-- <button type="button" class="btn btn-custom btn-block applybutton"><b>APPLY<b></button> -->
              </div>
              <div class="col-md-4 col-md-offset-4 applybuttoncol">
                <button type="submit" class="btn btn-custom btn-block applybutton"><b>APPLY</b></button>
              </div>
            </form>
        </div>
    </div>

    <div id="myModal" class="modal fade" role="dialog">
        <div class="modal-dialog modaldialog">
            <!-- Modal content-->
            <div class="modal-content modalcontent">
              <div class="modal-header">
                <h4 class="modal-title" style="text-align: center;"><span class="glyphicon glyphicon-globe" style="color: #FAFAFA"></span><b style="color: #FAFAFA"> ACTIVE USERS</b></h4>
              </div>
              <div id="activeusersmodal" class="modal-body modalbody" style="overflow-y:scroll">
                <ul class="list-group nopadding" id="activeuserslist" >
                </ul>
              </div>
              <div class="modal-footer">
                <button type="button" class="btn btn-custom btn-block" data-dismiss="modal"><b>CLOSE</b></button>
              </div>
            </div>
        </div>
    </div>
	<script>
	  (function(i,s,o,g,r,a,m){i['GoogleAnalyticsObject']=r;i[r]=i[r]||function(){
	  (i[r].q=i[r].q||[]).push(arguments)},i[r].l=1*new Date();a=s.createElement(o),
	  m=s.getElementsByTagName(o)[0];a.async=1;a.src=g;m.parentNode.insertBefore(a,m)
	  })(window,document,'script','https://www.google-analytics.com/analytics.js','ga');
	
	  ga('create', 'UA-83022135-1', 'auto');
	  ga('send', 'pageview');
	
	</script>
  <script src="${req.contextPath}/resources/js/jquery-2.1.3.min.js"></script>
	<script src="${req.contextPath}/resources/js/jquery-ui.js"></script>
	<script src="${req.contextPath}/resources/js/toastr.js"></script>
	<script src="${req.contextPath}/resources/js/bootstrap.min.js"></script>
	<script src="${req.contextPath}/resources/js/sockjs-0.3.min.js"></script>
	<script src="${req.contextPath}/resources/js/stomp.min.js"></script>
	<script>
    	var chatUrl = "${req.contextPath}/chat";
    	var context = "${req.contextPath}";
		var toast = "${toast}";
	</script>
	<!-- Yandex.Metrika counter --> <script type="text/javascript"> (function (d, w, c) { (w[c] = w[c] || []).push(function() { try { w.yaCounter38640880 = new Ya.Metrika({ id:38640880, clickmap:true, trackLinks:true, accurateTrackBounce:true }); } catch(e) { } }); var n = d.getElementsByTagName("script")[0], s = d.createElement("script"), f = function () { n.parentNode.insertBefore(s, n); }; s.type = "text/javascript"; s.async = true; s.src = "https://mc.yandex.ru/metrika/watch.js"; if (w.opera == "[object Opera]") { d.addEventListener("DOMContentLoaded", f, false); } else { f(); } })(document, window, "yandex_metrika_callbacks"); </script> <noscript><div><img src="https://mc.yandex.ru/watch/38640880" style="position:absolute; left:-9999px;" alt="" /></div></noscript> <!-- /Yandex.Metrika counter -->
  <script src="${req.contextPath}/resources/js/settings.js"></script>
</body>
</html>
