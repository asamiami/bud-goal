package com.project.budgoal.controller;

import com.project.budgoal.dtos.request.SavingsRequest;
import com.project.budgoal.dtos.request.TransactionRequest;
import com.project.budgoal.dtos.response.ApiResponse;
import com.project.budgoal.dtos.response.SavingsResponse;
import com.project.budgoal.dtos.response.UserResponse;
import com.project.budgoal.services.SavingsServ;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/budgoal/savings")
@RequiredArgsConstructor
public class SavingsController {

    private final SavingsServ savingsService;

    @PostMapping("/create")
    public ApiResponse<SavingsResponse> createSavings (@RequestBody SavingsRequest savingsRequest, @RequestParam Long user){
        ApiResponse<SavingsResponse> response = savingsService.createSavings(savingsRequest,user);
        return new ApiResponse<>(response.getMessage(), response.getCode(), response.getData());
    }


    @PutMapping("/add-user/{user}")
    public ApiResponse<List<SavingsResponse>> addMember (@RequestParam Long newUser, @RequestParam Long savingsId, @PathVariable Long user){
        ApiResponse<List<SavingsResponse>> response = savingsService.addMembers(user,newUser,savingsId);
        return new ApiResponse<>(response.getMessage(), response.getCode(), response.getData());
    }

    @GetMapping("/all-savings")
    public ApiResponse<List<SavingsResponse>> allSavings ( @RequestParam Long userId){
       ApiResponse<List<SavingsResponse>> response = savingsService.viewAllSavings(userId);
       return new ApiResponse<>(response.getMessage(),response.getCode(), response.getData());
    }

    @GetMapping("/all-members")
    public ApiResponse<List<UserResponse>> allMembers (@RequestParam Long savingsId){
        ApiResponse<List<UserResponse>> response = savingsService.viewMembers(savingsId);
        return new ApiResponse<>(response.getMessage(),response.getCode(), response.getData());
    }

    @PostMapping("/add-transaction")
    public ApiResponse<SavingsResponse> addTransaction(@RequestParam Long savingsId, @RequestBody TransactionRequest transactionRequest, @RequestParam Long userId){
        ApiResponse<SavingsResponse> response = savingsService.addTransaction(savingsId, transactionRequest, userId);
        return new ApiResponse<>(response.getMessage(), response.getCode(), response.getData());
    }

    @GetMapping("/view-transactions")
    public ApiResponse<Map<String, Long>> viewTransactions (@RequestParam Long savingsId){
        ApiResponse<Map<String,Long>> response = savingsService.viewTransaction(savingsId);
        return new ApiResponse<>(response.getMessage(), response.getCode(), response.getData());
    }

}

