package com.atharva.auth.core.dao;

import com.atharva.auth.core.model.ProjectHashModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectHashRepository extends JpaRepository<ProjectHashModel, String> {

}
