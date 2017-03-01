package kz.codingwolves.unichat.controllers;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.security.Principal;
import java.util.Date;
import java.util.UUID;

import javax.imageio.ImageIO;
import javax.mail.MessagingException;

import org.apache.commons.lang.WordUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import kz.codingwolves.identicons.IdenticonGenerator;
import kz.codingwolves.unichat.MessageStorer;
import kz.codingwolves.unichat.models.ChatMessage;
import kz.codingwolves.unichat.models.User;
import kz.codingwolves.unichat.services.MailSenderService;
import kz.codingwolves.unichat.services.UserService;

@Controller
public class AdminController {

	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	
	@Autowired
	private MailSenderService mailSenderService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private SimpMessagingTemplate template;
	
	@Autowired
	private MessageStorer messageStorer;
	
	@RequestMapping(value = "/forgot", method = RequestMethod.GET)
	@ResponseBody
	public String forgot(Principal principal, @RequestParam("email") String email) {
		User user = userService.getUserByUsername(principal.getName());
		if (user == null || (!user.getNuId().equals("201200976") && !user.getNuId().equals("201395684"))) {
			return "you don't have rights";
		}
		if (email == null || email.isEmpty()) {
			return "write email";
		}
		User user2 = userService.getUserByEmail(email);
		if (user2 == null || !user2.getRegistered()) {
			return "not registered";
		}
		String password = UUID.randomUUID().toString();
        try {
        	mailSenderService.send(user2.getEmail(), password, true);
			user2.setPassword(password);
			user2.setWasInSettings(false);
			userService.save(user2);
			logger.info(email + " forgot password");
			return "success";
		} catch (MailException | MessagingException ex) {
			logger.info(email + " MailException happened");
		}
		logger.info("Admin "+ user.getNuId() + " has dropped password of " + user2.getNickname());
        return "exception happened with mail sending";
	}
	
	@RequestMapping(value = "/changegender", method = RequestMethod.GET)
	@ResponseBody
	public String changeGender(Principal principal, @RequestParam("email") String email) {
		User user = userService.getUserByUsername(principal.getName());
		if (user == null || (!user.getNuId().equals("201200976") && !user.getNuId().equals("201395684"))) {
			return "you don't have rights";
		}
		if (email == null || email.isEmpty()) {
			return "write email";
		}
		User user2 = userService.getUserByEmail(email);
		if (user2 == null || !user2.getRegistered()) {
			return "not registered";
		}
		user2.setMale(!user2.isMale());
		userService.save(user2);
		generateIdenticon(user2);
		logger.info("Admin "+ user.getNuId() + " has changed gender of " + user2.getNickname());
        return "success";
	}
	
	@RequestMapping(value = "/makemale", method = RequestMethod.GET)
	@ResponseBody
	public String makeMale(Principal principal, @RequestParam("email") String email) {
		User user = userService.getUserByUsername(principal.getName());
		if (user == null || (!user.getNuId().equals("201200976") && !user.getNuId().equals("201395684"))) {
			return "you don't have rights";
		}
		if (email == null || email.isEmpty()) {
			return "write email";
		}
		User user2 = userService.getUserByEmail(email);
		if (user2 == null) {
			return "not registered";
		}
		user2.setMale(true);
		userService.save(user2);
		generateIdenticon(user2);
		logger.info("Admin "+ user.getNuId() + " has changed gender of " + user2.getNickname());
        return "success";
	}
	
	@RequestMapping(value = "/adduser", method = RequestMethod.GET)
	@ResponseBody
	public String addUser(Principal principal, @RequestParam("email") String email, @RequestParam("nickname") String nickname, @RequestParam("nuid") String nuid, @RequestParam("sex") String sex, @RequestParam("school") String school) {
		User admin = userService.getUserByUsername(principal.getName());
		if (admin == null || (!admin.getNuId().equals("201200976") && !admin.getNuId().equals("201395684"))) {
			return "you don't have rights";
		}
		if (email == null || email.isEmpty() || nickname == null || nickname.isEmpty() || nuid == null || nuid.isEmpty() || sex == null || sex.isEmpty() || school == null || school.isEmpty()) {
			return "error";
		}
		email = email.toUpperCase();
		nickname = WordUtils.capitalizeFully(nickname);
		User checkUser = userService.getUserByEmail(email);
		if (checkUser != null) {
			return "email is busy";
		}
		checkUser = userService.getUserByNuId(nuid);
		if (checkUser != null) {
			return "nuid is busy";
		}
		checkUser = userService.getUserByUsername(nickname);
		if (checkUser != null) {
			return "nuid is busy";
		}
		User user = new User();
		user.setEmail(email);
		user.setNuId(nuid);
		user.setNickname(nickname);
		user.setLastRouletteAccess(new Date(0));
		user.setLastAccess(new Date(0));
		user.setRegistrationDate(new Date(0));
		user.setPhrase("Hi there! I am using Unichat!");
		user.setMale(sex.toUpperCase().equals("MALE"));
		user.setRegistered(false);
		user.setSchool(school);
		user.setWasInSettings(false);
		userService.save(user);
		generateIdenticon(user);
		logger.info("Admin "+ admin.getNuId() + " has added new user " + user.getEmail());
		return "success";
	}
	
	@RequestMapping(value = "/changename", method = RequestMethod.GET)
	@ResponseBody
	public String changeName(Principal principal, @RequestParam("email") String email, @RequestParam("name") String name) {
		User user = userService.getUserByUsername(principal.getName());
		if (user == null || (!user.getNuId().equals("201200976") && !user.getNuId().equals("201395684"))) {
			return "you don't have rights";
		}
		if (email == null || email.isEmpty() || name == null || name.isEmpty()) {
			return "write email and name";
		}
		name = WordUtils.capitalizeFully(name);
		User checkUser = userService.getUserByUsername(name);
		if (checkUser != null) {
			return "nickname is busy";
		}
		User user2 = userService.getUserByEmail(email);
		if (!user2.getRegistered()) {
			return "not registered";
		}
		String exNickname = user2.getNickname();
		user2.setNickname(name);
		userService.save(user2);
		generateIdenticon(user2);
		logger.info("Admin "+ user.getNuId() + " has changed nickname of " + exNickname + " to " + user2.getNickname());
		return "success";
	}
	
	@RequestMapping(value = "/sendall", method = RequestMethod.GET)
	@ResponseBody
	public String sendAll(Principal principal, @RequestParam("message") String message) {
		User user = userService.getUserByUsername(principal.getName());
		if (user == null || !user.getNuId().equals("201200976")) {
			return "you don't have rights";
		}
		if (message == null || message.isEmpty()) {
			return "write your message";
		}
		logger.info("Admin "+ user.getNuId() + " has sent message " + message);
    	ChatMessage chatMessage = new ChatMessage("Unichat", new Date(), message);
        template.convertAndSend("/topic/shout", chatMessage);
    	messageStorer.add(chatMessage);
		return "success";
	}
	
	private void generateIdenticon(User user) {
		File existingFile = new File("/opt/unichat/identicons/" + user.getNuId() + ".png");
		if (existingFile.exists()) {
			existingFile.delete();
		}
		BufferedImage identicon = IdenticonGenerator.generate(user.getNickname(), !user.isMale());
		File outputfile = new File("/opt/unichat/identicons/" + user.getNuId() + ".png");
		try {
			ImageIO.write(identicon, "png", outputfile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
