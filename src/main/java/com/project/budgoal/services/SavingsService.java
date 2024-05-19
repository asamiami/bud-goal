package com.project.budgoal.services;

import com.project.budgoal.dtos.SavingsRequest;
import com.project.budgoal.dtos.TransactionRequest;
import com.project.budgoal.entites.Savings;
import com.project.budgoal.entites.Transaction;
import com.project.budgoal.entites.Users;
import com.project.budgoal.enums.Roles;
import com.project.budgoal.enums.TransactionCatgory;
import com.project.budgoal.repository.SavingsRepo;
import com.project.budgoal.repository.TransactionsRepo;
import com.project.budgoal.repository.UserRepository;
import com.project.budgoal.response.ApiResponse;
import com.project.budgoal.response.SavingsResponse;
import com.project.budgoal.response.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class SavingsService {

    private final SavingsRepo savingsRepo;
    private final UserRepository userRepo;
    private final TransactionsRepo transactionsRepo;


    public ResponseEntity<ApiResponse<SavingsResponse>> createSavings(SavingsRequest request, Long userId) {

        var user = userRepo.findById(userId);


        if (user.isPresent()) {
            Users users = userRepo.findUsersById(userId);
            List<Users> usersList = new ArrayList<>();
            Savings savings = new Savings();

            savings.setTargetAmount(request.amount());
            users.setUserRoles(Roles.GROUP_OWNER);
            usersList.add(users);
            savings.setUsersList(usersList);
            savings.setTargetAmount(request.amount());
            savings.setStartDate(request.start());
            savings.setEndDate(request.endDate());
            savings.setSavingsName(request.name());
            savings.setSavingsCategory(request.category());
            savings.setCreatedDate(LocalDate.now());
            userRepo.save(users);
            savingsRepo.save(savings);
            Period duration = Period.between(request.endDate(), request.start());
            SavingsResponse savingsResponse = new SavingsResponse(savings.getSavingsName(), duration.getMonths() + " months", savings.getSavingsCategory(), savings.getSavingsMembers(), savings.getTargetAmount(), 0);
            ApiResponse<SavingsResponse> response = new ApiResponse<>(savings.getSavingsName() + " Savings created successfully", HttpStatus.CREATED, savingsResponse);
            return new ResponseEntity<>(response, response.getCode());
        } else {
            ApiResponse response = new ApiResponse<>("Kindly register to create a savings plan", HttpStatus.BAD_REQUEST);
            return new ResponseEntity<>(response, response.getCode());
        }

    }


    public ResponseEntity<ApiResponse<SavingsResponse>> addMembers(Long userId, Long newUserId, Long savingsId) {

        var user = userRepo.findById(userId);

        if (user.isPresent()) {
            var newUSer = userRepo.findUsersById(newUserId);
            var savngs = savingsRepo.findSavingsById(savingsId);
            List<Users> usersList = new ArrayList<>();
            if (!savngs.getUsersList().contains(newUSer)) {
                newUSer.setUserRoles(Roles.GROUP_MEMBER);
                usersList.add(newUSer);
                savngs.setUsersList(usersList);
                savngs.setSavingsMembers(usersList.size());
                userRepo.save(newUSer);
                savingsRepo.save(savngs);
                Period period = Period.between(savngs.getEndDate(), savngs.getStartDate());
                SavingsResponse savingsResponse = new SavingsResponse(savngs.getSavingsName(), period.getMonths() + " months", savngs.getSavingsCategory(), savngs.getSavingsMembers(), savngs.getTargetAmount(), savngs.getTransactions().size());
                ApiResponse response = new ApiResponse<>(newUSer.getNickName() + " has been added to the savings list", HttpStatus.OK, savingsResponse);
                return new ResponseEntity<>(response, response.getCode());
            } else {
                ApiResponse resp = new ApiResponse<>("User is on the savings list", HttpStatus.BAD_REQUEST);
                return new ResponseEntity<>(resp, resp.getCode());
            }
        } else {
            ApiResponse response = new ApiResponse<>("User is not registerd", HttpStatus.BAD_REQUEST);
            return new ResponseEntity<>(response, response.getCode());
        }
    }

    public ResponseEntity<ApiResponse<List<SavingsResponse>>> viewAllSavings(Long userId) {
        var user = userRepo.findById(userId);

        List<SavingsResponse> savingsResponseList = new ArrayList<>();
        if (user.isPresent()) {
            Users users = userRepo.findUsersById(userId);
            var savings = savingsRepo.findAll();
            for (Savings saving : savings) {
                if (saving.getUsersList().contains(users)) {
                    Period period = Period.between(saving.getEndDate(), saving.getStartDate());
                    SavingsResponse savingsResponse = new SavingsResponse(saving.getSavingsName(), period.getMonths() + " months", saving.getSavingsCategory(), saving.getSavingsMembers(), saving.getTargetAmount(), saving.getTransactions().size());
                    savingsResponseList.add(savingsResponse);
                }
            }

            if (savingsResponseList.isEmpty()) {
                ApiResponse apiResponse = new ApiResponse<>("This user has no savings", HttpStatus.BAD_REQUEST);
                return new ResponseEntity<>(apiResponse, apiResponse.getCode());
            }

            ApiResponse apiResponse = new ApiResponse<>("Here are your list of savings", HttpStatus.OK, savingsResponseList);
            return new ResponseEntity<>(apiResponse, apiResponse.getCode());
        }

        ApiResponse apiResponse = new ApiResponse<>("User not found", HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(apiResponse, apiResponse.getCode());
    }


    public ResponseEntity<ApiResponse<List<UserResponse>>> viewMembers(Long savingsId) {
        var savings = savingsRepo.findSavingsById(savingsId);
        List<UserResponse> userResponses = new ArrayList<>();
        if (savingsRepo.existsById(savingsId)) {
            List<Users> users = savings.getUsersList();
            for (Users users1 : users) {
                UserResponse userResponse = new UserResponse(users1.getFirstName() + users1.getLastName(), users1.getNickName());
                userResponses.add(userResponse);
            }
            ApiResponse apiResponse = new ApiResponse<>("The members of the Savings are below", HttpStatus.OK, userResponses);
            return new ResponseEntity<>(apiResponse, apiResponse.getCode());

        } else {
            ApiResponse apiResponse = new ApiResponse<>("Saving does not exist", HttpStatus.BAD_REQUEST, userResponses);
            return new ResponseEntity<>(apiResponse, apiResponse.getCode());
        }
    }





        public ResponseEntity<ApiResponse<SavingsResponse>> addTransaction (Long savingsId, TransactionRequest
        transactionRequest, Long userId){


            var user1 = userRepo.findById(userId);

            Transaction transaction = new Transaction();
            Map<String, Long> transactionList = new HashMap<>();

            var savings = savingsRepo.findSavingsById(savingsId);

            if (user1.isPresent()) {

                Users user = userRepo.findUsersById(userId);
                if (savings.getUsersList().contains(user)) {


                    transaction.setSavings(savings);
                    transaction.setAmount(transactionRequest.amount());
                    transaction.setCategory(TransactionCatgory.SAVINGS);
                    transactionList.put(user.getNickName(), transactionRequest.amount());
                    savings.setTransactions(transactionList);
                    transactionsRepo.save(transaction);
                    savingsRepo.save(savings);

                    Long remainder = savings.getTargetAmount() - transactionRequest.amount();

                    Period period = Period.between(savings.getEndDate(), savings.getStartDate());
                    SavingsResponse savingsResponse = new SavingsResponse(savings.getSavingsName(), period.getMonths() + " months", savings.getSavingsCategory(), savings.getUsersList().size(), savings.getTargetAmount(), savings.getTransactions().size());

                    ApiResponse apiResponse = new ApiResponse<>(user.getNickName() + " has added " + transactionRequest.amount() + " to " + savings.getSavingsName() + ". We have " + remainder + " to go", HttpStatus.OK, savingsResponse);
                    return new ResponseEntity<>(apiResponse, apiResponse.getCode());

                } else {
                    ApiResponse response = new ApiResponse<>("This user is not a member", HttpStatus.BAD_REQUEST);
                    return new ResponseEntity<>(response, response.getCode());
                }

            } else {

                ApiResponse resp = new ApiResponse<>("Kindly register to create savings", HttpStatus.BAD_REQUEST);

                return new ResponseEntity<>(resp, resp.getCode());
            }


        }


        public ResponseEntity<ApiResponse<Map<String, Long>>> viewTransaction (Long savingsId){


            var savins = savingsRepo.findById(savingsId);


            if (savins.isPresent()) {

                Savings savings = savingsRepo.findSavingsById(savingsId);
                Map<String, Long> transactionResponses = savings.getTransactions();
                ApiResponse apiResponse = new ApiResponse<>("The following are the transactions in " + savings.getSavingsName(), HttpStatus.OK, transactionResponses);
                return new ResponseEntity<>(apiResponse, apiResponse.getCode());
            } else {
                ApiResponse apiResponse = new ApiResponse<>("This savings does not exist", HttpStatus.BAD_REQUEST);
                return new ResponseEntity<>(apiResponse, apiResponse.getCode());
            }
        }

    }



