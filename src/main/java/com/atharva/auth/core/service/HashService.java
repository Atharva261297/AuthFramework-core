package com.atharva.auth.core.service;

import com.atharva.auth.core.dao.HashRepository;
import com.atharva.auth.core.model.ErrorCodes;
import com.atharva.auth.core.utils.hash.HashUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.Base64;

@Service
//@Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class)
@EnableTransactionManagement
public class HashService {

    @Autowired private HashRepository dao;

    public ErrorCodes add(String id, String pass) {
        if (dao.existsById(id)) {
            return ErrorCodes.ID_ALREADY_EXITS;
        } else {
            dao.save(HashUtils.getHashModel(id, pass));
            return ErrorCodes.SUCCESS;
        }
    }

    public ErrorCodes verify(String id, String pass) {
        if (dao.existsById(id)) {
            return ErrorCodes.ID_INCORRECT;
        } else if (HashUtils.verifyHashModel(dao.getOne(new String(Base64.getDecoder().decode(id))), pass)) {
            return ErrorCodes.SUCCESS;
        } else {
            return ErrorCodes.PASS_INCORRECT;
        }
    }

    public ErrorCodes update(String id, String oldPass, String newPass) {
        ErrorCodes code = verify(id, oldPass);
        if (code == ErrorCodes.SUCCESS) {
            dao.update(id, HashUtils.getHashModel(new String(Base64.getDecoder().decode(id)), newPass).getPass());
            return ErrorCodes.SUCCESS;
        } else {
            return code;
        }
    }

    public ErrorCodes delete(String id, String pass) {
        ErrorCodes code = verify(id, pass);
        if (code == ErrorCodes.SUCCESS) {
            dao.deleteById(id);
            return ErrorCodes.SUCCESS;
        } else {
            return code;
        }
    }
}
