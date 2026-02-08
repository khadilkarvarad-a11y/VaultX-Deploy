package com.vbs.demo.dto;

import lombok.AllArgsConstructor;          //these 3 to create getter setter
import lombok.NoArgsConstructor;            //meths so vars in logindto class can be accessed
import lombok.Data;                               //by user controller file via using funcs

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginDto {

    String username;
    String password;
    String role;
}
