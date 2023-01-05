package AssClass;

import javax.persistence.*;
import Class.*;
import Student.*;

@Entity
@Table(name = "assign_class")
public class assign_class {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    int id;

    @ManyToOne
    @JoinColumn(name = "stu_id", referencedColumnName = "user_id")
    private student student;

    @ManyToOne
    @JoinColumn(name = "class_id", referencedColumnName = "id")
    private subclass subclass;

    @Column(name = "midterm")
    private float midterm;
    @Column(name = "finalscore")
    private float finalscore;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setstudent(student student) {
        this.student = student;
    }

    public student getstudent() {
        return this.student;
    }

    public void setsubclass(subclass subclass) {
        this.subclass = subclass;

    }

    public subclass getsubclass() {
        return this.subclass;
    }

    public void setmidterm(float midterm) {
        this.midterm = midterm;
    }

    public float getmidterm() {
        return this.midterm;
    }

    public void setfinalscore(float finalscore) {
        this.finalscore = finalscore;
    }

    public float getfinalscore() {
        return this.finalscore;
    }

}
