var stompClient;
var friend = "";

function connect() {
	$('#comment1').attr("readonly", true); 
	$('#comment2').attr("readonly", true); 
	$('#comment1').val("We are looking for a pair for you");
	$('#comment2').val("We are looking for a pair for you");
    var socket = new SockJS(chatUrl);
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function(frame) {
        console.log('Connected: ' + frame);
        stompClient.subscribe('/topic/active.users', function(activeUsers) {
        	renderActiveUsers(JSON.parse(activeUsers.body).activeUsers);
        });
    	stompClient.send('/app/roulette', {});
        stompClient.subscribe('/topic/roulette', function(message) {
        	stompClient.send('/app/roulette', {});
        });
        stompClient.subscribe('/user/queue/roulette', function(message) {
        	result = JSON.parse(message.body);
        	if (result.type == 0) {
        		if (friend == "") {
        			renderPair(result.message);
        			playNewPair();
        			if (result.message2 != null) {
	        			renderMessage(myMessage, true);
	        			renderMessage(result.message2, false);
        			}
        		} else {
        			if (result.message != friend) {
        				goOn();
        			} else if (result.message2 != null) {
	        			renderMessage(myMessage, true);
	        			renderMessage(result.message2, false);
        			}
        		}
        	} else if (result.type == 1) {
            	play();
        		renderMessage(result.message, false);
        	} else if (result.type == 2) {
        		if (friend != "") {
	    			friend = "";
	    			$('#comment1').attr("readonly", true); 
	    			$('#comment2').attr("readonly", true); 
	    			$('#comment1').val("Your pair has been disconnected from the conversation. Hit next!");
	    			$('#comment2').val("Your pair has been disconnected from the conversation. Hit next!");
	            	stompClient.disconnect();
        		}
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

function renderPair(pair) {
	friend = pair;
	$('#comment1').attr("readonly", false); 
	$('#comment2').attr("readonly", false); 
	$('#comment1').val("");
	$('#comment2').val("");
}

function escapeHTML(html) {
    return html.replace(/&/g,'&amp;').replace(/</g,'&lt;').replace(/>/g,'&gt;');
}

function renderMessage(message, own) {
	if (message == null || message == "" || friend == "") {
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
    	sender = toTitleCase(name);
    } else {
    	li.className = "left clearfix";
    	cls = "none";
    	cls2 = "pull-left";
    	sender = friend;
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

function toTitleCase(str) {
    return str.replace(/\w\S*/g, function(txt){return txt.charAt(0).toUpperCase() + txt.substr(1).toLowerCase();});
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

function sendMessage() {
	if (friend == "") {
		return;
	}
    var whisper = $("#comment1");
    whisper.val(whisper.val().replace(/\n/g, ""));
    whisper.val(whisper.val().replace(/<br>/g, ""));
    whisper.val(whisper.val().replace(/;&nbsp;/g, ""));
    whisper.val(whisper.val().replace(/&nbsp;/g, ""));
    whisper.val(whisper.val().replace(/\n/g, ""));
    if (!(whisper.val().replace(/\s/g,"") == "")) {
    	stompClient.send("/app/whisper", {}, whisper.val());
    	renderMessage(whisper.val(), true);
    	whisper.val("");
    	$("#scrollablechatwindow").scrollTop($("#scrollablechatwindow")[0].scrollHeight);
    }
}

function sendMessageXSSM() {
	if (friend == "") {
		return;
	}
	var whisper = $("#comment2");
    whisper.val(whisper.val().replace(/\n/g, ""));
    whisper.val(whisper.val().replace(/<br>/g, ""));
    whisper.val(whisper.val().replace(/;&nbsp;/g, ""));
    whisper.val(whisper.val().replace(/&nbsp;/g, ""));
    whisper.val(whisper.val().replace(/\n/g, ""));
    if (!(whisper.val().replace(/\s/g,"") == "")) {
    	stompClient.send("/app/whisper", {}, whisper.val());
    	renderMessage(whisper.val(), true);
    	whisper.val("");
    	$("#scrollablechatwindow").scrollTop($("#scrollablechatwindow")[0].scrollHeight);
    }
}

$(document).keypress(function(e) {
    if(e.keyCode == 13) {
        e.preventDefault();
        sendMessage();
        sendMessageXSSM();
    }
});

function goOn() {
	friend = "";
	if (stompClient.connected) {
		stompClient.send("/app/next", {});
		stompClient.disconnect();
	}
	$("#chatul").html("");
	$('#comment1').attr("readonly", true); 
	$('#comment2').attr("readonly", true); 
	$('#comment1').val("We are looking for a pair for you");
	$('#comment2').val("We are looking for a pair for you");
	connect();
}

var userMsg = document.getElementById('chatul');
userMsg.onclick = function(event) {
	var target = getEventTarget(event);
	if (target.id == "userMsg"){
		ajaxInfo(target.innerHTML.trim());	
	}	
}

function getEventTarget(e) {
    e = e || window.event;
    return e.target || e.srcElement; 
}

function ajaxInfo(name){
	 $.ajax({
		    method: "GET",
		    url: context + "/getInfo?name=" + name,
		    beforeSend: function(xhr) {
		    	xhr.overrideMimeType("text/plain; charset=x-user-defined");    		    	
		    }
		}).done(function(data) {
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
			    Command: toastr["info"](data);	
		});	
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

function playNewPair() {
    var audio = document.getElementById('newPair');
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