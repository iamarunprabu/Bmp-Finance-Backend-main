package com.security.JWT.Service;

import java.util.List;

import com.security.JWT.Bean.Inverstment;

public interface InverstmentService {

    String createInvestment(Inverstment investment);

    List<Inverstment> getUserInvestments(String username);

    Inverstment updateInvestment(Long id, Inverstment investment, String username);

    void deleteInvestment(Long id, String username);
}
