package kz.codingwolves.unichat.usercheckers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import kz.codingwolves.unichat.models.User;
import kz.codingwolves.unichat.services.UserService;

public class ActiveUserService {

	@Autowired
	private UserService userService;
	
	public void mark(String nickname) {
		userService.mark(nickname);
	}
	
	public synchronized List<User> getActiveUsers() {
		List<User> active = new ArrayList<>();
		for (User user: userService.getAllUsers()) {
		    if ((System.currentTimeMillis() - user.getLastAccess().getTime()) < 5000) {
		    	active.add(user);
		    }
	    }
	    return active;
	}
}
