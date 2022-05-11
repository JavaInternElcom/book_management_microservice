package com.elcom.report.model.log;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.Date;

@Entity
@Table(name = "book_loan")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookLoan {

    @Id
    @Size(min = 1, max = 40)
    @Column(name = "uuid")
    private String uuid;

    @Column(name = "student_id")
    private String studentId;

    @Column(name = "book_id")
    private String bookId;

    @Column(name = "book_name")
    private String bookName;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "borrow_date")
    private Date borrowDate = new Date();

    private Integer status;//0: da tra, 1: chua tra
}