package com.hana.bankai.domain.user.repository;

import com.hana.bankai.domain.user.entity.UserJob;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserJobRepository extends JpaRepository<UserJob, UUID> {
}
