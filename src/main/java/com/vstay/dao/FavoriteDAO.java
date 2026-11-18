package com.vstay.dao;

import com.vstay.model.Favorite;
import com.vstay.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class FavoriteDAO {

    public List<Favorite> findByUserId(Long userId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Favorite WHERE user.id = :userId", Favorite.class)
                    .setParameter("userId", userId)
                    .list();
        }
    }

    public Favorite findByUserAndHotel(Long userId, Long hotelId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Favorite WHERE user.id = :userId AND hotel.id = :hotelId", Favorite.class)
                    .setParameter("userId", userId)
                    .setParameter("hotelId", hotelId)
                    .uniqueResult();
        }
    }

    public Favorite save(Favorite favorite) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.persist(favorite);
            tx.commit();
            return favorite;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw e;
        }
    }

    public void delete(Long userId, Long hotelId) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            Favorite fav = findByUserAndHotel(userId, hotelId);
            if (fav != null) {
                fav = session.merge(fav);
                session.remove(fav);
            }
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw e;
        }
    }

    public boolean isFavorite(Long userId, Long hotelId) {
        return findByUserAndHotel(userId, hotelId) != null;
    }
}
