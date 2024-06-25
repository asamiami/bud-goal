package com.project.budgoal.services;

import com.project.budgoal.dtos.request.SavingsRequest;
import com.project.budgoal.dtos.request.TransactionRequest;
import com.project.budgoal.dtos.response.ApiResponse;
import com.project.budgoal.dtos.response.SavingsResponse;
import com.project.budgoal.dtos.response.UserResponse;

import java.util.List;
import java.util.Map;

public interface SavingsServ {

    public ApiResponse<SavingsResponse> createSavings(SavingsRequest request, Long userId);

    public ApiResponse<List<SavingsResponse>> addMembers(Long userId, Long newUserId, Long savingsId);

    public ApiResponse<List<SavingsResponse>> viewAllSavings(Long userId);

    public ApiResponse<List<UserResponse>> viewMembers(Long savingsId);

    public ApiResponse<SavingsResponse> addTransaction (Long savingsId, TransactionRequest transactionRequest, Long userId);

    public ApiResponse<Map<String, Long>> viewTransaction (Long savingsId);


}
