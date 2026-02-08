package com.vbs.demo.controller;

import com.vbs.demo.dto.TransactionDto;
import com.vbs.demo.dto.TransferDto;
import com.vbs.demo.dto.UpdateDto;
import com.vbs.demo.models.Transaction;
import com.vbs.demo.models.User;
import com.vbs.demo.repositories.TransactionRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.vbs.demo.repositories.UserRepo;

import java.util.List;

@RestController
@CrossOrigin(origins="*")
public class TransactionController {

    @Autowired
    UserRepo userRepo;
    @Autowired
    TransactionRepo transactionRepo;

    @PostMapping("/deposit")
    public String deposit(@RequestBody TransactionDto obj)
    {
        User user=userRepo.findById(obj.getId()).orElseThrow(()->new RuntimeException("not found"));
        double newBal=obj.getAmount()+user.getBalance();
        user.setBalance(newBal);
        userRepo.save(user);

        Transaction t=new Transaction();
        t.setAmount(obj.getAmount());
        t.setCurrBalance(newBal);
        t.setUserId(user.getId());
        t.setDescription("Rs "+obj.getAmount()+" successfully deposited");
        transactionRepo.save(t);
        return "successful deposit";
    }

    @PostMapping("/withdraw")
    public String withdraw(@RequestBody TransactionDto obj)
    {
        User user=userRepo.findById(obj.getId()).orElseThrow(()->new RuntimeException("not found"));
        double newBal=user.getBalance()-obj.getAmount();
        if(newBal<0)
        {
            return ("Balance insufficient");
        }
        user.setBalance(newBal);
        userRepo.save(user);

        Transaction t=new Transaction();
        t.setAmount(obj.getAmount());
        t.setCurrBalance(newBal);
        t.setUserId(user.getId());
        t.setDescription("Rs "+obj.getAmount()+" successfully withdrawal");
        transactionRepo.save(t);
        return "successful withdrawal";
    }

    @PostMapping("/transfer")
    public String transfer(@RequestBody TransferDto obj) {
        User r = userRepo.findByUsername(obj.getUsername());
        User s = userRepo.findById(obj.getId()).orElseThrow(() -> new RuntimeException("not found"));
        if (s.getBalance() < obj.getAmount()) {
            return ("Insufficient balance");
        }
        if (s.getId() == r.getId()) {
            return ("cant do self transfer");
        }
        if (obj.getAmount() < 1) {
            return ("cant send less than 1");
        }
        double bal1 = s.getBalance() - obj.getAmount();
        double bal2 = r.getBalance() + obj.getAmount();
        s.setBalance(bal1);
        r.setBalance(bal2);
        userRepo.save(r);
        userRepo.save(s);

        Transaction t1 = new Transaction();
        t1.setAmount(obj.getAmount());
        t1.setCurrBalance(bal1);
        t1.setUserId(s.getId());
        t1.setDescription("Rs " + obj.getAmount() + " sent to " + r.getUsername());
        transactionRepo.save(t1);

        Transaction t2 = new Transaction();
        t2.setAmount(obj.getAmount());
        t2.setCurrBalance(bal2);
        t2.setUserId(r.getId());
        t2.setDescription("Rs " + obj.getAmount() + " received from " + s.getUsername());
        transactionRepo.save(t2);


        return ("successfully transferred");
    }

    @GetMapping("/passbook/{id}")
    public List<Transaction> gettrans(@PathVariable int id)
    {
        return transactionRepo.findAllByUserId(id);
    }

    @PostMapping("/update")
    public String update(@RequestBody UpdateDto obj)
    {
        User u=userRepo.findById(obj.getId()).orElseThrow(() -> new RuntimeException("not found"));

        if("name".equals(obj.getKey()))
        {
            u.setUsername(obj.getValue());
        }
        else if ("email".equals(obj.getKey()))
        {
            User u2=userRepo.findByEmail(obj.getValue());
            if(u2!=null)
            {
                return "email already exists";
            }
            u.setEmail(obj.getValue());
        }
        else
        {
            u.setPassword(obj.getValue());
        }
        userRepo.save(u);
        return "successfully updated";
    }

}


