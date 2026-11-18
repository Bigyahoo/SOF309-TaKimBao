package com.vstay.dao;

import com.vstay.model.Booking;
import com.vstay.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class BookingDAO {

    public Booking findById(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(Booking.class, id);
        }
    }

    public Booking findByBookingCode(String code) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Booking WHERE bookingCode = :code", Booking.class)
                    .setParameter("code", code)
                    .uniqueResult();
        }
    }

    public List<Booking> findByUserId(Long userId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Booking WHERE user.id = :userId ORDER BY createdAt DESC", Booking.class)
                    .setParameter("userId", userId)
                    .list();
        }
    }

    public Booking save(Booking booking) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.persist(booking);
            tx.commit();
            return booking;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw e;
        }
    }

    public Booking update(Booking booking) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            booking = session.merge(booking);
            tx.commit();
            return booking;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw e;
        }
    }

    public List<Booking> findAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Booking ORDER BY createdAt DESC", Booking.class).list();
        }
    }

    public long countAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("SELECT COUNT(*) FROM Booking", Long.class).uniqueResult();
        }
    }
}
