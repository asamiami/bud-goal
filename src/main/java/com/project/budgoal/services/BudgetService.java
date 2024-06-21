package com.project.budgoal.services;

import com.project.budgoal.dtos.BudgetRequest;
import com.project.budgoal.dtos.TransactionRequest;
import com.project.budgoal.entites.Budget;
import com.project.budgoal.entites.Users;
import com.project.budgoal.enums.TransactionCatgory;
import com.project.budgoal.repository.BudgetRepo;
import com.project.budgoal.repository.SavingsRepo;
import com.project.budgoal.repository.UserRepository;
import com.project.budgoal.response.ApiResponse;
import com.project.budgoal.response.BudgetResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class BudgetService {

    private final BudgetRepo budgetRepo;

    private final UserRepository userRepo;


    public ResponseEntity createBudget(BudgetRequest budgetRequest, Long userId){

        var user = userRepo.findUsersById(userId);

            List<Users> usersList = new ArrayList<>();

            Budget newBudget = new Budget();

            newBudget.setBudgetAmount(budgetRequest.amount());
            newBudget.setBudgetName(budgetRequest.name());
            newBudget.setBudgetCategory(budgetRequest.budgetCategory());
            usersList.add(user);
            newBudget.setUsersList(usersList);
            newBudget.setBudgetMembers(usersList.size());
            newBudget.setCreatedDate(LocalDate.now());
            budgetRepo.save(newBudget);

            BudgetResponse budgetResponse = new BudgetResponse(newBudget.getBudgetName(), newBudget.getBudgetAmount(), newBudget.getTransactions(), newBudget.getBudgetMembers());

        ApiResponse response =  new ApiResponse<>(newBudget.getBudgetName() + " Budget has been created", HttpStatus.CREATED, budgetResponse);

        return new ResponseEntity<>(response, response.getCode());
    }

    public ResponseEntity<ApiResponse<List<BudgetResponse>>> addBudgetMembers (Long userId, Long newUserId, Long budgetId){

        var user = userRepo.findById(userId);
        List<BudgetResponse> listOfMembers = new ArrayList<>();
        if(user.isPresent() && userRepo.existsById(newUserId)) {
            var newUSer = userRepo.findUsersById(newUserId);
            var budget = budgetRepo.findBudgetById(budgetId);

            if (!budget.getUsersList().contains(newUserId)) {
               budget.addUsersToUserList(newUSer);
                budgetRepo.save(budget);
                BudgetResponse budgetResponse = new BudgetResponse(budget.getBudgetName(), budget.getBudgetAmount(), budget.getTransactions(), budget.getBudgetMembers());
                listOfMembers.add(budgetResponse);
                ApiResponse response = new ApiResponse<>("User added succesfully", HttpStatus.OK, listOfMembers);
                return new ResponseEntity<>(response, response.getCode());
            } else {
                ApiResponse response = new ApiResponse<>("This User already exists in the Budget", HttpStatus.BAD_REQUEST);
                return new ResponseEntity<>(response, response.getCode());
            }
        }else {
            ApiResponse response = new ApiResponse<>("Kindly ensure the user is a registered user", HttpStatus.BAD_REQUEST);
            return new ResponseEntity<>(response, response.getCode());
        }

    }

    public ResponseEntity<ApiResponse<List<BudgetResponse>>> allUsersBudget(Long userId){

        var user = userRepo.findUsersById(userId);

        if (user != null) {
            var budgets = budgetRepo.findAllByUsersList(user);
            List<BudgetResponse> listOfBudgets = new ArrayList<>();
            for (Budget budget : budgets) {
                BudgetResponse budgetResponse = new BudgetResponse(budget.getBudgetName(), budget.getBudgetAmount(), budget.getTransactions(), budget.getBudgetMembers());
                listOfBudgets.add(budgetResponse);
            }
            if (!listOfBudgets.isEmpty()) {
                ApiResponse<List<BudgetResponse>> response = new ApiResponse<>("The list of budgets", HttpStatus.OK, listOfBudgets);
                return new ResponseEntity<>(response, response.getCode());
            }
        }

        ApiResponse apiResponse = new ApiResponse<>("This user has no budget", HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(apiResponse, apiResponse.getCode());
    }





    public ResponseEntity<ApiResponse<BudgetResponse>> editBudget( Long budgetId,BudgetRequest budgetRequest){

        var budget = budgetRepo.findBudgetById(budgetId);

        budget.setBudgetAmount(budgetRequest.amount());
        budget.setBudgetName(budgetRequest.name());
        budget.setUpdatedDate(LocalDateTime.now());
        budgetRepo.save(budget);

        ApiResponse response = new ApiResponse<>("Budget edited Successfully", HttpStatus.OK);

        return new ResponseEntity<>(response, response.getCode());

    }

    public ResponseEntity<ApiResponse<BudgetResponse>> addTransaction(Long budgetId, TransactionRequest request) {
        var budget1 = budgetRepo.findById(budgetId);



        Map<String, Long> transactions = new HashMap<>();

        if (budget1.isPresent()) {
            Budget budget = budgetRepo.findBudgetById(budgetId);

            if (request.category() == TransactionCatgory.INCOME) {
                budget.setBudgetAmount(budget.getBudgetAmount() + request.amount());


                budget.addTransactionToTransactionsList(request.category().toString(), request.amount());

                budgetRepo.save(budget);
                BudgetResponse budgetResponse = new BudgetResponse(budget.getBudgetName(), budget.getBudgetAmount(), budget.getTransactions(), budget.getBudgetMembers());
                ApiResponse response = new ApiResponse<>("An income has been added", HttpStatus.OK, budgetResponse);
                return new ResponseEntity<>(response, response.getCode());
            } else if (request.category() == TransactionCatgory.EXPENSES && request.amount() < budget.getBudgetAmount()) {
                budget.setBudgetAmount(budget.getBudgetAmount() - request.amount());

                budget.addTransactionToTransactionsList(request.category().toString(), request.amount());
                budgetRepo.save(budget);
                BudgetResponse budgetResponse = new BudgetResponse(budget.getBudgetName(), budget.getBudgetAmount(), budget.getTransactions(), budget.getBudgetMembers());
                ApiResponse response = new ApiResponse<>("An expense has been added", HttpStatus.OK, budgetResponse);
                return new ResponseEntity<>(response, response.getCode());
            } else if (request.amount() > budget.getBudgetAmount()) {
                ApiResponse response = new ApiResponse<>("This is beyond your budget. Please let us try and stick to the money available", HttpStatus.BAD_REQUEST);
                return new ResponseEntity<>(response, response.getCode());
            }else {
                ApiResponse response = new ApiResponse<>("This category is not allowed", HttpStatus.BAD_REQUEST);
                return new ResponseEntity<>(response, response.getCode());
            }


        }else {
            ApiResponse response = new ApiResponse<>("This budget does not exist", HttpStatus.BAD_REQUEST);
            return new ResponseEntity<>(response, response.getCode());

        }

    }


    public ResponseEntity<ApiResponse<Map<String, Long>>> viewTransactions (Long budgetId){
        var budget =  budgetRepo.findById(budgetId);

        if (budget.isPresent()){
            Budget budgets = budgetRepo.findBudgetById(budgetId);

            Map<String, Long> transactions = budgets.getTransactions();

            ApiResponse apiResponse = new ApiResponse<>("The following are the transactions in " + budgets.getBudgetName(), HttpStatus.OK, transactions);
            return new ResponseEntity<>(apiResponse, apiResponse.getCode());
        } else {
            ApiResponse apiResponse = new ApiResponse<>("This budget does not exist", HttpStatus.BAD_REQUEST);
            return new ResponseEntity<>(apiResponse, apiResponse.getCode());
        }
    }
}

