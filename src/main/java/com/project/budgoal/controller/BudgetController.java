package com.project.budgoal.controller;

import com.project.budgoal.dtos.request.BudgetRequest;
import com.project.budgoal.dtos.request.TransactionRequest;
import com.project.budgoal.dtos.response.ApiResponse;
import com.project.budgoal.dtos.response.BudgetResponse;
import com.project.budgoal.services.BudgetService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
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
    public ResponseEntity<ApiResponse<BudgetResponse>> createBudget (@RequestBody BudgetRequest budgetRequest, @RequestParam Long userId){
       var response = budgetService.createBudget(budgetRequest, userId);
        return  new ResponseEntity<>(response, response.getCode());
    }

    @PutMapping ("/add-user/{userId}")
    public ResponseEntity<ApiResponse<List<BudgetResponse>>> addMembers (@RequestParam Long newUser, @PathVariable Long userId, @RequestParam Long budgetId){
      var response=  budgetService.addBudgetMembers(userId,newUser,budgetId);
      return  new ResponseEntity<>(response, response.getCode());
    }

    @GetMapping("/all-budget")
    public ResponseEntity<ApiResponse<List<BudgetResponse>>> getAllBudget(@RequestParam Long userid){
        var response = budgetService.allUsersBudget(userid);
        return  new ResponseEntity<>(response, response.getCode());
    }

    @PutMapping("/edit-budget/{userId}")
    public ResponseEntity<ApiResponse<BudgetResponse>> editBudget (@RequestBody BudgetRequest budgetRequest, @RequestParam Long budgetId){
        var response =  budgetService.editBudget(budgetId,budgetRequest);
        return new ResponseEntity<>(response, response.getCode());
    }


    @PostMapping("/add-transaction/{userId}")
    public ResponseEntity<ApiResponse<BudgetResponse>> addTransaction (@RequestBody TransactionRequest budgetTransaction, @RequestParam Long budgetId){
        var response =  budgetService.addTransaction(budgetId,budgetTransaction);
        return new ResponseEntity<>(response, response.getCode());
    }


    @GetMapping("/view-transactions")
    public ResponseEntity<ApiResponse<Map<String, Long>>> viewTransactions (@RequestParam Long budgetId){
        return new ResponseEntity<>(budgetService.viewTransactions(budgetId), HttpStatusCode.valueOf(200));
    }


}
