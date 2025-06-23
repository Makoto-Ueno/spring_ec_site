package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.authorizeHttpRequests(authorize -> authorize.requestMatchers("/css/**").permitAll() // CSSファイルは認証不要
				.requestMatchers("/").permitAll() // トップページは認証不要
				.anyRequest().authenticated() // 他のURLはログイン後アクセス可能
		).formLogin(login -> login.permitAll()); // ログインページは認証不要

		return http.build();
	}

public class SecrityConfig {

}
