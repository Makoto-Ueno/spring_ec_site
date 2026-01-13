package com.example.demo.controller.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.util.StringUtils;

import com.example.demo.entity.Product;
import com.example.demo.repository.ProductRepository;

@Controller("UserController")
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    @GetMapping("/")
    public String listView(Model model,
            @RequestParam(name = "name", required = false) String name,
            @PageableDefault(page = 0, size = 20) Pageable pageable) {

        Page<Product> productsPage;
        if (StringUtils.hasText(name)) {
            // name が空でなければ検索
            productsPage = productRepository.findByNameContainingAndStatus(name, 0, pageable);
            model.addAttribute("name", name);
        } else {
            // name が空なら通常一覧
            productsPage = productRepository.findByStatus(0, pageable);
            model.addAttribute("name", ""); // null のままだとテンプレートで 'null' 表示されることを防ぐ
        }

        model.addAttribute("page", productsPage);
        model.addAttribute("products", productsPage.getContent());
        return "user/index";
    }
}
