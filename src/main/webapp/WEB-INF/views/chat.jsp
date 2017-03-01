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
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"> 
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Unichat</title>   
    <link rel="icon" href="${req.contextPath}/resources/img/logo.png">
    <link rel="stylesheet" type="text/css" href="${req.contextPath}/resources/css/bootstrap.min.css">
    <link rel="stylesheet" type="text/css" href="${req.contextPath}/resources/css/font-awesome.css">
    <link rel="stylesheet" type="text/css" href="${req.contextPath}/resources/css/chat.css">    
    <link rel="stylesheet" type="text/css" href="${req.contextPath}/resources/css/xbbcode.css">
    <link type="text/css" href="${req.contextPath}/resources/css/toastr.css" rel="stylesheet"/>
</head>
<body onload="connect();">
<!-- navbar -->
  <nav class="navbar  navbar-custom noshadow nopadding " >
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
                  <li class="hidden-lg hidden-md"><a href="#" data-toggle="modal" data-target="#myModal"><span class="glyphicon glyphicon-globe"></span><b class="hidden-sm"> ACTIVE USERS</b></a></li>
              <li id="activeli" class="active"><a><span class="glyphicon glyphicon-comment active-font"></span><b class="hidden-sm active-font"> CHAT</b></a></li>
              <li><a href="${req.contextPath}/roulette"><span class="glyphicon glyphicon-random"></span><b class="hidden-sm"> ROULETTE</b></a></li>
          </ul>
          <ul class="nav navbar-nav navbar-right">
            <li class="hidden-xs" style="transform: translateX(-6%) ">
              <img src="${req.contextPath}/getAvatar?name=${principal}" alt="User Avatar" style="width: 36px; height: 36px; margin-top: 7px; padding: 5px; margin-right: 10px" />
            </li>
            <li class="volume soundclick"><a onclick="toggleSound()"><span id="volumeglyph" class="glyphicon glyphicon-volume-up"></span><b class="hidden-sm hidden-md hidden-lg"> SOUND</b></a></li>
              <li><a href="${req.contextPath}/settings"><span class="glyphicon glyphicon-cog"></span><b class="hidden-sm"> SETTINGS</b></a></li>
              <li><a href="${req.contextPath}/logout"><span class="glyphicon glyphicon-log-out"></span><b class="hidden-sm"> LOG OUT</b></a></li>
          </ul>
        </div><!--/.nav-collapse -->
      </div>
  </nav>

<div class="container-fluid chatbox">
        <div class="row">
            <div class="col-md-3 col-md-offset-1 visible-md visible-lg activeusersheader">
                <ul class="list-group nopadding" style="border-radius: 0px; margin-right: -10px; margin-left: 1px">
                  <li class="list-group-item border-up" style="background-color:#232A30; text-align: center; transform: translateY(-12%)"><span class="glyphicon glyphicon-globe" style="color: #FAFAFA"></span><b style="color: #FAFAFA;"> ACTIVE USERS<span style="height: 15px"></span></b></li>
                </ul>
            </div>
        </div>
        <div class="row chatboxrow nopadding" >
          <div class="col-md-3 col-md-offset-1 visible-md visible-lg activeusersrow">
                <ul class="list-group nopadding" id="activeuserslist">
                	<li class="list-group-item activeuseritem">Chat is empty</li>
                </ul>
          </div>

          <div class="col-md-7 chatboxcol">
                <div class="panel-borderless panel-primary chatboxpanel" >
                    <div class="panel-body chatboxpanelbody" id="scrollablechatwindow">
                        <ul class="chat" id="chatul">
                        </ul>
                    </div>
                </div>
                

                <div class="row inputrow hidden-xs hidden-sm">
                    <div class="col-md-12 nopadding">
                        <form role="form" class="inputform">
                            <div class="form-group nopadding">
                                <textarea class="form-control inputarea" maxlength="255" rows="3" id="comment1"></textarea>
                            </div>
                        </form>
                    </div>
                </div>

                <div class="row inputrow visible-xs visible-sm">
                    <div class="col-md-12 nopadding">
                        <form role="form" class="inputform">
                            <div class="form-group nopadding">
                                <textarea class="form-control inputarea" maxlength="255" rows="3" id="comment2" onclick='document.getElementById("scrollablechatwindow").scrollTop = document.getElementById("scrollablechatwindow").scrollHeight;'></textarea>
                            </div>
                        </form>
                    </div>
                </div>


                <div class="row">
                    <div class="col-md-12 hidden-lg nopadding">
                        <button class="btn btn-custom btn-block sendbutton" onclick="this.blur(); sendMessage2(); sendMessage();"> <b>SEND</b></button>
                    </div>
                </div>

              </div>

            </div>
        </div>
        

        
    <!-- </div> -->

    <!-- MODAL ACTIVE USERS FOR PHONES -->
    <div id="myModal" class="modal fade" role="dialog">
        <div class="modal-dialog modaldialog">

            <!-- Modal content-->
            <div class="modal-content modalcontent">
              <div class="modal-header">
                <h4 class="modal-title" style="text-align: center;"><span class="glyphicon glyphicon-globe" style="color: #FAFAFA"></span><b style="color: #FAFAFA"> ACTIVE USERS</b></h4>
              </div>
              <div id="activeusersmodal" class="modal-body modalbody" style="overflow-y:scroll">
                <ul class="list-group nopadding" id="activeuserslist2">
					<li class="list-group-item activeuseritem">Chat is empty</li>
                </ul>
              </div>
              <div class="modal-footer">
                <button type="button" class="btn btn-custom btn-block" data-dismiss="modal"><b>CLOSE</b></button>
              </div>
            </div>

        </div>
    </div>
