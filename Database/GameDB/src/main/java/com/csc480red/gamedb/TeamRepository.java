package com.csc480red.gamedb;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

public interface TeamRepository extends JpaRepository<Team, Long> {
	
	List<Team> findByName(@Param("name") String name);
	
}
