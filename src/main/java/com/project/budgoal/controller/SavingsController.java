package com.project.budgoal.controller;

import com.project.budgoal.dtos.SavingsRequest;
import com.project.budgoal.dtos.TransactionRequest;
import com.project.budgoal.response.ApiResponse;
import com.project.budgoal.response.SavingsResponse;
import com.project.budgoal.response.UserResponse;
import com.project.budgoal.services.SavingsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/budgoal/savings")
@RequiredArgsConstructor
public class SavingsController {

    private final SavingsService savingsService;

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<SavingsResponse>> createSavings (@RequestBody SavingsRequest savingsRequest, @RequestParam Long user){

        return savingsService.createSavings(savingsRequest, user);
    }


    @PutMapping("/add-user/{user}")
    public ResponseEntity<ApiResponse<List<SavingsResponse>>> addMember (@RequestParam Long newUser, @RequestParam Long savingsId, @PathVariable Long user){

        return savingsService.addMembers(user, newUser, savingsId);
    }

    @GetMapping("/all-savings")
    public ResponseEntity<ApiResponse<List<SavingsResponse>>> allSavings ( @RequestParam Long userId){
        return savingsService.viewAllSavings(userId);
    }

    @GetMapping("/all-members")
    public ResponseEntity<ApiResponse<List<UserResponse>>> allMembers (@RequestParam Long savingsId){
        return savingsService.viewMembers(savingsId);
    }

    @PostMapping("/add-transaction")
    public ResponseEntity<ApiResponse<SavingsResponse>> addTransaction(@RequestParam Long savingsId, @RequestBody TransactionRequest transactionRequest, @RequestParam Long userId){
        return savingsService.addTransaction(savingsId,transactionRequest,userId);
    }

    @GetMapping("/view-transactions")
    public ResponseEntity<ApiResponse<Map<String, Long>>> viewTransactions (@RequestParam Long savingsId){
        return savingsService.viewTransaction(savingsId);
    }

    }

