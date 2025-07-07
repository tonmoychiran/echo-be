package com.example.goppho.entities;

import jakarta.persistence.*;

import java.sql.Date;


@Entity
@Table(name = "user_information")
public class UserInformationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(length = 36)
    private String userInformationId;

    @Column(length = 50)
    private String name;

    private Date dob;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    public UserInformationEntity(String name, UserEntity user) {
        this.name = name;
        this.user = user;
    }

    public UserInformationEntity() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }
}
