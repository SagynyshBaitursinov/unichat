package kz.codingwolves.unichat;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

public class CustomAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException exception) throws IOException, ServletException {
		if (exception.getClass().isAssignableFrom(UsernameNotFoundException.class)) {
			response.setStatus(400);
		    PrintWriter writer = response.getWriter();
		    writer.flush();
		    writer.print("Not found");
		    writer.close();
		} else if (exception.getClass().isAssignableFrom(BadCredentialsException.class)) {
			response.setStatus(400);
			PrintWriter writer = response.getWriter();
			writer.flush();
		    writer.print("Not correct");
		    writer.close();
		}
	}
	
}
