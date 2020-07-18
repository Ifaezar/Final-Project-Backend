package com.cimb.exam.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cimb.exam.entity.GameLibrary;

public interface GameLibraryRepo extends JpaRepository<GameLibrary, Integer>{

}
