package com.security.JWT.ServiceImpl;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Year;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.google.common.base.Optional;
import com.security.JWT.Bean.Inverstment;
import com.security.JWT.Repository.InverstmentRepository;
import com.security.JWT.Service.InverstmentService;

@Service
public class InverstmentServiceImpl implements InverstmentService {

	private final InverstmentRepository repository;

	private final JasperReportService jasperReportService;

	public InverstmentServiceImpl(InverstmentRepository repository, JasperReportService jasperReportService) {
		this.repository = repository;
		this.jasperReportService = jasperReportService;
	}

	@Override
	public String createInvestment(Inverstment inverstment) {

		LocalDate now = LocalDate.now();
		int month = now.getMonthValue();
		int year = now.getYear();

		Optional<Inverstment> existingOpt = repository.findMonthlyInvestment(inverstment.getCreatedBy(), month, year);

		if (existingOpt.isPresent()) {

			Inverstment existing = existingOpt.get();

			existing.setInversmentAmt(inverstment.getInversmentAmt());
			existing.setUpdatedDt(new Date());
			existing.setUpdatedBy(inverstment.getCreatedBy());

			repository.save(existing);

			return "Your Existing investment Amount ₹" + existing.getInversmentAmt() + " Updated to ₹ "
					+ inverstment.getInversmentAmt() + " Successfully";

		} else {
			Inverstment investment = new Inverstment();

			investment.setInversmentAmt(inverstment.getInversmentAmt());
			investment.setCreatedBy(inverstment.getCreatedBy());
			investment.setCreatedDt(new Date());

			repository.save(investment);
			return "Your investment amount ₹" + inverstment.getInversmentAmt() + " Saved Successfully";
		}
	}

	@Override
	public List<Inverstment> getUserInvestments(String username) {
		return repository.findByCreatedBy(username);
	}

	@Override
	public Inverstment updateInvestment(Long id, Inverstment investment, String username) {

		Inverstment existing = repository.findById(id).orElseThrow(() -> new RuntimeException("Investment not found"));

		if (!existing.getCreatedBy().equals(username)) {
			throw new RuntimeException("Unauthorized access");
		}

		existing.setInversmentAmt(investment.getInversmentAmt());
		existing.setUpdatedDt(new Date());
		existing.setUpdatedBy(username);

		return repository.save(existing);
	}

	@Override
	public void deleteInvestment(Long id, String username) {

		Inverstment existing = repository.findById(id).orElseThrow(() -> new RuntimeException("Investment not found"));

		if (!existing.getCreatedBy().equals(username)) {
			throw new RuntimeException("Unauthorized access");
		}

		repository.delete(existing);
	}

	@Override
	public List<Inverstment> getCurrentMonthInvestmentsByUsername(String username) {
		LocalDate now = LocalDate.now();
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, now.getYear());
		cal.set(Calendar.MONTH, now.getMonthValue() - 1);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		Date startDate = cal.getTime();
		cal.add(Calendar.MONTH, 1);
		cal.add(Calendar.SECOND, -1);
		Date endDate = cal.getTime();
		return repository.findByMonthAndYear(startDate, endDate).stream()
				.filter(i -> username.equals(i.getCreatedBy()))
				.collect(Collectors.toList());
	}

	@Override
	public List<Inverstment> getInvestmentsByMonthYear(int month, int year) throws Exception {
		// compute date range and delegate to legacy repository method
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, year);
		cal.set(Calendar.MONTH, month - 1);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		Date startDate = cal.getTime();
		cal.add(Calendar.MONTH, 1);
		cal.add(Calendar.SECOND, -1);
		Date endDate = cal.getTime();

		System.out.printf("Querying investments from %s to %s\n", startDate, endDate);
		List<Inverstment> items = repository.findByMonthAndYear(startDate, endDate);
		System.out.printf("getInvestmentsByMonthYear(%d,%d) returned %d records\n", month, year, items.size());
		if (items.isEmpty()) {
			System.out.println("WARNING: No investments found for the date range!");
		} else {
			items.forEach(inv -> System.out.println("  - " + inv.getCreatedBy() + ": " + inv.getInversmentAmt()));
		}
		return items;
	}

	@Override
	public byte[] generateInvestmentReport(int month, int year) throws Exception {
		// compute date range identical to getInvestmentsByMonthYear
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, year);
		cal.set(Calendar.MONTH, month - 1);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		Date startDate = cal.getTime();
		cal.add(Calendar.MONTH, 1);
		cal.add(Calendar.SECOND, -1);
		Date endDate = cal.getTime();

		System.out.printf("Generating PDF report for %d/%d (from %s to %s)\n", month, year, startDate, endDate);
		List<Inverstment> data = repository.findByMonthAndYear(startDate, endDate);
		System.out.printf("generateInvestmentReport(%d,%d) data count = %d\n", month, year, data.size());
		if (data.isEmpty()) {
			System.out.println("WARNING: PDF will be blank - no investment records found for the date range!");
		} else {
			data.forEach(
					inv -> System.out.println("  PDF Data: " + inv.getCreatedBy() + " = ₹" + inv.getInversmentAmt()));
		}

		Map<String, Object> parameters = new HashMap<>();
		parameters.put("reportTitle", "Investment Report - " + month + "/" + year);

		return jasperReportService.generatePDF("investment_report", data, parameters);
	}

}