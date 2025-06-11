package com.example.financetracker.repository;

import com.example.financetracker.model.Transaction;
import com.example.financetracker.model.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    
    // Find transactions by type (INCOME or EXPENSE)
    List<Transaction> findByType(TransactionType type);
    
    // Find transactions by date range
    List<Transaction> findByDateBetween(LocalDate startDate, LocalDate endDate);
    
    // Find transactions by category
    List<Transaction> findByCategory(String category);
    
    // Custom query to get total by type
    @Query("SELECT SUM(t.amount) FROM Transaction t WHERE t.type = ?1")
    BigDecimal getTotalByType(TransactionType type);
    
    // Get all transactions ordered by date (newest first)
    List<Transaction> findAllByOrderByDateDesc();

    // Find similar transactions within a date range
    @Query("SELECT t FROM Transaction t WHERE " +
           "ABS(t.amount - :amount) <= 0.01 AND " +  // Allow tiny amount differences
           "LOWER(t.description) LIKE LOWER(CONCAT('%', :baseDescription, '%')) AND " +
           "t.date BETWEEN :startDate AND :endDate")
    List<Transaction> findSimilarTransactions(
        @Param("amount") BigDecimal amount,
        @Param("baseDescription") String description,
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate
    );
}