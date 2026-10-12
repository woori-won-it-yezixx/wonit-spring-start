package com.example.wonit.service;

import com.example.wonit.model.Account;
import com.example.wonit.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import com.example.wonit.exception.*;

@Service
@RequiredArgsConstructor
public class AccountService {
    @Autowired
    private final AccountRepository accountRepository;

    // 모든 계좌를 리턴
    public List<Account> findAll(){
        return accountRepository.findAll();
    }

    // 하나의 계좌 정보를 repo에서 받아서 리턴하거나 NPE 막아주는 함수
    public Optional<Account> findOne(String accountNo) {
        return accountRepository.findByAccountNo(accountNo);
    }

    // 특정 계좌 1개 계좌번호로 조회 -> 받아서 Acoount 자료형으로 받거나 예외를 일으키는 함수
    public Account getAccount(String accountNo) throws AccountNotFoundException {
        return findOne(accountNo)
                .orElseThrow(() -> new AccountNotFoundException(accountNo + "계좌를 찾을 수 없습니다."));
    }

    // '정상' 계좌만 출력
    public List<String> normalAccountNos() {
        return accountRepository.findAll().stream()
                .filter(account -> account.getStatus().equals("정상"))
                .map(Account::getStatus)
                .toList();
    }

    // 전체 계좌 중 accountType이 '적금'인 경우만 금액을 더해서 리턴
    public long savingBalanceSum() {
        return accountRepository.findAll().stream()
                .filter(account -> account.getAccountType().equals("적금"))
                .mapToLong(Account::getBalance).sum();
    }
}
