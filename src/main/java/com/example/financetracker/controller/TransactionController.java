package com.example.financetracker.controller;

import com.example.financetracker.model.Transaction;
import com.example.financetracker.model.TransactionType;
import com.example.financetracker.service.TransactionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/transactions")
public class TransactionController {
    
    @Autowired
    private TransactionService transactionService;
    
    // Show all transactions
    @GetMapping
    public String listTransactions(Model model) {
        model.addAttribute("transactions", transactionService.getAllTransactions());
        return "transactions/list";
    }
    
    // Show form to add new transaction
    @GetMapping("/add")
    public String showAddForm(Model model) {
        if (!model.containsAttribute("transaction")) {
            model.addAttribute("transaction", new Transaction());
        }
        model.addAttribute("transactionTypes", TransactionType.values());
        model.addAttribute("categories", getCategories());
        // Add recent descriptions for autocomplete
        model.addAttribute("recentDescriptions", 
            transactionService.getAllTransactions().stream()
                .map(Transaction::getDescription)
                .distinct()
                .limit(10)
                .toList());
        return "transactions/add";
    }
    
    // Process form submission
    @PostMapping("/add")
    public String addTransaction(@Valid @ModelAttribute Transaction transaction,
                               BindingResult result,
                               @RequestParam(required = false) String frequency,
                               @RequestParam(required = false) String recurrenceType,
                               @RequestParam(required = false) LocalDate endDate,
                               Model model,
                               RedirectAttributes redirectAttributes) {
        
        if (result.hasErrors()) {
            model.addAttribute("transactionTypes", TransactionType.values());
            model.addAttribute("categories", getCategories());
            return "transactions/add";
        }
        
        // Handle recurring transactions
        if ("recurring".equals(frequency) && endDate != null) {
            List<Transaction> recurringTransactions = generateRecurringTransactions(
                transaction, recurrenceType, endDate);
            transactionService.saveAllTransactions(recurringTransactions);
            redirectAttributes.addFlashAttribute("successMessage", 
                "Successfully added " + recurringTransactions.size() + " recurring transactions!");
        } else {
            transactionService.saveTransaction(transaction);
            redirectAttributes.addFlashAttribute("successMessage", "Transaction added successfully!");
        }
        
        return "redirect:/transactions";
    }
    
    // Check for similar transactions
    @GetMapping("/check-duplicate")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> checkDuplicate(
            @RequestParam BigDecimal amount,
            @RequestParam String description,
            @RequestParam LocalDate date) {
        
        List<Transaction> similarTransactions = transactionService.findSimilarTransactions(
            amount, description, date);
        
        Map<String, Object> response = new HashMap<>();
        response.put("hasSimilar", !similarTransactions.isEmpty());
        response.put("similarTransactions", similarTransactions);
        
        return ResponseEntity.ok(response);
    }
    
    // Delete transaction
    @PostMapping("/delete/{id}")
    public String deleteTransaction(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        transactionService.deleteTransaction(id);
        redirectAttributes.addFlashAttribute("successMessage", "Transaction deleted successfully!");
        return "redirect:/transactions";
    }
    
    // Helper method to generate recurring transactions
    private List<Transaction> generateRecurringTransactions(
            Transaction baseTransaction,
            String recurrenceType,
            LocalDate endDate) {
        
        List<Transaction> transactions = new ArrayList<>();
        LocalDate currentDate = baseTransaction.getDate();
        
        while (!currentDate.isAfter(endDate)) {
            Transaction newTransaction = new Transaction(
                baseTransaction.getDescription(),
                baseTransaction.getAmount(),
                currentDate,
                baseTransaction.getType(),
                baseTransaction.getCategory()
            );
            
            transactions.add(newTransaction);
            
            // Calculate next date based on recurrence type
            currentDate = switch (recurrenceType) {
                case "weekly" -> currentDate.plusWeeks(1);
                case "monthly" -> currentDate.plusMonths(1);
                case "yearly" -> currentDate.plusYears(1);
                default -> endDate.plusDays(1); // breaks the loop
            };
        }
        
        return transactions;
    }
    
    // Helper method to provide category options
    private String[] getCategories() {
        return new String[]{
            "Food & Dining", "Transportation", "Shopping", "Entertainment", 
            "Bills & Utilities", "Healthcare", "Education", "Travel", 
            "Salary", "Business", "Investments", "Other"
        };
    }
}