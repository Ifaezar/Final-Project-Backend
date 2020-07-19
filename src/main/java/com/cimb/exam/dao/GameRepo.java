package com.cimb.exam.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import com.cimb.exam.entity.Game;

public interface GameRepo extends JpaRepository<Game, Integer>,PagingAndSortingRepository<Game, Integer> {
	@Query(value = "SELECT * FROM game WHERE name like %:name%", nativeQuery = true)
	public Page<Game> findByName(@Param("name") String name, Pageable pageable);
	
	@Query(value = "select * from category_game cg join game g on g.id = cg.game_id join category c on c.id = cg.category_id where c.category_name= ?1 order by name asc", nativeQuery = true)
	public Page<Game> findGameFilterNameAsc(String category, Pageable pageable);
	
	@Query(value = "select * from category_game cg join game g on g.id = cg.game_id join category c on c.id = cg.category_id where c.category_name= ?1 order by name desc", nativeQuery = true)
	public Page<Game> findGameFilterNameDesc(String category, Pageable pageable);
	
	@Query(value = "select * from category_game cg join game g on g.id = cg.game_id join category c on c.id = cg.category_id where c.category_name= ?1 order by price asc", nativeQuery = true)
	public Page<Game> findGameFilterPriceAsc(String category, Pageable pageable);
	
	@Query(value = "select * from category_game cg join game g on g.id = cg.game_id join category c on c.id = cg.category_id where c.category_name= ?1 order by price desc", nativeQuery = true)
	public Page<Game> findGameFilterPriceDesc(String category, Pageable pageable);
	
	@Query(value = "SELECT * FROM game order by sold desc", nativeQuery = true)
	public Iterable<Game> sortBySold();
}
