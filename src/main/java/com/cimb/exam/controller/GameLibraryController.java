package com.cimb.exam.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cimb.exam.dao.GameLibraryRepo;
import com.cimb.exam.dao.GameRepo;
import com.cimb.exam.dao.TransactionRepo;
import com.cimb.exam.dao.UserRepo;
import com.cimb.exam.entity.Game;
import com.cimb.exam.entity.GameLibrary;
import com.cimb.exam.entity.Transaction;
import com.cimb.exam.entity.User;
import com.cimb.exam.util.EmailUtil;

@RestController
@RequestMapping("/gameLibrary")
@CrossOrigin
public class GameLibraryController {
	
	@Autowired
	private UserRepo userRepo;
	
	@Autowired
	private GameRepo gameRepo;
	
	@Autowired
	private TransactionRepo transactionRepo;
	
	@Autowired
	private GameLibraryRepo gameLibraryRepo;
	
	@Autowired
	private EmailUtil emailUtil;
	
	@GetMapping("/{userId}")
	public List<GameLibrary> getGameLibraryByUserId(@PathVariable int userId){
		User findUser = userRepo.findById(userId).get();
		return findUser.getGameLibrary();
	}
	
	@GetMapping("/gameLibraryById/{gameLibraryId}")
	public GameLibrary getGameLibraryById(@PathVariable int gameLibraryId) {
		GameLibrary findGameLibrary = gameLibraryRepo.findById(gameLibraryId).get();
		return findGameLibrary;
	}
	
	@PostMapping("/add/{transactionId}/{gameId}/{quantity}")
	public GameLibrary addToGameLibrary(
			@PathVariable int transactionId, 
			@PathVariable int gameId,
			@PathVariable int quantity,
			@RequestBody GameLibrary gameLibrary
			) {
		Transaction findTransaction = transactionRepo.findById(transactionId).get();
		Game findGame = gameRepo.findById(gameId).get();
		
		gameLibrary.setUser(findTransaction.getUser());
		gameLibrary.setGame(findGame);
		gameLibrary.setStock(quantity);
		gameLibraryRepo.save(gameLibrary);
	
		return gameLibrary;
	}
	
	@PostMapping("/addToFriend/{transactionId}/{email}")
	public GameLibrary addGameToFriend(@PathVariable String email, @PathVariable int transactionId, @RequestBody GameLibrary gameLibrary) {
		GameLibrary findGameLibrary = gameLibraryRepo.findById(transactionId).get();
		User findUser = userRepo.findByEmail(email).get();
		
		if(findGameLibrary.getStock() == 1) {
			gameLibraryRepo.deleteById(findGameLibrary.getId());
		}else {
			findGameLibrary.setStock(findGameLibrary.getStock() - 1);
			gameLibraryRepo.save(findGameLibrary);			
		}
		
		gameLibrary.setStock(1);
		gameLibrary.setUser(findUser);
		gameLibrary.setGame(findGameLibrary.getGame());
		gameLibraryRepo.save(gameLibrary);
		
		
		this.emailUtil.sendEmail(findUser.getEmail(), "New Game", "<h1>Selamat anda mendapatkan game : " + findGameLibrary.getGame().getName()
				+ "dari " + findGameLibrary.getUser().getEmail()  +"</h1>");
				
		return gameLibrary;
	}

}
