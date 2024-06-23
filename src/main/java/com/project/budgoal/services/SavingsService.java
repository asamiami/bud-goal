package com.project.budgoal.services;

import com.project.budgoal.dtos.request.SavingsRequest;
import com.project.budgoal.dtos.request.TransactionRequest;
import com.project.budgoal.entites.Savings;
import com.project.budgoal.entites.Users;
import com.project.budgoal.enums.Roles;
import com.project.budgoal.repository.SavingsRepo;
import com.project.budgoal.repository.UserRepository;
import com.project.budgoal.dtos.response.ApiResponse;
import com.project.budgoal.dtos.response.SavingsResponse;
import com.project.budgoal.dtos.response.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class SavingsService {

    private final SavingsRepo savingsRepo;
    private final UserRepository userRepo;




    public ApiResponse<SavingsResponse> createSavings(SavingsRequest request, Long userId) {

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
            return  new ApiResponse<>(savings.getSavingsName() + " Savings created successfully", HttpStatus.CREATED, savingsResponse);

        } else {
            return new ApiResponse<>("Kindly register to create a savings plan", HttpStatus.BAD_REQUEST);

        }

    }



    public ApiResponse<List<SavingsResponse>> addMembers(Long userId, Long newUserId, Long savingsId) {

        var user = userRepo.findById(userId);

        List<SavingsResponse> listOfMembers = new ArrayList<>();
        if (user.isPresent() && userRepo.existsById(newUserId)) {
            var newUser = userRepo.findUsersById(newUserId);
            var savngs = savingsRepo.findSavingsById(savingsId);
            List<Users> usersList = new ArrayList<>();
            if (!savngs.getUsersList().contains(newUser)) {

                newUser.setUserRoles(Roles.GROUP_MEMBER);

                savngs.addUsersToUserList(newUser);

                savngs.setSavingsMembers(usersList.size());
                userRepo.save(newUser);
                savingsRepo.save(savngs);
                Period period = Period.between(savngs.getEndDate(), savngs.getStartDate());
                SavingsResponse savingsResponse = new SavingsResponse(savngs.getSavingsName(), period.getMonths() + " months", savngs.getSavingsCategory(), savngs.getSavingsMembers(), savngs.getTargetAmount(), savngs.getTransactions().size());
                listOfMembers.add(savingsResponse);
                return new ApiResponse<>(newUser.getNickName() + " has been added to the savings list", HttpStatus.OK, listOfMembers);

            } else {
                return new ApiResponse<>("User is on the savings list", HttpStatus.BAD_REQUEST);

            }
        } else {
            return new ApiResponse<>("User is not registerd", HttpStatus.BAD_REQUEST);

        }
    }

    public ApiResponse<List<SavingsResponse>> viewAllSavings(Long userId) {
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
                return new ApiResponse<>("This user has no savings", HttpStatus.BAD_REQUEST);

            }

            return new ApiResponse<>("Here are your list of savings", HttpStatus.OK, savingsResponseList);

        }

        return new ApiResponse<>("User not found", HttpStatus.BAD_REQUEST);

    }


    public ApiResponse<List<UserResponse>> viewMembers(Long savingsId) {
        var savings = savingsRepo.findSavingsById(savingsId);
        List<UserResponse> userResponses = new ArrayList<>();
        if (savingsRepo.existsById(savingsId)) {
            List<Users> users = savings.getUsersList();
            for (Users users1 : users) {
                UserResponse userResponse = new UserResponse(users1.getFirstName() + " " + users1.getLastName(), users1.getNickName());
                userResponses.add(userResponse);
            }
            return new ApiResponse<>("The members of the Savings are below", HttpStatus.OK, userResponses);


        } else {
            return new ApiResponse<>("Saving does not exist", HttpStatus.BAD_REQUEST, userResponses);

        }
    }





        public ApiResponse<SavingsResponse> addTransaction (Long savingsId, TransactionRequest transactionRequest, Long userId){

            var user1 = userRepo.findById(userId);
            var savings = savingsRepo.findSavingsById(savingsId);

            if (user1.isPresent()) {

                Users user = userRepo.findUsersById(userId);
                if (savings.getUsersList().contains(user)) {

                    savings.addTransactionToTransactionsList(user.getNickName(), transactionRequest.amount());
                    savingsRepo.save(savings);

                    long remainder = savings.getTargetAmount() - transactionRequest.amount();

                    Period period = Period.between(savings.getEndDate(), savings.getStartDate());
                    SavingsResponse savingsResponse = new SavingsResponse(savings.getSavingsName(), period.getMonths() + " months", savings.getSavingsCategory(), savings.getUsersList().size(), savings.getTargetAmount(), savings.getTransactions().size());

                    return new ApiResponse<>(user.getNickName() + " has added " + transactionRequest.amount() + " to " + savings.getSavingsName() + ". We have " + remainder + " to go", HttpStatus.OK, savingsResponse);


                } else {
                    return new ApiResponse<>("This user is not a member", HttpStatus.BAD_REQUEST);
                }

            } else {

                return new ApiResponse<>("Kindly register to create savings", HttpStatus.BAD_REQUEST);
            }


        }


        public ApiResponse<Map<String, Long>> viewTransaction (Long savingsId){
            var savins = savingsRepo.findById(savingsId);

            if (savins.isPresent()) {

                Savings savings = savingsRepo.findSavingsById(savingsId);
                Map<String, Long> transactionResponses = savings.getTransactions();
                return new ApiResponse<>("The following are the transactions in " + savings.getSavingsName(), HttpStatus.OK, transactionResponses);

            } else {
                return new ApiResponse<>("This savings does not exist", HttpStatus.BAD_REQUEST);

            }
        }

    }



