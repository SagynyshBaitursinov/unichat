package kz.codingwolves.unichat.interceptors;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

public class LoggedInChecker extends HandlerInterceptorAdapter {
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
		if (request.getUserPrincipal() != null) {
			String app = request.getParameter("app");
			if (app != null && app.equals("unipins")) {
				response.sendRedirect("http://unipins.kz?cookie=" + request.getSession().getId());
				return false;
			}
			if (app != null && app.equals("unifaces")) {
				response.sendRedirect("/unifaces");
				return false;
			}
			response.sendRedirect(request.getContextPath() + "/chat");
			return false;
		}
		return true;
	}
}