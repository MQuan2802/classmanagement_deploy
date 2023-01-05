package Class;

import org.hibernate.SessionFactory;
import org.hibernate.SessionFactory.*;
import org.hibernate.cfg.AnnotationConfiguration;

import com.mysql.cj.Query;
import org.hibernate.SQLQuery;
import userloginmanage.*;
import Teacher.*;
import Student.*;
import AssClass.*;
import Subject.*;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import java.util.*;

public class Manageclass {
    private static SessionFactory factory;

    public static void addnewclass(int subjectID, String teacherID) throws HibernateException, Exception {
        try {
            factory = new AnnotationConfiguration().configure()
                    .addAnnotatedClass(User.class)
                    .addAnnotatedClass(teacher.class)
                    .addAnnotatedClass(student.class)
                    .addAnnotatedClass(subclass.class)
                    .addAnnotatedClass(assign_class.class)
                    .addAnnotatedClass(subject.class)
                    .buildSessionFactory();
        } catch (Throwable ex) {
            System.err.println("Failed to create sessionFactory object." + ex);
            throw new ExceptionInInitializerError(ex);
        }

        Session session = factory.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            subject updatesubject = (subject) session.get(subject.class, subjectID);
            teacher teacher = (teacher) session.get(teacher.class, teacherID);
            // student student = (student) session.get(student.class, studentID);
            // assign_class assign_class = new assign_class();
            subclass subclass = new subclass();
            Set<subclass> setsubclass = new HashSet<>();
            setsubclass.add(subclass);
            teacher.setsubclasses(setsubclass);
            session.update(teacher);
            subclass.setteachertoclass(teacher);
            subclass.setsubject(updatesubject);
            updatesubject.setsubclasses(setsubclass);
            session.update(updatesubject);
            Integer subclassID = (Integer) session.save(subclass);
            session.getTransaction().commit();

        } catch (HibernateException e) {
            if (tx != null)
                tx.rollback();
            e.printStackTrace();
            throw new Exception(e.getMessage());
        } finally {
            session.close();
        }

    }

    public static List getopenedclasses(String teacherID) {
        List<subclass> subclasses = null;
        try {
            factory = new AnnotationConfiguration().configure()
                    .addAnnotatedClass(User.class)
                    .addAnnotatedClass(teacher.class)
                    .addAnnotatedClass(student.class)
                    .addAnnotatedClass(subclass.class)
                    .addAnnotatedClass(assign_class.class)
                    .addAnnotatedClass(subject.class)
                    .buildSessionFactory();
        } catch (Throwable ex) {
            System.err.println("Failed to create sessionFactory object." + ex);
            // throw new ExceptionInInitializerError(ex);

            return null;
        }

        Session session = factory.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            String hql = "FROM subclass";
            org.hibernate.Query query = session.createQuery(hql);
            List results = (List<subclass>) query.list();
            if (results.size() != 0)
                subclasses = results;
            tx.commit();
            return subclasses;

        } catch (HibernateException e) {
            if (tx != null)
                tx.rollback();
            e.printStackTrace();
            return null;
        } finally {
            session.close();
        }

    }

    public static boolean deleteclass(int classID, String teacherID) {

        try {
            factory = new AnnotationConfiguration().configure()
                    .addAnnotatedClass(User.class)
                    .addAnnotatedClass(teacher.class)
                    .addAnnotatedClass(student.class)
                    .addAnnotatedClass(subclass.class)
                    .addAnnotatedClass(assign_class.class)
                    .addAnnotatedClass(subject.class)
                    .buildSessionFactory();
        } catch (Throwable ex) {
            System.err.println("Failed to create sessionFactory object." + ex);
            return false;
        }

        Session session = factory.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            String sql = "DELETE class FROM class INNER JOIN user ON class.teacherid=user.user_id Where class.teacherid='"
                    + teacherID
                    + "'" + " AND class.id='" + classID + "'";
            System.out.println(sql);
            SQLQuery query = session.createSQLQuery(sql);
            int result = query.executeUpdate();
            tx.commit();
            if (result == 0)
                return false;
            return true;
        } catch (HibernateException e) {
            if (tx != null)
                tx.rollback();
            e.printStackTrace();
            return false;
        } finally {
            session.close();
        }

    }

    public static subclass getclassfromclassID(int classID) {

        try {
            factory = new AnnotationConfiguration().configure()
                    .addAnnotatedClass(User.class)
                    .addAnnotatedClass(teacher.class)
                    .addAnnotatedClass(student.class)
                    .addAnnotatedClass(subclass.class)
                    .addAnnotatedClass(assign_class.class)
                    .addAnnotatedClass(subject.class)
                    .buildSessionFactory();
        } catch (Throwable ex) {
            System.err.println("Failed to create sessionFactory object." + ex);
            return null;
        }

        Session session = factory.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            String hql = "FROM subclass E WHERE E.id =:ID";
            org.hibernate.Query query = session.createQuery(hql);
            query.setParameter("ID", classID);
            subclass result = (subclass) query.uniqueResult();
            tx.commit();
            return result;
        } catch (HibernateException e) {
            if (tx != null)
                tx.rollback();
            e.printStackTrace();
            return null;
        } finally {
            session.close();
        }

    }

    public static List getallclass() {
        List<subclass> subclasses = null;
        try {
            factory = new AnnotationConfiguration().configure()
                    .addAnnotatedClass(User.class)
                    .addAnnotatedClass(teacher.class)
                    .addAnnotatedClass(student.class)
                    .addAnnotatedClass(subclass.class)
                    .addAnnotatedClass(assign_class.class)
                    .addAnnotatedClass(subject.class)
                    .buildSessionFactory();
        } catch (Throwable ex) {
            System.err.println("Failed to create sessionFactory object." + ex);
            return null;
        }

        Session session = factory.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            String hql = "FROM subclass ";
            org.hibernate.Query query = session.createQuery(hql);
            List<subclass> result = query.list();
            if (result.size() == 0)
                return null;
            tx.commit();
            return result;
        } catch (HibernateException e) {
            if (tx != null)
                tx.rollback();
            e.printStackTrace();
            return null;
        } finally {
            session.close();
        }

    }

    public static boolean checkteacherandclass(String teacherID, int class_id) {
        try {
            factory = new AnnotationConfiguration().configure()
                    .addAnnotatedClass(User.class)
                    .addAnnotatedClass(teacher.class)
                    .addAnnotatedClass(student.class)
                    .addAnnotatedClass(subclass.class)
                    .addAnnotatedClass(assign_class.class)
                    .addAnnotatedClass(subject.class)
                    .buildSessionFactory();
        } catch (Throwable ex) {
            System.err.println("Failed to create sessionFactory object." + ex);
            return false;
        }

        Session session = factory.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            String sql = "SELECT teacherid FROM class Where id=" + class_id;
            System.out.println(sql);
            SQLQuery query = session.createSQLQuery(sql);
            String result = (String) query.uniqueResult();
            tx.commit();
            if (teacherID.compareTo(result) == 0)
                return true;
            return false;
        } catch (HibernateException e) {
            if (tx != null)
                tx.rollback();
            e.printStackTrace();
            return false;
        } finally {
            session.close();
        }
    }
}
