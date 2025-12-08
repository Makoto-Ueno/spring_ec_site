package com.example.demo.controller.admin;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.entity.Product;
import com.example.demo.entity.Stock;
import com.example.demo.entity.User;

import com.example.demo.form.ProductRegistForm;
import com.example.demo.repository.ProductRepository;
import com.example.demo.repository.StockRepository;
import com.example.demo.repository.UserRepository;


@Controller
public class ProductController {
	@Value("${image.path}")
	private String filePath;

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private StockRepository stockRepository;

	@Autowired
	private UserRepository userRepository;
	
	@GetMapping("/admin")
	public String indexAdmin(Model model, @PageableDefault(page = 0, size = 20) Pageable pageable) {
		Page<Product> productsPage = productRepository.findAll(pageable);
		model.addAttribute("page", productsPage);
		model.addAttribute("products", productsPage.getContent());
		return "admin/product/index";
	}

	@GetMapping("/admin/product/new")
	public String registView(Model model, ProductRegistForm form) {
		model.addAttribute(form);
		return "admin/product/regist";
	}

	@PostMapping("/admin/product/new")
	public String register(Model model, @ModelAttribute @Validated ProductRegistForm form, BindingResult bindingResult,
			RedirectAttributes redirectAttributes) throws IOException {

		boolean hasFormError = false;
		boolean hasImageRegistError = false;
		if (bindingResult.hasErrors()) {
			model.addAttribute(form);
			hasFormError = true;
		}

		// ▼ アップロードされた画像を取得
		MultipartFile image = form.getImage();
		String imgUrl = "/img/no_image.png"; // デフォルト

		if (image != null && !image.isEmpty()) {

			try {
				// 画像をアップロードするパスを指定
				// image.getOriginalFilename()：ファイル名を取得
				String imgPath = filePath + image.getOriginalFilename();

				// 画像をバイナリデータとして取得
				byte[] content = image.getBytes();
				// 画像を保存
				// Files.write：ファイルの書き込み
				// Paths.get(...)：パスを取得
				// content：保存するデータ
				Files.write(Paths.get(imgPath), content);

				// アップロードした画像のURLを指定
				// HTMLで画像を読み込む際は「static」配下からパスを指定する
				imgUrl = "/img/" + image.getOriginalFilename();
			}

			catch (IOException e) {
				hasImageRegistError = true;
				model.addAttribute("UnSuccessed", true);
			}

		}

		if (hasFormError || hasImageRegistError) {
			return "admin/product/regist";
		}
		
		// ▼ 認証情報（メール）から User を検索して userId を取得する
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth == null || auth.getPrincipal() == null || !auth.isAuthenticated()) {
			throw new IllegalStateException("認証されていません。ログインしてください。");
		}

		String currentUserMail = auth.getName(); // UserDetailsService で username に mail を設定している想定
		User user = userRepository.findByMail(currentUserMail)
				.orElseThrow(() -> new IllegalStateException("ログインユーザが見つかりません: " + currentUserMail));
		int userId = user.getId();

		Product product = new Product();
		product.setName(form.getName());
		product.setAmount(form.getAmount());
		product.setDescription(form.getDescription());
		product.setImageUrl(imgUrl); // ここで保存した URL を使う
		product.setCreateUser(userId); // 作成者 ID をセット
		product.setUpdateUser(userId); // 初回は更新者も同じ
		product.setStatus(form.getStatus());
		productRepository.saveAndFlush(product);

		Stock stock = new Stock();
		stock.setQuantity(form.getQuantity());

		stock.setProductId(form.getProductId());
		stock.setProduct(product);
		stockRepository.saveAndFlush(stock);

		redirectAttributes.addFlashAttribute("successed", true);

