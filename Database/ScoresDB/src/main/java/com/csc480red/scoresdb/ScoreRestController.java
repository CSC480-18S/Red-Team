package com.csc480red.scoresdb;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ScoreRestController {
	@Autowired
	ScoreRepository scoreRepository;
	
	//HERE ARE THE ENDPOINTS
	
	@RequestMapping(method = RequestMethod.PUT, value = "scores")
	public ResponseEntity<?> putScore(@RequestBody Score input){
		Score score = new Score(input.getA(), input.getB(), input.getC(), input.getD(), input.getE(), input.getF(), input.getG(), input.getH(), input.getI(), input.getJ(), input.getK(), input.getL(), input.getM(), input.getN(), input.getO(), input.getP(), input.getQ(), input.getR(), input.getS(), input.getT(), input.getU(), input.getV(), input.getW(), input.getX(), input.getY(), input.getZ());
		scoreRepository.deleteAll();
		scoreRepository.save(score);
		
		return ResponseEntity.accepted().build();

	}
	
	@RequestMapping(method = RequestMethod.GET, value = "scores")
	public Score scores() {
		return scoreRepository.getOne(0L);
	}
	
	
}
