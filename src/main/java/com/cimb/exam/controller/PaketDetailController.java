package com.cimb.exam.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cimb.exam.dao.GameRepo;
import com.cimb.exam.dao.PaketDetailRepo;
import com.cimb.exam.dao.PaketRepo;
import com.cimb.exam.dao.UserRepo;
import com.cimb.exam.entity.Game;
import com.cimb.exam.entity.Paket;
import com.cimb.exam.entity.PaketDetail;

@RestController
@RequestMapping("/paketDetail")
@CrossOrigin
public class PaketDetailController {

	@Autowired
	private PaketRepo paketRepo;
	
	@Autowired
	private PaketDetailRepo paketDetailRepo;
	
	@Autowired
	private GameRepo gameRepo;
	
	@GetMapping
	public Iterable<PaketDetail> showAllPaketDetail(){
		return paketDetailRepo.findAll();
	}
	
	@PostMapping("/{paketId}/{gameId}")
	public PaketDetail addToPaketDetail(@PathVariable int paketId, @PathVariable int gameId, @RequestBody PaketDetail paketDetail) {
		Paket findPaket = paketRepo.findById(paketId).get();
		Game findGame = gameRepo.findById(gameId).get();
		
		paketDetail.setGame(findGame);
		paketDetail.setPaket(findPaket);
		
		
		findGame.setStokAdmin(findGame.getStokAdmin() - findPaket.getStock());
		findGame.setStokUser(findGame.getStokUser() - findPaket.getStock());
		
		gameRepo.save(findGame);
		
		return paketDetailRepo.save(paketDetail);
		
	}
}
