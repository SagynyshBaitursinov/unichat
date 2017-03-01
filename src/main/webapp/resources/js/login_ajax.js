var status = 0;
var oldMail = "";
var progress = false;

$('#register_form').on('submit', function(e) {
    e.preventDefault();
    if (status == 1) {
    	var emailStr = $('#email').val().trim();
    	var passStr = $('#password-input').val(); 
	    $.ajax({
		    type: "POST",
		    url: springSecurityUrl,
		    data: $('#register_form').serialize()
		}).done(function (data) {
			if (data == "ok") {
				window.location = rouletteUrl;
			} else if (data == "settings") {
				window.location = settingsUrl;
			}
		}).fail(function (data) {
			toastr.options = {
		              "closeButton": false,
		              "debug": false,
		              "newestOnTop": true,
		              "progressBar": false,
		              "positionClass": "toast-top-center",
		              "preventDuplicates": false,
		              "onclick": null,
		              "showDuration": "300",
		              "hideDuration": "1000",
		              "timeOut": "5000",
		              "extendedTimeOut": "1000",
		              "showEasing": "swing",
		              "hideEasing": "linear",
		              "showMethod": "fadeIn",
		              "hideMethod": "fadeOut"
		    	}
		        Command: toastr["error"]("Your password is incorrect", "Error")
	    });
    }
    return false;
});

function reset() {
	oldMail = "";
	status = 0;
	emailUnconfirm();
	$('#registerButton').remove();
	$('#pass-container').remove();
	$('#dive').remove();
	onProgressRemove();
}

function onRegistered() {
	status = 1;
	emailConfirm();
	$('#registerButton').remove();
	$('#pass-container').remove();
	$('#dive').remove();
	onProgressRemove();
	var pass = "<div id = \"pass-container\" class='col-md-12 inputrow '><div class='col-md-4 emailicon hidden-xs hidden-sm'><div id='emailicon'><span class='fa fa-lock fa-3x' aria-hidden='true'></span>"
		+"</div></div><div id='emailAppend' class='col-md-5 emailcol'><div id='emailinput' class='row'>"+
				"<input id=\"password-input\" type='password' name='password' placeholder='Please, enter you password' onfocus='this.placeholder = ''' onblur='this.placeholder'"+
				" = 'Please, enter you NU email'  /></div></div><div class='col-md-3 emaildummy'></div>";
	
	var dive = "<div class='row' id='password-input'><input type=\"submit\" class=\"btn btn-primary col-md-12\" value=\"DIVE\"/ id =\"dive\"></div>";
	$(pass).hide().appendTo("#appendTo").fadeIn(500);
	$(dive).hide().appendTo("#emailAppend").fadeIn(500);
    toastr.options = {
        "closeButton": false,
        "debug": false,
        "newestOnTop": true,
        "progressBar": false,
        "positionClass": "toast-top-center",
        "preventDuplicates": true,
        "onclick": null,
        "showDuration": "300",
        "hideDuration": "1000",
        "timeOut": "5000",
        "extendedTimeOut": "1000",
        "showEasing": "swing",
        "hideEasing": "linear",
        "showMethod": "fadeIn",
        "hideMethod": "fadeOut"
    }
    Command: toastr["success"]("Please login", "Great!")
}

function onProgress(id) {
	if (!progress) {
		progress = true;
		var scope = "<span class=\"fa fa-cog fa-spin spinner\" id=\"spinner\"></span>";
		var appendedToId =  document.getElementById(id);
		$(scope).appendTo(appendedToId);
	}
}

function onProgressRemove() {
	if (progress) {
		progress = false;
		$('.spinner').fadeOut(1000, function() { $(this).remove(); });
	}
}

function emailConfirm(){
	var icon = document.getElementById("emailNotConfirmed");
	//$(icon).attr("id","emailConfirmed");
}

function emailUnconfirm(){
	var icon = document.getElementById("emailConfirmed");
	//$(icon).attr("id","emailNotConfirmed");
}

