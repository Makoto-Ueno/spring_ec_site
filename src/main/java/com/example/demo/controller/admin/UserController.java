package com.example.demo.controller.admin;

import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.entity.User;
import com.example.demo.form.MailChangeForm;
import com.example.demo.form.PassChangeForm;
import com.example.demo.form.UserRegistForm;
import com.example.demo.repository.UserRepository;

@Controller
public class UserController {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	PasswordEncoder passwordEncoder;

	@Autowired
	@Qualifier("AdminDetailsServiceImpl")
	UserDetailsService userDetailsService;

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
			return "admin/user/register";
		}
		// ▼ 追加: メールアドレスの重複チェック
		if (!userRepository.findByMail(form.getMail()).isEmpty()) {
			bindingResult.rejectValue("mail", "error.mail", "このメールアドレスは既に使用されています");
			return "admin/user/register";
		}

		User user = new User();
		user.setMail(form.getMail());
		// ハッシュ化
		var hashPassword = passwordEncoder.encode(form.getPassword());
		user.setPassword(hashPassword);
		user.setType(1);
		Date now = new Date();
		user.setCreateAt(now);
		user.setUpdateAt(now);

		userRepository.saveAndFlush(user);
		return "redirect:/admin/login";
	}

	@GetMapping("/admin/setting")
	public String settingView(Model model) {
		// ログイン中のメールアドレスを取得
		final String mail = SecurityContextHolder.getContext().getAuthentication().getName();
		model.addAttribute("mail", mail);

		return "/admin/user/setting";
	}

	@GetMapping("/admin/setting/mail/edit")
	public String changeMail(Model model) {
		// ログイン中のメールアドレスを取得
		final String mail = SecurityContextHolder.getContext().getAuthentication().getName();

		MailChangeForm form = new MailChangeForm();
		form.setMail(mail); // 初期値を設定
		model.addAttribute("mailChangeForm", form);

		return "/admin/user/changeMail";
	}

	@PostMapping("/admin/setting/mail/edit")
	public String changeMail(Model model, @ModelAttribute @Validated MailChangeForm form, BindingResult bindingResult,
			RedirectAttributes redirectAttributes) {

		// バリデーションエラーがある場合は画面に戻る
		if (bindingResult.hasErrors()) {
			return "/admin/user/changeMail";
		}

		// メールアドレス重複チェック
		if (!userRepository.findByMail(form.getMail()).isEmpty()) {
			bindingResult.rejectValue("mail", "error.mail", "このメールアドレスは既に使用されています");
			return "/admin/user/changeMail";
		}

		SecurityContext context = SecurityContextHolder.getContext();
		String currentUserMail = context.getAuthentication().getName();
		userRepository.findByMail(currentUserMail);
		Optional<User> userOpt = userRepository.findByMail(currentUserMail);

		if (userOpt.isEmpty()) {
			// TODO: ユーザーが見つからなかった場合の処理
			return "";

		}

		User user = userOpt.get();
		user.setMail(form.getMail());
		userRepository.saveAndFlush(user);

		// セッション情報の更新
		// 更新しないと設定画面のメールアドレスが変更前のままになる
		UserDetails userd = userDetailsService.loadUserByUsername(user.getMail());
		context.setAuthentication(
				new UsernamePasswordAuthenticationToken(userd, userd.getPassword(), userd.getAuthorities()));

		redirectAttributes.addFlashAttribute("Successed", true);
		return "redirect:/admin/setting";
	}

	@GetMapping("/admin/setting/password/edit")
	public String showChangePasswordForm(Model model) {
		// Do not set the new password to the current password
		model.addAttribute("passwordChangeForm", new PassChangeForm());
		return "/admin/user/changePassword";
	}

	@PostMapping("/admin/setting/password/edit")
	public String handleChangePassword(Model model,
			@ModelAttribute("passwordChangeForm") @Validated PassChangeForm form, BindingResult bindingResult,
			RedirectAttributes redirectAttributes) {

		// ▼ パスワードと確認用パスワードが一致しているかをチェック
		if (!form.getNewPassword().equals(form.getNewPasswordConfirm())) {
			// ▼ 一致しない場合、BindingResultにエラーを追加
			bindingResult.rejectValue("newPasswordConfirm", "error.newPasswordConfirm", "パスワードと入力が一致しません");
		}

		// ▼ バリデーションエラーがある場合、登録画面に戻る
		if (bindingResult.hasErrors()) {
			model.addAttribute("passwordChangeForm", form);
			return "admin/user/changePassword";
		}

		// ▼ ユーザー情報を取得
		SecurityContext context = SecurityContextHolder.getContext();
		String currentUserMail = context.getAuthentication().getName();
		Optional<User> userOpt = userRepository.findByMail(currentUserMail);

		if (userOpt.isEmpty()) {
			// TODO: ユーザーが見つからなかった場合の処理
			return "";

		}
		User user = userOpt.get();
		// パスワードエンコードにマッチズ今のユーザーのパスワードとユーザーが入力したパスワードのチェック処理
		if (!passwordEncoder.matches(form.getNowPassword(), user.getPassword())) {
			// ▼ 一致しない場合、BindingResultにエラーを追加
			bindingResult.rejectValue("nowPassword", "error.nowPassword", "パスワードが違います");
			return "admin/user/changePassword";
		}

		// ハッシュ化
		var hashPassword = passwordEncoder.encode(form.getNewPassword());
		user.setPassword(hashPassword);
		user.setType(1);
		Date now = new Date();
		user.setCreateAt(now);
		user.setUpdateAt(now);

		userRepository.saveAndFlush(user);
		redirectAttributes.addFlashAttribute("successMessage", true);
		return "redirect:/admin/setting";
	}

}
