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
	 <meta http-equiv="X-UA-Compatible" content="IE=edge">
	 <meta name="viewport" content="width=device-width, initial-scale=1">
	    <title>Unichat</title>
	    <link rel="icon" href="${req.contextPath}/resources/img/logo.png">
	    <link type="text/css" href="${req.contextPath}/resources/css/toastr.css" rel="stylesheet"/>
	    <link rel="stylesheet" type="text/css" href="${req.contextPath}/resources/css/bootstrap.min.css">
	    <link rel="stylesheet" type="text/css" href="${req.contextPath}/resources/css/font-awesome.css">
   	    <link rel="stylesheet" type="text/css" href="${req.contextPath}/resources/css/main.css">
	</head>    
	<body>	
	<div id="background">
		<div class="container-fluid main-content">
			<div class="row">
				<div class="col-lg-12">
					<p id="label">Unichat</p>
				</div>
			</div>
         	<form action="${req.contextPath}/j_spring_security_check" method="POST" accept-charset="UTF-8" id="register_form">
				<div id="appendTo" class="row">
					<div class="col-md-12 inputrow ">
						<div class="col-md-4 emailicon hidden-xs hidden-sm">
							<div id="emailicon">
								<span class="fa fa-envelope fa-2x" aria-hidden="true">
								</span>
							</div>
						</div>
						<div class="col-md-5 emailcol">
							<div id="emailinput" class="row">
								<input type="email" id="email" name="username" placeholder="Please, enter you NU email"  onfocus="this.placeholder = ''" onblur="this.placeholder = 'Please, enter you NU email'" />
							</div>
						</div>
					</div>
				</div>
			</form>
		</div>
	</div>
	<script src="${req.contextPath}/resources/js/jquery-2.1.3.min.js"></script>
	<script src="${req.contextPath}/resources/js/bootstrap.min.js"></script>
	<script src="${req.contextPath}/resources/js/toastr.js"></script>
	<script src="${req.contextPath}/resources/js/login_ajax.js"></script>
	<script>
		var checkUrl = "${req.contextPath}/exists";
		var rouletteUrl = "${req.contextPath}/${app}";
		var settingsUrl = "${req.contextPath}/settings";
		var springSecurityUrl = "${req.contextPath}/j_spring_security_check";
		var registrationUrl = "${req.contextPath}/register";
	    var forgot = "${req.contextPath}/forgot";
	</script>
	<!-- Yandex.Metrika counter --> <script type="text/javascript"> (function (d, w, c) { (w[c] = w[c] || []).push(function() { try { w.yaCounter38640880 = new Ya.Metrika({ id:38640880, clickmap:true, trackLinks:true, accurateTrackBounce:true }); } catch(e) { } }); var n = d.getElementsByTagName("script")[0], s = d.createElement("script"), f = function () { n.parentNode.insertBefore(s, n); }; s.type = "text/javascript"; s.async = true; s.src = "https://mc.yandex.ru/metrika/watch.js"; if (w.opera == "[object Opera]") { d.addEventListener("DOMContentLoaded", f, false); } else { f(); } })(document, window, "yandex_metrika_callbacks"); </script> <noscript><div><img src="https://mc.yandex.ru/watch/38640880" style="position:absolute; left:-9999px;" alt="" /></div></noscript> <!-- /Yandex.Metrika counter -->
	<script>
	  (function(i,s,o,g,r,a,m){i['GoogleAnalyticsObject']=r;i[r]=i[r]||function(){
	  (i[r].q=i[r].q||[]).push(arguments)},i[r].l=1*new Date();a=s.createElement(o),
	  m=s.getElementsByTagName(o)[0];a.async=1;a.src=g;m.parentNode.insertBefore(a,m)
	  })(window,document,'script','https://www.google-analytics.com/analytics.js','ga');
	
	  ga('create', 'UA-83022135-1', 'auto');
	  ga('send', 'pageview');
	
	</script>
	</body>
</html>