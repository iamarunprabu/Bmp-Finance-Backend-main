package com.security.JWT.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.security.JWT.Bean.Plan;
import com.security.JWT.Service.PlanService;

@RestController
@RequestMapping(path = {"/api/plans"})
@CrossOrigin(origins = "*")
public class PlanController {

	
	@Autowired
	private PlanService planService;
	
	@GetMapping("/all")
	public ResponseEntity<List<Plan>> getActivePlan(){
		
		List<Plan> plans = planService.getAllPlans();
		return ResponseEntity.ok(plans);
	}
}
