package com.example.goppho.repositories;

import com.example.goppho.entities.UserAuthOTPEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserAuthOTPRepository extends JpaRepository<UserAuthOTPEntity, String> {
}
