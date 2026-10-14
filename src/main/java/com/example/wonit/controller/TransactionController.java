package com.example.wonit.controller;

import com.example.wonit.dto.TransactionResponse;
import com.example.wonit.model.Transaction;
import com.example.wonit.service.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/transactions")
@RequiredArgsConstructor
@Tag(name = "Transaction", description = "거래내역 조회 API")
public class TransactionController {

    private final TransactionService transactionService;

    /**
     * 카테고리명으로 거래내역 검색
     * 예시: GET /transactions/select?category=식비
     */
    @GetMapping("/select")
    @Operation(
            summary = "카테고리별 거래내역 검색",
            description = "카테고리명으로 거래내역을 검색합니다. 카테고리명 일부 일치를 지원합니다."
    )
    public List<TransactionResponse> getTransactionByCategory(
            @Parameter(description = "검색할 카테고리명", example = "식비")
            @RequestParam String category
    ) {
        return transactionService.getTransactionByCategory(category).stream()
                .map(TransactionResponse::from)
                .toList();
    }

    /**
     * 카테고리 또는 메모로 거래내역 검색
     * 예시: GET /transactions/select?category=식비&memo=회사
     */
    @GetMapping("/select/category-or-memo")
    @Operation(
            summary = "카테고리 또는 메모로 거래내역 검색",
            description = "카테고리명 또는 메모에 검색어가 포함된 거래내역을 조회합니다. 두 조건은 OR로 처리합니다."
    )
    public List<TransactionResponse> getTransactionByCategoryOrMemo(
            @Parameter(description = "검색할 카테고리명", example = "식비")
            @RequestParam(required = false) String category,

            @Parameter(description = "검색할 메모", example = "회사")
            @RequestParam(required = false) String memo
    ) {
        return transactionService.getTransactionByCategoryOrMemo(category, memo).stream()
                .map(TransactionResponse::from)
                .toList();
    }

    /**
     * 최소/최대 금액으로 거래내역 검색
     * 예시: GET /transactions/select2?minAmount=10000&maxAmount=20000
     */
    @GetMapping("/select2")
    @Operation(
            summary = "금액 범위별 거래내역 검색",
            description = "최소 금액 이상, 최대 금액 이하인 거래내역을 조회합니다."
    )
    public List<TransactionResponse> getTransactionByAmount(
            @Parameter(description = "최소 금액 (포함)", example = "10000")
            @RequestParam Long minAmount,

            @Parameter(description = "최대 금액 (포함)", example = "20000")
            @RequestParam Long maxAmount
    ) {
        return transactionService.getTransactionByAmount(minAmount, maxAmount).stream()
                .map(TransactionResponse::from)
                .toList();
    }
}
