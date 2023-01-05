package Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import Student.student;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.ui.*;
import userloginmanage.*;
import java.text.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.*;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.validation.*;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import jwt.*;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import javax.servlet.http.HttpSession;
import Teacher.*;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.HibernateException;
import AssClass.*;
import Class.*;
import Subject.*;
import org.springframework.web.servlet.mvc.support.*;

@Controller
public class homecontroller {
    Pattern pattern1 = Pattern
            .compile("^[A-Za-z0-9\\^\\-\\]\\.\\$\\*\\+\\?\\(\\)\\]\\{\\}\\|\\_\\/\\~`@%&:\"';,\\\\]{1,}$");
    Pattern pattern2 = Pattern
            .compile("[\\^\\-\\]\\.\\$\\*\\+\\?\\(\\)\\[\\]\\{\\}\\|\\_\\/\\~`@%&:\"';,\\\\]{1,}");
    Pattern pattern3 = Pattern.compile("[A-z]+");
    Pattern pattern4 = Pattern.compile("[0-9]+");

    private static SessionFactory factory;

    @Autowired
    private PasswordEncoder passwordencoder;

    @Autowired
    private Tokenmanager tokenManager;

    @Autowired
    @Qualifier("UserValidator")
    private UserValidator validator;

    @InitBinder("user")
    public void customizeBinding(WebDataBinder binder) {
        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
        dateFormatter.setLenient(false);
        binder.setValidator(validator);
        binder.registerCustomEditor(Date.class, "dateofbirth",
                new CustomDateEditor(dateFormatter, true));

    }

