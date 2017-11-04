package entities;

import annotations.Column;
import annotations.Entity;
import annotations.Id;
import java.util.Date;

@Entity(name = "users")
public class User {
    @Id
    @Column(name = "id")
    private Integer id;
    @Column(name = "username")
    private String username;
    @Column(name = "age")
    private int age;
    @Column(name = "registration_date")
    private Date registrationDate;

    public User() {
    }

    public User(String username, int age, Date registrationDate) {
        this.username = username;
        this.age = age;
        this.registrationDate = registrationDate;
    }

    public String getUsername() {
        return username;
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Date getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(Date registrationDate) {
        this.registrationDate = registrationDate;
    }
}
