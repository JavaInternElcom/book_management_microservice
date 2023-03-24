package com.elcom.report.repository.library;

import com.elcom.report.model.dto.BookAuthorDTO;
import com.elcom.report.model.dto.BookCategoryDTO;
import com.elcom.report.model.dto.BookFirstLetterDTO;
import com.elcom.report.repository.BaseRepository;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManagerFactory;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class BookCustomizeRepository extends BaseRepository {

    private Logger logger = LoggerFactory.getLogger(BookCustomizeRepository.class);

    protected BookCustomizeRepository(@Qualifier("mysqlEntityManagerFactory") EntityManagerFactory factory) {
        super(factory);
    }

    public List<BookAuthorDTO> statisticsByAuthor(){
        Session session = openSession();

        List<Object[]> result = new ArrayList<>();

        try {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("SELECT author_id, first_name, last_name, count(*) AS total FROM book\n" +
                    "JOIN author WHERE author.uuid = book.author_id \n" +
                    "GROUP BY author_id ");

            result = session.createNativeQuery(stringBuilder.toString()).getResultList();

            List<BookAuthorDTO> bookAuthorDTOS = result.stream()
                    .map(item -> {
                        BookAuthorDTO bookAuthorDTO = new BookAuthorDTO();
                        bookAuthorDTO.setAuthorId((String) item[0]);
                        bookAuthorDTO.setFirstName((String) item[1]);
                        bookAuthorDTO.setLastName((String) item[2]);
                        bookAuthorDTO.setTotal((BigInteger) item[3]);
                        return bookAuthorDTO;
                    })
                    .collect(Collectors.toList());

            return bookAuthorDTOS;
        } finally {
            closeSession(session);
        }
    }

    public List<BookCategoryDTO> statisticsByCategory(){
        Session session = openSession();

        List<Object[]> result = new ArrayList<>();

        try {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("SELECT category_id, c.name, count(*) AS total \n" +
                    "FROM book b JOIN category c\n" +
                    "WHERE b.category_id = c.uuid\n" +
                    "GROUP BY category_id ");

            result = session.createNativeQuery(stringBuilder.toString()).getResultList();

            List<BookCategoryDTO> bookCategoryDTOS = result.stream()
                    .map(item -> {
                        BookCategoryDTO bookCategoryDTO = new BookCategoryDTO();
                        bookCategoryDTO.setCategoryId((String) item[0]);
                        bookCategoryDTO.setCategoryName((String) item[1]);
                        bookCategoryDTO.setTotal((BigInteger) item[2]);
                        return bookCategoryDTO;
                    })
                    .collect(Collectors.toList());

            return bookCategoryDTOS;
        } finally {
            closeSession(session);
        }
    }

    public List<BookFirstLetterDTO> statisticsByFirstLetter(){
        Session session = openSession();

        List<Object[]> result = new ArrayList<>();

        try {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("SELECT first_letter, count(*) AS total FROM book GROUP BY first_letter ");

            result = session.createNativeQuery(stringBuilder.toString()).getResultList();

            List<BookFirstLetterDTO> bookFirstLetterDTOList = result.stream()
                    .map(item -> {
                        BookFirstLetterDTO bookFirstLetterDTO = new BookFirstLetterDTO();
                        bookFirstLetterDTO.setFirstLetter((String) item[0]);
                        bookFirstLetterDTO.setTotal((BigInteger) item[1]);
                        return bookFirstLetterDTO;
                    })
                    .collect(Collectors.toList());

            return bookFirstLetterDTOList;
        } finally {
            closeSession(session);
        }
    }

}