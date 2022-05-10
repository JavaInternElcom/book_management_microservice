package com.elcom.id.repository;

import com.elcom.id.messaging.rabbitmq.model.User;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityNotFoundException;
import javax.persistence.NoResultException;
import javax.persistence.Query;

@Repository
public class UserCustomizeRepository extends BaseRepository{

    private static final Logger LOGGER = LoggerFactory.getLogger(UserCustomizeRepository.class);

    @Autowired
    protected UserCustomizeRepository(EntityManagerFactory factory) {
        super(factory);
    }

    public User findByUuid(String uuid){
        Session session = openSession();
        try {
            User user = session.load(User.class, uuid);
            return user;
        } catch (EntityNotFoundException ex){
            LOGGER.error(ex.toString());
        } finally {
            closeSession(session);
        }
        return null;
    }

    public User findByUsername(String username){
        Session session = openSession();
        Object result = null;
        try {
            Query query = session.createNativeQuery("SELECT * FROM user WHERE username = ?", User.class);
            query.setParameter(1, username);
            result = query.getSingleResult();
        } catch (NoResultException ex){
            LOGGER.error(ex.toString());
        } finally {
            closeSession(session);
        }
        return result != null ? (User) result : null;
    }
}
