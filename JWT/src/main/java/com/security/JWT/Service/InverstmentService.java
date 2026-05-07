package com.security.JWT.Service;

import java.util.List;
import java.util.Map;

import com.security.JWT.Bean.Inverstment;

public interface InverstmentService {

    String createInvestment(Inverstment investment);

    List<Inverstment> getUserInvestments(String username);

    Inverstment updateInvestment(Long id, Inverstment investment, String username);

    void deleteInvestment(Long id, String username);

	List<Inverstment> getInvestmentsByMonthYear(int month, int year) throws Exception;

	byte[] generateInvestmentReport(int month, int year) throws Exception;

	List<Inverstment> getCurrentMonthInvestmentsByUsername(String username);

}
