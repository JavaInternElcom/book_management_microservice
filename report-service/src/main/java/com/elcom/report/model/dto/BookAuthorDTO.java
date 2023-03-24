package com.elcom.report.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookAuthorDTO {

    private String authorId;
    private String firstName;
    private String lastName;
    private BigInteger total;
}