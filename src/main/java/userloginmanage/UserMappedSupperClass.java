package userloginmanage;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;

import java.util.Date;
import java.util.Set;

@MappedSuperclass
@Table(name = "user")
public class UserMappedSupperClass {

    @Id
    @Column(name = "user_id")
    protected String id;

    @Column(name = "fullname")
    protected String fullname;

    @Column(name = "username")
    protected String username;

    @Column(name = "password")
    protected String password;

    @Column(name = "role")
    protected String role = "student";

    @Column(name = "enabled")
    protected boolean enabled;

    @Column(name = "email")
    protected String email;

    @Column(name = "dateofbirth")
    protected Date dateofbirth;

    @Column(name = "verify")
    protected boolean verify;

    public void setverify(boolean verify) {
        this.verify = verify;
    }

    public boolean getverify() {
        return this.verify;
    }

    public void hashpassword(PasswordEncoder passwordencoder) {
        this.password = passwordencoder.encode(this.password);
    }

    public void setemail(String mail) {
        this.email = mail;
    }

    public String getemail() {
        return this.email;
    }

    public void setid(String id) {
        this.id = id;
    }

    public String getid() {
        return this.id;
    }

    public void setrole(String role) {
        // this.role = role;
    }

    public String getrole() {
        return this.role;
    }

    public void setenabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean getenabled() {
        return this.enabled;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setfullname(String fullname) {
        this.fullname = fullname;
    }

    public String getfullname() {
        return this.fullname;
    }

    public void setdateofbirth(Date dateofbirth) {
        this.dateofbirth = dateofbirth;
    }

    public Date getdateofbirth() {
        return this.dateofbirth;
    }

}
