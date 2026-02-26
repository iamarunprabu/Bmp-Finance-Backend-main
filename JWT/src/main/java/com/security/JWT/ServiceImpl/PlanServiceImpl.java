package com.security.JWT.ServiceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.security.JWT.Bean.Plan;
import com.security.JWT.Repository.PlanRepository;
import com.security.JWT.Service.PlanService;

@Service
public class PlanServiceImpl implements PlanService{

	@Autowired
	private PlanRepository planRepository;
	
	@Override
	public List<Plan> getAllPlans() {
		
		return planRepository.findAll();
	}

}
