package kz.codingwolves.unichat;
 
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import kz.codingwolves.unichat.models.User;
import kz.codingwolves.unichat.services.UserService;

public class CustomAuthenticationProvider implements AuthenticationProvider {
	
	@Autowired
	private UserService userService;
	
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
    	String password = (String) authentication.getCredentials();
    	User user = userService.getUserByEmail(authentication.getName());
    	if (user == null || !user.getRegistered()) {
    		throw new UsernameNotFoundException("Username not found");
    	}
        if (!password.equals(user.getPassword())) {
            throw new BadCredentialsException("Wrong password");
        }
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new GrantedAuthority() {
        	
			private static final long serialVersionUID = 1L;

			@Override
			public String getAuthority() {
				return "ROLE_USER";
			}
		});
        return new UsernamePasswordAuthenticationToken(user.getNickname(), password, authorities);
    }
 
    public boolean supports(Class<?> arg0) {
        return true;
    }
}