var stompClient = null;

function connect() {
    var socket = new SockJS(chatUrl);
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function(frame) {
    	stompClient.send('/app/active.users', {});
        stompClient.subscribe('/topic/active.users', function(activeUsers) {
        	renderActiveUsers(JSON.parse(activeUsers.body).activeUsers);
        	stompClient.send('/app/active.users', {});
        });
    });
}

function renderActiveUsers(namesArray) {
	var activeUsersDiv = document.getElementById('activeuserslist');
	activeUsersDiv.innerHTML = "";
	for (i = 0; i < namesArray.length; i++) {
		var lastChar =  namesArray[i].substr(namesArray[i].length - 1);
		if (lastChar == "!") {
			namesArray[i] = namesArray[i].substring(0,namesArray[i].length - 1);
			renderEach(namesArray[i], activeUsersDiv, true);
		} else {
			renderEach(namesArray[i], activeUsersDiv, false);	
		}
	}
}

function renderEach(item, activeUsersDiv, inRoulette) {
	var p = document.createElement('li');
	p.className= "list-group-item";
	var glyp;
	if (inRoulette) {
		glyp = "glyphicon-random";
	} else {
		glyp = "glyphicon-comment";
	}
	p.innerHTML = "<span class=\"glyphicon " + glyp + "\"></span> <a class=\"userlink\" href=\"" + context + "/user/" + item + "\">" + item + "</a>";
	activeUsersDiv.appendChild(p);
}

$('#form').on('submit', function(e) {
	if ($('#new').val() != $('#new2').val()) {
	    e.preventDefault();
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
	        Command: toastr["warning"]("Your passwords don't match")
		    return false;
	}
});
$(function() {
    $(document).tooltip();
});
if (toast == "Successfully saved") {
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
        Command: toastr["success"]("Successfully saved")
};
if (toast == "Password is wrong") {
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
        Command: toastr["error"]("Password is wrong")
};