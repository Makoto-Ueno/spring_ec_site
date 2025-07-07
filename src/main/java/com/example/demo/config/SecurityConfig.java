package com.example.demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.authorizeHttpRequests(
				authorize -> authorize.requestMatchers("/admin/login", "/css/**", "/js/**").permitAll() // 認証不要ページの指定
						.anyRequest().authenticated() // 他のURLはログイン後アクセス可能
		).formLogin(login -> login.loginPage("/admin/login") // ログインフォームのURL
				.loginProcessingUrl("/admin/login") // フォームのPOST先
				.defaultSuccessUrl("/admin", true) // ログイン成功時の遷移先
				.failureUrl("/admin/login?error") // エラー時
				.permitAll().usernameParameter("mail").passwordParameter("password"));
		return http.build();
	}

}
