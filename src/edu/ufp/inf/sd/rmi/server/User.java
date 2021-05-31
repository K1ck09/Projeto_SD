package edu.ufp.inf.sd.rmi.server;
import java.io.Serializable;

public class User implements Serializable {


    private final String username;
    private final String password;
    private Integer credits;

    public User(String username, String password, Integer credits) {
        this.username = username;
        this.password = password;
        this.credits=credits;
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public Integer getCredits() {
        return credits;
    }

    public void setCredits(Integer credits) {
        this.credits = credits;
    }

    public void addCredits(Integer plus){
        this.credits+=plus;
    }

}
