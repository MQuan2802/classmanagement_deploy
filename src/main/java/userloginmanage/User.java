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

@Entity
@Table(name = "user")
public class User extends UserMappedSupperClass {

    public User() {
    };

    public User(confirmpassword user) {
        this.id = user.getid();
        this.username = user.getUsername();
        this.fullname = user.getfullname();
        this.dateofbirth = user.getdateofbirth();
        this.email = user.getemail();
        this.role = user.getrole();
        this.enabled = user.getenabled();
        this.password = user.getPassword();
        this.verify = getverify();
    }

}
