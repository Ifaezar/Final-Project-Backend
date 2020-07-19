package com.cimb.exam.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

import com.cimb.exam.dao.CategoryRepo;
import com.cimb.exam.dao.GameRepo;
import com.cimb.exam.dao.UserRepo;
import com.cimb.exam.entity.Category;
import com.cimb.exam.entity.Game;
import com.cimb.exam.entity.User;
import com.cimb.exam.util.EmailUtil;

@RestController
@RequestMapping("/game")
@CrossOrigin
public class GameControler {

	@Autowired
	private GameRepo gameRepo;
	
	@Autowired
	private CategoryRepo categoryRepo;
	
	@Autowired
	private UserRepo userRepo;
	
	@Autowired
	private EmailUtil emailUtil;
	

	@GetMapping
	public Iterable<Game> showAllGame(){
		return gameRepo.findAll();
	}
	
	@GetMapping("/sold")
	public Iterable<Game> showGameSold(){
		return gameRepo.sortBySold();
	}
	
	@GetMapping("/gamesPage")
	public Page<Game> showGamePage( Pageable pageable){
		return gameRepo.findAll(pageable);
	}
	
	@GetMapping("/gamesNameAsc")
	public Page<Game> showGameByCategoryNameAsc(@RequestParam String category, Pageable pageable){
		return gameRepo.findGameFilterNameAsc(category, pageable);		
	}
	
	@GetMapping("/gamesNameDesc")
	public Page<Game> showGameByCategoryNameDesc(@RequestParam String category, Pageable pageable){
		return gameRepo.findGameFilterNameDesc(category, pageable);
	}
	
	@GetMapping("/gamesPriceAsc")
	public Page<Game> showGameByCategoryPriceAsc(@RequestParam String category, Pageable pageable){
		return gameRepo.findGameFilterPriceAsc(category, pageable);	
	}
	
	@GetMapping("/gamesPriceDesc")
	public Page<Game> showGameByCategoryPriceDesc(@RequestParam String category, Pageable pageable){
		return gameRepo.findGameFilterPriceDesc(category, pageable);		
	}
	
	@GetMapping("/filter")
	public Page<Game> showGameFilter2(@RequestParam int pageNo, @RequestParam String sort,  @RequestParam String orderBy){
		System.out.print(sort);
		if(orderBy.equals("asc")) {
			Pageable paging = PageRequest.of(pageNo, 3, Sort.by(sort).ascending());		
			return gameRepo.findAll(paging);
		}else {
			Pageable paging = PageRequest.of(pageNo, 3, Sort.by(sort).descending());
			return gameRepo.findAll(paging);
		}		
	}
	
	@GetMapping("/gameCategory/{categoryId}")
	public List<Game> showGameByCategory(@PathVariable int categoryId) {
		Category findCategory = categoryRepo.findById(categoryId).get();
		return findCategory.getGame();
	}
	
	@GetMapping("/{gameId}")
	public Game showGameById(@PathVariable int gameId) {
		Game findGame = gameRepo.findById(gameId).get();
		return findGame;
	}
	
	@PostMapping
	public Game addGame(@RequestBody Game game) {
		game.setStokUser(game.getStokAdmin());
		return gameRepo.save(game);
	}
	
	@PostMapping("/{gameId}/category/{categoryId}")
	public Game addCategoryToGame(@PathVariable int gameId, @PathVariable int categoryId) {
		Game findGame = gameRepo.findById(gameId).get();
		
		Category findCategory = categoryRepo.findById(categoryId).get();
		
		findGame.getCategory().add(findCategory);
		return gameRepo.save(findGame);
		
	}
	
	@PostMapping("/{gameId}/categoryName/{categoryName}")
	public Game addCategoryToGameByCategoryName(@PathVariable int gameId, @PathVariable String categoryName) {
		Game findGame = gameRepo.findById(gameId).get();
		
		Category findCategory = categoryRepo.findByCategoryName(categoryName);
//		findGame.getCategory().forEach(val ->{
//			if(val.getCategoryName() != categoryName) {
//				findGame.getCategory().add(findCategory);				
//			}
//		});
		
		findGame.getCategory().add(findCategory);	
		return gameRepo.save(findGame);
		
	}
	
	@PutMapping("/edit/{stokAwal}")
	public Game editGame(@PathVariable int stokAwal, @RequestBody Game game) {
		Game findGame = gameRepo.findById(game.getId()).get();
		List<User> findUser = userRepo.findAll();
	
		if(game.getStokUser() == 0) {
			findUser.forEach(val ->{
				val.getWishlist().forEach(value ->{
					if(value.getGame().getId() == game.getId()) {
						this.emailUtil.sendEmail(val.getEmail(), "Update Stock", "<h1>Yeay Game = " + game.getName() +" sudah ditambahkan Stoknya buruan beli sebelum kehabisan</h1>");
					}
				});
			});
		}
		
		if(stokAwal > game.getStokAdmin()) {
			game.setStokUser(game.getStokUser() - (stokAwal - game.getStokAdmin()));
		}else if(stokAwal < game.getStokAdmin()) {
			game.setStokUser(game.getStokUser() + (game.getStokAdmin() - stokAwal));
		}else {
			game.setStokUser(game.getStokAdmin());
		}
		return gameRepo.save(game);
	}
	
	@DeleteMapping("/{id}")
	public void deleteGame(@PathVariable int id) {
		gameRepo.deleteById(id);
	}
	
	@DeleteMapping("/{gameId}/delete/{categoryId}")
	public Game deleteCategoryGame(@PathVariable int gameId, @PathVariable int categoryId) {
		Game findGame = gameRepo.findById(gameId).get();
		
		Category findCategory = categoryRepo.findById(categoryId).get();
		findGame.getCategory().remove(findCategory);
		return gameRepo.save(findGame);
	}
	
	
	
	@GetMapping("/custom")
	public Page<Game> customQueryGet(@RequestParam String name, Pageable pageable){
		return gameRepo.findByName(name, pageable);
	}
	
	

}