<audio id="msgSound"> 
  <source src="${req.contextPath}/resources/sounds/ula.mp3" type="audio/mpeg">
  <source src="${req.contextPath}/resources/sounds/ula.ogg" type="audio/ogg">
  <source src="${req.contextPath}/resources/sounds/ula.wav">
</audio>
<script src="${req.contextPath}/resources/js/sockjs-0.3.min.js"></script>
<script src="${req.contextPath}/resources/js/stomp.min.js"></script>
<script src="${req.contextPath}/resources/js/jquery-2.1.3.min.js"></script>
<script src="${req.contextPath}/resources/js/toastr.js"></script>
<script src="${req.contextPath}/resources/js/chat.js"></script>
<script src="${req.contextPath}/resources/js/bootstrap.min.js"></script>
<script src="${req.contextPath}/resources/js/xbbcode.js"></script>
<script>
    var name = "${principal}";
    var myMessage = "${mymessage}";
    var chatUrl = "${req.contextPath}/chat";
    var context = "${req.contextPath}";
    var lastMessages = "${req.contextPath}/getlastmessages"
</script>
<script>
    $(document).ready(function(){
	    var _originalSize = $(window).width() + $(window).height();
	    $(window).resize(function(){
	      if($(window).width() + $(window).height() != _originalSize){
	        document.getElementById("scrollablechatwindow").scrollTop = document.getElementById("scrollablechatwindow").scrollHeight;
	      }else{
	        document.getElementById("scrollablechatwindow").scrollTop = document.getElementById("scrollablechatwindow").scrollHeight; 
	      }
	    });
    });
</script>
<script>
  (function(i,s,o,g,r,a,m){i['GoogleAnalyticsObject']=r;i[r]=i[r]||function(){
  (i[r].q=i[r].q||[]).push(arguments)},i[r].l=1*new Date();a=s.createElement(o),
  m=s.getElementsByTagName(o)[0];a.async=1;a.src=g;m.parentNode.insertBefore(a,m)
  })(window,document,'script','https://www.google-analytics.com/analytics.js','ga');

  ga('create', 'UA-83022135-1', 'auto');
  ga('send', 'pageview');

</script>
<script type="text/javascript"> (function (d, w, c) { (w[c] = w[c] || []).push(function() { try { w.yaCounter38640880 = new Ya.Metrika({ id:38640880, clickmap:true, trackLinks:true, accurateTrackBounce:true }); } catch(e) { } }); var n = d.getElementsByTagName("script")[0], s = d.createElement("script"), f = function () { n.parentNode.insertBefore(s, n); }; s.type = "text/javascript"; s.async = true; s.src = "https://mc.yandex.ru/metrika/watch.js"; if (w.opera == "[object Opera]") { d.addEventListener("DOMContentLoaded", f, false); } else { f(); } })(document, window, "yandex_metrika_callbacks"); </script> <noscript><div><img src="https://mc.yandex.ru/watch/38640880" style="position:absolute; left:-9999px;" alt="" /></div></noscript> <!-- /Yandex.Metrika counter -->
</body>
</html>
