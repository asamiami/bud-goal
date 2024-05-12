package com.project.budgoal.controller;

import com.project.budgoal.dtos.BudgetRequest;
import com.project.budgoal.dtos.BudgetTransactionRequest;
import com.project.budgoal.entites.BudgetTransaction;
import com.project.budgoal.response.ApiResponse;
import com.project.budgoal.response.BudgetResponse;
import com.project.budgoal.services.BudgetService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public ResponseEntity<ApiResponse<BudgetResponse>> addMembers (@RequestBody Long newUser, @PathVariable Long userId, @RequestParam Long budgetId){
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
    public ResponseEntity<ApiResponse<BudgetResponse>> addTrasnaction (@RequestBody BudgetTransactionRequest budgetTransaction, @RequestParam Long budgetId){
        return budgetService.addTransaction(budgetId,budgetTransaction);
    }
}
