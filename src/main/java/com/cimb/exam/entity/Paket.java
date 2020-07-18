package com.cimb.exam.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Paket {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private String image;
	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public List<Cart> getCart() {
		return cart;
	}

	public void setCart(List<Cart> cart) {
		this.cart = cart;
	}

	private int stock;
	private int totalPrice;
	private String paketName;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "paket", cascade	= CascadeType.ALL)
	@JsonIgnore
	private List<Cart> cart;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "paket", cascade	= CascadeType.ALL)
	private List<PaketDetail> paketDetail;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getStock() {
		return stock;
	}

	public String getPaketName() {
		return paketName;
	}

	public void setPaketName(String paketName) {
		this.paketName = paketName;
	}

	public void setStock(int stock) {
		this.stock = stock;
	}

	public int getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(int totalPrice) {
		this.totalPrice = totalPrice;
	}

	public List<PaketDetail> getPaketDetail() {
		return paketDetail;
	}

	public void setPaketDetail(List<PaketDetail> paketDetail) {
		this.paketDetail = paketDetail;
	}
}
