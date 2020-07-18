package com.cimb.exam.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cimb.exam.entity.Wishlist;

public interface WishlistRepo extends JpaRepository<Wishlist, Integer> {

}
