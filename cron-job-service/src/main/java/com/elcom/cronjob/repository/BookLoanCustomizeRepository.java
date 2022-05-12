package com.elcom.cronjob.repository;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManagerFactory;
import java.util.List;

@Repository
public class BookLoanCustomizeRepository {

    private Logger logger = LoggerFactory.getLogger(BookLoanCustomizeRepository.class);

    private SessionFactory sessionFactory;

    @Autowired
    public BookLoanCustomizeRepository(EntityManagerFactory factory) {
        if(factory.unwrap(SessionFactory.class) == null)
            throw new NullPointerException("Factory is not a hibernate factory!");
        this.sessionFactory = factory.unwrap(SessionFactory.class);
    }

    public List<Object[]> getBookLoanExpired(){
        Session session = openSession();

        List<Object[]> result;
        try {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("SELECT b.book_name FROM book_loan b")
                    .append(" WHERE b.borrow_date <= now() - INTERVAL '30 DAYS'");

            result = session.createNativeQuery(stringBuilder.toString()).getResultList();
            return result;
        } finally {
            closeSession(session);
        }

    }

    private Session openSession() {
        Session session = this.sessionFactory.openSession();
        return session;
    }

    private void closeSession(Session session) {
        if( session.isOpen() ) {
            session.disconnect();
            session.close();
        }
    }
}
