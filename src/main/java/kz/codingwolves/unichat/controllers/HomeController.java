package kz.codingwolves.unichat.controllers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.Principal;
import java.util.Date;
import java.util.Deque;
import java.util.UUID;

import javax.mail.MessagingException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mail.MailException;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import kz.codingwolves.unichat.MessageStorer;
import kz.codingwolves.unichat.models.ChatMessage;
import kz.codingwolves.unichat.models.User;
import kz.codingwolves.unichat.services.MailSenderService;
import kz.codingwolves.unichat.services.UserService;

@Controller
public class HomeController {
	
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	
	@Autowired
	private MessageStorer messageStorer;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private MailSenderService mailSenderService;
	
	@RequestMapping(value = "/loggeduser", method = RequestMethod.GET)
	@ResponseBody
	public String loggeruser(HttpServletRequest request) {
	    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	    if (auth instanceof AnonymousAuthenticationToken) {
	    	return "anon";
	    } else {
		    User user = userService.getUserByUsername(auth.getName());
		    if (user == null || !user.getRegistered()) {
		    	return "anon";
		    } else {
		    	return user.getEmail();
		    }
	    }
	}
	
	@RequestMapping(value = "/getlastmessages", method = RequestMethod.GET)
	@ResponseBody
	public Deque<ChatMessage> getLastMessages() {
		return messageStorer.getAll();
	}
	
	@RequestMapping(value = "/chat", method = RequestMethod.GET)
	public String chat(Principal principal, Model model) {
		model.addAttribute("principal", principal.getName().toUpperCase());
		return "chat";
	}
	
	@RequestMapping(value = "/roulette", method = RequestMethod.GET)
	public String roulette(Principal principal, Model model) {
		User user = userService.getUserByUsername(principal.getName());
		model.addAttribute("principal", principal.getName().toUpperCase());
		model.addAttribute("mymessage", user.getPhrase());
		return "roulette";
	}
	
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(@RequestParam(value = "app", required = false) String app, Model model) {
		if (app == null) {
			model.addAttribute("app", "chat");
		} else if (app.equals("unifaces")) {
			model.addAttribute("app", app);
		} else if (app.equals("unipins")) {
			model.addAttribute("app", app);
		} else {
			model.addAttribute("app", "chat");
		}
		return "home";
	}
	
	@RequestMapping(value = "/settings", method = RequestMethod.GET)
	public String settings(Principal principal, Model model) throws Exception {
		User user = userService.getUserByUsername(principal.getName());
		if (user == null) {
			return "redirect:/";
		}
		model.addAttribute("toast", "");
		model.addAttribute("principal", principal.getName().toUpperCase());
		model.addAttribute("message", user.getPhrase());
		return "settings";
	}
	
	@RequestMapping(value = "/settings", method = RequestMethod.POST)
	public String settings(Principal principal, String password, String newpassword, String message, Model model) {
		User user = userService.getUserByUsername(principal.getName());
		if (newpassword.length() > 25 || message.length() > 255) {
			//
		} else if (user != null) {
			if (user.getPassword().equals(password)) {
				if (newpassword != null && newpassword.length() != 0) {
					user.setPassword(newpassword);
				}
		    	message = message.replaceAll("\\s+", " ");
				user.setPhrase(message);
				userService.save(user);
				model.addAttribute("toast", "Successfully saved");
			} else {
				model.addAttribute("toast", "Password is wrong");
			}
		} else {
			return "redirect:/";
		}
		model.addAttribute("principal", principal.getName().toUpperCase());
		model.addAttribute("message", user.getPhrase());
		return "settings";
	}
	
	@RequestMapping(value = "/register", method = RequestMethod.POST)
	@ResponseBody
	public String register(@RequestParam("email") String email) {
		User user = userService.getUserByEmail(email);
		if (user != null && !user.getRegistered()) {
			String password = UUID.randomUUID().toString();
            try {
            	mailSenderService.send(user.getEmail(), password, false);
				user.setRegistered(true);
				user.setPassword(password);
				user.setRegistrationDate(new Date());
				userService.save(user);
				logger.info(email + " just registered");
				return "success";
			} catch (MailException | MessagingException ex) {
				logger.info(email + " MailException happened");
			}
		}
		return "error";
	}
	
	@RequestMapping(value = "/exists", method = RequestMethod.GET)
	@ResponseBody
	public String exists(@RequestParam("email") String email) {
		User user = userService.getUserByEmail(email);
		if (user == null) {
			return "null";
		} else if (!user.getRegistered()) {
			return "not registered";
		} else {
			return "registered";
		}
	}
	
	@ExceptionHandler(value = Exception.class)
	public String exceptionHandler(Exception e) {
		return "redirect:/";
	}
	
	@ResponseBody
	@RequestMapping(value = "/getAvatar", method = RequestMethod.GET, produces = MediaType.IMAGE_PNG_VALUE)
	public byte[] getAvatar(@RequestParam("name") String nickname) {
		if (nickname != null && nickname.equals("Unichat")) {
			InputStream in = null;
			try {
				in = new FileInputStream(new File("/opt/unichat/unichat.png"));
			} catch (FileNotFoundException e1) {
				return "".getBytes();
			}
			try {
				return IOUtils.toByteArray(in);
			} catch (IOException e) {
				return "".getBytes();
			}
		}
		User user = userService.getUserByUsername(nickname);
		if (user == null || !user.getRegistered()) {
			InputStream in = null;
			try {
				in = new FileInputStream(new File("/opt/unichat/dummy.png"));
			} catch (FileNotFoundException e1) {
				return "".getBytes();
			}
			try {
				return IOUtils.toByteArray(in);
			} catch (IOException e) {
				return "".getBytes();
			}
		}
	    InputStream in = null;
		try {
			in = new FileInputStream(new File("/opt/unichat/identicons/" + user.getNuId() + ".png"));
		} catch (FileNotFoundException e1) {
			return "".getBytes();
		}
	    try {
			return IOUtils.toByteArray(in);
		} catch (IOException e) {
			return "".getBytes();
		}
	}
	
	@ResponseBody
	@RequestMapping(value = "/getInfo", method = RequestMethod.GET, produces = MediaType.TEXT_PLAIN_VALUE)
	public String getInfo(@RequestParam("name") String nickname) {
		User user = userService.getUserByUsername(nickname);
		if (user == null || !user.getRegistered()) {
			return "Not found";
		}
		return user.getSchool() + " id" + user.getNuId().substring(0, 4) + (user.isMale() ? " MALE" : " FEMALE");
	}
	
	@RequestMapping(value = "/user/{nickname}", method = RequestMethod.GET)
	public String profile(@PathVariable String nickname, Principal principal, Model model) {
		if (principal.getName().equals(nickname)) {
			return "redirect:/settings";
		}
		User user = userService.getUserByUsername(nickname.trim());
		if (user == null || !user.getRegistered()) {
			return "redirect:/";
		}
		model.addAttribute("principal", principal.getName().toUpperCase());
		model.addAttribute("user", user);		
		return "profile";
	}
	
	@RequestMapping(value = "/unipins", method = RequestMethod.GET)
	public String unipins(Principal principal, Model model, HttpServletRequest request) {
		Cookie[] cookies = request.getCookies();
		for (Cookie cookie: cookies) {
			if (cookie.getName().equals("JSESSIONID")) {
				return "redirect:http://unipins.kz?cookie=" + cookie.getValue();
			}
		}
		return "redirect:/";
	}
}