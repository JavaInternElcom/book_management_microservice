package com.elcom.id.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentDTO implements Serializable {

    private String uuid;
    private String lastName;
    private String firstName;
    private String phoneNumber;
    private String email;
    private String address;
    private Integer gender;

}
