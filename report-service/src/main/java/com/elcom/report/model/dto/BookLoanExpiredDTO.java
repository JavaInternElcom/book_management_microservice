package com.elcom.report.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookLoanExpiredDTO {

    private String bookId;
    private String bookName;
    private String studentId;
}