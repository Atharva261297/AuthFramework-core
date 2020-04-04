package com.atharva.auth.core.dao;

import com.atharva.auth.core.model.UserHashModel;
import com.atharva.auth.core.model.UserId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserHashRepository extends JpaRepository<UserHashModel, UserId> {

}
