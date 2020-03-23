package com.atharva.auth.core.service;

import com.atharva.auth.core.dao.HashRepository;
import com.atharva.auth.core.model.ErrorCodes;
import com.atharva.auth.core.utils.hash.HashUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

@Service
//@Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class)
@EnableTransactionManagement
public class HashService {

    @Autowired private HashRepository dao;

    Logger log = LoggerFactory.getLogger(HashService.class);

    public ErrorCodes add(String id, String pass) {
        if (dao.existsById(id)) {
            return ErrorCodes.ID_ALREADY_EXITS;
        } else {
            dao.save(HashUtils.getHashModel(id, pass));
            return ErrorCodes.SUCCESS;
        }
    }

    public ErrorCodes verify(String id, String pass) {
        log.debug("Login 2 : " + id);
        if (dao.existsById(id)) {
            if (HashUtils.verifyHashModel(dao.getOne(id), pass)) {
                return ErrorCodes.SUCCESS;
            } else {
                return ErrorCodes.PASS_INCORRECT;
            }
        } else {
            return ErrorCodes.ID_INCORRECT;
        }
    }

    @Transactional
    public ErrorCodes update(String oldId, String oldPass, String newId,String newPass) {
        ErrorCodes code = verify(oldId, oldPass);
        if (code == ErrorCodes.SUCCESS) {
            if (oldId.equals(newId)) {
                dao.getOne(oldId).setPass(HashUtils.getHashModel(oldId, newPass).getPass());
                return ErrorCodes.SUCCESS;
            } else {
                return ErrorCodes.ID_NOT_SAME;
            }
        } else {
            return code;
        }
    }

    @Transactional
    public ErrorCodes reset(String id, String pass) {
        if (dao.existsById(id)) {
            dao.getOne(id).setPass(HashUtils.getHashModel(id, pass).getPass());
            return ErrorCodes.ID_ALREADY_EXITS;
        } else {
            return ErrorCodes.ID_INCORRECT;
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