    @GetMapping(value = "/registerform")
    public String index(Model model, HttpServletRequest req) {
        model.addAttribute("registerpath", "/classmanagement/register");
        return "register";

    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String register(@Valid @ModelAttribute("user") confirmpassword user, BindingResult bindingResult,
            Model model, HttpServletRequest req) {
        if (bindingResult.hasErrors()) {
            if (bindingResult.hasFieldErrors("username"))
                model.addAttribute("insufficientusername", bindingResult.getFieldError("username").getCode());
            if (bindingResult.hasFieldErrors("dateofbirth"))
                model.addAttribute("insufficientdateofbirth", bindingResult.getFieldError("dateofbirth").getCode());
            if (bindingResult.hasFieldErrors("fullname"))
                model.addAttribute("insufficientfullname", bindingResult.getFieldError("fullname").getCode());
            if (bindingResult.hasFieldErrors("id"))
                model.addAttribute("insufficientID", bindingResult.getFieldError("id").getCode());
            if (bindingResult.hasFieldErrors("email"))
                model.addAttribute("sufficientemail", bindingResult.getFieldError("email").getCode());
            if (bindingResult.hasFieldErrors("password"))
                model.addAttribute("passwordnotconfirm", bindingResult.getFieldError("password").getCode());
            model.addAttribute("registerpath", "/classmanagement/register");
            return "register";
        } else {
            try {
                User userwithoutconfirmpassword = new User(user);
                userwithoutconfirmpassword.hashpassword(passwordencoder);
                ManageUser.addUser(userwithoutconfirmpassword);
                final MyUserDetail userDetails = new MyUserDetail(user);
                final String token = tokenManager.generateJwtToken(userDetails);
                mail mail = new mail();
                String mess = "<!DOCTYPE html>\n"
                        + "<html>\n"
                        + "<head>\n"
                        + "<meta charset=\"UTF-8\">\n"
                        + "<title>INIT</title>\n"
                        + "</head>\n"
                        + "<body>\n"
                        + "<h3>Verification</h3>\n"
                        + "<form action=\"http://localhost:8080/classmanagement/verification?token="
                        + URLEncoder.encode(token, StandardCharsets.UTF_8)
                        + "\" method=\"GET\" accept-charset=\"utf-8\">"
                        + "<input type=\"submit\" value=\"verify\">"
                        + "</form>"
                        + "</body>"
                        + "</html>";
                mail.send(mess, userDetails.getemail());
                return "successfulregister";
            } catch (Exception e) {
                e.printStackTrace();
                model.addAttribute("result", "fail to register. Try again later");
                model.addAttribute("registerpath", "/classmanagement/register");
                return "register";
            }

        }
    }

    @GetMapping(value = "/verification")
    @ResponseBody
    public String verification(HttpServletRequest req) throws Exception {
        try {
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

            if (principal instanceof MyUserDetail) {
                String username = ((MyUserDetail) principal).getUsername();
                ManageUser.verifyuser(username);
            } else {
                throw new Exception(principal + " can not be recognized by verification system");
            }
        } catch (Exception e) {
            e.printStackTrace();
            HttpSession session = req.getSession();
            session.invalidate();
            return "failed Verification";
        }
        HttpSession session = req.getSession();
        session.invalidate();
        return "successful verification";
    }

    @PostMapping(value = "/reverification")
    public String reverification(HttpServletRequest req, @RequestParam String email, Model model) throws Exception {

        User user = ManageUser.getUserbyEmail(email);
        if (user != null) {
            if (!user.getverify()) {
                mail mail = new mail();
                String token = tokenManager.generateJwtToken(new MyUserDetail(user));
                String mess = "<!DOCTYPE html>\n"
                        + "<html>\n"
                        + "<head>\n"
                        + "<meta charset=\"UTF-8\">\n"
                        + "<title>INIT</title>\n"
                        + "</head>\n"
                        + "<body>\n"
                        + "<h3>Verification</h3>\n"
                        + "<form action=\"http://localhost:8080/classmanagement/verification?token="
                        + URLEncoder.encode(token, StandardCharsets.UTF_8)
                        + "\" method=\"GET\" accept-charset=\"utf-8\">"
                        + "<input type=\"submit\" value=\"verify\">"
                        + "</form>"
                        + "</body>"
                        + "</html>";
                mail.send(mess, email);
                model.addAttribute("insufficientverficationemail", "re-sent");
            } else if (user.getverify()) {
                model.addAttribute("insufficientverficationemail", "account verified aready!");
            }
        } else
            model.addAttribute("insufficientverficationemail", "can not find this account!");

        return "successfulregister";
    }

    @GetMapping(value = "/changepasswordform")
    public String changepasswordform() {
        return "passwordchange";
    }

    @RequestMapping(value = "/changepassword", method = RequestMethod.POST, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public String changepassword(Model model, @RequestBody MultiValueMap<String, String> reqparam) {
        try {
            int newpasssize = reqparam.get("newpassword").get(0).length();
            Matcher matcher1 = pattern1.matcher(reqparam.get("newpassword").get(0));
            Matcher matcher2 = pattern2.matcher(reqparam.get("newpassword").get(0));
            Matcher matcher3 = pattern3.matcher(reqparam.get("newpassword").get(0));
            Matcher matcher4 = pattern4.matcher(reqparam.get("newpassword").get(0));
            if (reqparam.get("newpassword").get(0) == null || reqparam.get("newpassword").get(0).compareTo("") == 0)
                throw new Exception("new password is empty");
            if (reqparam.get("oldpassword").get(0) == null || reqparam.get("oldpassword").get(0).compareTo("") == 0)
                throw new Exception("old password is empty");
            if (reqparam.get("newpassword").get(0).compareTo(reqparam.get("confirmnewpassword").get(0)) != 0)
                throw new Exception("new password and confirm password not match");
            if (reqparam.get("newpassword").get(0).compareTo(reqparam.get("oldpassword").get(0)) == 0)
                throw new Exception("new password must different from old password");
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (!passwordencoder.matches(reqparam.get("oldpassword").get(0), ((MyUserDetail) principal).getPassword()))
                throw new Exception("old password not match");
            boolean matchFound1 = matcher1.find();
            boolean matchFound2 = matcher2.find();
            boolean matchFound3 = matcher3.find();
            boolean matchFound4 = matcher4.find();
            if (!matchFound1)
                throw new Exception("only alphabet , numeric and special characters are allowed");
            if (!matchFound2)
                throw new Exception("must contain at least 1 special character");
            if (!matchFound3)
                throw new Exception("must contain at least 1 uppercase");
            if (!matchFound4)
                throw new Exception("must contain numeric");
            if (newpasssize < 8)
                throw new Exception("new password must have 8 characters at least");
            ManageUser.changepassword((MyUserDetail) principal,
                    passwordencoder.encode((String) reqparam.get("newpassword").get(0)));
            model.addAttribute("insufficientchangepassword", "successfully changed");
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("insufficientchangepassword", e.getMessage());
        }
        return "passwordchange";
    }

    @PostMapping(value = "/sendmailresetpassword")
    public String recoverpassword(Model model, @RequestBody MultiValueMap<String, String> reqparam) {
        try {
            User user = ManageUser.getUserbyEmail(reqparam.get("email").get(0));
            if (user != null) {
                final String Token = tokenManager.generateJwtToken(new MyUserDetail(user));
                String mess = "<!DOCTYPE html>\n"
                        + "<html>\n"
                        + "<head>\n"
                        + "<meta charset=\"UTF-8\">\n"
                        + "<title>Expired</title>\n"
                        + "</head>\n"
                        + "<body>\n"
                        + "<h3>RESET PASSWORD</h3>\n"
                        + "<form action=\"http://localhost:8080/classmanagement/resetpasswordform?token="
                        + URLEncoder.encode(Token, StandardCharsets.UTF_8)
                        + "\" method=\"GET\" accept-charset=\"utf-8\">"
                        + "<input type=\"submit\" value=\"verify\">"
                        + "</form>"
                        + "</body>"
                        + "</html>";
                mail mail = new mail();
                mail.send(mess, user.getemail());
                model.addAttribute("forgetpasswordresult", "success!");

            } else
                throw new Exception("can not find accout");
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("forgetpasswordresult", e.getMessage());
        }
        return "forgetpassword";
    }

    @GetMapping(value = "/forgetpasswordform")
    public String forgetpasswordform() {
        return "forgetpassword";
    }

    @GetMapping(value = "/resetpasswordform")
    public String resetpasswordform(Model model, HttpServletRequest request) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        model.addAttribute("token", "\"" + tokenManager.generateJwtToken((MyUserDetail) principal) + "\"");
        request.getSession().invalidate();
        return "resetpassword";
    }

    @PostMapping(value = "/resetpassword", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public String resetpassword(Model model, @RequestBody MultiValueMap<String, String> reqparam,
            HttpServletRequest req) {

        try {
            int newpasssize = reqparam.get("newpassword").get(0).length();
            Matcher matcher1 = pattern1.matcher(reqparam.get("newpassword").get(0));
            Matcher matcher2 = pattern2.matcher(reqparam.get("newpassword").get(0));
            Matcher matcher3 = pattern3.matcher(reqparam.get("newpassword").get(0));
            Matcher matcher4 = pattern4.matcher(reqparam.get("newpassword").get(0));
            if (reqparam.get("newpassword").get(0) == null || reqparam.get("newpassword").get(0).compareTo("") == 0)
                throw new Exception("empty new password");
            if (reqparam.get("newpassword").get(0).compareTo(reqparam.get("confirmnewpassword").get(0)) != 0)
                throw new Exception("confirm password not match");
            boolean matchFound1 = matcher1.find();
            boolean matchFound2 = matcher2.find();
            boolean matchFound3 = matcher3.find();
            boolean matchFound4 = matcher4.find();
            if (!matchFound1)
                throw new Exception("only alphabet , numeric and special characters are allowed");
            if (!matchFound2)
                throw new Exception("must contain at least 1 special character");
            if (!matchFound3)
                throw new Exception("must contain at least 1 uppercase");
            if (!matchFound4)
                throw new Exception("must contain numeric");
            if (newpasssize < 8)
                throw new Exception("new password must have 8 characters at least");
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            ManageUser.changepassword((MyUserDetail) principal,
                    passwordencoder.encode((String) reqparam.get("newpassword").get(0)));
            model.addAttribute("resetpassword", "success!");
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("resetpassword", e.getMessage());
        }
        req.getSession().invalidate();
        return "resetpassword";
    }

    @GetMapping("/registerteacherform")
    public String registerteacher(Model model, HttpServletRequest request) {
        model.addAttribute("registerpath", "/classmanagement/registerteacher");
        return "register";
    }

    @PostMapping("/registerteacher")
    public String registerteacher(@Valid @ModelAttribute("user") confirmpassword user, BindingResult bindingResult,
            Model model, HttpServletRequest req) {
        if (bindingResult.hasErrors()) {
            for (ObjectError object : bindingResult.getFieldErrors()) {
                System.out.println(object.getCode());
            }
            if (bindingResult.hasFieldErrors("username"))
                model.addAttribute("insufficientusername", bindingResult.getFieldError("username").getCode());
            if (bindingResult.hasFieldErrors("dateofbirth"))
                model.addAttribute("insufficientdateofbirth", bindingResult.getFieldError("dateofbirth").getCode());
            if (bindingResult.hasFieldErrors("fullname"))
                model.addAttribute("insufficientfullname", bindingResult.getFieldError("fullname").getCode());
            if (bindingResult.hasFieldErrors("id"))
                model.addAttribute("insufficientID", bindingResult.getFieldError("id").getCode());
            if (bindingResult.hasFieldErrors("email"))
                model.addAttribute("sufficientemail", bindingResult.getFieldError("email").getCode());
            if (bindingResult.hasFieldErrors("password"))
                model.addAttribute("passwordnotconfirm", bindingResult.getFieldError("password").getCode());
            model.addAttribute("registerpath", "/classmanagement/registerteacher");
            return "register";
        } else {
            try {
                User userwithoutconfirmpassword = new User(user);
                userwithoutconfirmpassword.setverify(true);
                userwithoutconfirmpassword.setrole("teacher");
                userwithoutconfirmpassword.hashpassword(passwordencoder);
                ManageUser.addUser(userwithoutconfirmpassword);
                return "successfullregisterADMIN";
            } catch (Exception e) {
                model.addAttribute("registerpath", "/classmanagement/registerteacher");
                return "register";
            }
        }

    }

    @GetMapping("/teacherindex")
    public String teacherindex(Model model, HttpServletRequest request) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String teacherID = ((MyUserDetail) principal).getid();
        List<subclass> subclasses = Manageclass.getopenedclasses(teacherID);
        if (subclasses != null) {
            String teacherindex = "";
            String navigatorform = "";
            String updatescoreform = "";
            for (subclass subclass : subclasses) {
                navigatorform = "<a href=\"./teacherindex?ID="
                        + URLEncoder.encode(Integer.toString(subclass.getid()), StandardCharsets.UTF_8) + "\">"
                        + subclass.getsubject().getsubname() + subclass.getid() + " " + "</a>";
                teacherindex += navigatorform;
            }
            int classid;
            Map<String, String[]> paramMap = request.getParameterMap();
            if (paramMap.containsKey("ID")) {
                try {
                    System.out.println(request.getParameter("ID"));
                    classid = Integer.parseInt(request.getParameter("ID"));
                } catch (Exception e) {
                    e.printStackTrace();
                    classid = subclasses.get(0).getid();
                }
            } else
                classid = subclasses.get(0).getid();
            System.out.println(classid);
            subclass currentsubclass = Manageclass.getclassfromclassID(classid);
            if (currentsubclass != null) {
                teacherindex += "<br>\n" + "<p>" + currentsubclass.getsubject().getsubname() + " "
                        + currentsubclass.getid()
                        + "</p><br>"
                        + "<table style=\"width:100%\">\n"
                        + "<tr>\n"
                        + "<th>order</th>\n"
                        + "<th>name</th>\n"
                        + "<th>id</th>\n"
                        + "<th>midterm</th>\n"
                        + "<th>finalscore</th>\n"
                        + "</tr>\n";
                List<assign_class> assign_classes = Manageassign_class.getassign_classfromclassID(classid);
                if (assign_classes != null) {
                    int i = 0;
                    for (assign_class assign_class : assign_classes) {
                        teacherindex += "<tr>\n"
                                + "<th>" + (++i) + "</th>\n"
                                + "<th>" + assign_class.getstudent().getfullname() + "</th>\n"
                                + "<th>" + assign_class.getstudent().getid() + "</th>\n"
                                + "<th>" + assign_class.getmidterm() + "</th>\n"
                                + "<th>" + assign_class.getfinalscore() + "</th>\n"
                                + "</tr>\n";
                    }
                    teacherindex += "</table>\n";
                    updatescoreform = "<p>UPDATE SCORE</p>"
                            + "<p>" + model.getAttribute("updatescore") + "</p>"
                            + "<form action=\"/classmanagement/updatescore\" method=\"post\">"
                            + "<p>studentID</p><br>"
                            + "<input type=\"text\" name=\"studentID\">"
                            + "<p>midterm</p><br>"
                            + "<input type=\"number\" step=\"0.1\" name=\"midterm\">"
                            + "<p>finalscore</p><br>"
                            + "<input type=\"number\" step=\"0.1\" name=\"finalscore\">"
                            + "<input type=\"hidden\" name=\"classID\" value=\"" + currentsubclass.getid() + "\"><br>"
                            + "<input type=\"submit\" value=\"update\">"
                            + "</form>";
                }
            } else {
                teacherindex = "wrong class id";
            }
            model.addAttribute("teacherIndex", teacherindex);

            model.addAttribute("updatescore", updatescoreform);
        } else
            model.addAttribute("teacherIndex", "No class opened yet");

        return "teacherindex";
    }

    @PostMapping("/updatescore")
    public String updatescore(HttpServletRequest req, RedirectAttributes redirectAttributes, Model model) {
        if (req.getParameter("studentID") == "" || req.getParameter("finalscore") == ""
                || req.getParameter("midterm") == "") {
            redirectAttributes.addFlashAttribute("updatescore", "studentID, finalscore and midterm can not be empty");
            return "redirect:/teacherindex";
        }
        int classid = Integer.parseInt(req.getParameter("classID"));
        float finalscore = Float.parseFloat(req.getParameter("finalscore"));
        float midterm = Float.parseFloat(req.getParameter("midterm"));
        String studentID = req.getParameter("studentID");
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String teacherID = ((MyUserDetail) principal).getid();
        if (!Manageclass.checkteacherandclass(teacherID, classid)) {
            redirectAttributes.addFlashAttribute("updatescore", "wrong classid");
            return "redirect:/teacherindex";
        }
        if (Manageassign_class.updatescore(midterm, finalscore, classid, studentID)) {
            redirectAttributes.addFlashAttribute("updatescore", "success !!!");
            return "redirect:/teacherindex";
        }
        redirectAttributes.addFlashAttribute("updatescore", "fail to update !!!");
        return "redirect:/teacherindex";
    }

    @PostMapping("/deleteclassteacher")
    public String deleteclass(@RequestParam("class") int id, RedirectAttributes redirectAttributes) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String teacherID = ((MyUserDetail) principal).getid();
        if (Manageclass.deleteclass(id, teacherID)) {
            redirectAttributes.addFlashAttribute("result", "<p>successfully delete class !!!</p>");
            return "redirect:/openclassform";
        } else
            return "faildeleteclass";

    }

