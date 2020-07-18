package com.cimb.exam.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cimb.exam.entity.TransactionDetail;

public interface TransactionDetailRepo extends JpaRepository<TransactionDetail, Integer>{

}
