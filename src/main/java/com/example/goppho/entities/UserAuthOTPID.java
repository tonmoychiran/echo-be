package com.example.goppho.entities;

import java.io.Serializable;
import java.util.Objects;

public class UserAuthOTPID implements Serializable {

    private Long createdAt;

    private UserEntity user;

    public UserAuthOTPID() {
    }

    public UserAuthOTPID(Long createdAt, UserEntity user) {
        this.createdAt = createdAt;
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserAuthOTPID that = (UserAuthOTPID) o;
        return Objects.equals(createdAt, that.createdAt) && Objects.equals(user, that.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(createdAt, user);
    }
}
