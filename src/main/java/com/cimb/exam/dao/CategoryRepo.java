package com.cimb.exam.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cimb.exam.entity.Category;

public interface CategoryRepo extends JpaRepository<Category, Integer> {
	public Category findByCategoryName(String categoryName);
}
