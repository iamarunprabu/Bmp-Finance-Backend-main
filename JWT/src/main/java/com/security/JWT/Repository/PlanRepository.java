package com.security.JWT.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.security.JWT.Bean.Plan;

@Repository
public interface PlanRepository extends JpaRepository<Plan, Integer> {

}
