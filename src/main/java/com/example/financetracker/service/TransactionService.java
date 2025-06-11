package com.example.financetracker.service;

import com.example.financetracker.model.Transaction;
import com.example.financetracker.model.TransactionType;
import com.example.financetracker.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class TransactionService {
    
    @Autowired
    private TransactionRepository transactionRepository;
    
    // Save a new transaction
    public Transaction saveTransaction(Transaction transaction) {
        return transactionRepository.save(transaction);
    }
    
    // Save multiple transactions
    public List<Transaction> saveAllTransactions(List<Transaction> transactions) {
        return transactionRepository.saveAll(transactions);
    }
    
    // Get all transactions
    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAllByOrderByDateDesc();
    }
    
    // Get transaction by ID
    public Optional<Transaction> getTransactionById(Long id) {
        return transactionRepository.findById(id);
    }
    
    // Delete transaction
    public void deleteTransaction(Long id) {
        transactionRepository.deleteById(id);
    }
    
    // Get total income
    public BigDecimal getTotalIncome() {
        BigDecimal total = transactionRepository.getTotalByType(TransactionType.INCOME);
        return total != null ? total : BigDecimal.ZERO;
    }
    
    // Get total expenses
    public BigDecimal getTotalExpenses() {
        BigDecimal total = transactionRepository.getTotalByType(TransactionType.EXPENSE);
        return total != null ? total : BigDecimal.ZERO;
    }
    
    // Get current balance (income - expenses)
    public BigDecimal getCurrentBalance() {
        return getTotalIncome().subtract(getTotalExpenses());
    }
    
    // Get transactions by category
    public List<Transaction> getTransactionsByCategory(String category) {
        return transactionRepository.findByCategory(category);
    }
    
    // Find similar transactions (potential duplicates)
    public List<Transaction> findSimilarTransactions(BigDecimal amount, String description, LocalDate date) {
        // Look for transactions with the same amount and description within a 7-day window
        LocalDate startDate = date.minusDays(7);
        LocalDate endDate = date.plusDays(7);
        return transactionRepository.findSimilarTransactions(amount, description, startDate, endDate);
    }
}