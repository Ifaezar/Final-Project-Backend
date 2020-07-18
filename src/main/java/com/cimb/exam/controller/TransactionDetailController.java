package com.cimb.exam.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cimb.exam.dao.GameRepo;
import com.cimb.exam.dao.TransactionDetailRepo;
import com.cimb.exam.dao.TransactionRepo;
import com.cimb.exam.entity.Game;
import com.cimb.exam.entity.Transaction;
import com.cimb.exam.entity.TransactionDetail;

@RestController
@RequestMapping("/transactionDetail")
@CrossOrigin
public class TransactionDetailController {
	
	@Autowired
	private TransactionDetailRepo transactionDetailRepo;
	
	@Autowired
	private TransactionRepo transactionRepo;
	
	@Autowired
	private GameRepo gameRepo;
	
	@GetMapping
	public Iterable<TransactionDetail> showAllTransactionDetail(){
		return transactionDetailRepo.findAll();
	}
	
	@PostMapping("/{transactionId}/{gameId}")
	public TransactionDetail addToTransactionDetail(
			@PathVariable int transactionId,
			@PathVariable int gameId,
			@RequestBody TransactionDetail transactionDetail,
			@RequestParam int priceProduct,
			@RequestParam int quantity
			) 
	{
		Transaction findTransaction = transactionRepo.findById(transactionId).get();
		Game findGame = gameRepo.findById(gameId).get();
		
		
		transactionDetail.setTransaction(findTransaction);
		transactionDetail.setGame(findGame);
		transactionDetail.setPriceProduct(priceProduct);
		transactionDetail.setQuantity(quantity);
		return transactionDetailRepo.save(transactionDetail);
	}

}
