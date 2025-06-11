package com.example.financetracker;

import com.example.financetracker.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    
    @Autowired
    private TransactionService transactionService;
    
    @GetMapping("/")
    public String home(Model model) {
        // Add financial summary to the model
        model.addAttribute("totalIncome", transactionService.getTotalIncome());
        model.addAttribute("totalExpenses", transactionService.getTotalExpenses());
        model.addAttribute("currentBalance", transactionService.getCurrentBalance());
        model.addAttribute("recentTransactions", transactionService.getAllTransactions().stream().limit(5).toList());
        
        return "index";
    }
}