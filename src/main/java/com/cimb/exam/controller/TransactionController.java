package com.cimb.exam.controller;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.cimb.exam.dao.GameRepo;
import com.cimb.exam.dao.PaketRepo;
import com.cimb.exam.dao.TransactionRepo;
import com.cimb.exam.dao.UserRepo;
import com.cimb.exam.entity.Paket;
import com.cimb.exam.entity.Transaction;
import com.cimb.exam.entity.User;
import com.cimb.exam.util.EmailUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

@RestController
@RequestMapping("/transaction")
@CrossOrigin
public class TransactionController {

	@Autowired
	private TransactionRepo transactionRepo;
	
	@Autowired
	private UserRepo userRepo;
	
	@Autowired
	private GameRepo gameRepo;
	
	@Autowired
	private PaketRepo paketRepo;
	
	@Autowired
	private EmailUtil emailUtil;
	
	private String uploadPath = System.getProperty("user.dir") + "/src/main/resources/static/images/BuktiTrf";
	private String Message;
	
	@GetMapping
	public Iterable<Transaction> showAllTransaction(@RequestParam String name){
		return transactionRepo.findByName(name);
	}
	
	
	@PostMapping("/{userId}/{totalPrice}")
	public Transaction addToTransaction(@PathVariable int userId, @PathVariable int totalPrice, @RequestBody Transaction transaction, @RequestParam String checkoutTime){
		User findUser = userRepo.findById(userId).get();
		transaction.setTotal_price(totalPrice);
		transaction.setUser(findUser);
		transaction.setCheckoutTime(checkoutTime);
		return transactionRepo.save(transaction);
	}
	
	@PostMapping("/{userId}/{totalPrice}/{paketId}")
	public Transaction addToTransactionPaket(@PathVariable int userId, @PathVariable int totalPrice, @PathVariable int paketId, @RequestBody Transaction transaction, @RequestParam String checkoutTime){
		User findUser = userRepo.findById(userId).get();
		Paket findPaket = paketRepo.findById(paketId).get();
		transaction.setTotal_price(totalPrice);
		transaction.setUser(findUser);
		transaction.setCheckoutTime(checkoutTime);
		transaction.setPaket(findPaket);
		return transactionRepo.save(transaction);
	}
	
	@GetMapping("/{transactionId}/{userId}")
	public User showUser(@PathVariable int transactionId, @PathVariable int userId) {
		User findUser = userRepo.findById(userId).get();
		return findUser;
	}
	
	@GetMapping("/history/{userId}")
	public List<Transaction> showTransactionByUser(@PathVariable int userId) {
		User findUser = userRepo.findById(userId).get();
		return findUser.getTransaction();
	}
	
	@PostMapping("/uploadBuktiTransfer/{transactionId}")
	public String addBuktiTransfer(@RequestParam("file") MultipartFile file, @PathVariable int transactionId)throws JsonMappingException, JsonProcessingException{
		Transaction findTransaction = transactionRepo.findById(transactionId).get();
		
		Date date = new Date();
		
		String fileExtension = file.getContentType().split("/")[1];
		
		String newFileName = "PROD-" + date.getTime() + "." + fileExtension;
		
		String fileName = StringUtils.cleanPath(newFileName);
		Path path = Paths.get(StringUtils.cleanPath(uploadPath) + fileName);
		try {
			Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
		}catch(IOException e) {
			e.printStackTrace();
		}
		
		String fileDownloadUrl = ServletUriComponentsBuilder.fromCurrentContextPath().path("/transaction/download/").path(fileName).toUriString();
		findTransaction.setBuktiTransfer(fileDownloadUrl);
		
		transactionRepo.save(findTransaction);
		return fileDownloadUrl;
	}
	
	@GetMapping("/download/{fileName:.+}")
	public ResponseEntity<Object> downloadFile(@PathVariable String fileName){
		Path path = Paths.get(uploadPath + fileName);
		Resource resource = null;
		
		try {
			resource = new UrlResource(path.toUri());
		}catch (MalformedURLException e) {
			e.printStackTrace();
		}
		System.out.println("DOWNLAOD");
		
		return ResponseEntity.ok().contentType(MediaType.parseMediaType("application/octet-stream"))
				.header(HttpHeaders.CONTENT_DISPOSITION,"attachment: filename=\"" + resource.getFilename()+ "\"").body(resource);
	}
	
	@PutMapping("/reject/{transactionId}")
	public Transaction rejectTransaction(@PathVariable int transactionId) {
		Transaction findTransaction = transactionRepo.findById(transactionId).get();
		User findUser = userRepo.findById(findTransaction.getUser().getId()).get();
		findTransaction.setBuktiTransfer(null);
		
		this.emailUtil.sendEmail(findUser.getEmail(), "Transaction Reject", "<h1>Mohon maaf proses transaksi anda gagal mohon kirim ulang bukti transfer </h1>");
		
		return transactionRepo.save(findTransaction);
	}
	
	@PutMapping("/accept/{transactionId}")
	public Transaction acceptTransaction(@PathVariable int transactionId, @RequestParam String acceptTime) {
		Transaction findTransaction = transactionRepo.findById(transactionId).get();
		User findUser = userRepo.findById(findTransaction.getUser().getId()).get();
		Message ="";
		findTransaction.setAcceptTime(acceptTime);
		findTransaction.setStatus("Accept");
		
		if(findTransaction.getPaket() != null) {
			Paket findPaket = paketRepo.findById(findTransaction.getPaket().getId()).get();
			findPaket.setStock(findPaket.getStock() - 1);
			paketRepo.save(findPaket);
		}
		
		Message = "<h1> Terima Kasih sudah membeli di Epic Game </h1>";
		
		findTransaction.getTransactionDetail().forEach(val ->{
			val.getGame().setStokAdmin((val.getGame().getStokAdmin()- val.getQuantity()));
			val.getGame().setSold(val.getQuantity());
			Message += "<h1> Game Name = " + val.getGame().getName()+ "jumlah = " + val.getQuantity() + "harga game = " + val.getPriceProduct() +"</h1>";
			gameRepo.save(val.getGame());
		});
		
		Message += "<h1> total harga = " + findTransaction.getTotal_price() + "</h1>";
		
		this.emailUtil.sendEmail(findUser.getEmail(), "Transaction Success", Message);
		return transactionRepo.save(findTransaction);
		
	}
}
