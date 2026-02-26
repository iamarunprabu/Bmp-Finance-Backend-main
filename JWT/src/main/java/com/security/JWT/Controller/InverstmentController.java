package com.security.JWT.Controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import com.security.JWT.Bean.Inverstment;
import com.security.JWT.Service.InverstmentService;

@RestController
@RequestMapping("api/investment")
public class InverstmentController {

	private final InverstmentService service;

	public InverstmentController(InverstmentService service) {
		this.service = service;
	}

	@PostMapping("/save")
    public String createInvestment(
            @RequestBody Inverstment investment) {

		Map<String, String> response = new HashMap<>();

       String message = service.createInvestment(investment);
	    response.put("message", message);
         
         return message;
    }

	@GetMapping
	public ResponseEntity<List<Inverstment>> getUserInvestments(@AuthenticationPrincipal UserDetails userDetails) {

		return ResponseEntity.ok(service.getUserInvestments(userDetails.getUsername()));
	}

	@PutMapping("/{id}")
	public ResponseEntity<?> updateInvestment(@PathVariable Long id, @RequestBody Inverstment investment,
			@AuthenticationPrincipal UserDetails userDetails) {

		return ResponseEntity.ok(service.updateInvestment(id, investment, userDetails.getUsername()));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteInvestment(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails) {

		service.deleteInvestment(id, userDetails.getUsername());
		return ResponseEntity.ok("Deleted successfully");
	}
}