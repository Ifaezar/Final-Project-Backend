package com.cimb.exam.controller;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.nio.file.StandardCopyOption;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.cimb.exam.dao.PageRepo;
import com.cimb.exam.dao.UserRepo;
import com.cimb.exam.entity.User;
import com.cimb.exam.util.EmailUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

@RestController
@RequestMapping("/users")
@CrossOrigin
public class UserController {

	private PasswordEncoder pwEncoder= new BCryptPasswordEncoder();
	
	private String uploadPath = System.getProperty("user.dir") + "/src/main/resources/static/images/";
	
	@Autowired
	private UserRepo userRepo;
	
	@Autowired
	private PageRepo pageRepo;
	
	@Autowired
	private EmailUtil emailUtil;
	
	
	@GetMapping
	public Iterable<User> showAllUser(){
		return userRepo.findAll();
	}
	
	
	@GetMapping("/{id}")
	public User showUserByID(@PathVariable int id){
		User findUser = userRepo.findById(id).get();
		return findUser;
	}
	
	@GetMapping("/profile/{username}")
	public User showUserByUsername(@PathVariable String username){
		User findUser = userRepo.findByUsername(username).get();
		return findUser;
	}
	
	@PostMapping
	public User addUser(@RequestBody User user) {
		Optional<User> findUser = userRepo.findByUsername(user.getUsername());
		Optional<User> findUserEmail = userRepo.findByEmail(user.getEmail());
		
		if(findUser.toString() != "Optional.empty") {
			throw new RuntimeException("Username exists");
		}else if(findUserEmail.toString() != "Optional.empty") {
			throw new RuntimeException("Email exists");
		}else {
			String encodedPassword = pwEncoder.encode(user.getPassword());
			user.setPassword(encodedPassword);
			user.setRole("user");
			user.setVerifikasi("false");
			User saveUser = userRepo.save(user);
			
			this.emailUtil.sendEmail(user.getEmail(), "Verifikasi Email", "<h1>Silahkan klik <a href = \"http://localhost:8080/users/sukses/"+user.getEmail()+"/" + user.getId() +"\">Link<a> untuk verifikasi Email Anda</h1>");
			
			return saveUser;			
		}
	}
	
	@GetMapping("/sukses/{email}/{userId}")
	public String verifikasiEmail(@PathVariable String email, @PathVariable int userId) {
		User findUser = userRepo.findById(userId).get(); 
				
		findUser = userRepo.findByEmail(email).get();
		
		findUser.setVerifikasi("true");
		userRepo.save(findUser);
		return "Selamat Email Anda Sudah Terverifikasi";
		
	}
	
	@GetMapping("/email/{email}")
	public void sendEmailForPassword(@PathVariable String email) {
		User findUser = userRepo.findByEmail(email).get();
		this.emailUtil.sendEmail(email, "Change Password", "<h1>Silahkan klik <a href = \"http://localhost:3000/newPassword/"+findUser.getEmail()+"/" + findUser.getId() +"\">Link<a> untuk merubah password</h1>");
	}
	
	@GetMapping("/password/{email}/{userId}")
	public User newPassword(@PathVariable String email, @PathVariable int userId) {
		User findUser = userRepo.findById(userId).get(); 
				
		findUser = userRepo.findByEmail(email).get();
		
		return findUser;
		
	}
	
	@PostMapping("/login")
	public User loginUser(@RequestBody User user) {
		User findUser = userRepo.findByUsername(user.getUsername()).get();
		
		if(findUser.getVerifikasi().equals("false")) {
			throw new RuntimeException("Maaf mohon veifikasi email terlebih dahulu");
		}else {
			if(pwEncoder.matches(user.getPassword(), findUser.getPassword())) {
				return findUser;
			}else {
				throw new RuntimeException("Maaf Password Salah");
			}
		}
		
		
	}
	
	@PutMapping("/editProfile")
	public User userEdit(@RequestBody User user) {
		return userRepo.save(user);
	}
	
	@PutMapping("/newPassword")
	public User userNewPassword(@RequestBody User user) {
		String encodedPassword = pwEncoder.encode(user.getPassword());
		user.setPassword(encodedPassword);
		return userRepo.save(user);
	}
	
	
	@PutMapping("/{username}/{oldPassword}/{newPassword}")
	public User changePassword(@PathVariable String username, @PathVariable String oldPassword, @PathVariable String newPassword) {
		User findUser = userRepo.findByUsername(username).get();
		String encodedPassword = pwEncoder.encode(newPassword);
		if(pwEncoder.matches(oldPassword, findUser.getPassword())) {
			findUser.setPassword(encodedPassword);
		}else {
			  throw new RuntimeException("maaf passowrd yang anda masuukan salah");
		}

		return userRepo.save(findUser);
	}
	
	@DeleteMapping("/{id}")
	public void deleteUser(@PathVariable int id) {
		userRepo.deleteById(id);
	}
	
	@GetMapping("/user")
	public Page<User> getAllPgaeUser(Pageable pageable){
		return pageRepo.findAll(pageable);
	}
	
	@GetMapping("/users")
	public Page<User> getAllUsers(Pageable pageable){
		return pageRepo.findAllUsers(pageable);
	}
	
	@PostMapping("/profilePicture/{userId}")
	public String AddProfilePicture(@RequestParam("file") MultipartFile file, @PathVariable int userId) throws JsonMappingException, JsonProcessingException { 
		User findUser = userRepo.findById(userId).get();
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
		
		String fileDownloadUrl = ServletUriComponentsBuilder.fromCurrentContextPath().path("/users/download/").path(fileName).toUriString();
		findUser.setPicture(fileDownloadUrl);
		
		userRepo.save(findUser);
		
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
}
