package AssClass;

import org.hibernate.SessionFactory;
import org.hibernate.SessionFactory.*;
import org.hibernate.cfg.AnnotationConfiguration;

import com.mysql.cj.Query;

import userloginmanage.*;
import Teacher.*;
import Student.*;
import AssClass.*;
import Subject.*;
import Class.*;
import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;
import java.util.*;

public class Manageassign_class {
    private static SessionFactory factory;

    public static List<assign_class> getassign_classfromclassID(int classID) {

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
            String hql = "SELECT E FROM assign_class E inner join E.subclass D WHERE D.id=:ID";
            org.hibernate.Query query = session.createQuery(hql);
            query.setParameter("ID", classID);
            List<assign_class> result = query.list();
            tx.commit();
            if (result.size() == 0)
                return null;
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

    public static List<assign_class> getassign_classstudentID(String studentID) {

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
            String hql = "SELECT E FROM assign_class E inner join E.student D WHERE D.id=:ID";
            org.hibernate.Query query = session.createQuery(hql);
            query.setParameter("ID", studentID);
            List<assign_class> result = query.list();
            tx.commit();
            if (result.size() == 0)
                return null;
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

    public static boolean deleteassign_class(int assign_classID, String studentID) {

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
            String sql = "DELETE assign_class FROM assign_class INNER JOIN class ON assign_class.class_id=class.id Where assign_class.stu_id='"
                    + studentID
                    + "'" + " AND assign_class.id='" + assign_classID + "'";
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

    public static boolean addnewassign_class(int classID, String studentID) throws HibernateException, Exception {
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
            student student = (student) session.get(student.class, studentID);
            subclass subclass = (subclass) session.get(subclass.class, classID);
            if (subclass == null || student == null) {
                System.err.println("either student or subclass is null");
                return false;
            }
            assign_class assign_class = new assign_class();
            assign_class.setstudent(student);
            assign_class.setsubclass(subclass);
            Integer subclassID = (Integer) session.save(assign_class);
            session.getTransaction().commit();
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

    public static boolean updatescore(float midterm, float finalscore, int classID, String studentID) {
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
            String sql = "UPDATE assign_class SET midterm=" + midterm + " , finalscore=" + finalscore
                    + " WHERE class_id=" + classID + " AND stu_id=" + studentID;
            System.out.println(sql);
            SQLQuery query = session.createSQLQuery(sql);
            int result = query.executeUpdate();
            tx.commit();
            if (result == 1)
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
