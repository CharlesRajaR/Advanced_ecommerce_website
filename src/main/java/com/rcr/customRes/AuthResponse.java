package com.rcr.customRes;

import lombok.Data;

@Data
public class AuthResponse {
    private String jwt;
    private String message;
    private String role;

    public String getJwt() {
        return jwt;
    }

    public String getMessage() {
        return message;
    }

    public String getRole() {
        return role;
    }

    public void setJwt(String jwt) {
        this.jwt = jwt;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
