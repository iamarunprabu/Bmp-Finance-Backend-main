package com.security.JWT.Bean;

import java.text.SimpleDateFormat;
import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.security.JWT.Enumeration.Status;

@Data
@Entity
@Table(name = "LOAN_REQUESTS")
public class LoanRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "loan_no")
    private long loanNo;
    
    @Column(name = "customer_name")
    private String customerName;
    
    private double amount;
    
    private String plan;
    
    @Column(name = "request_month")
    private String requestMonth;
    
    @Column(name = "sold_by")
    private String soldBy;
    
    @Column(name = "user_id")
    private String userId;
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    @Column(name = "created_at")
    private Date createdAt;
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    @Column(name = "updated_at")
    private Date updatedAt;
    
    // This MUST be ORDINAL to match the NUMBER type in DB
    @Enumerated(EnumType.ORDINAL)
    private Status status;
    
    private String remark;
    
    @Override
    public String toString() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String createdAtStr = (createdAt != null) ? sdf.format(createdAt) : null;
        String updatedAtStr = (updatedAt != null) ? sdf.format(updatedAt) : null;

        return "LoanRequest{" +
                "id=" + id +
                ", loanNo=" + loanNo +
                ", customerName='" + customerName + '\'' +
                ", amount=" + amount +
                ", plan='" + plan + '\'' +
                ", requestMonth='" + requestMonth + '\'' +
                ", soldBy='" + soldBy + '\'' +
                ", userId=" + userId +
                ", createdAt='" + createdAtStr + '\'' +
                ", updatedAt='" + updatedAtStr + '\'' +
                ", status='" + status + '\'' +
                ", remark='" + remark + '\'' +
                '}';
    }
}