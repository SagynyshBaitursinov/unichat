var stompClient = null;

function connect() {
    var socket = new SockJS(chatUrl);
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function(frame) {
        console.log('Connected: ' + frame);
        $.getJSON(lastMessages, function(data) {
        	for (i = 0; i < data.length; i++) {
            	showMessage(data[i].sender, data[i].date, data[i].message);
        	}
        	$("#scrollablechatwindow").scrollTop($("#scrollablechatwindow").prop("scrollHeight"));
        });
        stompClient.subscribe('/topic/shout', function(whisper) {
        	result = JSON.parse(whisper.body);
        	play();
        	showMessage(result.sender, result.date, result.message);
        });
        stompClient.subscribe('/user/queue/shout', function(message) {
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
        });
    	stompClient.send('/app/active.users', {});
        stompClient.subscribe('/topic/active.users', function(activeUsers) {
        	renderActiveUsers(JSON.parse(activeUsers.body).activeUsers);
        	stompClient.send('/app/active.users', {});
        });
    });
}

function renderActiveUsers(namesArray) {
	var activeUsersDiv = document.getElementById('activeuserslist');
	var activeUsersDiv2 = document.getElementById('activeuserslist2');
	activeUsersDiv.innerHTML = "";
	activeUsersDiv2.innerHTML = "";
	if (!namesArray || namesArray.length == 0)  {
		var p = document.createElement('ul');
		p.className = "list-group-item activeuseritem";
		p.innerHTML = "<span>Chat is empty<span>";
		var p2 = document.createElement('ul');
		p2.className = "list-group-item activeuseritem";
		p2.innerHTML = "<span>Chat is empty<span>";
		activeUsersDiv.appendChild(p);
		activeUsersDiv2.appendChild(p2);
	}
	for (i = 0; i < namesArray.length; i++) {
		var lastChar =  namesArray[i].substr(namesArray[i].length - 1);
		if (lastChar == "!") {
			namesArray[i] = namesArray[i].substring(0,namesArray[i].length - 1);
			renderEach(namesArray[i], activeUsersDiv, true);
			renderEach(namesArray[i], activeUsersDiv2, true);
		} else {
			renderEach(namesArray[i], activeUsersDiv, false);
			renderEach(namesArray[i], activeUsersDiv2, false);	
		}
	}
}

function renderEach(item, activeUsersDiv, inRoulette) {
	var p = document.createElement('ul');
	p.className= "list-group-item activeuseritem";
	var glyp;
	if (inRoulette) {
		glyp = "glyphicon-random";
	} else {
		glyp = "glyphicon-comment";
	}
	p.innerHTML = "<span class=\"glyphicon " + glyp + "\"></span> <a class=\"userlink\" href=\"" + context + "/user/" + item + "\">" + item + "</a>";
	activeUsersDiv.appendChild(p);
}

function sendMessage() {
    var shout = $("#comment1");
    shout.val(shout.val().replace(/\n/g, ""));
    shout.val(shout.val().replace(/<br>/g, ""));
    shout.val(shout.val().replace(/;&nbsp;/g, ""));
    shout.val(shout.val().replace(/&nbsp;/g, ""));
    if (!(shout.val().replace(/\s/g,"") == "")) {
    	stompClient.send("/app/shout", {}, shout.val());
    	shout.val("");
		$("#scrollablechatwindow").scrollTop($("#scrollablechatwindow")[0].scrollHeight);
    }
}

function sendMessage2() {
    var shout = $("#comment2");
    shout.val(shout.val().replace(/\n/g, ""));
    shout.val(shout.val().replace(/<br>/g, ""));
    shout.val(shout.val().replace(/;&nbsp;/g, ""));
    shout.val(shout.val().replace(/&nbsp;/g, ""));
    if (!(shout.val().replace(/\s/g,"") == "")) {
    	stompClient.send("/app/shout", {}, shout.val());
    	shout.val("");
		$("#scrollablechatwindow").scrollTop($("#scrollablechatwindow")[0].scrollHeight);
    }}

$(document).keypress(function(e) {
    if (e.keyCode == 13) {
        e.preventDefault();
        sendMessage();
        sendMessage2();
    }
});

function showMessage(sender, date, message) {
	message = XBBCODE.process({
		text: escapeHTML(message),
	    removeMisalignedTags: false,
	    addInLineBreaks: false  
	}).html;
	var dt = new Date(date);
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
    var li = document.createElement('li');
    var cls;
    var myname = toTitleCase(name);
    if(sender == myname){
    	cls = "right-message";
    	li.className = "right clearfix";
    	cls2 = "pull-right";
    	cls3 = "none";
    } else if (sender == 'Unichat') {
    	li.className = "left clearfix";
    	cls = "none";
    	cls2 = "pull-left";
    	cls3 = "unichatmsg";
    } else {
    	li.className = "left clearfix";
    	cls = "none";
    	cls2 = "pull-left";
    	cls3 = "none";
    }
    var a = 
    	"<span class=\"chat-img " + cls2 + "\">" +
        "<div class=\"img-circle\">" +
            "<img src=\""+context+"/getAvatar?name="+sender+"\" alt=\"User Avatar\"/>" +
        "</div>" +
        "</span>" +
	      "<div class=\"chat-body clearfix\">" +
	          "<div class=\"header\">";
    if (sender == myname) {
    	a = a + "<small class=\"text-muted\"> <span class=\"glyphicon glyphicon-time\"></span>" + hours + ":" + minutes + "</small>" +
    	"<strong class=\"primary-font userMsg pull-right\" id=\"userMsg\">" + sender + "</strong>" +
        "</div>" + 
        "<p class=\"" + cls + "\">" + message + "</p>" +
    "</div>";
    } else {
    	a = a + "<strong class=\"primary-font userMsg\" id=\"userMsg\">" + sender + "</strong>" +
    	"<small class=\"pull-right text-muted\"> <span class=\"glyphicon glyphicon-time\"></span>" + hours + ":" + minutes + "</small>" +
	    "</div>" + 
	    "<p class=\"" + cls + " " + cls3 + "\">" + message + "</p>" +
	    "</div>";
    }
    li.innerHTML = a;
    $("#chatul").append(li);
	if(bottom == true) {
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
    } else {
    	bottom = false;
    }
}

function escapeHTML(html) {
    return html.replace(/&/g,'&amp;').replace(/</g,'&lt;').replace(/>/g,'&gt;');
}

function getEventTarget(e) {
    e = e || window.event;
    return e.target || e.srcElement; 
}

var userMsg = document.getElementById('chatul');

userMsg.onclick = function(event) {
	var target = getEventTarget(event);
	if (target.id == "userMsg"){
		ajaxInfo(target.innerHTML.trim());	
	}	
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
    } else {
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