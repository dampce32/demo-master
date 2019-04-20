package org.demo.lombok.n0001;

import java.util.Date;

import lombok.Data;

@Data
public class User {

    private int id;
    
    private String name;
    
    private String password;
    
    private Date birthday;
    
}
