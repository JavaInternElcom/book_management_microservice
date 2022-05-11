package com.elcom.bookloan.service.log.impl;

import com.elcom.bookloan.model.log.BookLoan;
import com.elcom.bookloan.repository.log.BookLoanRepository;
import com.elcom.bookloan.service.log.BookLoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookLoanServiceImpl implements BookLoanService {

    @Autowired
    private BookLoanRepository bookLoanRepository;

    @Override
    public List<BookLoan> getAll() {
        return bookLoanRepository.getBookLoanByStatus();
    }

    @Override
    public BookLoan save(BookLoan bookLoan) {
        return bookLoanRepository.save(bookLoan);
    }

    @Override
    public BookLoan findBookLoanUnpaid(String uuid) {
        return bookLoanRepository.getBookLoanUnpaid(uuid);
    }

    @Override
    public List<BookLoan> findBookLoanExpired() {
        return bookLoanRepository.getBookLoanExpired();
    }

}