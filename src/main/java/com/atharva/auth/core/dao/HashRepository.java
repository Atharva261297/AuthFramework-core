package com.atharva.auth.core.dao;

import com.atharva.auth.core.model.HashModel;
import com.atharva.auth.core.utils.DaoConstants;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface HashRepository extends JpaRepository<HashModel, String> {

    @Query(DaoConstants.updateCred)
    void update(@Param("id") String id, @Param("pass") byte[] pass);

}
