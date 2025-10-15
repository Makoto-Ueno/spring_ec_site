package com.example.demo.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class ResetPasswordForm {

	@NotBlank(message = " パスワードを入力してください")
	@Pattern(regexp = "^[a-zA-Z0-9'!\\-\"#$%&()*,./:;?@\\[\\]^_`{|}~+<=>]+$", message = "半角英数字・記号「'-!\"#$%&()*,./:;?@[]^_`{|}~+<=>」で入力してください")
	@Size(min = 6, max = 16, message = "6文字以上16文字以下で入力してください")
	private String newPassword;

	private String newPasswordConfirm;

	private Integer userId;

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	public String getNewPasswordConfirm() {
		return newPasswordConfirm;
	}

	public void setNewPasswordConfirm(String newPasswordConfirm) {
		this.newPasswordConfirm = newPasswordConfirm;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

}