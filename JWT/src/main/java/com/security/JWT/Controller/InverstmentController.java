package com.security.JWT.Controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
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

	@GetMapping("/userInverstmentAmt")
	public ResponseEntity<List<Inverstment>> getCurrentMonthInvestmentsByUsername(@RequestParam("username") String username) {
		return ResponseEntity.ok(service.getCurrentMonthInvestmentsByUsername(username));
	}

	@GetMapping("/all")
	public ResponseEntity<List<Inverstment>> getAllInvestments(@RequestParam("month") int month, 
	        @RequestParam("year") int year) throws Exception  {
			List<Inverstment> investments = service.getInvestmentsByMonthYear(month, year);
			return ResponseEntity.ok(investments);
		
	}
	
	@GetMapping("/report/pdf") 
	public ResponseEntity<byte[]> downloadInvestmentPDF( 
			 @RequestParam("month") int month, 
		        @RequestParam("year") int year) throws Exception {
		 byte[] pdfBytes = service.generateInvestmentReport(month, year); 
		HttpHeaders headers = new HttpHeaders(); 
		headers.setContentType(MediaType.APPLICATION_PDF); 
		headers.setContentDisposition(ContentDisposition.attachment() .filename("investment_report_" + month + "_" + year + ".pdf").build());
		return ResponseEntity.ok() .headers(headers) .body(pdfBytes); 
		
		} 
}