package com.example.wonit.dto;

import com.example.wonit.model.Transaction;

public record TransactionResponse(String counterparty,
                                  String txType,
                                  String Category,
                                  String memo,
                                  long amount,
                                  java.time.LocalDateTime dtDatetime) {
    public static TransactionResponse from(Transaction transaction){
        return new TransactionResponse(
                transaction.getCounterparty(),
                transaction.getTxType(),
                transaction.getCategory(),
                transaction.getMemo(),
                transaction.getAmount(),
                transaction.getTxDatetime()
        );
    }
}