    @PostMapping("/openclass")
    public String openclass(@RequestParam("subject") int id, RedirectAttributes redirectAttributes) {
        try {
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            String teacherID = ((MyUserDetail) principal).getid();
            Manageclass.addnewclass(id, teacherID);
            redirectAttributes.addFlashAttribute("result", "<p>successfully open new class !!!</p>");
            return "redirect:/openclassform";
        } catch (Exception e) {
            e.printStackTrace();
            return "failopenclass";
        }
    }

    @GetMapping("/openclassform")
    public String registersubjectteacherform(Model model) {
        List<subject> subjectlist = Managesubject.getsubjectlist();
        if (subjectlist.size() == 0) {
            model.addAttribute("subjectform", "No subject has been opened yet");
        } else {
            String subjectformlist = "";
            String form = "";
            for (subject sub : subjectlist) {
                form = "<form action=\"/classmanagement/openclass\" method=\"post\">\n"
                        + "<input type=\"hidden\" name=\"subject\" value=\"" + sub.getid() + "\">\n"
                        + "<p>" + sub.getsubname() + "<p>\n"
                        + "<input type=\"submit\" value=\"open\">"
                        + "</form>\n";
                subjectformlist += form;
            }
            model.addAttribute("subjectform", subjectformlist);
        }
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String teacherID = ((MyUserDetail) principal).getid();
        List<subclass> subclasses = Manageclass.getopenedclasses(teacherID);
        if (subclasses != null) {
            String classformlist = "";
            String form = "";
            for (subclass subclass : subclasses) {
                form = "<form action=\"/classmanagement/deleteclassteacher\" method=\"post\">\n"
                        + "<input type=\"hidden\" name=\"class\" value=\"" + subclass.getid() + "\">\n"
                        + "<p>" + subclass.getid() + " " + (subclass.getsubject()).getsubname() + "<p>\n"
                        + "<input type=\"submit\" value=\"delete\">"
                        + "</form>\n";
                classformlist += form;
            }
            model.addAttribute("openedclasses", classformlist);
        } else
            model.addAttribute("openedclasses", "can not load opened class");
        return "openclassform";
    }

