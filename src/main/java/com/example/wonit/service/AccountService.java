package com.example.wonit.service;

import com.example.wonit.dto.AccountCreateRequest;
import com.example.wonit.dto.AccountPatchRequest;
import com.example.wonit.dto.AccountResponse;
import com.example.wonit.model.Account;
import com.example.wonit.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import com.example.wonit.exception.*;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountService {
    @Autowired
    private final AccountRepository accountRepository;

    // 모든 계좌를 리턴
    public List<Account> findAll(){
        // 코드1
        List<Account> accounts = accountRepository.findAll();
        // 코드2
        log.info("전체 계좌 조회 건수 출력={}", accounts.size());
        log.info("전체 계좌 조회 건수 개별 출력={}", accounts.toString());
        // 코드3: 로그는 return이 일어나기 전에 보통 찍음
        return accounts;
    }

    // 하나의 계좌 정보를 repo에서 받아서 리턴하거나 NPE 막아주는 함수
    public Optional<Account> findOne(String accountNo) {
        return accountRepository.findByAccountNo(accountNo);
    }

    /**
     * 특정 계좌번호의 검색 결과 단건 반환
     * @param accountNo
     * @return Account
     * @throws AccountNotFoundException
     */
    // 특정 계좌 1개 계좌번호로 조회 -> 받아서 Acoount 자료형으로 받거나 예외를 일으키는 함수
    public Account getAccount(String accountNo) throws AccountNotFoundException {
        log.info("계좌 조회={}", accountNo);
        return findOne(accountNo)
                .orElseThrow(() -> new AccountNotFoundException(accountNo + "계좌를 찾을 수 없습니다."));
    }

    // '정상' 계좌만 출력
    public List<String> normalAccountNos() {
        return accountRepository.findAll().stream()
                .filter(account -> account.getStatus().equals("정상"))
                .map(Account::getAccountNo)
                .toList();
    }

    // 전체 계좌 중 accountType이 '적금'인 경우만 금액을 더해서 리턴
    public long savingBalanceSum() {
        return accountRepository.findAll().stream()
                .filter(account -> account.getAccountType().equals("적금"))
                .mapToLong(Account::getBalance).sum();
    }

    // min~max 사이 금액이 든 계좌만 조회하는 메서드
    // AccountRepository 인터페이스의 사용자 정의 쿼리 메소드를 사용합니다
    public List<Account> getAccountBetweenBalance(long minBalance, long maxBalance) {
        log.debug("Repository 잔액 조회 시작: minBalance={}, maxBalance={}",
                minBalance, maxBalance);

        List<Account> accounts =
                accountRepository.findByBalanceBetween(minBalance, maxBalance);

        log.debug("Repository 잔액 조회 완료: resultCount={}", accounts.size());
        return accounts;
    }

    // balance 순서대로 작은 금액에서 큰 금액 순서로 모든 계좌 조회
    public List<Account> findAllAccountsByBalanceAsc(){
        return accountRepository.findAllOrderBy();
    }

    // status 에서 정상 / 휴면 / 지급정지 들어오면 각 상황에 맞는 계좌만 필터링 -> 예외나 엣지케이스 테스트케이스 같이 만들고 처리
    public List<Account> findAllAccountsByStatus(String status){
        return accountRepository.findByStatusContains(status);
    }

    // account_type에 적금 / 입출금 들어오면 각 상황에 맞는 계좌만 필터링
    public List<Account> findAllAccountsByAccountType(String accountType){
        return accountRepository.findByAccountTypeLike(accountType);
    }

    public Account createAccount(AccountCreateRequest request) {
        if (request.customerId() == null || request.customerId() <= 0) {
            throw new InvalidAccountRequestException("올바른 customerId가 필요합니다.");
        }

        if (!List.of("입출금", "적금").contains(request.accountType())) {
            throw new InvalidAccountRequestException("지원하지 않는 accountType입니다.");
        }

        // 중복되지 않는 accountNo 생성
        String accountNo;
        do {
            accountNo = java.util.UUID.randomUUID()
                    .toString()
                    .replace("-", "")
                    .substring(0, 20);
        } while (accountRepository.existsByAccountNo(accountNo));

        log.info("생성된 계좌 번호={}", accountNo);
        log.info("계좌 생성 시작");
        Account account = accountRepository.save(
                Account.open(request.customerId(), accountNo, request.accountType())
        );
        log.info("생성된 계좌 정보={}",account.toString());
        return account;
    }

    public Account updateOne(String accountNo, AccountCreateRequest request) {
        // 원본 가져오기
        Account account = getAccount(accountNo);
        // 화면에 뿌리기 (통째로 바꾸는 경우에는 생략 가능 / 일부만 가져올 경우에는)
        //  - 원본의 내용과 일치하는 필드인지 -> 일치하면 변경, 그렇지 않으면 스킵
        account.setCustomerId(request.customerId());
        account.setAccountType(request.accountType());
        // 바뀐 내용 새로 객체로 저장하기
        // DB에 반영하기(레파지토리)
        Account saved = accountRepository.save(account);
        // 반영된 상태 확인하기
        return saved;
    }

    public Account changeStatus(String accountNo, AccountPatchRequest request) {
        Account account = getAccount(accountNo);

        if (request.accountType() != null) {
            if (!List.of("입출금", "적금").contains(request.accountType())) {
                throw new InvalidAccountRequestException("지원하지 않는 accountType입니다.");
            }
            account.setAccountType(request.accountType());
        }

        if (request.status() != null) {
            if ("정상".equals(request.status())) {
                account.setStatus("정상");
            } else if ("휴면".equals(request.status())) {
                account.setStatus("휴면");
            } else if ("지급정지".equals(request.status())) {
                account.setStatus("지급정지");
            } else {
                throw new InvalidAccountRequestException("상태는 정상 · 휴면 · 지급정지 중 하나여야 합니다.");
            }
        }

        return accountRepository.save(account);
    }

    // hard delete
    @Transactional
    public void deleteOneByAccountNo(String accountNo) {
        // 이미 삭제된 계좌여도 정상 동작함
        accountRepository.deleteByAccountNo(accountNo);
    }
    @Transactional
    public void deleteOne(String accountNo) {
        // 조회 후 삭제하기 때문에 이미 삭제된 계좌 삭제 시 에러 발생
        Account account = getAccount(accountNo);
        accountRepository.delete(account);
    }
}

