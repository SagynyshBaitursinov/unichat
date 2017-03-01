package kz.codingwolves.unichat.usercheckers;

import java.util.List;

public class ActiveUsers {

	private List<String> activeUsers;

	public ActiveUsers(List<String> activeUsers) {
		this.activeUsers = activeUsers;
	}
	
	public List<String> getActiveUsers() {
		return activeUsers;
	}

	public void setActiveUsers(List<String> activeUsers) {
		this.activeUsers = activeUsers;
	}	
}