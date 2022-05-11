package com.elcom.bookloan.service.log;

import com.elcom.bookloan.model.log.BookLoan;

import java.util.List;

public interface BookLoanService {

    List<BookLoan> getAll();

    BookLoan save(BookLoan bookLoan);

    BookLoan findBookLoanUnpaid(String uuid);

    List<BookLoan> findBookLoanExpired();

}