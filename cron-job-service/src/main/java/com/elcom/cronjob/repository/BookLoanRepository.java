package com.elcom.cronjob.repository;

import com.elcom.cronjob.model.BookLoan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookLoanRepository extends JpaRepository<BookLoan, String> {

    @Query(value = "SELECT b FROM BookLoan b WHERE b.borrow_date <= now() - INTERVAL '30 DAYS'", nativeQuery = true)
    public List<BookLoan> getBookLoanExpired();
}