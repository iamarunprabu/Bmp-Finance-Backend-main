package com.security.JWT.Controller;

import com.security.JWT.Bean.LoanRequest;
import static com.security.JWT.Constant.SecurityConstant.*;
import com.security.JWT.Domain.HttpResponse;
import com.security.JWT.Service.LoanRequestService;

import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping(path = "/api/loan")
@CrossOrigin(origins = "*")
public class LoanRequestController {

	private final LoanRequestService loanRequestService;

	@Autowired
	public LoanRequestController(LoanRequestService loanRequestService) {
		this.loanRequestService = loanRequestService;
	}

	@PostMapping("/request")
	@PreAuthorize("hasAuthority('user:read')")
	public ResponseEntity<Map<String, Object>> createLoanRequest(@RequestBody LoanRequest loanRequest,
			Authentication authentication) {
		LoanRequest createdRequest = loanRequestService.createLoanRequest(loanRequest, authentication.getName());
		Map<String, Object> response = new HashMap<>();
		response.put("success", true);
		response.put("message", LOAN_SUCCESS);
		response.put("data", createdRequest);
		return new ResponseEntity<>(response, CREATED);
	}

	@GetMapping("/requests")
	@PreAuthorize("hasAuthority('user:read')")
	public ResponseEntity<List<LoanRequest>> getUserLoanRequests(Authentication authentication) {
		List<LoanRequest> loanRequests = loanRequestService.getUserLoanRequestsByUsername(authentication.getName());
		return new ResponseEntity<>(loanRequests, OK);
	}

	@GetMapping("/requests/all")
	@PreAuthorize("hasAuthority('user:create')")
	public ResponseEntity<List<LoanRequest>> getAllLoanRequests(@RequestParam(name = "username",defaultValue = "") String username) {
		return new ResponseEntity<>(loanRequestService.getAllLoanRequests(username), OK);
	}
	
	@GetMapping("/requests/allLoan")
	@PreAuthorize("hasAuthority('user:create')")
	public ResponseEntity<List<LoanRequest>> getAllLoanRequests() {
		return new ResponseEntity<>(loanRequestService.getAllLoanRequests(), OK);
	}

	@GetMapping("/request/{id}")
	@PreAuthorize("hasAuthority('user:read')")
	public ResponseEntity<LoanRequest> getLoanRequestById(@PathVariable Long id) {
		return new ResponseEntity<>(loanRequestService.getLoanRequestById(id), OK);
	}

	@PutMapping("/request/{id}/status")
	@PreAuthorize("hasAuthority('user:create')")
	public ResponseEntity<Map<String, Object>> updateLoanRequestStatus(@PathVariable(name = "id") Long id,
			@RequestBody Map<String, String> body) {
		String status = body.get("status");
		String remarks = body.getOrDefault("remark", "");

		LoanRequest updatedRequest = loanRequestService.updateLoanRequestStatus(id, status, remarks);

		Map<String, Object> response = new HashMap<>();
		response.put("success", true);
		response.put("message", "Status updated to " + status);
		response.put("data", updatedRequest);

		return new ResponseEntity<>(response, OK);
	}

	@GetMapping("/requests/filter")
	@PreAuthorize("hasAnyAuthority('user:create','user:read')")
	public ResponseEntity<List<LoanRequest>> filterLoanRequests(
			@RequestParam(name = "status", required = false) String status,
			@RequestParam(name = "username", required = false,defaultValue = "") String username) {
		List<LoanRequest> results = loanRequestService.filterLoanRequests(status, username);
		return new ResponseEntity<>(results, OK);
	}

	// In LoanRequestController.java
	@GetMapping("/dashboard")
	@PreAuthorize("hasAnyAuthority('user:create','user:read')")
	public ResponseEntity<Map<String, Object>> getDashboardStats(
			@RequestParam(name = "username", required = false, defaultValue = "") String username) {
	    return ResponseEntity.ok(loanRequestService.getDashboardStats(username));
	}

	@GetMapping("/monthly-report")
	@PreAuthorize("hasAnyAuthority('user:create','user:read')")
	public ResponseEntity<Map<String, Object>> getMonthlyReport(
			@RequestParam(name = "username",  required = false, defaultValue = "") String username) {
	    return ResponseEntity.ok(loanRequestService.getMonthlyReport(username));
	}


	@DeleteMapping("/request/{id}")
	@PreAuthorize("hasAuthority('user:delete')")
	public String deleteLoanRequest(@PathVariable Long id) {
		loanRequestService.deleteLoanRequest(id);
		return "Loan request deleted successfully";
	}

	@GetMapping("/next-loan-number")
	public ResponseEntity<String> getNextLoanNumber() {
		String nextLoan = loanRequestService.getNextLoanNumber();
		return new ResponseEntity<>(nextLoan, OK);
	}

	@GetMapping("/request/pending")
	@PreAuthorize("hasAuthority('user:create')")
	public ResponseEntity<List<LoanRequest>> getPendingLoanRequest() {
		return new ResponseEntity<>(loanRequestService.getPendingLoanRequests(), OK);
	}

	@PostMapping("/import-excel")
	@PreAuthorize("hasAuthority('user:create')")
	public ResponseEntity<Map<String, Object>> importLoanRequestsFromExcel(@RequestParam("file") MultipartFile file) {
		Map<String, Object> response = new HashMap<>();
		try {
			int importedCount = loanRequestService.importLoansFromExcel(file);
			response.put("success", true);
			response.put("message", importedCount + " loan requests imported successfully");
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.put("success", false);
			response.put("message", "Import failed: " + e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}
	}

	// Excel Export Endpoint
	@GetMapping("/export-excel")
	@PreAuthorize("hasAuthority('user:create')")
	public void exportLoanRequestsToExcel(HttpServletResponse response) throws IOException {
		response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
		response.setHeader("Content-Disposition", "attachment; filename=loan_requests.xlsx");

		loanRequestService.exportLoansToExcel(response.getOutputStream());

		// Ensure the stream is flushed and closed properly
		response.flushBuffer();
	}

	/*
	 * private ResponseEntity<HttpResponse> response(HttpStatus status, String
	 * message, Object data) { return new ResponseEntity<>(new
	 * HttpResponse(status.value(), status, message, data), status); }
	 */
}
