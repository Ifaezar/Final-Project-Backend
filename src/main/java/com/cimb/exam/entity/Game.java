package com.cimb.exam.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Game {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	private String name;
	private int price;
	private String description;
	private String picture;
	private String Developer;
	private int stokUser;
	private int stokAdmin;
	public int getSold() {
		return sold;
	}

	public void setSold(int sold) {
		this.sold = sold;
	}

	private int sold;
	
	public int getStokUser() {
		return stokUser;
	}

	public void setStokUser(int stokUser) {
		this.stokUser = stokUser;
	}

	public int getStokAdmin() {
		return stokAdmin;
	}

	public void setStokAdmin(int stokAdmin) {
		this.stokAdmin = stokAdmin;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "game", cascade	= CascadeType.ALL)
	@JsonIgnore
	private List<Cart> cart;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "game", cascade	= CascadeType.ALL)
	@JsonIgnore
	private List<GameLibrary> gameLibrary;
	
	public List<GameLibrary> getGameLibrary() {
		return gameLibrary;
	}

	public void setGameLibrary(List<GameLibrary> gameLibrary) {
		this.gameLibrary = gameLibrary;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "game", cascade	= CascadeType.ALL)
	@JsonIgnore
	private List<TransactionDetail> transactionDetail;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "game", cascade	= CascadeType.ALL)
	@JsonIgnore
	private List<PaketDetail> paketDetail;
	
	public List<PaketDetail> getPaketDetail() {
		return paketDetail;
	}

	public void setPaketDetail(List<PaketDetail> paketDetail) {
		this.paketDetail = paketDetail;
	}

	public List<TransactionDetail> getTransactionDetail() {
		return transactionDetail;
	}

	public void setTransactionDetail(List<TransactionDetail> transactionDetail) {
		this.transactionDetail = transactionDetail;
	}

	public List<Cart> getCart() {
		return cart;
	}

	public void setCart(List<Cart> cart) {
		this.cart = cart;
	}

	public String getDeveloper() {
		return Developer;
	}

	public void setDeveloper(String developer) {
		Developer = developer;
	}

	@ManyToMany(fetch = FetchType.LAZY, cascade = { CascadeType.DETACH, CascadeType.PERSIST,
			CascadeType.REFRESH })
	@JoinTable(name = "category_game",joinColumns = @JoinColumn(name = "game_id"), 
	inverseJoinColumns = @JoinColumn(name = "category_id"))
	private List<Category> category;
	

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<Category> getCategory() {
		return category;
	}

	public void setCategory(List<Category> category) {
		this.category = category;
	}
	
	public String getPicture() {
		return picture;
	}

	public void setPicture(String picture) {
		this.picture = picture;
	}
}
