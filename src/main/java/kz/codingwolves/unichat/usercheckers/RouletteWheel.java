package kz.codingwolves.unichat.usercheckers;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.commons.lang.WordUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import kz.codingwolves.unichat.models.RouletteMessage;
import kz.codingwolves.unichat.models.User;
import kz.codingwolves.unichat.services.UserService;

@Component
public class RouletteWheel {

	@Autowired
	private UserService userService;
	
	@Autowired
	private SimpMessagingTemplate template;
	
    private Random randomGenerator;

	private List<User> userList;
	
	private List<Pair> pairList;
	
	private List<Pair> exList;
	
	private List<String> queue;
	
	public RouletteWheel() {
		exList = new ArrayList<Pair>();
		userList = new ArrayList<User>();
		pairList = new ArrayList<Pair>();
		queue = new ArrayList<String>();
		randomGenerator = new Random();
	}
		
	public void mark(String nickname) {
		userService.markInRoulette(nickname);
	}
	
	public String getPair(String nickname) {
		for (Pair pair: pairList) {
			if (pair.getLeft().equals(nickname)) {
				return pair.getRight();
			} else if (pair.getRight().equals(nickname)) {
				return pair.getLeft();
			}
		}
		return null;
	}

	public synchronized void deletePair(String nickname) {
		List<Pair> allPairs = new ArrayList<Pair>();
		allPairs.addAll(pairList);
		for (Pair pair: allPairs) {
			if (pair.getLeft().equals(nickname) || pair.getRight().equals(nickname)) {
				pairList.remove(pair);
			}
		}
	}
	
	public boolean getMePair(String nickname) {
		if (queue.isEmpty()) {
			queue.add(nickname);
			return false;
		} else if (queue.contains(nickname)) {
			return false;
		}
        int index = randomGenerator.nextInt(queue.size());
        String result = queue.get(index);
        queue.remove(index);
        if (isInRoulette(result) && !exList.contains(new Pair(result, nickname))) {
        	addPair(result, nickname);
        	User user1 = userService.getUserByUsername(nickname);
        	User user2 = userService.getUserByUsername(result);
        	template.convertAndSendToUser(result, "/queue/roulette", new RouletteMessage(0, WordUtils.capitalizeFully(nickname), user1.getPhrase()));
        	template.convertAndSendToUser(nickname, "/queue/roulette", new RouletteMessage(0, WordUtils.capitalizeFully(result), user2.getPhrase()));
        	return true;
        } else {
        	queue.add(nickname);
        	return false;
        }
	}
	
	public synchronized void addPair(String first, String second) {
		Pair pair = new Pair(first, second);
		pairList.add(pair);
		List<Pair> exExList = new ArrayList<Pair>();
		exExList.addAll(exList);
		for (Pair each: exExList) {
			if (each.getRight().equals(first) || each.getLeft().equals(first) || each.getRight().equals(second) || each.getLeft().equals(second)) {
				exList.remove(each);
			}
		}
		exList.add(pair);
	}
	
	public boolean isInRoulette(String nickname) {
		List<User> list = userList;
		for (User user: list) {
			if (user.getNickname().equals(nickname)) {
				return true;
			}
		}
		return false;
	}
	
	public List<User> getRouletteUsers() {
		return userList;
	}
	
	public synchronized void updateActiveUsers() {
		List<User> active = new ArrayList<>();
		for (User user: userService.getAllUsers()) {
		    if ((System.currentTimeMillis() - user.getLastRouletteAccess().getTime()) < 5000) {
		    	active.add(user);
		    }
	    }
	    userList = active;
	}
}