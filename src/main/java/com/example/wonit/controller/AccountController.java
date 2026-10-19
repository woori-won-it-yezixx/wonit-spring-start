package com.example.wonit.controller;

import com.example.wonit.dto.AccountCreateRequest;
import com.example.wonit.dto.AccountPatchRequest;
import com.example.wonit.dto.AccountResponse;
import com.example.wonit.exception.AccountNotFoundException;
import com.example.wonit.exception.InvalidAccountRequestException;
import com.example.wonit.model.Account;
import com.example.wonit.service.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

// RestController = 인터넷 주소(URL)를 받아 Service 를 부르는 부분. 돌려주는 자바 객체는 Spring 이 JSON 으로 바꿔 응답한다
// REST 규칙: URL 은 명사(무엇을), HTTP 메서드는 동사(어떻게)
//   GET    /api/accounts                     목록      200
//   GET    /api/accounts/{accountNo}         하나      200
//   POST   /api/accounts                     개설      201 + Location 헤더로 개설된 주소 전송
//   PUT    /api/accounts/{accountNo}         통째로 교체 200
//   PATCH  /api/accounts/{accountNo}/status  상태 변경 200
//   DELETE /api/accounts/{accountNo}         해지      204
//   POST   /api/accounts/{accountNo}/withdrawals    출금      201
//   POST   /api/accounts/{accountNo}/deposits       입금      201
//   GET    /api/accounts/{accountNo}/transactions   거래내역  200 (쪽 단위로 출력)

@Slf4j
@Tag(name = "계좌 조회 테스트 API", description = "swagger 테스트를 진행하는 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/accounts")
public class AccountController {
    @Autowired // 연결된 Bean임을 알려줌, 생략하면 스프링 컨테이너가 넣어줌
    private final AccountService accountService;
    /**
     * 새 계좌를 개설합니다.
     *
     * <p>요청 예시:
     * <pre>
     * POST /api/accounts
     * Content-Type: application/json
     * {"customerId": 1, "accountType": "적금"}
     * </pre>
     *
     * <p>계좌번호, 초기 잔액, 상태는 서버에서 설정합니다. 계좌 ID는
     * {@code Account}의 IDENTITY 전략으로 DB가 생성합니다.
     * H2에서 시드 데이터가 {@code account_id} 1, 2, 3을 사용한다면,
     * 시드 INSERT 이후 다음 ID가 4가 되도록 설정해야 기본 키 충돌을 방지할 수 있습니다.
     *
     * @param request 개설할 고객 ID와 계좌 종류
     * @return 생성된 계좌 정보
     */
//    @Operation(summary = "계좌 생성", description = "새로운 Account를 생성합니다.")
//    @PostMapping
//    public AccountResponse createAccount(
//            @RequestBody AccountCreateRequest request
//    ) {
//        log.info("받은 request={}", request.toString());
//        Account account = accountService.createAccount(request);
//
//        return AccountResponse.from(account);
//    }
    @Operation(summary = "계좌 생성", description = "새로운 Account를 생성합니다.")
    @PostMapping
    public ResponseEntity<AccountResponse> createAccount(
            @RequestBody AccountCreateRequest request
    ) {
        log.info("받은 request={}", request.toString());
        Account account = accountService.createAccount(request);

//         새로 발급된 accountNo 사용하여 redirect하는 부분
        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/api/accounts/{accountNo}")
                .buildAndExpand(account.getAccountNo())
                .toUri();

        return ResponseEntity
                .created(location) // header에 새로 생긴 계좌번호를 달아서 주소를 전달
                .body(AccountResponse.from(account)); // body에 실제 생성된 계좌 정보를 전달
    }

    /*
    * Inversion Of Control
    * 개발자가 Bean으로 등록해놓은 객체는 스프링 컨테이너가 관리하므로 개발자는 가져다 쓰기만 하면됨
    */
    // 모든 account 조회

