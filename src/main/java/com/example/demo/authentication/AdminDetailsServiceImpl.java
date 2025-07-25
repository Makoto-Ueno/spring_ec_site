package com.example.demo.authentication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.example.demo.repository.UserRepository;

@Component("AdminDetailsServiceImpl")
public class AdminDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;
	final int ADMIN_TYPE = 1;

	@Override
	public UserDetails loadUserByUsername(String mail) throws UsernameNotFoundException {
		var user = userRepository.findByMailAndType(mail, ADMIN_TYPE)
				.orElseThrow(() -> new UsernameNotFoundException(mail));
		String role = getRoles(user.getType());
		return User.withUsername(user.getMail()).password(user.getPassword()).roles(role).build();
	}

	private String getRoles(int type) {
		if (type == 0) {
			return "USER";
		} else if (type == 1) {
			return "ADMIN";
		} else {
			return "";
		}
	}
}
