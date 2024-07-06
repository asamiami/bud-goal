package com.project.budgoal.controller;

import com.project.budgoal.dtos.request.BudgetRequest;
import com.project.budgoal.dtos.request.TransactionRequest;
import com.project.budgoal.dtos.response.ApiResponse;
import com.project.budgoal.dtos.response.BudgetResponse;
import com.project.budgoal.services.BudgetServ;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/budgoal/budget")
@RequiredArgsConstructor
public class BudgetController {

    private final BudgetServ budgetService;


    @PostMapping("/create-budget")
    public ApiResponse<BudgetResponse> createBudget (@RequestBody BudgetRequest budgetRequest, @RequestParam Long userId){
       ApiResponse<BudgetResponse> response = budgetService.createBudget(budgetRequest, userId);
        return  new ApiResponse<>(response.getMessage(), response.getCode(), response.getData());
    }

    @PutMapping ("/add-user/{userId}")
    public ApiResponse<List<BudgetResponse>> addMembers (@RequestParam Long newUser, @PathVariable Long userId, @RequestParam Long budgetId){
      ApiResponse<List<BudgetResponse>> response=  budgetService.addBudgetMembers(userId,newUser,budgetId);
      return  new ApiResponse<>(response.getMessage(), response.getCode(), response.getData());
    }

    @GetMapping("/all-budget")
    public ApiResponse<List<BudgetResponse>> getAllBudget(@RequestParam Long userid) {
        ApiResponse<List<BudgetResponse>> response = budgetService.allUsersBudget(userid);
        return new ApiResponse<>(response.getMessage(), response.getCode(), response.getData());
    }

    @PutMapping("/edit-budget/{userId}")
    public ApiResponse<BudgetResponse> editBudget (@RequestBody BudgetRequest budgetRequest, @RequestParam Long budgetId){
        ApiResponse<BudgetResponse> response =  budgetService.editBudget(budgetId,budgetRequest);
        return new ApiResponse<>(response.getMessage(), response.getCode(), response.getData());
    }


    @PostMapping("/add-transaction/{userId}")
    public ApiResponse<BudgetResponse> addTransaction (@RequestBody TransactionRequest budgetTransaction, @RequestParam Long budgetId){
        ApiResponse<BudgetResponse> response =  budgetService.addTransaction(budgetId,budgetTransaction);
        return new ApiResponse<>(response.getMessage(), response.getCode(), response.getData());
    }


    @GetMapping("/view-transactions")
    public ApiResponse<Map<String, Long>> viewTransactions (@RequestParam Long budgetId){
        ApiResponse<Map<String, Long>> response =  budgetService.viewTransactions(budgetId);
         return new ApiResponse<>(response.getMessage(), response.getCode(), response.getData());
    }


}
