package com.example.wonit.model;

import jakarta.persistence.*;
import lombok.*;


// 외부에서 접근할 수 없도록 @Setter 닫아둠
// 보안이 중요한 데이터 처리는 Setter 직접 만들어서 사용
// @Data
@Setter
@Getter
@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
@AllArgsConstructor
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

    // 생성 메서드
    public static Account open(Integer customerId, String accountNo, String accountType) {
        Account account = new Account();
        account.customerId = customerId;
        account.accountNo = accountNo;
        account.accountType = accountType;
        account.status = "정상";
        account.balance = 0;
        return account;
    }

    // Setter 대신 우리가 만든 별도의 status가
    // 지급정지는 아닌지, 잔액이 불충분하진 않은지
    // 확인하는 예외를 만들어서 처리
    
}
