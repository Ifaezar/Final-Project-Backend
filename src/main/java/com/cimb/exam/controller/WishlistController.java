package com.cimb.exam.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cimb.exam.dao.GameRepo;
import com.cimb.exam.dao.UserRepo;
import com.cimb.exam.dao.WishlistRepo;
import com.cimb.exam.entity.Cart;
import com.cimb.exam.entity.Game;
import com.cimb.exam.entity.User;
import com.cimb.exam.entity.Wishlist;

@RestController
@RequestMapping("/wishlist")
@CrossOrigin
public class WishlistController {

	@Autowired
	private UserRepo userRepo;
	
	@Autowired
	private GameRepo gameRepo;
	
	@Autowired
	private WishlistRepo wishlistRepo;
	
	String message;
	
	@PostMapping("/addTowishlist/{userId}/{gameId}")
	public Wishlist addToCart( @PathVariable int userId, @PathVariable int gameId,@RequestBody Wishlist wishlist) {
		Game findGame = gameRepo.findById(gameId).get();
		User findUser = userRepo.findById(userId).get();
		
		wishlist.setGame(findGame);
		wishlist.setUser(findUser);
		return wishlistRepo.save(wishlist);
	}
	
	@GetMapping("/{userId}")
	public List<Wishlist> showWishlistUser(@PathVariable int userId){
		User findUser = userRepo.findById(userId).get();
		return findUser.getWishlist();
	}
	
	@DeleteMapping("/delete/{wishlistId}")
	public void emptyCart(@PathVariable int wishlistId) {
		wishlistRepo.deleteById(wishlistId);
	}
	
}
