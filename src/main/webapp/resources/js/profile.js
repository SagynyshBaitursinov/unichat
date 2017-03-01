var stompClient;
var timer = null;
var online = false;

function connect() {
	$('#comment1').attr("readonly", true); 
	$('#comment2').attr("readonly", true);
	$('#comment1').val("This person is not in the private chatroom with you - First, ask to join the conversation");
	$('#comment2').val("This person is not in the private chatroom with you - First, ask to join the conversation");
    var socket = new SockJS(chatUrl);
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function(frame) {
        console.log('Connected: ' + frame);
    	stompClient.send('/app/private.users', {}, personName);
        stompClient.subscribe('/topic/private.users', function() {
        	stompClient.send('/app/private.users', {}, personName);
        });
        stompClient.subscribe('/topic/active.users', function(activeUsers) {
        	renderActiveUsers(JSON.parse(activeUsers.body).activeUsers);
        });
        stompClient.subscribe('/user/queue/private', function(message) {
        	result = JSON.parse(message.body);
        	if (result.type == 2 && result.name == personName) {
        		if (timer != null) {
        			clearTimeout(timer);
        		}
    		    makeGreen();
    			timer = setTimeout(function() {makeRed();}, 5000);
        	} else if (result.type == 1 && result.name == personName) {
        		play();
        		renderMessage(result.message, false);
        	} else if (result.type == 3) {
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
                Command: toastr["error"]("Don't flood, please")  
  			}
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

$(document).keypress(function(e) {
    if(e.keyCode == 13) {
        e.preventDefault();
        send();
        send2();
    }
});

function send() {
	if (online) {
		var msg = $("#comment1").val();
		$("#comment1").val(""); 
	    msg = msg.replace(/\n/g, "");
	    msg = msg.replace(/<br>/g, "");
	    msg = msg.replace(/;&nbsp;/g, "");
	    msg = msg.replace(/&nbsp;/g, "");
	    msg = msg.replace(/\n/g, "");
	    if (!(msg.replace(/\s/g,"") == "")) {
	    	var message = {
    			name: personName,
    			message : msg
	    	}
			stompClient.send('/app/private', {}, JSON.stringify(message));
	    	renderMessage(message.message, true);
			$("#scrollablechatwindow").scrollTop($("#scrollablechatwindow")[0].scrollHeight);
	    }
	}
}

function send2() {
	if (online) {
		var msg = $("#comment2").val();
		$("#comment2").val(""); 
	    msg = msg.replace(/\n/g, "");
	    msg = msg.replace(/<br>/g, "");
	    msg = msg.replace(/;&nbsp;/g, "");
	    msg = msg.replace(/&nbsp;/g, "");
	    msg = msg.replace(/\n/g, "");
	    if (!(msg.replace(/\s/g,"") == "")) {
	    	var message = {
    			name: personName,
    			message : msg
	    	}
			stompClient.send('/app/private', {}, JSON.stringify(message));
	    	renderMessage(message.message, true);
			$("#scrollablechatwindow").scrollTop($("#scrollablechatwindow")[0].scrollHeight);
	    }
	}
}

function toTitleCase(str) {
    return str.replace(/\w\S*/g, function(txt){return txt.charAt(0).toUpperCase() + txt.substr(1).toLowerCase();});
}

function renderMessage(message, own) {
	if (message == null || message == "") {
		return;
	}
	message = XBBCODE.process({
		text: escapeHTML(message),
	    removeMisalignedTags: false,
	    addInLineBreaks: false  
	}).html;
    var li = document.createElement('li');
    var dt = new Date();
    var hours, minutes, seconds;
    if (dt.getHours() < 10) {
    	hours = "0" + dt.getHours();
    } else {
    	hours = dt.getHours();
    }
    if (dt.getMinutes() < 10) {
    	minutes = "0" + dt.getMinutes();
    } else {
    	minutes = dt.getMinutes();
    }
    if (dt.getSeconds() < 10) {
    	seconds = "0" + dt.getSeconds();
    } else {
    	seconds = dt.getSeconds();
    }
    if (own) {
    	cls = "right-message";
    	li.className = "right clearfix";
    	cls2 = "pull-right";
    	sender = toTitleCase(myName);
    } else {
    	li.className = "left clearfix";
    	cls = "none";
    	cls2 = "pull-left";
    	sender = personName;
    }
    var a = 
    	"<span class=\"chat-img " + cls2 + "\">" +
        "<div class=\"img-circle\">" +
            "<img src=\""+context+"/getAvatar?name="+sender+"\" alt=\"User Avatar\"/>" +
        "</div>" +
        "</span>" +
	      "<div class=\"chat-body clearfix\">" +
	          "<div class=\"header\">";
    if (own) {
    	a = a + "<small class=\"text-muted\"> <span class=\"glyphicon glyphicon-time\"></span>" + hours + ":" + minutes + "</small>" +
    	"<strong class=\"primary-font userMsg pull-right\" id=\"userMsg\">" + sender + "</strong>" +
        "</div>" + 
        "<p class=\"" + cls + "\">" + message + "</p>" +
    "</div>";
    } else {
    	a = a + "<strong class=\"primary-font userMsg\" id=\"userMsg\">" + sender + "</strong>" +
    	"<small class=\"pull-right text-muted\"> <span class=\"glyphicon glyphicon-time\"></span>" + hours + ":" + minutes + "</small>" +
	    "</div>" + 
	    "<p class=\"" + cls + "\">" + message + "</p>" +
	    "</div>";
    }
    li.innerHTML = a;
    $("#chatul").append(li);
    if(bottom == true){
		$("#scrollablechatwindow").scrollTop($("#scrollablechatwindow")[0].scrollHeight);
    }
}

function escapeHTML(html) {
    return html.replace(/&/g,'&amp;').replace(/</g,'&lt;').replace(/>/g,'&gt;');
}

$(document).ready(function () {
    $('#scrollablechatwindow').bind('scroll', chk_scroll);
});
var bottom = true;
function chk_scroll(e) {
    var elem = $(e.currentTarget);
    if (elem[0].scrollHeight - elem.scrollTop() - elem.outerHeight() < 200) {
        bottom = true;
    } else{
    	bottom = false;
    }
}

function makeGreen() {
	online = true;
	$('#comment1').attr("readonly", false); 
	$('#comment2').attr("readonly", false); 
	$('#comment1').val("");
	$('#comment2').val("");
	$("#ifonline").removeClass("offline").addClass("online");
}

function makeRed() {
	online = false;
	$('#comment1').attr("readonly", true); 
	$('#comment2').attr("readonly", true);
	$('#comment1').val("This person is not in the private chatroom with you - First, ask to join the conversation");
	$('#comment2').val("This person is not in the private chatroom with you - First, ask to join the conversation");
	$("#ifonline").removeClass("online").addClass("offline");
}

function play() {
    var audio = document.getElementById('msgSound');
    if (audio.paused) {
        audio.play();
    }else{
        audio.pause();
        audio.currentTime = 0;
        audio.play();
    }
}

function toggleSound(){
	var audio = document.getElementById('msgSound');
	var soundIcon = document.getElementById('volumeglyph');
	if (audio.muted) {
		soundIcon.className = "glyphicon glyphicon-volume-up";
		audio.muted = false;
    }else{
        audio.muted = true;
        soundIcon.className = "glyphicon glyphicon-volume-off";
    }
}