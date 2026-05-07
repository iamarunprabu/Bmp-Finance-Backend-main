package com.security.JWT.ServiceImpl;

import com.google.common.base.Optional;
import com.security.JWT.Bean.LoanRequest;
import com.security.JWT.Bean.User;
import com.security.JWT.Dto.MonthlyReportDTO;
import com.security.JWT.Enumeration.Status;
import com.security.JWT.Repository.LoanRequestRepository;
import com.security.JWT.Repository.UserRepository;
import com.security.JWT.Service.LoanRequestService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.OutputStream;
import java.time.Month;
import java.time.Year;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class LoanRequestServiceImpl implements LoanRequestService {

    private final LoanRequestRepository loanRequestRepository;
    private final UserRepository userRepository;

    @Autowired
    public LoanRequestServiceImpl(LoanRequestRepository loanRequestRepository,
                                  UserRepository userRepository) {
        this.loanRequestRepository = loanRequestRepository;
        this.userRepository = userRepository;
    }

    @Override
    public LoanRequest createLoanRequest(LoanRequest loanRequest, String username) {
        User user = userRepository.findUserByUsername(username);
        if (user == null)
            throw new RuntimeException("User not found: " + username);

        loanRequest.setUserId(user.getUserId());
        loanRequest.setCreatedAt(new Date());
        loanRequest.setStatus(Status.PENDING);

        return loanRequestRepository.save(loanRequest);
    }

    @Override
    public List<LoanRequest> getUserLoanRequestsByUsername(String username) {
        User user = userRepository.findUserByUsername(username);
        return loanRequestRepository.findByUserId(user.getUserId());
    }

    @Override
	public List<LoanRequest> getAllLoanRequests(String username) {
		if (username == null || username.isEmpty()) {
			throw new RuntimeException("Username is required");
		}

		return loanRequestRepository.findByUserId(username);
	}
    
    @Override
	public List<LoanRequest> getAllLoanRequests() {

		return loanRequestRepository.findByStatus(Status.APPROVED);
	}

    @Override
    public LoanRequest getLoanRequestById(Long id) {
        return loanRequestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Loan request not found ID: " + id));
    }

    @Override
    public LoanRequest updateLoanRequestStatus(Long id, String status, String remarks) {
        LoanRequest loanRequest = getLoanRequestById(id);
        loanRequest.setStatus(Status.valueOf(status.toUpperCase()));
        loanRequest.setUpdatedAt(new Date());
        loanRequest.setRemark(remarks);

        return loanRequestRepository.save(loanRequest);
    }

    @Override
    public void deleteLoanRequest(Long id) {
        loanRequestRepository.deleteById(id);
    }

    @Override
    public List<LoanRequest> getUserLoanRequests(String userId) {
        return loanRequestRepository.findByUserId(userId);
    }

    @Override
    public LoanRequest updateLoanRequest(Long id, LoanRequest updatedLoan) {
        LoanRequest existing = getLoanRequestById(id);

        existing.setCustomerName(updatedLoan.getCustomerName());
        existing.setAmount(updatedLoan.getAmount());
        existing.setPlan(updatedLoan.getPlan());
        existing.setRequestMonth(updatedLoan.getRequestMonth());
        existing.setSoldBy(updatedLoan.getSoldBy());
        existing.setUpdatedAt(new Date());

        return loanRequestRepository.save(existing);
    }

    @Override
    public String getNextLoanNumber() {
        return loanRequestRepository.findMaxLoanNo()
                .map(maxVal -> String.valueOf(maxVal + 1))
                .orElse("1");
    }

    @Override
    public List<LoanRequest> getPendingLoanRequests() {
        return loanRequestRepository.findByStatus(Status.PENDING);
    }

    @Override
    public List<LoanRequest> filterLoanRequests(String status, String username) {

        String userId = null;

        if (username != null && !username.isBlank()) {
            User user = userRepository.findUserByUsername(username);
            if (user == null) return List.of();
            userId = user.getUserId();
        }

        if (status != null && !status.isBlank() && userId != null) {
            return loanRequestRepository.findByUserIdAndStatus(
                    userId, Status.valueOf(status.toUpperCase()));
        }

        if (status != null && !status.isBlank()) {
            return loanRequestRepository.findByStatus(
                    Status.valueOf(status.toUpperCase()));
        }

        if (userId != null) {
            return loanRequestRepository.findByUserId(userId);
        }

        return loanRequestRepository.findAll();
    }

    @Override
    public void exportLoansToExcel(OutputStream os) throws IOException {

        try (Workbook workbook = new XSSFWorkbook()) {

            Sheet sheet = workbook.createSheet("Loans");

            String[] columns = {
                    "loanNo", "customerName", "amount", "plan",
                    "requestMonth", "soldBy", "status", "remark"
            };

            Row header = sheet.createRow(0);

            for (int i = 0; i < columns.length; i++) {
                header.createCell(i).setCellValue(columns[i]);
            }

            List<LoanRequest> loans = loanRequestRepository.findAll();

            for (int i = 0; i < loans.size(); i++) {
                Row row = sheet.createRow(i + 1);
                LoanRequest loan = loans.get(i);

                row.createCell(0).setCellValue(loan.getLoanNo());
                row.createCell(1).setCellValue(loan.getCustomerName());
                row.createCell(2).setCellValue(loan.getAmount());
                row.createCell(3).setCellValue(loan.getPlan());
                row.createCell(4).setCellValue(loan.getRequestMonth());
                row.createCell(5).setCellValue(loan.getSoldBy());
                row.createCell(6).setCellValue(loan.getStatus().name());
                row.createCell(7).setCellValue(loan.getRemark());
            }

            workbook.write(os);
        }
    }

    @Override
    public int importLoansFromExcel(MultipartFile file) throws IOException {

        int uploadCount = 0;
        //User user = userRepository.findUserByUsername(username);
        try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {

            Sheet sheet = workbook.getSheetAt(0);

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {

                Row row = sheet.getRow(i);
                if (row == null || row.getCell(0) == null) continue;

                try {
                    LoanRequest loan = new LoanRequest();

                    loan.setLoanNo((long) row.getCell(0).getNumericCellValue());
                    loan.setCustomerName(row.getCell(1).getStringCellValue());
                    loan.setAmount(row.getCell(2).getNumericCellValue());
                    loan.setPlan(row.getCell(3).getStringCellValue());
                    loan.setRequestMonth(row.getCell(4).getStringCellValue());
                    loan.setSoldBy(row.getCell(5).getStringCellValue());
                    loan.setStatus(Status.valueOf(row.getCell(6).getStringCellValue()));
                    loan.setRemark(row.getCell(7).getStringCellValue());
                    //loan.setUserId(user.getUserId());
                    loan.setCreatedAt(new Date());
                    loanRequestRepository.save(loan);
                    uploadCount++;

                } catch (Exception e) {
                    System.err.println("Error importing row " + i + ": " + e.getMessage());
                }
            }
        }

        return uploadCount;
    }

    @Override
    public Map<String, Object> getDashboardStats(String username) {
        Map<String, Object> stats = new HashMap<>();

        if (username != null && !username.isEmpty()) {
            // Get data for specific user
            stats.put("totalRequests",
                    loanRequestRepository.countByUsername(username) != null
                            ? loanRequestRepository.countByUsername(username)
                            : 0);

            stats.put("approvedCount",
                    loanRequestRepository.countApprovedByUsername(username) != null
                            ? loanRequestRepository.countApprovedByUsername(username)
                            : 0);

            stats.put("rejectedCount",
                    loanRequestRepository.countRejectedByUsername(username) != null
                            ? loanRequestRepository.countRejectedByUsername(username)
                            : 0);

            stats.put("totalLoanRequestAmount",
                    loanRequestRepository.sumLoanAmountByUsername(username) != null
                            ? loanRequestRepository.sumLoanAmountByUsername(username)
                            : 0.0);

            stats.put("totalPendingCount",
                    loanRequestRepository.countPendingByUsername(username) != null
                            ? loanRequestRepository.countPendingByUsername(username)
                            : 0);
        } else {
            // Get all data (existing code)
            stats.put("totalRequests",
                    loanRequestRepository.countAll() != null
                            ? loanRequestRepository.countAll()
                            : 0);

            stats.put("approvedCount",
                    loanRequestRepository.countApproved() != null
                            ? loanRequestRepository.countApproved()
                            : 0);

            stats.put("rejectedCount",
                    loanRequestRepository.countRejected() != null
                            ? loanRequestRepository.countRejected()
                            : 0);

            stats.put("totalLoanRequestAmount",
                    loanRequestRepository.sumLoanAmount() != null
                            ? loanRequestRepository.sumLoanAmount()
                            : 0.0);

            stats.put("totalPendingCount",
                    loanRequestRepository.countPending() != null
                            ? loanRequestRepository.countPending()
                            : 0);
        }

        return stats;
    }

    @Override
    public Map<String, Object> getMonthlyReport(String username) {

        List<MonthlyReportDTO> results =
                (username != null && !username.isBlank())
                        ? loanRequestRepository.getMonthlyReportByUsername(username)
                        : loanRequestRepository.getMonthlyReport();

        List<Map<String, Object>> monthlyData = new ArrayList<>();

        for (MonthlyReportDTO row : results) {

            Map<String, Object> data = new HashMap<>();

            Integer monthNumber = row.getMonth() != null ? row.getMonth() : 1;

            data.put("month", Month.of(monthNumber).name());
            data.put("approvedCount", row.getApprovedCount() != null ? row.getApprovedCount() : 0L);
            data.put("pendingCount", row.getPendingCount() != null ? row.getPendingCount() : 0L);
            data.put("rejectedCount", row.getRejectedCount() != null ? row.getRejectedCount() : 0L);
            data.put("totalLoanAmount", row.getTotalAmount() != null ? row.getTotalAmount() : 0.0);

            monthlyData.add(data);
        }

        Map<String, Object> response = new HashMap<>();
        response.put("year", Year.now().getValue());
        response.put("data", monthlyData);

        return response;
    }



}
