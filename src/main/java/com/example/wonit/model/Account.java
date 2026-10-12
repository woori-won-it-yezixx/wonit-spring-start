package com.example.wonit.model;

import jakarta.persistence.*;
import lombok.Data;


@Data
@Entity
public class Account {
    @Id // pk 지정
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer accountId;
    private Integer customerId;
    private String accountNo;
    private String accountType;
    private String status;
    private long balance;

    // Setter 대신 우리가 만든 별도의 status가
    // 지급정지는 아닌지, 잔액이 불충분하진 않은지
    // 확인하는 예외를 만들어서 처리
    
}