    @PostMapping("/registerclass")
    public String registerclass(@RequestParam("class") int id, RedirectAttributes redirectAttributes) {
        try {
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            String studentID = ((MyUserDetail) principal).getid();
            Manageassign_class.addnewassign_class(id, studentID);
            redirectAttributes.addFlashAttribute("result", "<p>successfully register new class !!!</p>");
            return "redirect:/registerclassform";
        } catch (Exception e) {
            e.printStackTrace();
            return "failopenclass";
        }
    }

    @GetMapping("/registerclassform")
    public String registerclassform(Model model) {
        List<subclass> subclasses = Manageclass.getallclass();
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String studentID = ((MyUserDetail) principal).getid();
        List<assign_class> assign_classes = Manageassign_class.getassign_classstudentID(studentID);
        Map<Integer, Boolean> subclassid = new HashMap<>();
        if (assign_classes != null) {
            for (assign_class assign_class : assign_classes)
                subclassid.put(assign_class.getsubclass().getsubject().getid(), true);
        }
        if (subclasses != null) {
            String classformlist = "";
            String form = "";
            for (subclass sub : subclasses) {
                if (subclassid.get(sub.getsubject().getid()) != null)
                    continue;
                form = "<form action=\"/classmanagement/registerclass\" method=\"post\">\n"
                        + "<input type=\"hidden\" name=\"class\" value=\"" + sub.getid() + "\">\n"
                        + "<p>" + sub.getsubject().getsubname() + "<p>\n"
                        + "<input type=\"submit\" value=\"open\">"
                        + "</form>\n";
                classformlist += form;
            }
            model.addAttribute("classform", classformlist);
        } else
            model.addAttribute("classform", "No class has been opened yet");

        if (assign_classes != null) {
            String classtodeletelist = "";
            String form = "";
            for (assign_class assign_class : assign_classes) {
                form = "<form action=\"/classmanagement/deleteclasstudent\" method=\"post\">\n"
                        + "<input type=\"hidden\" name=\"class\" value=\"" + assign_class.getId() + "\">\n"
                        + "<p>" + assign_class.getsubclass().getid() + " "
                        + assign_class.getsubclass().getsubject().getsubname()
                        + "<p>\n"
                        + "<input type=\"submit\" value=\"delete\">"
                        + "</form>\n";
                classtodeletelist += form;
            }
            model.addAttribute("registerclasses", classtodeletelist);
        } else
            model.addAttribute("registerclasses", "can not load registered class");

        return "registerclassform";

    }

