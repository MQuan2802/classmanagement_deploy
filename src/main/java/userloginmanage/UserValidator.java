package userloginmanage;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.mail.internet.*;
import java.util.Calendar;
import org.hibernate.SessionFactory;
import org.hibernate.HibernateException;
import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.Query;
import java.util.*;

public class UserValidator implements Validator {

    private static SessionFactory factory;

    @Override
    public boolean supports(Class<?> paramClass) {
        return confirmpassword.class.equals(paramClass);
    }

    @Override
    public void validate(Object obj, Errors errors) {

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "email", "null email");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "fullname", "null fullname");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "id", "null id");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "username", "null username");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "dateofbirth", "null dateofbirth");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "null password");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "confirmpassword", "null confirm password");
        rejectId(obj, errors);
        rejectEmail(obj, errors);
        rejectdateofbirth(obj, errors);
        rejectfullname(obj, errors);
        rejectusername(obj, errors);
        rejectconfirmpassword(obj, errors);
        rejectpassword(obj, errors);
    }

    public static void rejectpassword(Object obj, Errors errors) {
        User user = (User) obj;
        String pass = user.getPassword();
        System.out.println(pass);
        Pattern pattern1 = Pattern
                .compile("^[A-Za-z0-9\\^\\-\\]\\.\\$\\*\\+\\?\\(\\)\\[\\{\\}\\|\\_\\/\\~`@%&:\"';,\\\\]+$");
        Pattern pattern2 = Pattern
                .compile("[\\^\\-\\]\\.\\$\\*\\+\\?\\(\\)\\[\\]\\{\\}\\|\\_\\/\\~`@%&:\"';,\\\\]{1,}");
        Pattern pattern3 = Pattern.compile("[A-Z]+");
        Pattern pattern4 = Pattern.compile("[0-9]+");
        int newpasssize = pass.length();
        Matcher matcher1 = pattern1.matcher(pass);
        Matcher matcher2 = pattern2.matcher(pass);
        Matcher matcher3 = pattern3.matcher(pass);
        Matcher matcher4 = pattern4.matcher(pass);
        boolean matchFound1 = matcher1.find();
        boolean matchFound2 = matcher2.find();
        boolean matchFound3 = matcher3.find();
        boolean matchFound4 = matcher4.find();
        if (!matchFound1)
            errors.rejectValue("password", "only alphabet , numeric and special characters are allowed");
        if (!matchFound2)
            errors.rejectValue("password", "must contain at least 1 special character and uppercase");
        if (!matchFound3)
            errors.rejectValue("password", "must contain upper case");
        if (!matchFound4)
            errors.rejectValue("password", "must contain numeric");
        if (newpasssize < 8)
            errors.rejectValue("password", "new password must have 8 characters at least");

    }

    public static void rejectconfirmpassword(Object obj, Errors errors) {

        confirmpassword confirm = (confirmpassword) obj;
        if (!(confirm.getPassword().compareTo(confirm.getconfirmpassword()) == 0))
            errors.rejectValue("password", "confirm password not match");

    }

    public static void rejectusername(Object obj, Errors errors) {

        User user = (User) obj;
        rejectduplicate("username", obj, errors, "username", user.getUsername());

    }

    public static void rejectId(

            Object obj, Errors errors) {
        User user = (User) obj;
        Pattern pattern = Pattern.compile("[0-9]{12}");
        Matcher matcher = pattern.matcher(user.getid());
        boolean matchFound = matcher.find();
        if (!matchFound || user.getid().length() > 12) {
            errors.rejectValue("id", "insufficient Id value");
        } else {

            rejectduplicate("id", obj, errors, "id", user.getid());
        }
    }

    public static void rejectEmail(Object obj, Errors errors) {
        try {
            User user = (User) obj;

            InternetAddress emailAddr = new InternetAddress(user.getemail());
            emailAddr.validate();
            rejectduplicate("email", obj, errors, "email", user.getemail());

        } catch (

        Exception e) {
            errors.rejectValue("email", e.getMessage());
        }
    }

    public static void rejectdateofbirth(Object obj, Errors errors) {
        try {
            User user = (User) obj;
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(user.getdateofbirth());
            int year = calendar.get(Calendar.YEAR);

            if ((Calendar.getInstance().get(Calendar.YEAR) - year) < 15
                    || (Calendar.getInstance().get(Calendar.YEAR) - year) > 25) {
                errors.rejectValue("dateofbirth", "insufficient age");
            }
        } catch (Exception e) {
            errors.rejectValue("dateofbirth", e.getMessage());
        }

    }

    public static void rejectfullname(Object obj, Errors errors) {

        User user = (User) obj;
        Pattern pattern = Pattern.compile("^[A-Za-z ]+$");
        Matcher matcher = pattern.matcher(user.getfullname());
        boolean matchFound = matcher.find();
        if (!matchFound) {
            errors.rejectValue("fullname", "insufficient fullname");
        }

    }

    public static void rejectduplicate(String errorfield, Object obj, Errors errors, String userfield,
            String userfieldvalue) {
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
            String hql = "FROM User E WHERE E." + userfield + " = :Value";
            Query query = session.createQuery(hql);
            query.setParameter("Value", userfieldvalue);
            List results = query.list();
            // List users = session.createQuery("FROM User").list();

            if (results.size() > 0) {
                errors.rejectValue(errorfield, errorfield + " duplicate");
            }
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null)
                tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
    }
}
