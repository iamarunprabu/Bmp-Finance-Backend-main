package com.security.JWT.ServiceImpl;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.google.common.base.Optional;
import com.security.JWT.Bean.Inverstment;
import com.security.JWT.Repository.InverstmentRepository;
import com.security.JWT.Service.InverstmentService;

@Service
public class InverstmentServiceImpl implements InverstmentService {

	private final InverstmentRepository repository;

	public InverstmentServiceImpl(InverstmentRepository repository) {
		this.repository = repository;
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
			 return "Your investment amount ₹" 
             + inverstment.getInversmentAmt() + " Saved Successfully";
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
}