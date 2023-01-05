package Student;

import userloginmanage.*;

import java.io.Serializable;
import java.util.*;
import javax.persistence.*;
import Class.*;
import AssClass.*;

@Entity
@Table(name = "user")
public class student extends UserMappedSupperClass implements Serializable {

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<assign_class> assign_classes = new HashSet<>();

    public void setassign_classes(Set assign_classes) {
        this.assign_classes = assign_classes;
    }

    public Set<assign_class> getassign_classes() {
        return this.assign_classes;
    }

}
