package com.cimb.exam.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cimb.exam.dao.GameRepo;
import com.cimb.exam.dao.PaketRepo;
import com.cimb.exam.dao.UserRepo;

@RestController
@RequestMapping("/paket")
@CrossOrigin
public class PaketController {
	
	@Autowired
	private PaketRepo paketRepo;
	
	@Autowired
	private UserRepo userRepo;
	
	@Autowired
	private GameRepo gameRepo;

}
