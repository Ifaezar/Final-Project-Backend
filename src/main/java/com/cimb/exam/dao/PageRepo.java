package com.cimb.exam.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.cimb.exam.entity.User;

public interface PageRepo extends PagingAndSortingRepository<User, Integer>{

	@Query(value = "SELECT * FROM user ORDER BY username asc", nativeQuery = true)
	Page<User> findAllUsers(Pageable pageable);
}
