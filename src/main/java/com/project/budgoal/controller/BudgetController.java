package com.project.budgoal.controller;

import com.project.budgoal.dtos.BudgetRequest;
import com.project.budgoal.dtos.TransactionRequest;
import com.project.budgoal.response.ApiResponse;
import com.project.budgoal.response.BudgetResponse;
import com.project.budgoal.services.BudgetService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/budgoal/budget")
@RequiredArgsConstructor
public class BudgetController {

    private final BudgetService budgetService;


    @PostMapping("/create-budget")
    public ResponseEntity createBudget (@RequestBody BudgetRequest budgetRequest, @RequestParam Long userId){
        return budgetService.createBudget(budgetRequest,userId);
    }

    @PutMapping ("/add-user/{userId}")
    public ResponseEntity<ApiResponse<List<BudgetResponse>>> addMembers (@RequestParam Long newUser, @PathVariable Long userId, @RequestParam Long budgetId){
       return  budgetService.addBudgetMembers(userId,newUser,budgetId);
    }

    @GetMapping("/all-budget")
    public ResponseEntity<ApiResponse<List<BudgetResponse>>> getAllBudget(@RequestParam Long userid){
        return budgetService.allUsersBudget(userid);
    }

    @PutMapping("/edit-budget/{userId}")
    public ResponseEntity<ApiResponse<BudgetResponse>> editBudget (@RequestBody BudgetRequest budgetRequest, @RequestParam Long budgetId){
        return budgetService.editBudget(budgetId,budgetRequest);
    }


    @PostMapping("/add-transaction/{userId}")
    public ResponseEntity<ApiResponse<BudgetResponse>> addTransaction (@RequestBody TransactionRequest budgetTransaction, @RequestParam Long budgetId, @PathVariable("userId") Long userId ){
        return budgetService.addTransaction(budgetId,budgetTransaction);
    }


    @GetMapping("/view-transactions")
    public ResponseEntity<ApiResponse<Map<String, Long>>> viewTransactions (@RequestParam Long budgetId){
        return budgetService.viewTransactions(budgetId);
    }
}
