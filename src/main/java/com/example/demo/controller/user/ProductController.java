package com.example.demo.controller.user;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.util.StringUtils;

import com.example.demo.entity.Product;
import com.example.demo.entity.Stock;
import com.example.demo.repository.ProductRepository;

@Controller("UserController")
public class ProductController {

	@Autowired
	private ProductRepository productRepository;

	@GetMapping("/")
	public String listView(Model model, @RequestParam(name = "name", required = false) String name,
			@PageableDefault(page = 0, size = 20) Pageable pageable) {

		// name をトリムして空白のみは null 扱いにする
		String keyword = StringUtils.hasText(name) ? name.trim() : null;

		// テンプレート側で使う属性をセット
		// header.html が ${name} を参照しているので空文字を入れておく（null 表示を避けるため）
		model.addAttribute("name", keyword != null ? keyword : "");
		// 検索メッセージ用に別名でも渡しておく（テンプレートが searchKeyword を参照する場合に対応）
		model.addAttribute("searchKeyword", keyword);

		Page<Product> productsPage;
		if (keyword != null) {
			productsPage = productRepository.findByNameContainingAndStatus(keyword, 0, pageable);
		} else {
			productsPage = productRepository.findByStatus(0, pageable);
		}

		model.addAttribute("page", productsPage);
		model.addAttribute("products", productsPage.getContent());
		return "user/index";
	}

	@GetMapping("/{id}")
	public String changeView(Model model, @PathVariable int id) {
		Optional<Product> productOpt = productRepository.findById(id);
		if (productOpt.isEmpty()) {
			// ここで400エラー画面へ（400.html）遷移させる
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "商品が存在しません");
		}
		Product product = productOpt.get();
		Stock stock = product.getStock();
		model.addAttribute("product", product);

		return "user/product/detail";
	}
}
