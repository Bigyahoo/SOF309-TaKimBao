package com.vstay.dao;

import com.vstay.model.Hotel;
import com.vstay.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

public class HotelDAO {

    public List<Hotel> findAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Hotel ORDER BY id", Hotel.class).list();
        }
    }

    public List<Hotel> findFeatured() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Hotel WHERE featured = true ORDER BY id", Hotel.class).list();
        }
    }

    public Hotel findById(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(Hotel.class, id);
        }
    }

    public List<Hotel> search(String city, Integer minPrice, Integer maxPrice, Integer stars, String category, String sort, int page, int pageSize) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            StringBuilder hql = new StringBuilder("FROM Hotel WHERE 1=1");

            if (city != null && !city.isEmpty()) {
                hql.append(" AND LOWER(city) LIKE :city");
            }
            if (minPrice != null) {
                hql.append(" AND price >= :minPrice");
            }
            if (maxPrice != null) {
                hql.append(" AND price <= :maxPrice");
            }
            if (stars != null) {
                hql.append(" AND stars = :stars");
            }
            if (category != null && !category.isEmpty()) {
                hql.append(" AND LOWER(category) LIKE :category");
            }

            // Sorting
            if ("price-asc".equals(sort)) {
                hql.append(" ORDER BY price ASC");
            } else if ("price-desc".equals(sort)) {
                hql.append(" ORDER BY price DESC");
            } else if ("rating".equals(sort)) {
                hql.append(" ORDER BY rating DESC");
            } else if ("discount".equals(sort)) {
                hql.append(" ORDER BY discountPercent DESC");
            } else {
                hql.append(" ORDER BY featured DESC, rating DESC");
            }

            Query<Hotel> query = session.createQuery(hql.toString(), Hotel.class);

            if (city != null && !city.isEmpty()) {
                query.setParameter("city", "%" + city.toLowerCase() + "%");
            }
            if (minPrice != null) {
                query.setParameter("minPrice", minPrice.longValue());
            }
            if (maxPrice != null) {
                query.setParameter("maxPrice", maxPrice.longValue());
            }
            if (stars != null) {
                query.setParameter("stars", stars);
            }
            if (category != null && !category.isEmpty()) {
                query.setParameter("category", "%" + category.toLowerCase() + "%");
            }

            query.setFirstResult((page - 1) * pageSize);
            query.setMaxResults(pageSize);

            return query.list();
        }
    }

    public long count(String city, Integer minPrice, Integer maxPrice, Integer stars, String category) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            StringBuilder hql = new StringBuilder("SELECT COUNT(*) FROM Hotel WHERE 1=1");

            if (city != null && !city.isEmpty()) {
                hql.append(" AND LOWER(city) LIKE :city");
            }
            if (minPrice != null) {
                hql.append(" AND price >= :minPrice");
            }
            if (maxPrice != null) {
                hql.append(" AND price <= :maxPrice");
            }
            if (stars != null) {
                hql.append(" AND stars = :stars");
            }
            if (category != null && !category.isEmpty()) {
                hql.append(" AND LOWER(category) LIKE :category");
            }

            Query<Long> query = session.createQuery(hql.toString(), Long.class);

            if (city != null && !city.isEmpty()) {
                query.setParameter("city", "%" + city.toLowerCase() + "%");
            }
            if (minPrice != null) {
                query.setParameter("minPrice", minPrice.longValue());
            }
            if (maxPrice != null) {
                query.setParameter("maxPrice", maxPrice.longValue());
            }
            if (stars != null) {
                query.setParameter("stars", stars);
            }
            if (category != null && !category.isEmpty()) {
                query.setParameter("category", "%" + category.toLowerCase() + "%");
            }

            return query.uniqueResult();
        }
    }

    public List<Hotel> findByCity(String city) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Hotel WHERE LOWER(city) LIKE :city", Hotel.class)
                    .setParameter("city", "%" + city.toLowerCase() + "%")
                    .list();
        }
    }

    public Hotel save(Hotel hotel) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.persist(hotel);
            tx.commit();
            return hotel;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw e;
        }
    }

    public Hotel update(Hotel hotel) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            hotel = session.merge(hotel);
            tx.commit();
            return hotel;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw e;
        }
    }

    public void delete(Long id) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            Hotel hotel = session.get(Hotel.class, id);
            if (hotel != null) {
                session.remove(hotel);
            }
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw e;
        }
    }

    public List<String> findDistinctCities() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("SELECT DISTINCT h.city FROM Hotel h ORDER BY h.city", String.class).list();
        }
    }
}
