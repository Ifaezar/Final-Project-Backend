package com.cimb.exam.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cimb.exam.dao.GameRepo;
import com.cimb.exam.dao.PaketDetailRepo;
import com.cimb.exam.dao.PaketRepo;
import com.cimb.exam.dao.UserRepo;
import com.cimb.exam.entity.Category;
import com.cimb.exam.entity.Game;
import com.cimb.exam.entity.Paket;
import com.cimb.exam.entity.PaketDetail;

@RestController
@RequestMapping("/paket")
@CrossOrigin
public class PaketController {
	
	@Autowired
	private PaketRepo paketRepo;
	
	
	@Autowired
	private GameRepo gameRepo;
	
	@Autowired
	private PaketDetailRepo paketDetailRepo;
	
	
	@GetMapping
	public Iterable<Paket> showAllPaket(){
		return paketRepo.findAll();
	}
	
	@GetMapping("/{paketId}")
	public Paket showPaketById(@PathVariable int paketId) {
		return paketRepo.findById(paketId).get();
	}
	
	@PostMapping
	public Paket addPaket(@RequestBody Paket paket) {
		if(paket.getStock() == 0) {
			throw new RuntimeException("Stok paket Habis");
		}else {
			return paketRepo.save(paket);			
		}
	}
	
	@PutMapping("/edit")
	public Paket editPaket(@RequestBody Paket paket) {
		return paketRepo.save(paket);
	}
	
	@DeleteMapping("/{paketId}")
	public void deletePaket(@PathVariable int paketId) {
		Paket findPaket = paketRepo.findById(paketId).get();
		findPaket.getPaketDetail().forEach(val ->{
			Game findGame = gameRepo.findById(val.getGame().getId()).get();
			findGame.setStokAdmin(findGame.getStokAdmin() + findPaket.getStock());
			findGame.setStokUser(findGame.getStokUser() + findPaket.getStock());
			
			gameRepo.save(findGame);
		});
		
		paketRepo.deleteById(paketId);
	}
	
	@DeleteMapping("/{paketId}/delete/{paketDetailId}")
	public Paket deleteCategoryGame(@PathVariable int paketId, @PathVariable int paketDetailId) {
		Paket findPaket = paketRepo.findById(paketId).get();
		
		PaketDetail findPaketDetail = paketDetailRepo.findById(paketDetailId).get();
		findPaket.getPaketDetail().remove(findPaketDetail);
		paketRepo.save(findPaket);
		return findPaket;
	}
	

}
