package com.simats.afinal;

import java.io.Serializable;

public class DoctorInfo implements Serializable {
    private String name;
    private String id;
    private String email;
    private String phone;
    private String specialization;
    private String role;
    private String password;
    private String status;

    public DoctorInfo(String name, String id, String email, String phone, String specialization, String role, String password, String status) {
        this.name = name;
        this.id = id;
        this.email = email;
        this.phone = phone;
        this.specialization = specialization;
        this.role = role;
        this.password = password;
        this.status = status;
    }

    public String getName() { return name; }
    public String getId() { return id; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public String getSpecialization() { return specialization; }
    public String getRole() { return role; }
    public String getPassword() { return password; }
    public String getStatus() { return status; }
    
    public void setStatus(String status) { this.status = status; }
}
