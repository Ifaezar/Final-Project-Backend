package com.cimb.exam.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cimb.exam.dao.CategoryRepo;
import com.cimb.exam.entity.Category;

@RestController
@RequestMapping("/category")
@CrossOrigin
public class CategoryController {

	@Autowired
	private CategoryRepo categoryRepo;
	
	@GetMapping
	public Iterable<Category> showAllCategory(){ 
		return categoryRepo.findAll();
	}
	
	@GetMapping("/{categoryName}")
	public Category showByCategoryname(@PathVariable String categoryName ){ 
		Category findCategory = categoryRepo.findByCategoryName(categoryName);
		return findCategory;
	}
	
	
	@PostMapping
	public Category addCategory(@RequestBody Category category) {
		return categoryRepo.save(category);
	}
	
	
}
