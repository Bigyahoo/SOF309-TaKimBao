package com.poly.dao;

import com.poly.model.Contact;
import com.poly.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class ContactDAO {

    public Contact save(Contact contact) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.persist(contact);
            tx.commit();
            return contact;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw e;
        }
    }

    public List<Contact> findAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Contact ORDER BY createdAt DESC", Contact.class).list();
        }
    }
}
