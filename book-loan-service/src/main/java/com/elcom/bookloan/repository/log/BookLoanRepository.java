package com.elcom.bookloan.repository.log;

import com.elcom.bookloan.model.log.BookLoan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookLoanRepository extends JpaRepository<BookLoan, String> {

    @Query("SELECT b FROM BookLoan b WHERE b.status = 1")
    public List<BookLoan> getBookLoanByStatus();

    @Query("SELECT b FROM BookLoan b WHERE b.uuid = ?1 AND b.status = 1")
    public BookLoan getBookLoanUnpaid(String uuid);

    @Query(value = "SELECT b FROM BookLoan b WHERE b.borrow_date <= now() - INTERVAL '30 DAYS'", nativeQuery = true)
    public List<BookLoan> getBookLoanExpired();
}