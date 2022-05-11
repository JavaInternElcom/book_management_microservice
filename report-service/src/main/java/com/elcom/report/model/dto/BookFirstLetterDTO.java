package com.elcom.report.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookFirstLetterDTO {

    private BigInteger total;
    private String firstLetter;
}