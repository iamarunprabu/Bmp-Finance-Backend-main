package com.security.JWT.Repository;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.google.common.base.Optional;
import com.security.JWT.Bean.Inverstment;

@Repository
public interface InverstmentRepository extends JpaRepository<Inverstment, Long> {

	List<Inverstment> findByCreatedBy(String username);

	@Query("""
			SELECT i
			FROM Inverstment i
			WHERE i.createdBy = :createdBy
			  AND EXTRACT(MONTH FROM i.createdDt) = :month
			  AND EXTRACT(YEAR FROM i.createdDt) = :year
			""")
	Optional<Inverstment> findMonthlyInvestment(@Param("createdBy") String createdBy, @Param("month") int month,
			@Param("year") int year);

	@Query("""
			SELECT i
			FROM Inverstment i
			WHERE i.createdBy = :createdBy
			  AND EXTRACT(MONTH FROM i.createdDt) = :month
			  AND EXTRACT(YEAR FROM i.createdDt) = :year
			""")
	List<Inverstment> findMonthlyInvestmentList(@Param("createdBy") String createdBy, @Param("month") int month,
			@Param("year") int year);

	 @Query("""
		        SELECT i FROM Inverstment i
		        WHERE EXTRACT(MONTH FROM i.createdDt) = :month
		          AND EXTRACT(YEAR FROM i.createdDt)  = :year
		        """)
		    List<Inverstment> findByMonthAndYear(@Param("month") int month,
		                                        @Param("year") int year);

	 @Query("SELECT i FROM Inverstment i WHERE i.createdDt BETWEEN :startDate AND :endDate")
	    List<Inverstment> findByMonthAndYear(@Param("startDate") java.util.Date startDate,
	                                        @Param("endDate") java.util.Date endDate);
}
