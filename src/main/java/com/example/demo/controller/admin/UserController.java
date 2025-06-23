package com.example.demo.controller.admin;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.entity.User;
import com.example.demo.form.UserRegistForm;
import com.example.demo.repository.UserRepository;

@Controller
public class UserController {

	@Autowired
	private UserRepository userRepository;

	@GetMapping("/admin/register")
	public String registerView(Model model, UserRegistForm form) {
		model.addAttribute(form);
		return "admin/user/register";
	}

	@PostMapping("/admin/register")
	public String register(Model model, @ModelAttribute @Validated UserRegistForm form, BindingResult bindingResult,
			RedirectAttributes redirectAttributes) {
		// ▼ パスワードと確認用パスワードが一致しているかをチェック
		if (!form.getPassword().equals(form.getPasswordConfirm())) {
			// ▼ 一致しない場合、BindingResultにエラーを追加
			bindingResult.rejectValue("passwordConfirm", "error.passwordConfirm", "パスワードと入力が一致しません");
		}
		// ▼ バリデーションエラーがある場合、登録画面に戻る
		if (bindingResult.hasErrors()) {
			model.addAttribute(form);
		}
		// ▼ 追加: メールアドレスの重複チェック
		if (userRepository.findByMail(form.getMail()) != null) {
			bindingResult.rejectValue("mail", "error.mail", "このメールアドレスは既に使用されています");
			return "admin/user/register";
		}

		User user = new User();
		user.setMail(form.getMail());
		user.setPassword(form.getPassword());
		// TODO:ハッシュ化未実装
		user.setType(form.getType());
		Date now = new Date();
		user.setCreateAt(now);
		user.setUpdateAt(now);

		userRepository.saveAndFlush(user);
		return "redirect:/admin";
		// TODO:ログイン画面作成後トップページに戻る

	}

}
