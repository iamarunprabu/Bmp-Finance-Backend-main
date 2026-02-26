package com.security.JWT.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.security.JWT.Bean.LoanRequest;
import com.security.JWT.Dto.MonthlyReportDTO;
import com.security.JWT.Enumeration.Status;

@Repository
public interface LoanRequestRepository extends JpaRepository<LoanRequest, Long> {

	List<LoanRequest> findByUserId(String userId);

	LoanRequest findByLoanNo(long loanNo);

	List<LoanRequest> findByStatus(Status status);

	List<LoanRequest> findByUserIdAndStatus(String userId, Status status);

	@Query("SELECT MAX(lr.loanNo) FROM LoanRequest lr")
	Optional<Long> findMaxLoanNo();

	@Query("SELECT SUM(l.amount) FROM LoanRequest l")
	Double findTotalLoanRequestAmount();

	@Query("SELECT COUNT(l) FROM LoanRequest l")
	Long countAll();

	@Query("""
			SELECT COUNT(l)
			FROM LoanRequest l
			WHERE l.status = com.security.JWT.Enumeration.Status.NOT_ELIGIBLE
			""")
	Long countRejected();

	@Query("""
			SELECT COUNT(l)
			FROM LoanRequest l
			WHERE l.status = com.security.JWT.Enumeration.Status.PENDING
			""")
	Long countPending();

	@Query("""
			SELECT COUNT(l)
			FROM LoanRequest l
			WHERE l.status = com.security.JWT.Enumeration.Status.APPROVED
			""")
	Long countApproved();

	@Query("SELECT SUM(l.amount) FROM LoanRequest l")
	Double sumLoanAmount();
	/*
	 * @Query("SELECT SUM(l.investmentAmount) FROM LoanRequest l") Double
	 * sumInvestmentAmount();
	 */

	@Query("""
		    SELECT 
		        EXTRACT(MONTH FROM l.createdAt) as month,
		        COALESCE(SUM(l.amount), 0) as totalAmount,
		        SUM(CASE 
		                WHEN l.status = com.security.JWT.Enumeration.Status.APPROVED 
		                THEN 1 ELSE 0 
		            END) as approvedCount,
		        SUM(CASE 
		                WHEN l.status = com.security.JWT.Enumeration.Status.PENDING 
		                THEN 1 ELSE 0 
		            END) as pendingCount,
		        SUM(CASE 
		                WHEN l.status = com.security.JWT.Enumeration.Status.NOT_ELIGIBLE 
		                THEN 1 ELSE 0 
		            END) as rejectedCount
		    FROM LoanRequest l
		    WHERE EXTRACT(YEAR FROM l.createdAt) = EXTRACT(YEAR FROM CURRENT_DATE)
		    GROUP BY EXTRACT(MONTH FROM l.createdAt)
		    ORDER BY EXTRACT(MONTH FROM l.createdAt)
		""")
		List<MonthlyReportDTO> getMonthlyReport();
		
		   @Query("""
		           SELECT COUNT(l)
		           FROM LoanRequest l
		           WHERE l.soldBy = :username
		           """)
		    Long countByUsername(@Param("username") String username);


		    @Query("""
		           SELECT COUNT(l)
		           FROM LoanRequest l
		           WHERE l.soldBy = :username
		             AND l.status = com.security.JWT.Enumeration.Status.APPROVED
		           """)
		    Long countApprovedByUsername(@Param("username") String username);


		    @Query("""
		           SELECT COUNT(l)
		           FROM LoanRequest l
		           WHERE l.soldBy = :username
		             AND l.status = com.security.JWT.Enumeration.Status.PENDING
		           """)
		    Long countPendingByUsername(@Param("username") String username);


		    @Query("""
		           SELECT COUNT(l)
		           FROM LoanRequest l
		           WHERE l.soldBy = :username
		             AND l.status = com.security.JWT.Enumeration.Status.NOT_ELIGIBLE
		           """)
		    Long countRejectedByUsername(@Param("username") String username);


		    @Query("""
		           SELECT COALESCE(SUM(l.amount),0)
		           FROM LoanRequest l
		           WHERE l.soldBy = :username
		           """)
		    Double sumLoanAmountByUsername(@Param("username") String username);


		    @Query("""
		        SELECT 
		            EXTRACT(MONTH FROM l.createdAt) as month,
		            COALESCE(SUM(l.amount), 0) as totalAmount,
		            SUM(CASE 
		                    WHEN l.status = com.security.JWT.Enumeration.Status.APPROVED 
		                    THEN 1 ELSE 0 
		                END) as approvedCount,
		            SUM(CASE 
		                    WHEN l.status = com.security.JWT.Enumeration.Status.PENDING 
		                    THEN 1 ELSE 0 
		                END) as pendingCount,
		            SUM(CASE 
		                    WHEN l.status = com.security.JWT.Enumeration.Status.NOT_ELIGIBLE 
		                    THEN 1 ELSE 0 
		                END) as rejectedCount
		        FROM LoanRequest l
		        WHERE l.soldBy = :username
		          AND EXTRACT(YEAR FROM l.createdAt) = EXTRACT(YEAR FROM CURRENT_DATE)
		        GROUP BY EXTRACT(MONTH FROM l.createdAt)
		        ORDER BY EXTRACT(MONTH FROM l.createdAt)
		    """)
		    List<MonthlyReportDTO> getMonthlyReportByUsername(@Param("username") String username);
			
			

}
