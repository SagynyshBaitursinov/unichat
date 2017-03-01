package kz.codingwolves.unichat.controllers;

import java.security.Principal;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import com.google.gson.Gson;

import kz.codingwolves.unichat.MessageStorer;
import kz.codingwolves.unichat.models.ChatMessage;
import kz.codingwolves.unichat.models.PrivateMessage;
import kz.codingwolves.unichat.models.RouletteMessage;
import kz.codingwolves.unichat.usercheckers.ActiveUserService;
import kz.codingwolves.unichat.usercheckers.MessageNumber;
import kz.codingwolves.unichat.usercheckers.RouletteWheel;

@Controller
public class ChatController {
	
	@Autowired
	private MessageStorer messageStorer;
	
	@Resource
	private List<MessageNumber> messageNumber;
	
	@Autowired
	private ActiveUserService activeUserService;
	
	@Autowired
	private SimpMessagingTemplate template;
	
	@Autowired
	private RouletteWheel wheel;
			
    @MessageMapping("/shout")
    public void shout(Principal principal, String message) throws Exception {
    	if (!isNotBanned((principal.getName()))) {
	    	template.convertAndSendToUser(principal.getName(), "/queue/shout", "");
    		return;
    	}
    	if (message.length() > 255) {
    		return;
    	}
    	message = message.replaceAll("\\s+", " ");
    	ChatMessage chatMessage = new ChatMessage(principal.getName(), new Date(), message);
    	messageStorer.add(chatMessage);
        template.convertAndSend("/topic/shout", chatMessage);
    }
    
    @MessageMapping("/whisper")
    public void whisper(Principal principal, String message) throws Exception {
    	if (!isNotBanned((principal.getName()))) {
	    	template.convertAndSendToUser(principal.getName(), "/queue/roulette", new RouletteMessage(3, ""));
    		return;
    	}
    	if (message.length() > 255) {
    		return;
    	}
    	String pair = wheel.getPair(principal.getName());
    	if (pair != null) {
    		message = message.replaceAll("\\s+", " ");
	    	RouletteMessage toBeSent = new RouletteMessage(1, message);
	    	template.convertAndSendToUser(pair, "/queue/roulette", toBeSent);
    	}
    }
    
    @MessageMapping("/private")
    public void privateMessage(Principal principal, String message) throws Exception {
    	if (!isNotBanned((principal.getName()))) {
	    	template.convertAndSendToUser(principal.getName(), "/queue/private", new PrivateMessage(3, ""));
    		return;
    	}
    	PrivateMessage privateMessage = new Gson().fromJson(message, PrivateMessage.class);
    	if (privateMessage.getMessage().length() > 255) {
    		return;
    	}
    	template.convertAndSendToUser(privateMessage.getName(), "/queue/private", new PrivateMessage(1, privateMessage.getMessage(), principal.getName()));
    }
    
    @MessageMapping("/active.users")
    public void activeUsers(Principal principal) {
    	activeUserService.mark(principal.getName());
    }
    
    @MessageMapping("/private.users")
    public void privateUsers(Principal principal, String message) {
    	activeUserService.mark(principal.getName());
    	template.convertAndSendToUser(message, "/queue/private", new PrivateMessage(2, principal.getName()));
    }
    
    @MessageMapping("/roulette")
    public void roulette(Principal principal) {
    	wheel.mark(principal.getName());
    	if (!wheel.isInRoulette(principal.getName())) {
    		return;
    	}
    	String pair = wheel.getPair(principal.getName());
    	boolean pairless = true;
    	if (pair != null) {
    		if (!wheel.isInRoulette(pair)) {
    			wheel.deletePair(principal.getName());
    			template.convertAndSendToUser(principal.getName(), "/queue/roulette", new RouletteMessage(2, ""));
    			return;
    		} else {
    			template.convertAndSendToUser(principal.getName(), "/queue/roulette", new RouletteMessage(0, pair));
    			pairless = false;
    		}
    	}
    	if (pairless) {
	    	if (!wheel.getMePair(principal.getName())) {
	    		template.convertAndSendToUser(principal.getName(), "/queue/roulette", new RouletteMessage(2, ""));
	    	}
    	}
    }
    
    @MessageMapping("/next")
    public void next(Principal principal) {
    	if (!isNotBanned((principal.getName()))) {
    		return;
    	}
    	String pair = wheel.getPair(principal.getName());
    	if (pair != null) {
			template.convertAndSendToUser(pair, "/queue/roulette", new RouletteMessage(2, ""));
    	}
    	wheel.deletePair(principal.getName());
    }
    
    private synchronized boolean isNotBanned(String mail) {
    	if (!messageNumber.contains(new MessageNumber(mail))) {
    		messageNumber.add(new MessageNumber(mail));
    		return true;
    	} else {
    		for (MessageNumber eachMessageNumber : messageNumber) {
    			if (eachMessageNumber.equals(new MessageNumber(mail))) {
    				int number = eachMessageNumber.getNumber();
    				if (number > 10) {
    					return false;
    				}
    				eachMessageNumber.setNumber(number + 1);
    				break;
    			}
    		}
    		return true;
    	}
    }
}