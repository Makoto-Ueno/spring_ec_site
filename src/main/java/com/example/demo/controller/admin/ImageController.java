package com.example.demo.controller.admin;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class ImageController {

	@Value("${image.path}")
	private String filePath;

	@GetMapping("/image")
	public String imageView(Model model) {
		// 引数に指定したパスのフォルダ/ファイルを操作できるファイルクラスを作成
		File dir = new File(filePath);
		// src/main/resources/static/img/に含まれるファイルを全て取得
		File[] files = dir.listFiles();
		List<String> imagePaths = new ArrayList<>();
		for (File file : files) {
			// 「static」から始まるパスを取得
			imagePaths.add("/img/" + file.getName());
		}
		model.addAttribute("imagePaths", imagePaths);
		return "image";
	}

	@PostMapping("/image")
	public String imageUpload(@RequestParam MultipartFile image, Model model) throws IOException {
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
		String imgUrl = "/img/" + image.getOriginalFilename();
		model.addAttribute("imageUrl", imgUrl);
		return "redirect:/image";
	}
}
