package com.example.goppho.repositories;

import com.example.goppho.entities.ConnectionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConnectionRepository extends JpaRepository<ConnectionEntity, String> {
}
