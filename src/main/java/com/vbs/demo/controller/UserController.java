package com.vbs.demo.controller;

import com.vbs.demo.dto.DisplayDto;
import com.vbs.demo.dto.LoginDto;
import com.vbs.demo.models.User;
import com.vbs.demo.repositories.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins="*")
public class UserController {
    @Autowired
    UserRepo userRepo;                       //we made obj of user repo outside any func so all funcs can use it


    @PostMapping("/register")      //first func for register feature i.e /regirste

    public String register(@RequestBody RegisterDto dto)
    {
    User user = new User();
    user.setName(dto.getName());
    user.setUsername(dto.getUsername());
    user.setEmail(dto.getEmail());
    user.setPassword(dto.getPassword());
    user.setRole(dto.getRole());
    user.setBalance(0.0);

    userRepo.save(user);
    return "Successful";
    }

    @PostMapping("/login")                         //func for login feature
    public String login(@RequestBody LoginDto u)
    {
        User user=userRepo.findByUsername(u.getUsername());
        if(user==null)
        {
            return "User Not Found";
        }
        if(!user.getPassword().equals(u.getPassword()))
        {
            return "Wrong Password";
        }
        if(!user.getRole().equals(u.getRole()))
        {
            return "Wrong Role";
        }
        return (String.valueOf(user.getId()));
    }

    @GetMapping("/get-details/{id}")
    public DisplayDto display(@PathVariable int id)
    {
        User user=userRepo.findById(id)
                .orElseThrow(()->new RuntimeException("User Not Found"));
        DisplayDto d=new DisplayDto();
        d.setUsername(user.getUsername());
        d.setBalance(user.getBalance());
        return d;
    }


}
