package com.example.wonit.repository;

import com.example.wonit.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByCategoryContaining(String category);

    List<Transaction> findByCategoryOrMemo(String s, String s1);

    List<Transaction> findByAmountBetween(Long minAmount, Long maxAmount);

    // 카테고리 완전 일치 OR 메모 부분 일치
    List<Transaction> findByCategoryOrMemoContaining(
            String category,
            String memo
    );
}
