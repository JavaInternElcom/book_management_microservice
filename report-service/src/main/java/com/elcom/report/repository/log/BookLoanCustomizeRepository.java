package com.elcom.report.repository.log;

import com.elcom.report.model.dto.BookLoanExpiredDTO;
import com.elcom.report.repository.BaseRepository;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManagerFactory;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class BookLoanCustomizeRepository extends BaseRepository {

    private Logger logger = LoggerFactory.getLogger(BookLoanCustomizeRepository.class);

    protected BookLoanCustomizeRepository(@Qualifier("entityManagerFactory") EntityManagerFactory factory) {
        super(factory);
    }

    public BigInteger countBookBorrow(Date fromDate, Date toDate){
        Session session = openSession();

        BigInteger result;
        try {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("SELECT count(*) FROM book_loan WHERE borrow_date >= '")
                    .append(fromDate).append("' AND borrow_date <= '").append(toDate).append("'");

            result = (BigInteger) session.createNativeQuery(stringBuilder.toString()).getSingleResult();

            return result;
        } finally {
            closeSession(session);
        }
    }

    public List<Object[]> maxBookNameBorrow(Date fromDate, Date toDate){
        Session session = openSession();

        List<Object[]> result;
        try {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("SELECT book_name FROM book_loan ")
                    .append(" WHERE borrow_date >= '").append(fromDate)
                    .append("' AND borrow_date <= '").append(toDate)
                    .append("' GROUP BY book_name HAVING count(*) = ")
                    .append(" (SELECT MAX(total) FROM (SELECT count(*) as total FROM book_loan ")
                    .append(" WHERE borrow_date >= '").append(fromDate)
                    .append("' AND borrow_date <= '").append(toDate)
                    .append("' GROUP BY book_name) as b)");

            result = session.createNativeQuery(stringBuilder.toString()).getResultList();
            return result;
        } finally {
            closeSession(session);
        }
    }

    public List<BookLoanExpiredDTO> getAllBookLoanExpired(){
        Session session = openSession();
        List<Object[]> result = null;
        try {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("SELECT book_id, student_id, book_name FROM book_loan")
                    .append(" WHERE borrow_date <= NOW() - INTERVAL '30 DAYS'");

            result = session.createNativeQuery(stringBuilder.toString()).getResultList();

            List<BookLoanExpiredDTO> bookLoanExpiredDTOs = result.stream()
                    .map(item -> {
                        BookLoanExpiredDTO bookLoanExpiredDTO = new BookLoanExpiredDTO();
                        bookLoanExpiredDTO.setBookId((String) item[0]);
                        bookLoanExpiredDTO.setStudentId((String) item[1]);
                        bookLoanExpiredDTO.setBookName((String) item[2]);
                        return bookLoanExpiredDTO;
                    })
                    .collect(Collectors.toList());

            return bookLoanExpiredDTOs;
        } finally {
            closeSession(session);
        }
    }

}