    /**
     * 계좌번호로 계좌 하나를 찾는다.
     * @test localhost:8080/api/accounts/1002-345-678901
     * @param accountNo 찾을 계좌번호 (예: 1002-345-112233)
     * @return 찾은 계좌
     * @throws AccountNotFoundException 그런 계좌가 없을 때
     */
    // 특정 account 조회
    @GetMapping("/{accountNo}")
    public AccountResponse findOne(@PathVariable String accountNo) {
//        log.trace("trace level의 로그 - 자잘함");
//        log.debug("debug level의 로그 - 개발자가 flow 따라갈 수 있는 수준의 로그");
//        log.info("info level의 로그 - 사용자나 시스템읭 의미있는 내용을 담은 로그, 실제 운영환경에서는 info 이상만 수집");
//        log.warn("warn level의 로그 - 현재는 문제 없지만 앞으로 문제가 될 수 있는 상황에 대한 경고");
//        log.error("error level의 로그 - 치명적인 로그");
        boolean validAccountNo = accountNo.matches(
                "(?i)(\\d{4}-\\d{3}-\\d{6}|[0-9a-f]{20})"
        );

        if (!validAccountNo) {
            throw new InvalidAccountRequestException(
                    "계좌번호 형식이 올바르지 않습니다."
            );
        }
        log.info("계좌 조회" + accountNo); // 먼저 문자열 더하는 과정은 거치고 나서 로그를 제외
        log.info("계좌 조회 시작={}", accountNo); // 보지도 않고 실행
        return AccountResponse.from(accountService.getAccount(accountNo));
    }


    /**
     * 정상 계좌 조회
     * @return 'status'가 '정상'인 계좌 리스트
     */
    @GetMapping("/report1")
    public List<String> normalAccountNos(){
        return accountService.normalAccountNos();
    }

    @GetMapping("/report2")
    public long savingBalanceSum(){
        return accountService.savingBalanceSum();
    }

    /**
     * 지정한 최소 잔액과 최대 잔액 사이의 계좌를 조회합니다.
     *
     * <p>요청 예시:
     * <pre>
     * GET /api/accounts/balance?minBalance=500000&amp;maxBalance=1300000
     * </pre>
     *
     * <p>경계값을 포함하여 조회합니다. Service에 전달할 때도 반드시
     * {@code minBalance}를 먼저, {@code maxBalance}를 나중에 전달해야 합니다.
     * 순서를 바꾸면 검색 조건이 뒤집혀 결과가 비어 있을 수 있습니다.
     *
     * @param minBalance 조회할 최소 잔액
     * @param maxBalance 조회할 최대 잔액
     * @return 잔액이 지정 범위에 포함되는 계좌 목록
     */
    @GetMapping("/balance")
    public List<AccountResponse> getAccountBetweenBalance(@RequestParam long minBalance, @RequestParam long maxBalance) {
        return accountService.getAccountBetweenBalance(minBalance, maxBalance).stream()
                .map(AccountResponse::from)
                .toList();
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


    //@RequestBody : 민감정보, 주소줄에 전달하기에 너무 긴 정보 받아옴
    // min~ max balance 사이의 계좌 정보를 검색
    // 전체 계좌 중 accountType이 '적금'인 경우만 금액을 더해서 리턴
    // '정상' 계좌만 출력
    @Operation(summary = "정보 모두 조회", description = "Account의 전체 정보를 조회합니다.")
    @GetMapping
    public List<AccountResponse> findAll(){
        log.info("전체 계좌 조회 시작");
        return accountService.findAll().stream()
                .map(AccountResponse::from)
                .toList();
    }

    // PUT
    @PutMapping("/{accountNo}")
    public AccountResponse updateOne(@PathVariable("accountNo") String accountNo,
                                     @RequestBody AccountCreateRequest request){
        return AccountResponse.from(accountService.updateOne(accountNo, request));
    }

    // PATCH
    @PatchMapping("/{accountNo}/status")
    public AccountResponse changeStatus(@PathVariable("accountNo") String accountNo,
                                     @RequestBody AccountPatchRequest request){
        return AccountResponse.from(accountService.changeStatus(accountNo, request));
    }

    @DeleteMapping("/{accountNo}")
    public void deleteOne(@PathVariable("accountNo") String accountNo){
//        accountService.deleteOneByAccountNo(accountNo);
        accountService.deleteOne(accountNo);
    }
}