function doCheck() {
	var emailStr = $("#email").val().trim();
	if (!progress && emailStr.length > 0 && oldMail !== emailStr) {
		if (emailStr.substring(emailStr.length - 10, emailStr.length) == '@nu.edu.kz') {
    		$.ajax({
    		    method: "GET",
    		    url: checkUrl,
    		    data: {email:emailStr},
    		    ifModified: true,
    		    beforeSend: function(xhr) {
    		    	xhr.overrideMimeType("text/plain; charset=x-user-defined");
    		    	$('#progress').remove();
        		    onProgress("email-container");    		    	
    		    }
    		}).done(function(data) {
    			oldMail = emailStr;
                if (data == "registered") {
                	if (status != 1) {
                		onRegistered();
                	}
                } else if (data == "not registered") {
                	if (status != 2) {
                		status = 2;
                		$('#password').remove();
                		$('#dive').remove();
                		/*var registerButton = "<button type=\"button\" class=\"btn btn-primary btn-full registerButton\" id=\"registerButton\">Register</button>";*/
                		/*var registerButton = "<div class='col-md-12'><div class='col-md-5 emailcol'><div class='row' id='password-input'><input type=\"submit\" class=\"btn btn-primary col-md-12\" value=\"REGISTER\"/ id=\"registerButton\"></div></div>";*/
                		var pass = "<div id = \"pass-container\" class='col-md-12 inputrow '><div class='col-md-4 emailicon hidden-xs hidden-sm'><div id='emailicon'>"
                			+"</div></div><div id='emailAppend' class='col-md-5 emailcol'><div id='emailinput' class='row'>"+
                					"</div></div><div class='col-md-3 emaildummy'></div>";
                		
                		var dive = "<div class='row' id='password-input'><input type=\"submit\" class=\"btn btn-primary col-md-12\" value=\"REGISTER\"/ id=\"registerButton\"></div>";
                		$(pass).hide().appendTo("#appendTo").fadeIn(500);
                		$(dive).hide().appendTo("#emailAppend").fadeIn(500);
                		$("#registerButton").click(function() {
	                    	$.ajax({
	                		    method: "POST",
	                		    url: registrationUrl,
	                		    data: {email:emailStr},
	                		    ifModified: true,
	                		    beforeSend: function(xhr) {
	                		    	onProgress("email-container");
	                		    	xhr.overrideMimeType("text/plain; charset=x-user-defined");
	                		    	$('#registerButton').prop('disabled', true);
	                		    }
	                		}).done(function(data) {
	                			onProgressRemove();
	                			if (data == "success") {
	                				toastr.options = {
	          	                          "closeButton": false,
	          	                          "debug": false,
	          	                          "newestOnTop": true,
	          	                          "progressBar": false,
	          	                          "positionClass": "toast-top-center",
	          	                          "preventDuplicates": true,
	          	                          "onclick": null,
	          	                          "showDuration": "300",
	          	                          "hideDuration": "1000",
	          	                          "timeOut": "10000",
	          	                          "extendedTimeOut": "1000",
	          	                          "showEasing": "swing",
	          	                          "hideEasing": "linear",
	          	                          "showMethod": "fadeIn",
	          	                          "hideMethod": "fadeOut"
	          	                	}
	          	                    Command: toastr["success"]("We have sent your password via email", "Welcome!")  
	                				onRegistered();
	                		    	emailConfirm();
		                			$('#registerButton').prop('disabled', false);
	                			} else {
	                				onProgressRemove();
	                				toastr.options = {
	          	                          "closeButton": false,
	          	                          "debug": false,
	          	                          "newestOnTop": true,
	          	                          "progressBar": false,
	          	                          "positionClass": "toast-top-center",
	          	                          "preventDuplicates": true,
	          	                          "onclick": null,
	          	                          "showDuration": "300",
	          	                          "hideDuration": "1000",
	          	                          "timeOut": "5000",
	          	                          "extendedTimeOut": "1000",
	          	                          "showEasing": "swing",
	          	                          "hideEasing": "linear",
	          	                          "showMethod": "fadeIn",
	          	                          "hideMethod": "fadeOut"
	          	                	}
	          	                    Command: toastr["error"]("Bad things happened", "Error")  
		                			$('#registerButton').prop('disabled', false);
	                			}
	                		});
	                    });
                		onProgressRemove();
	                    toastr.options = {
	                          "closeButton": false,
	                          "debug": false,
	                          "newestOnTop": true,
	                          "progressBar": false,
	                          "positionClass": "toast-top-center",
	                          "preventDuplicates": false,
	                          "onclick": null,
	                          "showDuration": "3000",
	                          "hideDuration": "1000",
	                          "timeOut": "5000",
	                          "extendedTimeOut": "1000",
	                          "showEasing": "swing",
	                          "hideEasing": "linear",
	                          "showMethod": "fadeIn",
	                          "hideMethod": "fadeOut"
	                	}
	                    Command: toastr["info"]("It seems you are new in here", "Welcome")  
                	}
                } else if (data == "null") {
            		status = 0;
            		$('#dive').remove();
            		$('#registerButton').remove();
            		$('#pass-container').remove();
            		onProgressRemove();
            		toastr.options = {
              			"closeButton": false,
                        "debug": false,
                        "newestOnTop": true,
                        "progressBar": false,
                        "positionClass": "toast-top-center",
                        "preventDuplicates": true,
                        "onclick": null,
                        "showDuration": "1000",
                        "hideDuration": "1000",
                        "timeOut": "5000",
                        "extendedTimeOut": "1000",
                        "showEasing": "swing",
                        "hideEasing": "linear",
                        "showMethod": "fadeIn",
                        "hideMethod": "fadeOut"
                  	}
                  	Command: toastr["warning"]("Email is incorrect or not registered in our system yet")
                }
		  	});
		} else {
			reset();
		}
	} else if (emailStr.length == 0) {
		reset();
	}
}

var myInterval = 0;

function startLoop() {
    if (myInterval > 0) clearInterval(myInterval);
    myInterval = setInterval("doCheck()", 400);
}

$(document).ready(function() {
	startLoop();
});

String.prototype.endsWith = function (s) {
	return this.length >= s.length && this.substr(this.length - s.length) == s;
}