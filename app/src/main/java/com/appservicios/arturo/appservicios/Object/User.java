package com.appservicios.arturo.appservicios.Object;

/**
 * Created by MyPC on 12/04/2016.
 */
public class User {
    public String id;
    public String name;
    public String email;
    public String connecttion;
    public String cratedAt;

    public User() {
    }

    public User(String id, String name, String email, String connecttion, String cratedAt, double latitude, double longitude) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.connecttion = connecttion;
        this.cratedAt = cratedAt;

    }
}
