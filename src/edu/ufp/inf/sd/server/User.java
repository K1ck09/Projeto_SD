package edu.ufp.inf.sd.server;

public class User {


    private final String username;
    private final String password;
    private int credits;

    public User(String username, String password, int credits) {
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

    public int getCredits() {
        return credits;
    }

    public void setCredits(int credits) {
        this.credits = credits;
    }

}
