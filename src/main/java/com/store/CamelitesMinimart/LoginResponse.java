package com.store.CamelitesMinimart;

public class LoginResponse {
    private boolean isAuthenticated;
    private Long id;
    private String accessLevel;


    public String getAccessLevel() {
        return accessLevel;
    }
    public void setAccessLevel(String accessLevel) {
        this.accessLevel = accessLevel;
    }
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public boolean isAuthenticated() {
        return isAuthenticated;
    }
    public void setAuthenticated(boolean isAuthenticated) {
        this.isAuthenticated = isAuthenticated;
    }
    

}
