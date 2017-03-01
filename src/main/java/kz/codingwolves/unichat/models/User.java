package kz.codingwolves.unichat.models;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "User_")
@NamedQueries({
    @NamedQuery(name="User.findAll",
            query="SELECT u FROM User u WHERE registered = true ORDER BY registrationDate"),
    @NamedQuery(name="User.findByEmail",
    		query="SELECT u FROM User u WHERE u.email = ?1"),
    @NamedQuery(name="User.findByNickname",
    		query="SELECT u FROM User u WHERE u.nickname = ?1"),
    @NamedQuery(name="User.findByUsername",
    		query="SELECT u FROM User u WHERE UPPER(u.nickname) = ?1"),
    @NamedQuery(name="User.findById",
    		query="SELECT u FROM User u WHERE u.id = ?1"),
    @NamedQuery(name="User.findByNuId",
    		query="SELECT u FROM User u WHERE u.nuId = ?1"),
    @NamedQuery(name="User.findAllAbsolute",
    		query="SELECT u FROM User u")
})
public class User {
	
	public enum School {
		SST, SENG, FOUNDATION, SHSS
	}
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
	
	@Column(unique=true)
	private String email;
	
	private String nuId;
	
	private String nickname;
	
	private String password;

	private String phrase;
	
	private Date registrationDate;
	
	private Date lastAccess; 
	
	private Date lastRouletteAccess;
		
	private boolean registered;
	
	private boolean isMale;
	
	private String school;
	
	private boolean wasInSettings;
	
	public boolean isWasInSettings() {
		return wasInSettings;
	}

	public void setWasInSettings(boolean wasInSettings) {
		this.wasInSettings = wasInSettings;
	}

	public Date getLastRouletteAccess() {
		return lastRouletteAccess;
	}

	public void setLastRouletteAccess(Date lastRouletteAccess) {
		this.lastRouletteAccess = lastRouletteAccess;
	}

	public String getPhrase() {
		return phrase;
	}

	public void setPhrase(String phrase) {
		this.phrase = phrase;
	}

	public String getNuId() {
		return nuId;
	}

	public void setNuId(String nuId) {
		this.nuId = nuId;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public boolean getRegistered() {
		return registered;
	}

	public void setRegistered(boolean registered) {
		this.registered = registered;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Date getRegistrationDate() {
		return registrationDate;
	}

	public void setRegistrationDate(Date registrationDate) {
		this.registrationDate = registrationDate;
	}

	public Date getLastAccess() {
		return lastAccess;
	}

	public void setLastAccess(Date lastAccess) {
		this.lastAccess = lastAccess;
	}

	public boolean isMale() {
		return isMale;
	}

	public void setMale(boolean isMale) {
		this.isMale = isMale;
	}

	public String getSchool() {
		return school;
	}

	public void setSchool(String school) {
		this.school = school;
	}
	
	public String toString() {
		return email;
	}
}