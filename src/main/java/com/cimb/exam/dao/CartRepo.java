package com.cimb.exam.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cimb.exam.entity.Cart;

public interface CartRepo extends JpaRepository<Cart, Integer>{

}
