package com.project.budgoal.services;

import com.project.budgoal.dtos.request.BudgetRequest;
import com.project.budgoal.dtos.request.TransactionRequest;
import com.project.budgoal.dtos.response.ApiResponse;
import com.project.budgoal.dtos.response.BudgetResponse;

import java.util.List;
import java.util.Map;

public interface BudgetServ {
    public ApiResponse<BudgetResponse> createBudget(BudgetRequest budgetRequest, Long userId);
    public ApiResponse<List<BudgetResponse>> addBudgetMembers (Long userId, Long newUserId, Long budgetId);

    public ApiResponse<List<BudgetResponse>> allUsersBudget(Long userId);

    public ApiResponse<BudgetResponse> editBudget( Long budgetId,BudgetRequest budgetRequest);

    public ApiResponse<BudgetResponse> addTransaction(Long budgetId, TransactionRequest request);

    public ApiResponse<Map<String, Long>> viewTransactions (Long budgetId);
}
