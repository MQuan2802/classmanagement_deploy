package Teacher;

import javax.persistence.Entity;
import org.springframework.boot.autoconfigure.security.SecurityProperties.User;

import java.io.Serializable;
import java.util.*;
import javax.persistence.*;
import Class.*;
import userloginmanage.UserMappedSupperClass;

@Entity
@Table(name = "user")
public class teacher extends UserMappedSupperClass implements Serializable {
    @OneToMany(mappedBy = "teachertoclass", cascade = CascadeType.ALL)
    private Set<subclass> subclasses;

    public Set<subclass> getSubclasses() {
        return this.subclasses;
    }

    public void setsubclasses(Set<subclass> subclasses) {
        this.subclasses = subclasses;
    }

}