		return "redirect:/admin";
	}


	@GetMapping("/admin/product/{id}")
	public String changeView(Model model, @PathVariable int id) {
		Optional<Product> productOpt = productRepository.findById(id);
		if (productOpt.isEmpty()) {
			//　商品が存在しない時、500エラーで500.htmlが表示される
	        throw new RuntimeException("商品が存在しません");
		}
		Product product = productOpt.get();
		ProductRegistForm form = new ProductRegistForm();
		form.setProductId(id);
		form.setName(product.getName());
		form.setAmount(product.getAmount());
		form.setDescription(product.getDescription());
		form.setImageUrl(product.getImageUrl());
		form.setStatus(product.getStatus());

		Stock stock = product.getStock();
		form.setQuantity(stock.getQuantity());

		model.addAttribute(form);
		return "admin/product/change";
	}

	@PostMapping("/admin/product/{id}")
	public String change(Model model, @ModelAttribute @Validated ProductRegistForm form, BindingResult bindingResult,
			RedirectAttributes redirectAttributes, @PathVariable int id) throws IOException {

		if (bindingResult.hasErrors()) {
			form.setProductId(id);
			model.addAttribute(form);
			return "admin/product/change";
		}

		// ▼ アップロードされた画像を取得
		MultipartFile image = form.getImage();
		String imgUrl = "/img/no_image.png";// デフォルト

		if (image != null && !image.isEmpty()) {

			try {
				// 画像をアップロードするパスを指定
				// image.getOriginalFilename()：ファイル名を取得
				String imgPath = filePath + image.getOriginalFilename();

				// 画像をバイナリデータとして取得
				byte[] content = image.getBytes();
				// 画像を保存
				// Files.write：ファイルの書き込み
				// Paths.get(...)：パスを取得
				// content：保存するデータ
				Files.write(Paths.get(imgPath), content);

				// アップロードした画像のURLを指定
				// HTMLで画像を読み込む際は「static」配下からパスを指定する
				imgUrl = "/img/" + image.getOriginalFilename();
			}

			catch (IOException e) {
				redirectAttributes.addFlashAttribute("UnSuccessed", true);
				return "redirect:/admin/product/{id}";
			}

		}
		Optional<Product> productOpt = productRepository.findById(id);
		if (productOpt.isEmpty()) {
			return "";
		}
		
		// ▼ 認証情報（メール）から User を検索して userId を取得する
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth == null || auth.getPrincipal() == null || !auth.isAuthenticated()) {
			throw new IllegalStateException("認証されていません。ログインしてください。");
		}

		String currentUserMail = auth.getName(); // UserDetailsService で username に mail を設定している想定
		User user = userRepository.findByMail(currentUserMail)
				.orElseThrow(() -> new IllegalStateException("ログインユーザが見つかりません: " + currentUserMail));
		int userId = user.getId();
		
		Product product = productOpt.get();
		product.setName(form.getName());
		product.setAmount(form.getAmount());
		product.setDescription(form.getDescription());
		product.setImageUrl(imgUrl); // ここで保存した URL を使う
		product.setStatus(form.getStatus());
		product.setUpdateUser(userId); 

		productRepository.saveAndFlush(product);

		Stock stock = product.getStock();
		stock.setQuantity(form.getQuantity());
		stock.setProduct(product);
		stockRepository.saveAndFlush(stock);

		redirectAttributes.addFlashAttribute("changeSuccessed", true);

		return "redirect:/admin/product/{id}";
	}

	@PostMapping("/admin/product/{id}/stop")
	public String changeStatus(Model model, @ModelAttribute ProductRegistForm form, BindingResult bindingResult,
			RedirectAttributes redirectAttributes, @PathVariable int id) {
		Optional<Product> productOpt = productRepository.findById(id);
		if (productOpt.isEmpty()) {
			//　商品が存在しない時、500エラーで500.htmlが表示される
	        throw new RuntimeException("商品が存在しません");
		}
		
		// ▼ 認証情報（メール）から User を検索して userId を取得する
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth == null || auth.getPrincipal() == null || !auth.isAuthenticated()) {
			throw new IllegalStateException("認証されていません。ログインしてください。");
		}

		String currentUserMail = auth.getName(); // UserDetailsService で username に mail を設定している想定
		User user = userRepository.findByMail(currentUserMail)
				.orElseThrow(() -> new IllegalStateException("ログインユーザが見つかりません: " + currentUserMail));
		int userId = user.getId();
		
		Product product = productOpt.get();
		product.setStatus(1);
		product.setUpdateUser(userId);

		productRepository.saveAndFlush(product);

		redirectAttributes.addFlashAttribute("statusStpoSuccessed", true);
		return "redirect:/admin/product/{id}";
	}

	@PostMapping("/admin/product/{id}/restart")
	public String restartStatus(Model model, @ModelAttribute ProductRegistForm form, BindingResult bindingResult,
			RedirectAttributes redirectAttributes, @PathVariable int id) {
		Optional<Product> productOpt = productRepository.findById(id);
		if (productOpt.isEmpty()) {
			//　商品が存在しない時、500エラーで500.htmlが表示される
	        throw new RuntimeException("商品が存在しません");
		}
		
		// ▼ 認証情報（メール）から User を検索して userId を取得する
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth == null || auth.getPrincipal() == null || !auth.isAuthenticated()) {
			throw new IllegalStateException("認証されていません。ログインしてください。");
		}

		String currentUserMail = auth.getName(); // UserDetailsService で username に mail を設定している想定
		User user = userRepository.findByMail(currentUserMail)
				.orElseThrow(() -> new IllegalStateException("ログインユーザが見つかりません: " + currentUserMail));
		int userId = user.getId();
		
		Product product = productOpt.get();
		product.setStatus(0);
		product.setUpdateUser(userId);

		productRepository.saveAndFlush(product);

		redirectAttributes.addFlashAttribute("restartSuccessed", true);
		return "redirect:/admin/product/{id}";
	}

}