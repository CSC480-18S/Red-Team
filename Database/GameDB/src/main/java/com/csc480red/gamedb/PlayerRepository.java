package com.csc480red.gamedb;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

public interface PlayerRepository extends JpaRepository<Player, Long>{
	
	List<Player> findByUsername(@Param("username") String username);
	
}
