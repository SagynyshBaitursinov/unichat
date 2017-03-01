package kz.codingwolves.unichat.usercheckers;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;

import kz.codingwolves.unichat.models.User;

public class ActiveUserPinger {
	
	@Resource
	private List<MessageNumber> messageNumber;
	
	private SimpMessagingTemplate template;
	
	private ActiveUserService activeUserService;

	private RouletteWheel rouletteWheel;
	
	public ActiveUserPinger(SimpMessagingTemplate template, ActiveUserService activeUserService, RouletteWheel rouletteWheel) {
		this.template = template;
	    this.activeUserService = activeUserService;
	    this.rouletteWheel = rouletteWheel;
	}
	  
	@Scheduled(fixedDelay = 2000)
	public synchronized void pingUsers() {
		List<User> activeUsers = activeUserService.getActiveUsers();
		List<String> chatNames = new ArrayList<>();
		rouletteWheel.updateActiveUsers();
		for (User user: activeUsers) {
			chatNames.add(user.getNickname());
		}
		for (User user: rouletteWheel.getRouletteUsers()) {
			if (chatNames.contains(user.getNickname())) {
				chatNames.remove(user.getNickname());
			}
			chatNames.add(user.getNickname() + "!");
		}
		ActiveUsers chatResponse = new ActiveUsers(chatNames);
		template.convertAndSend("/topic/active.users", chatResponse);
		template.convertAndSend("/topic/roulette", "");
		template.convertAndSend("/topic/private.users", "");
	}
	
	@Scheduled(fixedDelay = 10000)
	public void dropMessageCounts() {
		this.messageNumber.clear();
	}
}