    @PostMapping("/deleteclasstudent")
    public String deleteclasstudent(@RequestParam("class") int id, RedirectAttributes redirectAttributes) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String studentID = ((MyUserDetail) principal).getid();

        if (Manageassign_class.deleteassign_class(id, studentID)) {
            redirectAttributes.addFlashAttribute("result", "<p>successfully delete class !!!</p>");
            return "redirect:/registerclassform";
        } else
            return "faildeleteclass";

    }

    @GetMapping("/studentindex")
    public String studentindex(Model model) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String studentID = ((MyUserDetail) principal).getid();
        List<assign_class> assign_classes = Manageassign_class.getassign_classstudentID(studentID);
        String mess = "<table style=\"width:100%\">\n"
                + "<tr>\n"
                + "<th>order</th>\n"
                + "<th>subject name</th>\n"
                + "<th>class-id</th>\n"
                + "<th>midterm</th>\n"
                + "<th>finalscore</th>\n"
                + "</tr>\n";
        if (assign_classes != null) {
            int i = 0;
            for (assign_class assign_class : assign_classes) {
                mess += "<tr>\n"
                        + "<th>" + (++i) + "</th>\n"
                        + "<th>" + assign_class.getsubclass().getsubject().getsubname() + "</th>\n"
                        + "<th>" + assign_class.getsubclass().getid() + "</th>\n"
                        + "<th>" + assign_class.getmidterm() + "</th>\n"
                        + "<th>" + assign_class.getfinalscore() + "</th>\n"
                        + "</tr>\n";
            }
            mess += "</table>\n";

        }
        model.addAttribute("studentIndex", mess);
        return "studentindex";

    }

}