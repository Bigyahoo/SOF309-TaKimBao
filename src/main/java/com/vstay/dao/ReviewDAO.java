package com.vstay.dao;

import com.vstay.model.Review;
import com.vstay.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class ReviewDAO {

    public List<Review> findByHotelId(Long hotelId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Review WHERE hotel.id = :hotelId ORDER BY createdAt DESC", Review.class)
                    .setParameter("hotelId", hotelId)
                    .list();
        }
    }

    public Review save(Review review) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.persist(review);
            tx.commit();
            return review;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw e;
        }
    }

    public Double getAverageRating(Long hotelId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Double avg = session.createQuery("SELECT AVG(r.rating) FROM Review r WHERE r.hotel.id = :hotelId", Double.class)
                    .setParameter("hotelId", hotelId)
                    .uniqueResult();
            return avg != null ? avg : 0.0;
        }
    }
}
