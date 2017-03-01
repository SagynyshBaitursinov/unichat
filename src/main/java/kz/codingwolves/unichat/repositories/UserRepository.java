package kz.codingwolves.unichat.repositories;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import kz.codingwolves.unichat.models.User;

@Repository
public class UserRepository {

	@PersistenceContext
	private EntityManager entityManager;
		
	@Transactional
	public boolean newUser(User user) {
		try {
			entityManager.persist(user);
			return true;
		} catch (Exception e) {
			return false;
		}
	}	
	
	@Transactional
	public User save(User user) {
		User resultUser;
		try {
			resultUser = entityManager.merge(user);
		} catch (Exception e) {
			resultUser = null;
		}
		return resultUser;
	}
	
	public List<User> getAllUsers() {
		return entityManager.createNamedQuery("User.findAll", User.class).getResultList();
	}
	
	public List<User> getAllUsersAbsolute() {
		return entityManager.createNamedQuery("User.findAllAbsolute", User.class).getResultList();
	}
	
	public User getUserByEmail(String email) {
		try {
			return entityManager.createNamedQuery("User.findByEmail", User.class).setParameter(1, email).getSingleResult(); 
		} catch (NoResultException noResultException) {
			return null;
		}
	}
	
	public User getUserByNuId(String id) {
		try {
			return entityManager.createNamedQuery("User.findByNuId", User.class).setParameter(1, id).getSingleResult(); 
		} catch (NoResultException noResultException) {
			return null;
		}
	}
	
	public User getUserById(Integer id) {
		User user = entityManager.createNamedQuery("User.findById", User.class).setParameter(1, id).getSingleResult();
		return user;
	}
	
	public User getUserByUsername(String nickname) {
		try {
			return entityManager.createNamedQuery("User.findByUsername", User.class).setParameter(1, nickname.toUpperCase()).getSingleResult();
		} catch (NoResultException noResultException) {
			return null;
		}
	}
	
	@Transactional
	public boolean mark(String nickname) {
		try {
			User user = entityManager.createNamedQuery("User.findByNickname", User.class).setParameter(1, nickname).getSingleResult();		
			user.setLastAccess(new Date());
			entityManager.merge(user);
		} catch (Exception exception) {
			return false;
		}
		return true;
	}
	
	@Transactional
	public boolean markInRoulette(String nickname) {
		try {
			User user = entityManager.createNamedQuery("User.findByNickname", User.class).setParameter(1, nickname).getSingleResult();
			user.setLastRouletteAccess(new Date());
			entityManager.merge(user);
		} catch (Exception exception) {
			return false;
		}
		return true;
	}
	
	public int getCount() {
		return entityManager.createQuery("Select count(u) from User u", Long.class).getSingleResult().intValue();
	}
}