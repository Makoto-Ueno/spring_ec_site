package com.example.demo.form;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public class ProductRegistForm {

	private int productId;

	@NotEmpty(message = "商品名を登録してください")
	@Size(max = 50, message = "50文字以内で登録してください")
	private String name;

	@NotEmpty(message = "価格を登録してください")
	@Pattern(regexp = "0123456789", message = "半角数字で入力してください")
	@Positive(message = "価格をマイナスで登録することはできません")
	@Max(99999999)
	private int amount;

	@NotEmpty(message = "商品説明を登録してください")
	@Size(max = 300, message = "300文字以内で登録してください")
	private String description;

	private String imageUrl;
	private int status;

	@NotEmpty(message = "在庫数を登録してください")
	@Positive(message = "在庫数をマイナスで登録することはできません")
	private int quantity;

	public int getProductId() {
		return productId;
	}

	public void setProductId(int productId) {
		this.productId = productId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

}
