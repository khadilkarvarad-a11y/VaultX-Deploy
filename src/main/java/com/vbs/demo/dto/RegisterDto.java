package com.vbs.demo.dto;

import lombok.AllArgsConstructor;          //these 3 to create getter setter
import lombok.NoArgsConstructor;            //meths so vars in logindto class can be accessed
import lombok.Data;                               //by user controller file via using funcs

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterDto {
    private String name;
    private String username;
    private String email;
    private String password;
    private String role;
}
