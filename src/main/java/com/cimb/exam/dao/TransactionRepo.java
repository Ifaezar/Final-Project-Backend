package com.cimb.exam.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.cimb.exam.entity.Transaction;

public interface TransactionRepo extends JpaRepository<Transaction, Integer>{
	@Query(value = "SELECT * FROM transaction WHERE status like %:name%", nativeQuery = true)
	public Iterable<Transaction> findByName(@Param("name") String name);
}
