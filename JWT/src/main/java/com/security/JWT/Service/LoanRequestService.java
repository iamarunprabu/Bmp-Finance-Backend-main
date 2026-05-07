package com.security.JWT.Service;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.security.JWT.Bean.LoanRequest;

public interface LoanRequestService {

    LoanRequest createLoanRequest(LoanRequest loanRequest, String username);

    List<LoanRequest> getUserLoanRequestsByUsername(String username);

    List<LoanRequest> getAllLoanRequests(String username);
    
    List<LoanRequest> getAllLoanRequests();

    LoanRequest getLoanRequestById(Long id);

    LoanRequest updateLoanRequestStatus(Long id, String status, String remarks);

    void deleteLoanRequest(Long id);

    List<LoanRequest> getUserLoanRequests(String userId);

    LoanRequest updateLoanRequest(Long id, LoanRequest loanRequest);

    String getNextLoanNumber();

    List<LoanRequest> getPendingLoanRequests();

    List<LoanRequest> filterLoanRequests(String status, String username);

    void exportLoansToExcel(OutputStream os) throws IOException;

    int importLoansFromExcel(MultipartFile file) throws IOException;

    Map<String, Object>  getDashboardStats(String username);

	Map<String, Object> getMonthlyReport(String username);
}
