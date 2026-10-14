package com.example.wonit.model;

import jakarta.persistence.*;
import lombok.*;
import org.h2.value.Transfer;

import java.time.LocalDateTime;

@Entity
//@Table(
//        name = "transaction",
//        indexes = {
//                @Index(
//                        name = "idx_tx_account_dt",
//                        columnList = "account_id, tx_datetime"
//                )
//        }
//)
// 예약어로 거부당하는 경우 ` `(백틱)으로 감싸서 문자열임을 분명히 명시
//@Table(name = "`transaction`")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tx_id")
    private Long txId;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "account_id", nullable = false)
//    private Account account;

    @Column(name = "account_id")
    private Long accountId;

    @Column(name = "transfer_id")
    private Long transferId;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "transfer_id")
//    private Transfer transfer;

    @Column(name = "tx_type", nullable = false, length = 10)
    private String txType;

    @Column(nullable = false)
    private Long amount;

    @Column(name = "balance_after", nullable = false)
    private Long balanceAfter;

    @Column(length = 20)
    private String category;

    @Column(length = 100)
    private String memo;

    @Column(length = 50)
    private String counterparty;

    @Column(name = "tx_datetime", nullable = false)
    private LocalDateTime txDatetime;
}