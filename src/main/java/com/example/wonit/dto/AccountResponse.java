package com.example.wonit.dto;

import com.example.wonit.model.Account;

// Controller <-> Service
// 변경될 일 없음 -> record로
public record AccountResponse (String accountNo, String accountType, long balance, String status){
    public static AccountResponse from(Account account) {
        return new AccountResponse(
                account.getAccountNo(),
                account.getAccountType(),
                account.getBalance(),
                account.getStatus());
    }
}
