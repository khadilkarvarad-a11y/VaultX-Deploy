package com.vbs.demo.controller;

import com.vbs.demo.models.History;
import com.vbs.demo.models.Transaction;
import com.vbs.demo.models.User;
import com.vbs.demo.repositories.HistoryRepo;
import com.vbs.demo.repositories.TransactionRepo;
import com.vbs.demo.repositories.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins="*")
public class AdminController {

    @Autowired
    UserRepo userRepo;
    @Autowired
    TransactionRepo transactionRepo;
    @Autowired
    HistoryRepo historyRepo;

    @PostMapping("/add/{adminId}")
    public String add(@RequestBody User user,@PathVariable int adminId)
    {
        History h = new History();
        h.setDescription("Admin "+adminId+" added user "+user.getUsername());
        historyRepo.save(h);            //save history in history table

        userRepo.save(user);            //save new created user
        Transaction t = new Transaction();
        t.setAmount(user.getBalance());
        t.setCurrBalance(user.getBalance());
        t.setUserId(user.getId());
        t.setDescription("Rs " + user.getBalance() + " added to " + user.getUsername()+" by admin "+adminId);
        transactionRepo.save(t);
        return "Successful";
    }

    @GetMapping("/users")
    public List<User> getUsers(@RequestParam String sortBy,@RequestParam String order) {
        Sort s;

        if(order.equalsIgnoreCase("desc")) {
            s = Sort.by(sortBy).descending();
        }
        else {
            s= Sort.by(sortBy).ascending();
        }
        return userRepo.findAllByRole("customer",s);
    }

    @GetMapping("/users/{keyword}")
    public List<User> searchUsers(@PathVariable String keyword)
    {
        return userRepo.findByUsernameContainingIgnoreCaseAndRole(keyword,"customer");
    }

    @DeleteMapping("delete-user/{userId}/admin/{adminId}")
    public String delete(@PathVariable int adminId,@PathVariable int userId)
    {
        User user = userRepo.findById(userId).orElseThrow(()->new RuntimeException("user not found"));
        userRepo.deleteById(userId);
        History h=new History();
        h.setDescription("Admin "+adminId+" deleted user "+user.getUsername());
        historyRepo.save(h);
        return "user deleted successfully";
    }

    @GetMapping("/histories")
    public List<History> getHistories()
    {
        return historyRepo.findAll();
    }

}
