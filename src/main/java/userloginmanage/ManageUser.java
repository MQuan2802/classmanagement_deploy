package userloginmanage;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.Session;
import org.hibernate.Transaction;
import java.util.*;
import org.hibernate.HibernateException;
import org.hibernate.Query;

public class ManageUser {

    private static SessionFactory factory;

    public User getUserbyusername(String username) {
        User rturn = new User();
        try {
            factory = new AnnotationConfiguration().configure().addAnnotatedClass(User.class).
            // addPackage("com.xyz") //add package if used.
                    buildSessionFactory();
        } catch (Throwable ex) {
            System.err.println("Failed to create sessionFactory object." + ex);
            throw new ExceptionInInitializerError(ex);
        }
        Session session = factory.openSession();
        Transaction tx = null;

        try {
            tx = session.beginTransaction();
            String hql = "FROM User E WHERE E.username = :Uname";
            Query query = session.createQuery(hql);
            query.setParameter("Uname", username);
            List results = query.list();
            // List users = session.createQuery("FROM User").list();

            if (results.size() == 1) {
                rturn = (User) results.get(0);
            } else if (results.size() == 0) {
                System.out.println("can not find credential");
                return null;

            } else if (results.size() > 1)
                throw new HibernateException("found duplicate credential in database");
            tx.commit();

        } catch (HibernateException e) {
            if (tx != null)
                tx.rollback();
            rturn = null;
            e.printStackTrace();
        } finally {
            session.close();
        }
        return rturn;
    }

    public static String addUser(User user) throws Exception {
        try {
            factory = new AnnotationConfiguration().configure().addAnnotatedClass(User.class).buildSessionFactory();
        } catch (Throwable ex) {
            System.err.println("Failed to create sessionFactory object." + ex);
            throw new ExceptionInInitializerError(ex);
        }

        Session session = factory.openSession();
        Transaction tx = null;
        User rturn = new User();
        String userID;
        try {
            tx = session.beginTransaction();
            userID = (String) session.save(user);
            session.getTransaction().commit();

        } catch (HibernateException e) {
            if (tx != null)
                tx.rollback();
            e.printStackTrace();
            throw new Exception(e.getMessage());
        } finally {
            session.close();
        }
        return userID;
    }

    public static User getUserbyEmail(String email) {

        try {
            factory = new AnnotationConfiguration().configure().addAnnotatedClass(User.class).
            // addPackage("com.xyz") //add package if used.
                    buildSessionFactory();
        } catch (Throwable ex) {
            System.err.println("Failed to create sessionFactory object." + ex);
            throw new ExceptionInInitializerError(ex);
        }
        Session session = factory.openSession();
        Transaction tx = null;
        User rturn = new User();
        try {
            tx = session.beginTransaction();
            String hql = "FROM User E WHERE E.email = :Email";
            Query query = session.createQuery(hql);
            query.setParameter("Email", email);
            List results = query.list();
            // List users = session.createQuery("FROM User").list();
            if (results.size() == 1) {
                rturn = (User) results.get(0);
            } else if (results.size() == 0) {
                System.out.println("can not find credential");
                rturn = null;

            } else if (results.size() > 1)
                throw new HibernateException("found duplicate credential in database");
            tx.commit();
            return rturn;
        } catch (HibernateException e) {
            if (tx != null)
                tx.rollback();
            rturn = null;
            e.printStackTrace();

        } finally {
            session.close();
        }
        return rturn;

    }

    public static void verifyuser(String username) throws Exception {
        ManageUser manage = new ManageUser();
        User user = manage.getUserbyusername(username);
        if (user != null) {
            if (!user.getverify()) {
                try {
                    factory = new AnnotationConfiguration().configure().addAnnotatedClass(User.class).
                    // addPackage("com.xyz") //add package if used.
                            buildSessionFactory();
                } catch (Throwable ex) {
                    System.err.println("Failed to create sessionFactory object." + ex);
                    throw new ExceptionInInitializerError(ex);
                }

                Session session = factory.openSession();
                Transaction tx = null;
                User rturn = new User();
                try {
                    tx = session.beginTransaction();
                    String hql = "UPDATE User set verify = :Verify " +
                            "WHERE username = :Uname";

                    Query query = session.createQuery(hql);
                    query.setParameter("Uname", username);
                    query.setParameter("Verify", true);
                    int result = query.executeUpdate();
                    tx.commit();

                } catch (HibernateException e) {
                    if (tx != null)
                        tx.rollback();
                    e.printStackTrace();
                } finally {
                    session.close();
                }

            } else
                throw new Exception("account already verified");

        } else
            throw new Exception("no user available ");
    }

    public static void changepassword(MyUserDetail userDetail, String password) throws Exception {

        try {
            factory = new AnnotationConfiguration().configure().addAnnotatedClass(User.class).
            // addPackage("com.xyz") //add package if used.
                    buildSessionFactory();
        } catch (Throwable ex) {
            System.err.println("Failed to create sessionFactory object." + ex);
            throw new ExceptionInInitializerError(ex);
        }

        Session session = factory.openSession();
        Transaction tx = null;
        User rturn = new User();
        try {
            tx = session.beginTransaction();
            String hql = "UPDATE User set password = :Password " +
                    "WHERE username = :Uname";
            ;
            Query query = session.createQuery(hql);
            query.setParameter("Uname", userDetail.getUsername());
            query.setParameter("Password", password);
            tx.commit();

        } catch (HibernateException e) {
            if (tx != null)
                tx.rollback();
            e.printStackTrace();
            throw e;
        } finally {
            session.close();
        }

    }

}
