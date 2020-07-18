package com.cimb.exam.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cimb.exam.dao.CartRepo;
import com.cimb.exam.dao.GameRepo;
import com.cimb.exam.dao.PaketRepo;
import com.cimb.exam.dao.UserRepo;
import com.cimb.exam.entity.Cart;
import com.cimb.exam.entity.Game;
import com.cimb.exam.entity.Paket;
import com.cimb.exam.entity.User;

@RestController
@RequestMapping("/cart")
@CrossOrigin
public class CartController {
	
	@Autowired
	private UserRepo userRepo;
	
	@Autowired
	private GameRepo gameRepo;
	
	@Autowired
	private CartRepo cartRepo;
	
	@Autowired
	private PaketRepo paketRepo;
	
	@GetMapping
	public Iterable<Cart> showAllCart(){
		return cartRepo.findAll();
	}
	
	@PostMapping("/addToCart/{userId}/{gameId}")
	public Cart addToCart( @PathVariable int userId, @PathVariable int gameId,@RequestBody Cart cart) {
		Game findGame = gameRepo.findById(gameId).get();
		User findUser = userRepo.findById(userId).get();
		findGame.setStokUser((findGame.getStokUser() - cart.getQuantity()));
		gameRepo.save(findGame);
		
		cart.setGame(findGame);
		cart.setUser(findUser);
		return cartRepo.save(cart);
	}
	
	@PostMapping("/addPacketToCart/{userId}/{packetId}")
	public Cart addToPacketCart( @PathVariable int userId, @PathVariable int packetId, @RequestBody Cart cart) {
		Paket findPaket = paketRepo.findById(packetId).get();
		User findUser = userRepo.findById(userId).get();
		
		findPaket.getPaketDetail().forEach(val ->{
			Game findGame = gameRepo.findById(val.getGame().getId()).get();
			findGame.setStokUser((findGame.getStokUser() - cart.getQuantity()));
			gameRepo.save(findGame);
		});
		
		cart.setPaket(findPaket);
		cart.setUser(findUser);
		return cartRepo.save(cart);
	}
	
	@PutMapping("/addToCartSameId/{userId}/{gameId}/{cartId}")
	public Cart addToCartIfSame( @PathVariable int userId, @PathVariable int gameId, @PathVariable int cartId,@RequestBody Cart cart) {
		Game findGame = gameRepo.findById(gameId).get();
		User findUser = userRepo.findById(userId).get();
		Cart findCart = cartRepo.findById(cartId).get();
		findCart.setQuantity(findCart.getQuantity() + cart.getQuantity());
		findGame.setStokUser((findGame.getStokUser() - cart.getQuantity()));
		gameRepo.save(findGame);
		
		findCart.setGame(findGame);
		findCart.setUser(findUser);
		return cartRepo.save(findCart);
	}
	

	@DeleteMapping("/{cartId}/{gameId}")
	public void deleteCart(@PathVariable int cartId,@PathVariable int gameId) {
		Game findGame = gameRepo.findById(gameId).get();
		Cart findCart = cartRepo.findById(cartId).get();
		findGame.setStokUser((findGame.getStokUser() + findCart.getQuantity()));
		gameRepo.save(findGame);
		cartRepo.deleteById(cartId);
	}
	
	@GetMapping("/{userId}")
	public List<Cart> showCartUser(@PathVariable int userId){
		User findUser = userRepo.findById(userId).get();
		return findUser.getCart();
	}
	
	
	@DeleteMapping("/delete/{cartId}")
	public void emptyCart(@PathVariable int cartId) {
		cartRepo.deleteById(cartId);
	}

}
