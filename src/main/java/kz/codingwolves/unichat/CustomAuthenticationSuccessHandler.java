package kz.codingwolves.unichat;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import kz.codingwolves.unichat.models.User;
import kz.codingwolves.unichat.services.UserService;

public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler{

	@Autowired
	private UserService userService;
	
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
			throws IOException, ServletException {
		PrintWriter writer = response.getWriter();
		User user = userService.getUserByUsername(authentication.getName());
		if (user.isWasInSettings()) {
			writer.print("ok");
		} else {
			user.setWasInSettings(true);
			userService.save(user);
			writer.print("settings");
		}
		writer.flush();
		writer.close();
	}
}
