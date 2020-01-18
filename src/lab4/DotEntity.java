package lab4;


import javax.persistence.*;

@Entity
@Table(name = "dots")
public class DotEntity {
    private @Id
    @GeneratedValue
    Long id;
    @Column(name="x")
    private double x;
    @Column(name="y")
    private double y;
    @Column(name="r")
    private double r;
    @Column(name="inarea")
    private boolean inarea;

    @ManyToOne
    private UserEntity user;
    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getR() {
        return r;
    }

    public void setR(double r) {
        this.r = r;
    }

    public boolean isInarea() {
        return inarea;
    }

    public void setInarea(boolean inarea) {
        this.inarea = inarea;
    }

    public DotEntity(double x, double y, double r, boolean inarea, UserEntity user) {
        this.x = x;
        this.y = y;
        this.r = r;
        this.inarea = inarea;
        this.user = user;
    }

    public DotEntity() {
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }
}
