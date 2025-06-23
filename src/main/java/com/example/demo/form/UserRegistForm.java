package com.example.demo.form;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class UserRegistForm {
	private int userId;

	@NotEmpty(message = "メールアドレスを入力してください")
	@Email(message = "メールアドレス形式で入力してください")
	private String mail;

	@NotNull(message = " パスワードを入力してください")
	@Pattern(regexp = "^[a-zA-Z0-9'!\\-\"#$%&()*,./:;?@\\[\\]^_`{|}~+<=>]+$", message = "半角英数字・記号「'-!\"#$%&()*,./:;?@[]^_`{|}~+<=>」で入力してください")
	@Size(min = 6, max = 16, message = "6文字以上16文字以下で入力してください")
	private String password;

	private String passwordConfirm;

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPasswordConfirm() {
		return passwordConfirm;
	}

	public void setPasswordConfirm(String passwordConfirm) {
		this.passwordConfirm = passwordConfirm;
	}

}
