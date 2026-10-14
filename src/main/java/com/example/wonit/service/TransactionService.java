package com.example.wonit.service;

import com.example.wonit.model.Transaction;
import com.example.wonit.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TransactionService {

    private final TransactionRepository transactionRepository;

    /**
     * 카테고리명으로 거래내역 검색
     * 일부 일치 검색
     */
    public List<Transaction> getTransactionByCategory(String category) {
        if (category == null || category.isBlank()) {
            throw new IllegalArgumentException("카테고리를 입력해주세요.");
        }

        return transactionRepository.findByCategoryContaining(category);
    }

    /**
     * 카테고리 또는 메모로 거래내역 검색
     * 두 조건 중 하나라도 일치하면 조회
     */
    public List<Transaction> getTransactionByCategoryOrMemo(
            String category, String memo) {

        boolean hasCategory = category != null && !category.isBlank();
        boolean hasMemo = memo != null && !memo.isBlank();

        if (!hasCategory && !hasMemo) {
            throw new IllegalArgumentException(
                    "카테고리 또는 메모 중 하나 이상 입력해주세요."
            );
        }

        return transactionRepository.findByCategoryOrMemoContaining(
                hasCategory ? category : null,
                hasMemo ? memo : null
        );
    }

    /**
     * 최소 금액 및 최대 금액으로 거래내역 검색
     * 최소 금액과 최대 금액을 모두 포함
     */
    public List<Transaction> getTransactionByAmount(
            Long minAmount, Long maxAmount) {

        if (minAmount == null || maxAmount == null) {
            throw new IllegalArgumentException(
                    "최소 금액과 최대 금액을 입력해주세요."
            );
        }

        if (minAmount < 0 || maxAmount < 0) {
            throw new IllegalArgumentException(
                    "금액은 0 이상이어야 합니다."
            );
        }

        if (minAmount > maxAmount) {
            throw new IllegalArgumentException(
                    "최소 금액은 최대 금액보다 클 수 없습니다."
            );
        }

        return transactionRepository
                .findByAmountBetween(minAmount, maxAmount);
    }
}