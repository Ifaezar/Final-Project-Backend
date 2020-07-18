package com.cimb.exam.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	private String username;
	private String email;
	private String password;
	private String name;
	private String role;
	private String verifikasi;
	private String address;
	private long telp;
	private String picture;
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "user", cascade	= CascadeType.ALL)
	@JsonIgnore
	private List<Cart> cart;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "user", cascade	= CascadeType.ALL)
	@JsonIgnore
	private List<Transaction> transaction;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "user", cascade	= CascadeType.ALL)
	@JsonIgnore
	private List<GameLibrary> gameLibrary;
	
	public List<Wishlist> getWishlist() {
		return wishlist;
	}
	public void setWishlist(List<Wishlist> wishlist) {
		this.wishlist = wishlist;
	}
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "user", cascade	= CascadeType.ALL)
	@JsonIgnore
	private List<Wishlist> wishlist;
	
	public List<GameLibrary> getGameLibrary() {
		return gameLibrary;
	}
	public void setGameLibrary(List<GameLibrary> gameLibrary) {
		this.gameLibrary = gameLibrary;
	}
	public String getPicture() {
		return picture;
	}
	public void setPicture(String picture) {
		this.picture = picture;
	}
	
	public List<Transaction> getTransaction() {
		return transaction;
	}
	public void setTransaction(List<Transaction> transaction) {
		this.transaction = transaction;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public long getTelp() {
		return telp;
	}
	public void setTelp(long telp) {
		this.telp = telp;
	}
	
	public String getVerifikasi() {
		return verifikasi;
	}
	public void setVerifikasi(String verifikasi) {
		this.verifikasi = verifikasi;
	}
	public List<Cart> getCart() {
		return cart;
	}
	public void setCart(List<Cart> cart) {
		this.cart = cart;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	
}
