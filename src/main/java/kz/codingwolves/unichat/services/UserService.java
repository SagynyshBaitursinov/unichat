package kz.codingwolves.unichat.services;

import java.util.List;
import java.util.concurrent.locks.Lock;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.util.concurrent.Striped;

import kz.codingwolves.unichat.models.User;
import kz.codingwolves.unichat.repositories.UserRepository;

@Service
public class UserService {
	
	@Resource
	private Striped<Lock> locks;
	
	private UserRepository userRepository;
	 
	@Autowired
	public void setUserRepository(UserRepository userRepository) {
	    this.userRepository = userRepository;
	}
	
	public List<User> getAllUsers() {
		return userRepository.getAllUsers();
	}
	
	public List<User> getAllAbsolute() {
		return userRepository.getAllUsersAbsolute();
	}
	
	public User save(User user) {
		return userRepository.save(user);
	}
	
	public boolean newUser(User user) {
		return userRepository.newUser(user);
	}
	
	public User getUserById(Integer id) {
		return userRepository.getUserById(id);
	}
	
	public User getUserByNuId(String id) {
		return userRepository.getUserByNuId(id);
	}
	
	public User getUserByEmail(String email) {
		return userRepository.getUserByEmail(email.toUpperCase());
	}
	
	public User getUserByUsername(String nickname) {
		return userRepository.getUserByUsername(nickname);
	}
	
	public boolean mark(String nickname) {
		locks.get(nickname).lock();
		boolean result = userRepository.mark(nickname);
		locks.get(nickname).unlock();
		return result;
	}
	
	public boolean markInRoulette(String nickname) {
		locks.get(nickname).lock();
		boolean result = userRepository.markInRoulette(nickname);
		locks.get(nickname).unlock();
		return result;
	}
	
	public int getCount() {
		return userRepository.getCount();
	}
}
