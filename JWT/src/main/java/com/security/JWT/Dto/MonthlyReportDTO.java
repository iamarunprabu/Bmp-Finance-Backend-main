package com.security.JWT.Dto;


public interface MonthlyReportDTO {

    Integer getMonth();
    Double getTotalAmount();
    Long getApprovedCount();
    Long getPendingCount();
    Long getRejectedCount();
}
