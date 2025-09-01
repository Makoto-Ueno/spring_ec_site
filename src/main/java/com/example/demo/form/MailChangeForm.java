package com.example.demo.form;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;

public class MailChangeForm {

	@NotEmpty(message = "メールアドレスを入力してください")
	@Email(message = "メールアドレス形式で入力してください")
	private String mail;

	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}
}