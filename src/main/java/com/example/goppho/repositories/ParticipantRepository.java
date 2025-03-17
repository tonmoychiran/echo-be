package com.example.goppho.repositories;

import com.example.goppho.entities.ParticipantEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParticipantRepository extends JpaRepository<ParticipantEntity, String> {
}
