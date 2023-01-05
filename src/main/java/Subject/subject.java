package Subject;

import javax.persistence.*;
import AssClass.*;
import AssClass.assign_class;
import Class.subclass;

import java.util.*;

@Entity
@Table(name = "subject")
public class subject {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private int id;

    @Column(name = "subname")
    private String subname;

    @OneToMany(mappedBy = "subject", cascade = CascadeType.ALL)
    private Set<subclass> subclasses;

    public Set<subclass> getsubclasses() {
        return this.subclasses;
    }

    public void setsubclasses(Set<subclass> subclasses) {
        this.subclasses = subclasses;
    }

    public int getid() {
        return this.id;
    }

    public void setid(int id) {
        this.id = id;
    }

    public String getsubname() {
        return this.subname;
    }

    public void setsubname(String subname) {
        this.subname = subname;
    }

}
