package edu.ufp.inf.sd.server;

public interface UserFactoryRI {
    boolean register (String username,String password);
    UserSessionRI login (String username,String password);

}
