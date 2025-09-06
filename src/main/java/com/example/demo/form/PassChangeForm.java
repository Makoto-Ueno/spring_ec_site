package com.example.demo.form;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class PassChangeForm {

	@NotEmpty(message = " パスワードを入力してください")
	@Pattern(regexp = "^[a-zA-Z0-9'!\\-\"#$%&()*,./:;?@\\[\\]^_`{|}~+<=>]+$", message = "半角英数字・記号「'-!\"#$%&()*,./:;?@[]^_`{|}~+<=>」で入力してください")
	@Size(min = 6, max = 16, message = "6文字以上16文字以下で入力してください")
	private String password;

	private String passwordConfirm;

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