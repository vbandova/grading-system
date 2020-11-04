package com.interview.roiti.gradingsystem.model;

public class User {

    private String user;
    private Long id;
    private boolean isAdmin;

    public User() {
    }

    public User(String user, Long id, boolean isAdmin) {
        this.user = user;
        this.id = id;
        this.isAdmin = isAdmin;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }
}
