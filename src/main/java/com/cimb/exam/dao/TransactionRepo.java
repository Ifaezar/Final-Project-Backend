package com.cimb.exam.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cimb.exam.entity.Transaction;

public interface TransactionRepo extends JpaRepository<Transaction, Integer>{

}
