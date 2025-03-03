package com.example.demo.entity;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;

@Entity
public class Stock {
	@Id
	private int productId;

	@Column(nullable = false)
	private int quantity;

	@Column(nullable = false)
	private Date createAt;

	@Column(nullable = false)
	private int createUser;

	@Column(nullable = false)
	private Date updateAt;

	@Column(nullable = false)
	private int updateUser;

	@PrePersist
	public void preInsert() {
		Date date = new Date();
		setCreateAt(date);
		setUpdateAt(date);
	}

	@PreUpdate
	public void preUpdate() {
		setUpdateAt(new Date());
	}

	@OneToOne
	@MapsId
	@JoinColumn(name = "product_id", referencedColumnName = "product_id")
	private Product product;

	public int getProductId() {
		return productId;
	}

	public void setProductId(int productId) {
		this.productId = productId;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public Date getCreateAt() {
		return createAt;
	}

	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
	}

	public int getCreateUser() {
		return createUser;
	}

	public void setCreateUser(int createUser) {
		this.createUser = createUser;
	}

	public Date getUpdateAt() {
		return updateAt;
	}

	public void setUpdateAt(Date updateAt) {
		this.updateAt = updateAt;
	}

	public int getUpdateUser() {
		return updateUser;
	}

	public void setUpdateUser(int updateUser) {
		this.updateUser = updateUser;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}
}