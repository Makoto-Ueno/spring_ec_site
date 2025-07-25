package com.example.demo.authentication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.example.demo.repository.UserRepository;

@Component
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String mail) throws UsernameNotFoundException {
		var user = userRepository.findByMail(mail).orElseThrow(() -> new UsernameNotFoundException(mail));

		return User.withUsername(user.getMail()).password(user.getPassword()).build();
	}

}
