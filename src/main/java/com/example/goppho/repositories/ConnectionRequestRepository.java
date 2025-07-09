package com.example.goppho.repositories;

import com.example.goppho.entities.ConnectionRequestEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConnectionRequestRepository extends JpaRepository<ConnectionRequestEntity, String> {
}
