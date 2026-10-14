package com.example.wonit.repository;

import com.example.wonit.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

// JPA 규칙대로 메서드 설계해두면 Hibernate가 DB에 맞는 SQL문으로 변경해서 데이터 가져옴
// Hibernate가 실제로 구현해주기 때문에 interface로 작성
@Repository
public interface AccountRepository extends JpaRepository<Account, Integer> {
    Optional<Account> findByAccountNo(String accountNo);

    List<Account> findByBalanceBetween(long minBalance, long maBalance);

    List<Account> findAllOrderBy();

    List<Account> findByStatus(String status);

    List<Account> findByAccountType(String accountType);

    List<Account> findByStatusLike(String status);

    List<Account> findByStatusContains(String status);

    List<Account> findByAccountTypeLike(String accountType);

    boolean existsByAccountNo(String accountNo);
}
