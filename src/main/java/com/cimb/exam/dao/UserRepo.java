package com.cimb.exam.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cimb.exam.entity.User;

public interface UserRepo extends JpaRepository<User, Integer> {
	public Optional<User> findByUsername(String username);
	public Optional<User> findByEmail(String email);
}
