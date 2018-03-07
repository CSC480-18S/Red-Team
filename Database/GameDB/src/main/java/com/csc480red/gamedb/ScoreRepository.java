package com.csc480red.scoresdb;

import org.springframework.data.jpa.repository.JpaRepository;

//HERE IS THE ENDPOINT
public interface ScoreRepository extends JpaRepository<Score, Long> {

}
