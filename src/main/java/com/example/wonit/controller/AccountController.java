package com.example.wonit.controller;

import com.example.wonit.dto.AccountResponse;
import com.example.wonit.exception.AccountNotFoundException;
import com.example.wonit.model.Account;
import com.example.wonit.service.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "계좌 조회 테스트 API", description = "swagger 테스트를 진행하는 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/accounts")
public class AccountController {
    @Autowired // 연결된 Bean임을 알려줌, 생략하면 스프링 컨테이너가 넣어줌
    private final AccountService accountService;
    /*
    * Inversion Of Control
    * 개발자가 Bean으로 등록해놓은 객체는 스프링 컨테이너가 관리하므로 개발자는 가져다 쓰기만 하면됨
    */
    // 모든 account 조회
    @Operation(summary = "정보 모두 조회", description = "Account의 전체 정보를 조회합니다.")
    @GetMapping
    public List<AccountResponse> findAll(){
        return accountService.findAll().stream()
                .map(AccountResponse::from)
                .toList();
    }

    // 특정 account 조회
    @GetMapping("/{accountNo}")
    public AccountResponse findOne(@PathVariable String accountNo) throws AccountNotFoundException {
        return AccountResponse.from(accountService.getAccount(accountNo));
    }

    // '정상' 계좌만 출력
    @GetMapping("/report1")
    public List<String> normalAccountNos(){
        return accountService.normalAccountNos();
    }

    // 전체 계좌 중 accountType이 '적금'인 경우만 금액을 더해서 리턴
    @GetMapping("/report2")
    public long savingBalanceSum(){
        return accountService.savingBalanceSum();
    }

    // min~ max balance 사이의 계좌 정보를 검색
    @GetMapping("/balance")
    public List<Account> getAccountBetweenBalance(@RequestParam long minBalance, @RequestParam long maxBalance) {
        return accountService.getAccountBetweenBalance(minBalance, maxBalance);
    }

    @GetMapping("/sort")
    public List<AccountResponse> findAllAccountsByBalanceAsc() {
        return accountService.findAllAccountsByBalanceAsc().stream()
                .map(AccountResponse::from)
                .toList();
    }

    @GetMapping("/status/{status}")
    public List<AccountResponse> findAllAccountsByStatus(@PathVariable String status){
        return accountService.findAllAccountsByStatus(status).stream()
                .map(AccountResponse::from)
                .toList();
    }

    @GetMapping("/account-type/{accountType}")
    public List<AccountResponse> findAllAccountsByAccountType(@PathVariable String accountType){
        return accountService.findAllAccountsByAccountType(accountType).stream()
                .map(AccountResponse::from)
                .toList();
    }
}
