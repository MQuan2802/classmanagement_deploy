package Class;

import javax.persistence.*;
import Teacher.*;

import java.io.Serializable;
import java.util.*;
import Student.*;
import Subject.subject;
import AssClass.*;
import Subject.*;

@Entity
@Table(name = "class")
public class subclass implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private int id;

    @ManyToOne
    @JoinColumn(name = "teacherid")
    private teacher teachertoclass;

    @OneToMany(mappedBy = "subclass", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<assign_class> assign_classes = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "subjectid")
    private subject subject;

    public void setsubject(subject subject) {
        this.subject = subject;
    }

    public subject getsubject() {
        return this.subject;
    }

    public Set<assign_class> getassign_classes() {
        return this.assign_classes;
    }

    public void setassign_classes(Set<assign_class> assign_classes) {
        this.assign_classes = assign_classes;
    }

    public void setteachertoclass(teacher teachertoclass) {
        this.teachertoclass = teachertoclass;
    }

    public teacher getteachertoclass() {
        return this.teachertoclass;
    }

    public subclass() {

    }

    public void setid(int id) {
        this.id = id;
    }

    public int getid() {
        return this.id;
    }

}
