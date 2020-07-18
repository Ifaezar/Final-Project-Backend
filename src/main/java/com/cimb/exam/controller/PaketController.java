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
import com.cimb.exam.dao.PaketRepo;
import com.cimb.exam.dao.UserRepo;
import com.cimb.exam.entity.Paket;

@RestController
@RequestMapping("/paket")
@CrossOrigin
public class PaketController {
	
	@Autowired
	private PaketRepo paketRepo;
	
	
	@Autowired
	private GameRepo gameRepo;
	
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
		return paketRepo.save(paket);
	}
	

}
