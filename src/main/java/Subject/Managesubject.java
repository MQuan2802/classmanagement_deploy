package Subject;

import java.util.*;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.mapping.Subclass;
import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.Session;
import org.hibernate.Transaction;
import java.util.*;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import userloginmanage.User;
import Class.subclass;
import Teacher.*;
import Student.*;
import AssClass.*;

public class Managesubject {
    private static SessionFactory factory;

    public static List getsubjectlist() {
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
        List rturn = new ArrayList<>();
        try {
            tx = session.beginTransaction();
            String hql = "FROM subject";
            Query query = session.createQuery(hql);
            List results = (List<subject>) query.list();
            rturn = results;
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
}
