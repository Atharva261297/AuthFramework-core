package com.atharva.auth.core.dao;

import com.atharva.auth.core.model.AdminHashModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminHashRepository extends JpaRepository<AdminHashModel, String> {
}
